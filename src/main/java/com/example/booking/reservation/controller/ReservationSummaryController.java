package com.example.booking.reservation.controller;


import com.example.booking.reservation.dto.ReservationSummary;
import com.example.booking.reservation.service.ReservationService;
import com.example.booking.reservation.service.ReservationSummaryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("v1/reservations-summary")
public class ReservationSummaryController {

    private static final Logger logger = LoggerFactory.getLogger(ReservationSummaryController.class);

    private final ReservationSummaryService reservationSummaryService;

    public ReservationSummaryController(ReservationService reservationService,
                                        ReservationSummaryService reservationSummaryService) {
        this.reservationSummaryService = reservationSummaryService;
    }

    @GetMapping
    public ResponseEntity<ReservationSummary> find() {
        ResponseEntity<ReservationSummary> responseEntity;
        try {
            responseEntity = ResponseEntity.ok(reservationSummaryService.getReservationSummary());
        } catch (Exception exception) {
            logger.error("Exception occurred during reservations fetching", exception);
            responseEntity = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return responseEntity;
    }


}
