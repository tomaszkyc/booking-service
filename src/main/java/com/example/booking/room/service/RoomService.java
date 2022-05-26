package com.example.booking.room.service;

import com.example.booking.room.exception.RoomNotFoundException;
import com.example.booking.room.model.Room;
import com.example.booking.room.model.RoomType;
import com.example.booking.room.repository.RoomRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class RoomService {

    private static final Logger logger = LoggerFactory.getLogger(RoomService.class);

    private final RoomRepository roomRepository;

    public RoomService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    public Room create(Room room) {
        return roomRepository.save(room);
    }

    public Room update(Room room) {
        return roomRepository.save(room);
    }

    @Transactional
    public Optional<Room> reserveRoom(RoomType roomType) {
        Optional<Room> availableRoom = roomRepository.findFirstByRoomTypeAndReservedIsFalse(roomType);
        if (availableRoom.isEmpty()) {
            return Optional.empty();
        }
        Room room = availableRoom.get();
        room.setReserved(true);
        room = roomRepository.save(room);
        return Optional.of(room);
    }

    @Transactional
    public void changeRoomAvailability(Long roomId, boolean reserved) throws RoomNotFoundException {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new RoomNotFoundException(roomId));
        if (!reserved) {
            room.setReservation(null);
        }
        room.setReserved(reserved);
        roomRepository.save(room);
    }

    public void delete(Long id) {
        roomRepository.deleteById(id);
    }

    public List<Room> findAll() {
        return roomRepository.findAll();
    }

    public Optional<Room> findById(Long id) {
        return roomRepository.findById(id);
    }

    public Long countReservedRoomsByType(RoomType roomType) {
        return roomRepository.countAllByRoomTypeIsAndReserved(roomType, true);
    }

    public Long countAvailableRoomsByType(RoomType roomType) {
        return roomRepository.countAllByRoomTypeIsAndReserved(roomType, false);
    }

}
