package com.hotel.Hotel.service;

import com.hotel.Hotel.domain.Cliente;
import com.hotel.Hotel.domain.Habitacion;
import com.hotel.Hotel.domain.RangoFechas;
import com.hotel.Hotel.domain.Reserva;
import com.hotel.Hotel.dto.request.CrearReservaRequest;
import com.hotel.Hotel.dto.response.ReservaResponse;
import com.hotel.Hotel.exception.RecursoNoEncontradoException;
import com.hotel.Hotel.exception.ReglaNegocioException;
import com.hotel.Hotel.mapper.ReservaMapper;
import com.hotel.Hotel.repository.ClienteRepository;
import com.hotel.Hotel.repository.HabitacionRepository;
import com.hotel.Hotel.repository.ReservaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class ReservaService {

    private final ReservaRepository reservaRepository;
    private final ClienteRepository clienteRepository;
    private final HabitacionRepository habitacionRepository;
    private final ReservaMapper reservaMapper;

    public ReservaService(ReservaRepository reservaRepository,
            ClienteRepository clienteRepository,
            HabitacionRepository habitacionRepository,
            ReservaMapper reservaMapper) {
        this.reservaRepository = reservaRepository;
        this.clienteRepository = clienteRepository;
        this.habitacionRepository = habitacionRepository;
        this.reservaMapper = reservaMapper;
    }

    @Transactional
    public ReservaResponse crear(CrearReservaRequest request) {
        if (request.fechaFin().isBefore(request.fechaInicio())) {
            throw new ReglaNegocioException("La fecha de salida no puede ser anterior a la fecha de entrada.");
        }

        Cliente cliente = clienteRepository.findById(request.clienteId())
                .orElseThrow(() -> new RecursoNoEncontradoException("Cliente", request.clienteId()));

        Habitacion habitacion = habitacionRepository.findById(request.habitacionId())
                .orElseThrow(() -> new RecursoNoEncontradoException("Habitacion", request.habitacionId()));

        if (!cliente.isActivo()) {
            throw new ReglaNegocioException("El cliente está inactivo o penalizado y no puede crear reservas.");
        }

        RangoFechas periodo = new RangoFechas(request.fechaInicio(), request.fechaFin());

        Reserva nuevaReserva = new Reserva(cliente, habitacion, periodo);
        Reserva guardada = reservaRepository.save(nuevaReserva);

        return reservaMapper.toResponse(guardada);
    }

    @Transactional
    public ReservaResponse confirmar(UUID reservaId) {
        Reserva reserva = reservaRepository.findById(reservaId)
                .orElseThrow(() -> new IllegalArgumentException("Reserva no encontrada"));

        reserva.confirmar();
        return reservaMapper.toResponse(reservaRepository.save(reserva));
    }

    @Transactional
    public ReservaResponse cancelar(UUID reservaId) {
        Reserva reserva = reservaRepository.findById(reservaId)
                .orElseThrow(() -> new IllegalArgumentException("Reserva no encontrada"));

        reserva.cancelar();
        return reservaMapper.toResponse(reservaRepository.save(reserva));
    }

    @Transactional(readOnly = true)
    public ReservaResponse obtenerPorId(UUID id) {
        Reserva reserva = reservaRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Reserva", id));
        return reservaMapper.toResponse(reserva);
    }

    @Transactional(readOnly = true)
    public List<ReservaResponse> listarTodas() {
        return reservaMapper.toResponseList(reservaRepository.findAll());
    }
}