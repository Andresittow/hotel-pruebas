package com.hotel.Hotel.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "habitaciones_estandar")
public class HabitacionEstandar extends Habitacion {

    @Column(name = "camas_individuales", nullable = false)
    private int camasIndividuales;

    protected HabitacionEstandar() {
    }

    public HabitacionEstandar(String numero, int capacidadMaxima, double precioPorNoche, int camasIndividuales) {
        super(numero, capacidadMaxima, precioPorNoche);
        this.camasIndividuales = camasIndividuales;
    }

    public int getCamasIndividuales() {
        return camasIndividuales;
    }
}
