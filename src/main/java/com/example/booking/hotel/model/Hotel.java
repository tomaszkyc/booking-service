package com.example.booking.hotel.model;

import com.example.booking.room.model.Room;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.common.base.Objects;

import javax.persistence.*;
import java.util.Set;

@Entity(name = "hotel")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class Hotel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = "hotel")
    private Set<Room> rooms;

    public Hotel(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Hotel() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Room> getRooms() {
        return rooms;
    }

    public void setRooms(Set<Room> rooms) {
        this.rooms = rooms;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Hotel hotel = (Hotel) o;
        return Objects.equal(id, hotel.id) && Objects.equal(name, hotel.name);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id, name);
    }
}
