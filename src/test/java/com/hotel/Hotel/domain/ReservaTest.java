package com.hotel.Hotel.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class ReservaTest {

    @Test
    @DisplayName("Debe confirmar una reserva en estado PENDIENTE y cambiar estado de habitación")
    void debeConfirmarReservaPendiente() {
        // Arrange
        Cliente cliente = new Cliente("Carlos Gómez", "carlos@correo.com");

        // Instanciamos la subclase concreta
        Habitacion habitacion = new HabitacionEstandar("101", 2, 100.0, 1);

        RangoFechas periodo = new RangoFechas(
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(4));
        Reserva reserva = new Reserva(cliente, habitacion, periodo);

        // Act
        reserva.confirmar();

        // Assert
        assertEquals(EstadoReserva.CONFIRMADA, reserva.getEstado());
        assertEquals(EstadoHabitacion.OCUPADA, habitacion.getEstado());
    }
}