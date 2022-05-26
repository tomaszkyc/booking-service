package com.example.booking.reservation.model;


import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ReservationTest {

    private Reservation reservation;

    @Test
    void shouldCreateReservation() {
        // given

        // when
        reservation = new Reservation();

        // then
        assertThat(reservation).isNotNull();
    }

}