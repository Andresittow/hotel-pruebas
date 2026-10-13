package com.hotel.Hotel.domain;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "habitaciones")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Habitacion {

    @Id
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(nullable = false, unique = true, length = 20)
    private String numero;

    @Column(name = "capacidad_maxima", nullable = false)
    private int capacidadMaxima;

    @Column(name = "precio_por_noche", nullable = false)
    private double precioPorNoche;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoHabitacion estado;

    protected Habitacion() {
    }

    public Habitacion(String numero, int capacidadMaxima, double precioPorNoche) {
        if (numero == null || numero.isBlank())
            throw new IllegalArgumentException("Número obligatorio");
        if (capacidadMaxima < 1)
            throw new IllegalArgumentException("Capacidad mínima: 1");
        if (precioPorNoche < 0)
            throw new IllegalArgumentException("Precio no puede ser negativo");

        this.id = UUID.randomUUID();
        this.numero = numero;
        this.capacidadMaxima = capacidadMaxima;
        this.precioPorNoche = precioPorNoche;
        this.estado = EstadoHabitacion.DISPONIBLE;
    }

    public void asignarAReserva() {
        if (this.estado != EstadoHabitacion.DISPONIBLE) {
            throw new IllegalStateException("La habitación no está disponible");
        }
        this.estado = EstadoHabitacion.OCUPADA;
    }

    public void habilitar() {
        this.estado = EstadoHabitacion.DISPONIBLE;
    }

    public UUID getId() {
        return id;
    }

    public String getNumero() {
        return numero;
    }

    public int getCapacidadMaxima() {
        return capacidadMaxima;
    }

    public double getPrecioPorNoche() {
        return precioPorNoche;
    }

    public EstadoHabitacion getEstado() {
        return estado;
    }
}
