package com.example.booking.reservation.dto;

import com.example.booking.core.Currency;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;


@JsonPropertyOrder({ "amount", "currency" })
public class CreateReservationRequest {

    @ApiModelProperty(notes = "Amount available for reservation", example = "100", required = true)
    private BigDecimal amount;
    @ApiModelProperty(notes = "Amount currency", example = "EUR")
    private Currency currency = Currency.EUR;

    public CreateReservationRequest(BigDecimal amount, Currency currency) {
        this.amount = amount;
        this.currency = currency;
    }

    public CreateReservationRequest() {
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
}
