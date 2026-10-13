package com.hotel.Hotel.dto.request;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public record CrearReservaRequest(
    @NotNull(message = "El identificador del cliente es obligatorio.")
    UUID clienteId,

    @NotNull(message = "El identificador de la habitación es obligatorio.")
    UUID habitacionId,

    @NotNull(message = "La fecha de inicio es obligatoria.")
    @FutureOrPresent(message = "La fecha de inicio debe ser hoy o una fecha futura.")
    LocalDateTime fechaInicio,

    @NotNull(message = "La fecha de finalización es obligatoria.")
    @Future(message = "La fecha de finalización debe ser posterior a hoy.")
    LocalDateTime fechaFin
) {}
