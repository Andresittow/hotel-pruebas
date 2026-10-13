package com.hotel.Hotel.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "suites_presidenciales")
public class SuitePresidencial extends Habitacion {

    @Column(name = "incluye_mayordomo", nullable = false)
    private boolean incluyeMayordomo;

    @Column(name = "jacuzzi_privado", nullable = false)
    private boolean jacuzziPrivado;

    protected SuitePresidencial() {
    }

    public SuitePresidencial(String numero, int capacidadMaxima, double precioPorNoche, boolean mayordomo,
            boolean jacuzzi) {
        super(numero, capacidadMaxima, precioPorNoche);
        this.incluyeMayordomo = mayordomo;
        this.jacuzziPrivado = jacuzzi;
    }

    public boolean isIncluyeMayordomo() {
        return incluyeMayordomo;
    }

    public boolean isJacuzziPrivado() {
        return jacuzziPrivado;
    }
}
