package com.example.booking.reservation.repository;

import com.example.booking.reservation.model.Reservation;
import com.example.booking.room.model.RoomType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    List<Reservation> findAllByRoomRoomType(RoomType roomType);
}
