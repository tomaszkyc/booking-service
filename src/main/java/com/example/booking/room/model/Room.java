package com.example.booking.room.model;

import com.example.booking.hotel.model.Hotel;
import com.example.booking.reservation.model.Reservation;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

import javax.persistence.*;

@Entity(name = "room")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "room_type")
    private RoomType roomType;

    @Column(name = "capacity")
    private Short capacity = 1;

    @Column(name = "name")
    private String name;

    @Column(name = "reserved")
    private boolean reserved = false;

    @ManyToOne
    @JoinColumn(name = "hotel_id")
    private Hotel hotel;

    @OneToOne(mappedBy = "room")
    @JsonIgnore
    private Reservation reservation;

    public Room(Long id, RoomType roomType) {
        this.id = id;
        this.roomType = roomType;
    }

    public Room(RoomType roomType) {
        this.roomType = roomType;
    }

    public Room() {
    }

    @JsonProperty("reservationId")
    public Long getReservationId() {
        if (reservation == null) {
            return null;
        }
        return reservation.getId();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public RoomType getRoomType() {
        return roomType;
    }

    public void setRoomType(RoomType roomType) {
        this.roomType = roomType;
    }

    public Short getCapacity() {
        return capacity;
    }

    public void setCapacity(Short capacity) {
        this.capacity = capacity;
    }

    public boolean isReserved() {
        return reserved;
    }

    public boolean isNotReserved() {
        return !isReserved();
    }

    public void setReserved(boolean reserved) {
        this.reserved = reserved;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Hotel getHotel() {
        return hotel;
    }

    public void setHotel(Hotel hotel) {
        this.hotel = hotel;
    }

    public Reservation getReservation() {
        return reservation;
    }

    public void setReservation(Reservation reservation) {
        this.reservation = reservation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Room room = (Room) o;
        return reserved == room.reserved && Objects.equal(id, room.id) && roomType == room.roomType && Objects.equal(capacity, room.capacity) && Objects.equal(name, room.name);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id, roomType, capacity, name, reserved);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .add("roomType", roomType)
                .add("capacity", capacity)
                .add("name", name)
                .add("reserved", reserved)
                .toString();
    }
}
