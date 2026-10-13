package com.hotel.Hotel.domain;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "reservas")
public class Reserva {

    @Id
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "habitacion_id", nullable = false)
    private Habitacion habitacion;

    @Embedded
    private RangoFechas periodo;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false)
    private EstadoReserva estado;

    @Column(name = "costo_total", nullable = false)
    private double costoTotal;

    protected Reserva() {
    }

    public Reserva(Cliente cliente, Habitacion habitacion, RangoFechas periodo) {
        if (cliente == null)
            throw new IllegalArgumentException("Cliente obligatorio");
        if (habitacion == null)
            throw new IllegalArgumentException("Habitación obligatoria");
        if (periodo == null)
            throw new IllegalArgumentException("Periodo obligatorio");

        this.id = UUID.randomUUID();
        this.cliente = cliente;
        this.habitacion = habitacion;
        this.periodo = periodo;
        this.estado = EstadoReserva.PENDIENTE;
        this.costoTotal = habitacion.getPrecioPorNoche() * Math.max(1, periodo.getDias());
    }

    public void confirmar() {
        this.habitacion.asignarAReserva();
        this.estado = EstadoReserva.CONFIRMADA;
    }

    public void cancelar() {
        this.habitacion.habilitar();
        this.estado = EstadoReserva.CANCELADA;
    }

    public UUID getId() {
        return id;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public Habitacion getHabitacion() {
        return habitacion;
    }

    public RangoFechas getPeriodo() {
        return periodo;
    }

    public EstadoReserva getEstado() {
        return estado;
    }

    public double getCostoTotal() {
        return costoTotal;
    }
}