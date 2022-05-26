package com.example.booking.reservation.service;

import com.example.booking.hotel.model.Hotel;
import com.example.booking.reservation.exception.NoAvailableRoomsException;
import com.example.booking.reservation.model.Reservation;
import com.example.booking.reservation.repository.ReservationRepository;
import com.example.booking.room.model.Room;
import com.example.booking.room.model.RoomType;
import com.example.booking.room.repository.RoomRepository;
import com.example.booking.room.service.RoomService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ReservationServiceTest {

    private static final Hotel HOTEL = createHotel();
    private static final Room AVAILABLE_ECONOMY_ROOM = createRoom(1L, RoomType.ECONOMY);
    private static final Room AVAILABLE_PREMIUM_ROOM = createRoom(2L, RoomType.PREMIUM);
    private static final int AMOUNT_FOR_PREMIUM_ROOM = 102;
    private static final int AMOUNT_FOR_ECONOMY_ROOM = 30;
    private static final AtomicLong ROOM_ID_HOLDER = new AtomicLong(0);

    private ReservationRepository reservationRepository;
    private ReservationService reservationService;
    private RoomRepository roomRepository;
    private RoomService roomService;
    private Reservation reservation;

    @BeforeEach
    void setUp() {
        reservationRepository = mock(ReservationRepository.class);
        roomRepository = mock(RoomRepository.class);
        roomService = new RoomService(roomRepository);
        reservationService = new ReservationService(reservationRepository, roomService);

        when(roomRepository.findFirstByRoomTypeAndReservedIsFalse(eq(RoomType.ECONOMY)))
                .thenReturn(Optional.of(AVAILABLE_ECONOMY_ROOM));
        when(roomRepository.findFirstByRoomTypeAndReservedIsFalse(eq(RoomType.PREMIUM)))
                .thenReturn(Optional.of(AVAILABLE_PREMIUM_ROOM));
        when(roomRepository.save(eq(AVAILABLE_ECONOMY_ROOM))).thenReturn(AVAILABLE_ECONOMY_ROOM);
        when(roomRepository.save(eq(AVAILABLE_PREMIUM_ROOM))).thenReturn(AVAILABLE_PREMIUM_ROOM);
    }

    @Test
    void shouldBookPremiumRoomForPriceHigherThan100Eur() throws NoAvailableRoomsException {
        // given
        reservation = createReservation(AMOUNT_FOR_PREMIUM_ROOM);
        when(reservationRepository.save(eq(reservation))).thenReturn(reservation);

        // when
        reservation = reservationService.create(reservation);


        // then
        assertThat(reservation.getRoom()).isNotNull();
        assertThat(reservation.getRoom().getRoomType()).isEqualTo(RoomType.PREMIUM);
    }

    @Test
    void shouldBookEconomyRoomForPriceLowerThan100EurIfEconomyRoomsAreAvailable() throws NoAvailableRoomsException {
        // given
        reservation = createReservation(AMOUNT_FOR_ECONOMY_ROOM);
        when(reservationRepository.save(eq(reservation))).thenReturn(reservation);

        // when
        reservation = reservationService.create(reservation);


        // then
        assertThat(reservation.getRoom()).isNotNull();
        assertThat(reservation.getRoom().getRoomType()).isEqualTo(RoomType.ECONOMY);
    }

    @Test
    void shouldBookPremiumRoomForPriceLowerThan100EurIfEconomyRoomsAreUnavailable() throws NoAvailableRoomsException {
        // given
        reservation = createReservation(AMOUNT_FOR_ECONOMY_ROOM);
        when(reservationRepository.save(eq(reservation))).thenReturn(reservation);
        when(roomRepository.findFirstByRoomTypeAndReservedIsFalse(eq(RoomType.ECONOMY)))
                .thenReturn(Optional.empty());

        // when
        reservation = reservationService.create(reservation);


        // then
        assertThat(reservation.getRoom()).isNotNull();
        assertThat(reservation.getRoom().getRoomType()).isEqualTo(RoomType.PREMIUM);
    }

    @Test
    void shouldThrowExceptionWhenNoPremiumRoomsAvailable() {
        // given
        reservation = createReservation(AMOUNT_FOR_PREMIUM_ROOM);
        when(roomRepository.findFirstByRoomTypeAndReservedIsFalse(eq(RoomType.PREMIUM)))
                .thenReturn(Optional.empty());

        // when

        // then
        assertThatCode(() -> reservation = reservationService.create(reservation))
                .isInstanceOf(NoAvailableRoomsException.class);
    }

    @Test
    void shouldThrowExceptionWhenNoEconomyAndPremiumRoomsAvailable() {
        // given
        reservation = createReservation(AMOUNT_FOR_ECONOMY_ROOM);
        when(roomRepository.findFirstByRoomTypeAndReservedIsFalse(any(RoomType.class)))
                .thenReturn(Optional.empty());

        // when

        // then
        assertThatCode(() -> reservation = reservationService.create(reservation))
                .isInstanceOf(NoAvailableRoomsException.class);
    }


    @ParameterizedTest
    @MethodSource("provideInputAndResultsForShouldHandleMultipleReservationTypesCorrectly")
    void shouldHandleMultipleReservationTypesCorrectly(List<Reservation> reservations,
                                                       int numberOfPremiumRooms, int numberOfEconomyRooms,
                                                       int expectedUsagePremiumRooms, int expectedUsageEconomyRooms,
                                                       BigDecimal expectedPremiumRoomsIncome,
                                                       BigDecimal expectedEconomyRoomsIncome) {
        // given
        reservations.sort(Comparator.comparing(Reservation::getAmount).reversed());
        List<Room> economyRooms = createRooms(numberOfEconomyRooms, RoomType.ECONOMY);
        List<Room> premiumRooms = createRooms(numberOfPremiumRooms, RoomType.PREMIUM);

        when(roomRepository.findFirstByRoomTypeAndReservedIsFalse(eq(RoomType.ECONOMY)))
                .thenAnswer(a -> economyRooms.stream().filter(Room::isNotReserved).findAny());
        when(roomRepository.findFirstByRoomTypeAndReservedIsFalse(eq(RoomType.PREMIUM)))
                .thenAnswer(a -> premiumRooms.stream().filter(Room::isNotReserved).findAny());
        when(roomRepository.save(any())).thenAnswer(invocation -> invocation.getArguments()[0]);
        // when
        for (Reservation reservation : reservations) {
            try {
                reservation = reservationService.create(reservation);
            } catch (NoAvailableRoomsException ignored) {
                // we ignore here exception on purpose in this test
                // because we want to validate a series of reservations
                // and the data state summary after them
            }
        }

        // then
        long usedPremiumRooms = premiumRooms.stream()
                .filter(Room::isReserved)
                .count();
        long usedEconomyRooms = economyRooms.stream()
                .filter(Room::isReserved)
                .count();
        assertThat(usedPremiumRooms).isEqualTo(expectedUsagePremiumRooms);
        assertThat(usedEconomyRooms).isEqualTo(expectedUsageEconomyRooms);

        BigDecimal premiumRoomsIncome = reservations.stream()
                .filter(reservation -> reservation.hasRoomType(RoomType.PREMIUM))
                .map(Reservation::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal economyRoomsIncome = reservations.stream()
                .filter(reservation -> reservation.hasRoomType(RoomType.ECONOMY))
                .map(Reservation::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        assertThat(premiumRoomsIncome).isEqualByComparingTo(expectedPremiumRoomsIncome);
        assertThat(economyRoomsIncome).isEqualByComparingTo(expectedEconomyRoomsIncome);
    }

    private static Stream<Arguments> provideInputAndResultsForShouldHandleMultipleReservationTypesCorrectly() {
        return Stream.of(
                Arguments.of(createReservations(23, 45, 155, 374, 22, 99.99, 100, 101, 115, 209), 3, 3, 3, 3, BigDecimal.valueOf(738), BigDecimal.valueOf(167.99)),
                Arguments.of(createReservations(23, 45, 155, 374, 22, 99.99, 100, 101, 115, 209), 7, 5, 6, 4, BigDecimal.valueOf(1054), BigDecimal.valueOf(189.99)),
                Arguments.of(createReservations(23, 45, 155, 374, 22, 99.99, 100, 101, 115, 209), 2, 7, 2, 4, BigDecimal.valueOf(583), BigDecimal.valueOf(189.99)),
                Arguments.of(createReservations(23, 45, 155, 374, 22, 99.99, 100, 101, 115, 209), 7, 1, 7, 1, BigDecimal.valueOf(1099), BigDecimal.valueOf(99.99))
                // Commented test case below cause the expected value is invalid.
                // We can't reserve 1 economy room from given prices because
                // there is no combination of user input to sum up to value 45.99.
                // Arguments.of(createReservations(23, 45, 155, 374, 22, 99.99, 100, 101, 115, 209), 7, 1, 7, 1, BigDecimal.valueOf(1153), BigDecimal.valueOf(45.99)),
        );
    }

    @SafeVarargs
    private static <T extends Number> List<Reservation> createReservations(T... prices) {
        if (prices == null || prices.length == 0) {
            return Collections.emptyList();
        }
        return Arrays.stream(prices)
                .map(price -> createReservation(price.doubleValue()))
                .collect(Collectors.toList());
    }

    private static Reservation createReservation(double price) {
        return new Reservation(BigDecimal.valueOf(price).setScale(2));
    }

    private static List<Room> createRooms(int numberOfRooms, RoomType roomType) {
        if (numberOfRooms <= 0) {
            return Collections.emptyList();
        }
        return IntStream.range(0, numberOfRooms)
                .mapToObj(i -> new Room(ROOM_ID_HOLDER.getAndIncrement(), roomType))
                .collect(Collectors.toList());
    }

    private static Room createRoom(Long id, RoomType roomType) {
        Room room = new Room(id, roomType);
        room.setHotel(HOTEL);
        return room;
    }

    private static Hotel createHotel() {
        return new Hotel();
    }
}