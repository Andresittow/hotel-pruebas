package com.hotel.Hotel.exception;

public abstract class HotelDomainException extends RuntimeException {
    protected HotelDomainException(String mensaje) {
        super(mensaje);
    }
}
