package com.example.booking.hotel.model;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

class HotelTest {

    private static final Long HOTEL_ID = 1L;
    private static final String HOTEL_NAME = "Top Hotel";

    private Hotel hotel;

    @Test
    void shouldCreateHotelInstance() {
        // given

        // when
        hotel = new Hotel();

        // then
        assertThat(hotel).isNotNull();
    }

    @Test
    void shouldCreateHotelInstanceWithIdAndName() {
        // given
        hotel = null;

        // when
        assertThatCode(() -> hotel = new Hotel(HOTEL_ID, HOTEL_NAME)).doesNotThrowAnyException();

        // then
        assertThat(hotel).isNotNull();
        assertThat(hotel.getId()).isEqualTo(HOTEL_ID);
        assertThat(hotel.getName()).isEqualTo(HOTEL_NAME);
    }
}