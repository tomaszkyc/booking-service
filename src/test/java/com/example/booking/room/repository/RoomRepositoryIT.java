package com.example.booking.room.repository;

import com.example.booking.room.model.Room;
import com.example.booking.room.model.RoomType;
import com.example.booking.testing.AbstractIT;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class RoomRepositoryIT extends AbstractIT {

    public static final RoomType TEST_ROOM_TYPE = RoomType.ECONOMY;

    @Autowired
    private RoomRepository roomRepository;

    private Room room;

    @AfterEach
    void tearDown() {
        roomRepository.deleteAll();
    }

    @Test
    void shouldSaveRoomInDatabase() {
        // given
        room = createRoom();

        // when
        room = roomRepository.save(room);

        // then
        assertThat(room.getId()).isNotNull();
        assertThat(roomRepository.count()).isNotZero();
    }

    @Test
    void shouldFindFirstAvailableRoomByRoomTypeAndNotReserved() {
        // given
        room = createRoom();
        room.setReserved(true);
        Room anotherRoom = createRoom();
        room = roomRepository.save(room);
        anotherRoom = roomRepository.save(anotherRoom);

        // when
        Optional<Room> availableRoom = roomRepository.findFirstByRoomTypeAndReservedIsFalse(TEST_ROOM_TYPE);

        // then
        assertThat(availableRoom).isPresent();
        assertThat(availableRoom.get().getId()).isEqualTo(anotherRoom.getId());
    }


    private Room createRoom() {
        return new Room(TEST_ROOM_TYPE);
    }
}