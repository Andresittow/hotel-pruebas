package com.hotel.Hotel.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Embeddable
public record RangoFechas(
        @Column(name = "fecha_inicio", nullable = false) LocalDateTime fechaInicio,

        @Column(name = "fecha_fin", nullable = false) LocalDateTime fechaFin) {
    public RangoFechas {
        if (fechaInicio == null || fechaFin == null) {
            throw new IllegalArgumentException("Las fechas no pueden ser nulas");
        }
        if (fechaFin.isBefore(fechaInicio)) {
            throw new IllegalArgumentException("La fecha fin no puede ser anterior a la de inicio");
        }
    }

    public long getDias() {
        return ChronoUnit.DAYS.between(fechaInicio, fechaFin);
    }
}
