package com.example.booking.reservation.controller;


import com.example.booking.reservation.dto.CreateReservationRequest;
import com.example.booking.reservation.exception.NoAvailableRoomsException;
import com.example.booking.reservation.model.Reservation;
import com.example.booking.reservation.service.ReservationService;
import com.example.booking.room.controller.RoomController;
import com.google.common.base.Preconditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("v1/reservations")
public class ReservationController {


    private static final Logger logger = LoggerFactory.getLogger(RoomController.class);

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping
    public ResponseEntity<List<Reservation>> findAll() {
        ResponseEntity<List<Reservation>> responseEntity;
        try {
            responseEntity = ResponseEntity.ok(reservationService.findAll());
        } catch (Exception exception) {
            logger.error("Exception occurred during reservations fetching", exception);
            responseEntity = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return responseEntity;
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Reservation> findById(@PathVariable("id") Long id) {
        ResponseEntity<Reservation> responseEntity;
        try {
            responseEntity = ResponseEntity.of(reservationService.findById(id));
        } catch (Exception exception) {
            logger.error("Exception occurred during fetching reservation with id: {}", id, exception);
            responseEntity = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return responseEntity;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> create(@RequestBody CreateReservationRequest createReservationRequest) {
        ResponseEntity<?> responseEntity;
        try {
            Reservation reservation =
                    new Reservation(createReservationRequest.getAmount(), createReservationRequest.getCurrency());
            responseEntity = ResponseEntity.ok(reservationService.create(reservation));
        } catch (NoAvailableRoomsException noAvailableRoomsException) {
            logger.error("Exception occurred during reservation creation", noAvailableRoomsException);
            responseEntity = new ResponseEntity<>(noAvailableRoomsException.getMessage(), HttpStatus.NOT_FOUND);
        }
        catch (Exception exception) {
            logger.error("Exception occurred during reservation creation", exception);
            responseEntity = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return responseEntity;
    }

    @PutMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> update(@PathVariable("id") Long id, @RequestBody Reservation reservation) {
        ResponseEntity<?> responseEntity;
        try {
            Preconditions.checkNotNull(reservation);
            Preconditions.checkNotNull(id);
            reservation.setId(id);
            reservation = reservationService.update(reservation);
            responseEntity = ResponseEntity.ok(reservation);
        } catch (Exception exception) {
            logger.error("Exception occurred during reservation update", exception);
            responseEntity = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return responseEntity;
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Reservation> delete(@PathVariable("id") Long id) {
        ResponseEntity<Reservation> responseEntity;
        try {
            Preconditions.checkNotNull(id);
            reservationService.delete(id);
            responseEntity = new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception exception) {
            logger.error("Exception occurred during reservation removal", exception);
            responseEntity = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return responseEntity;
    }
}
