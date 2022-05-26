package com.example.booking.reservation.model;


import com.example.booking.core.Currency;
import com.example.booking.room.model.Room;
import com.example.booking.room.model.RoomType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.common.base.MoreObjects;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity(name = "reservation")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "room_id", referencedColumnName = "id")
    private Room room;

    @Column(name = "amount")
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "currency")
    private Currency currency = Currency.EUR;

    public Reservation(BigDecimal amount, Currency currency) {
        this.amount = amount;
        this.currency = currency;
    }

    public Reservation(BigDecimal amount) {
        this.amount = amount;
    }

    public Reservation() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public boolean hasRoomType(RoomType roomType) {
        return room != null &&
                room.getRoomType().equals(roomType);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .add("room", room)
                .add("amount", amount)
                .add("currency", currency)
                .toString();
    }
}
