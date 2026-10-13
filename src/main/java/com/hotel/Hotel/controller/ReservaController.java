package com.hotel.Hotel.controller;

import com.hotel.Hotel.dto.request.CrearReservaRequest;
import com.hotel.Hotel.dto.response.ReservaResponse;
import com.hotel.Hotel.service.ReservaService;
import jakarta.validation.Valid; //import obligatorio
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/reservas")
public class ReservaController {

    private final ReservaService reservaService;

    public ReservaController(ReservaService reservaService) {
        this.reservaService = reservaService;
    }

    @PostMapping
    public ResponseEntity<ReservaResponse> crearReserva(
            @Valid @RequestBody CrearReservaRequest request, // @Valid
            UriComponentsBuilder uriBuilder) {

        ReservaResponse creada = reservaService.crear(request);
        URI ruta = uriBuilder.path("/api/reservas/{id}").buildAndExpand(creada.id()).toUri();
        return ResponseEntity.created(ruta).body(creada);
    }

    @GetMapping
    public ResponseEntity<List<ReservaResponse>> listarReservas() {
        return ResponseEntity.ok(reservaService.listarTodas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReservaResponse> buscarPorId(@PathVariable UUID id) {
        // No requiere try-catch: si el service lanza RecursoNoEncontradoException,
        // el @RestControllerAdvice lo captura en el aire y devuelve HTTP 404
        return ResponseEntity.ok(reservaService.obtenerPorId(id));
    }
}