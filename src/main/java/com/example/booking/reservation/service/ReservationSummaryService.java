package com.example.booking.reservation.service;

import com.example.booking.reservation.dto.ReservationSummary;
import com.example.booking.reservation.model.Reservation;
import com.example.booking.room.model.RoomType;
import com.example.booking.room.service.RoomService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ReservationSummaryService {

    private final ReservationService reservationService;
    private final RoomService roomService;

    public ReservationSummaryService(ReservationService reservationService,
                                     RoomService roomService) {
        this.reservationService = reservationService;
        this.roomService = roomService;
    }

    public ReservationSummary getReservationSummary() {
        Map<RoomType, Map<String, Object>> reservationStatistics = getReservationStatistics();
        return new ReservationSummary(reservationStatistics);
    }

    private Map<RoomType, Map<String, Object>> getReservationStatistics() {
        Map<RoomType, Map<String, Object>> statistics = new HashMap<>();
        for (RoomType roomType : RoomType.values()) {
            List<Reservation> reservations = reservationService.findByRoomType(roomType);
            BigDecimal income = calculateIncome(reservations);
            Long reservedRooms = calculateReservedRooms(roomType);
            Long availableRooms = calculateAvailableRooms(roomType);
            Long allRooms = reservedRooms + availableRooms;

            Map<String, Object> properties = new HashMap<>();
            properties.put("income", income);
            properties.put("allRooms", allRooms);
            properties.put("reservedRooms", reservedRooms);
            properties.put("availableRooms", availableRooms);
            statistics.put(roomType, properties);
        }
        return statistics;
    }

    private BigDecimal calculateIncome(List<Reservation> reservations) {
        return reservations.stream()
                .filter(reservation -> reservation.getRoom() != null)
                .map(Reservation::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2);
    }

    private Long calculateReservedRooms(RoomType roomType) {
        return roomService.countReservedRoomsByType(roomType);
    }

    private Long calculateAvailableRooms(RoomType roomType) {
        return roomService.countAvailableRoomsByType(roomType);
    }
}
