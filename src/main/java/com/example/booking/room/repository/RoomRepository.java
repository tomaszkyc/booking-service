package com.example.booking.room.repository;

import com.example.booking.room.model.Room;
import com.example.booking.room.model.RoomType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.persistence.LockModeType;
import java.util.Optional;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Room> findFirstByRoomTypeAndReservedIsFalse(@Param("roomType") RoomType roomType);

    Long countAllByRoomTypeIsAndReserved(RoomType roomType, boolean reserved);

}
