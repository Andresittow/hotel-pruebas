package com.hotel.Hotel.exception;

public class HabitacionNoDisponibleException extends HotelDomainException {
    public HabitacionNoDisponibleException(String numeroHabitacion) {
        super(String.format("La habitación %s no está disponible en las fechas seleccionadas.", numeroHabitacion));
    }
}
