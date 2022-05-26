package com.example.booking.reservation.dto;

import com.example.booking.room.model.RoomType;

import java.util.Map;


public class ReservationSummary {

    private Map<RoomType, Map<String, Object>> roomTypeStatistics;

    public ReservationSummary(Map<RoomType, Map<String, Object>> roomTypeStatistics) {
        this.roomTypeStatistics = roomTypeStatistics;
    }

    public Map<RoomType, Map<String, Object>> getRoomTypeStatistics() {
        return roomTypeStatistics;
    }

    public void setRoomTypeStatistics(Map<RoomType, Map<String, Object>> roomTypeStatistics) {
        this.roomTypeStatistics = roomTypeStatistics;
    }
}
