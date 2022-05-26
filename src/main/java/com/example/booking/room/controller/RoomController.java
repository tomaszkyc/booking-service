package com.example.booking.room.controller;


import com.example.booking.room.model.Room;
import com.example.booking.room.service.RoomService;
import com.google.common.base.Preconditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("v1/rooms")
public class RoomController {

    private static final Logger logger = LoggerFactory.getLogger(RoomController.class);

    private final RoomService roomService;

    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }


    @GetMapping
    public ResponseEntity<List<Room>> findAll() {
        ResponseEntity<List<Room>> responseEntity;
        try {
            responseEntity = ResponseEntity.ok(roomService.findAll());
        } catch (Exception exception) {
            logger.error("Exception occurred during rooms fetching", exception);
            responseEntity = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return responseEntity;
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Room> findById(@PathVariable("id") Long id) {
        ResponseEntity<Room> responseEntity;
        try {
            responseEntity = ResponseEntity.of(roomService.findById(id));
        } catch (Exception exception) {
            logger.error("Exception occurred during room with id: {} fetching", id, exception);
            responseEntity = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return responseEntity;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Room> create(@RequestBody Room room) {
        ResponseEntity<Room> responseEntity;
        try {
            responseEntity = ResponseEntity.ok(roomService.create(room));
        } catch (Exception exception) {
            logger.error("Exception occurred during room creation", exception);
            responseEntity = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return responseEntity;
    }

    @PutMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> update(@PathVariable("id") Long id, @RequestBody Room room) {
        ResponseEntity<?> responseEntity;
        try {
            Preconditions.checkNotNull(room);
            Preconditions.checkNotNull(id);
            room.setId(id);
            roomService.create(room);
            responseEntity = new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception exception) {
            logger.error("Exception occurred during room update", exception);
            responseEntity = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return responseEntity;
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Room> delete(@PathVariable("id") Long id) {
        ResponseEntity<Room> responseEntity;
        try {
            Preconditions.checkNotNull(id);
            roomService.delete(id);
            responseEntity = new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception exception) {
            logger.error("Exception occurred during room update", exception);
            responseEntity = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return responseEntity;
    }
}
