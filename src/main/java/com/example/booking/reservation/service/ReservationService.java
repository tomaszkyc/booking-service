package com.example.booking.reservation.service;

import com.example.booking.reservation.exception.NoAvailableRoomsException;
import com.example.booking.reservation.model.Reservation;
import com.example.booking.reservation.repository.ReservationRepository;
import com.example.booking.room.model.Room;
import com.example.booking.room.model.RoomType;
import com.example.booking.room.service.RoomService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class ReservationService {

    private static final Logger logger = LoggerFactory.getLogger(ReservationService.class);

    private final ReservationRepository reservationRepository;
    private final RoomService roomService;

    public ReservationService(ReservationRepository reservationRepository, RoomService roomService) {
        this.reservationRepository = reservationRepository;
        this.roomService = roomService;
    }


    public Reservation create(Reservation reservation) throws NoAvailableRoomsException {
        BigDecimal amount = reservation.getAmount();
        Optional<Room> reservedRoom;
        if (amount.compareTo(BigDecimal.valueOf(100L)) >= 0) {
            reservedRoom = roomService.reserveRoom(RoomType.PREMIUM);
        } else {
            // try to reserve room for economy class
            reservedRoom = roomService.reserveRoom(RoomType.ECONOMY);
            if (reservedRoom.isEmpty()) {
                reservedRoom = roomService.reserveRoom(RoomType.PREMIUM);
            }
        }
        if (reservedRoom.isEmpty()) {
            throw new NoAvailableRoomsException();
        }
        Room room = reservedRoom.get();
        reservation.setRoom(room);
        room.setReservation(reservation);
        return reservationRepository.save(reservation);
    }

    public Reservation update(Reservation reservation) {
        return reservationRepository.save(reservation);
    }

    public void delete(Long id) {
        roomService.changeRoomAvailability(id, false);
        reservationRepository.deleteById(id);
    }

    public List<Reservation> findAll() {
        return reservationRepository.findAll();
    }

    public List<Reservation> findByRoomType(RoomType roomType) {
        return reservationRepository.findAllByRoomRoomType(roomType);
    }

    public Optional<Reservation> findById(Long id) {
        return reservationRepository.findById(id);
    }
}
