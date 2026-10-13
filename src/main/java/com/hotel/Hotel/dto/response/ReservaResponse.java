package com.hotel.Hotel.dto.response;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public record ReservaResponse(
                UUID id,
                String nombreHuesped,
                String habitacionNumero,
                LocalDateTime fechaInicio,
                LocalDateTime fechaFin,
                double costoTotal,
                String estado) {
}
