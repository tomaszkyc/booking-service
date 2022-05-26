package com.example.booking.room.model;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class RoomTest {

    private static final Long ROOM_ID = 1L;
    private static final RoomType ROOM_TYPE = RoomType.ECONOMY;
    public static final String CUSTOM_ROOM_NAME = "Standard King Room";

    private Room room;


    @Test
    void shouldCreateRoomInstanceWithoutParameters() {
        // given

        // when
        room = new Room();

        // then
        assertThat(room).isNotNull();
    }

    @Test
    void shouldCreateRoomInstanceWithParameters() {
        // given

        // when
        room = new Room(ROOM_ID, ROOM_TYPE);

        // then
        assertThat(room).isNotNull();
        assertThat(room.getId()).isEqualTo(ROOM_ID);
        assertThat(room.getRoomType()).isEqualTo(ROOM_TYPE);
    }

    @Test
    void shouldReserveRoom() {
        // given
        room = createRoom();

        // when
        room.setReserved(true);

        // then
        assertThat(room.isReserved()).isTrue();
    }

    @Test
    void shouldSetCustomNameForRoom() {
        // given
        room = createRoom();

        // when
        room.setName(CUSTOM_ROOM_NAME);

        // then
        assertThat(room.getName()).isEqualTo(CUSTOM_ROOM_NAME);
    }

    private Room createRoom() {
        return new Room(ROOM_ID, ROOM_TYPE);
    }

}