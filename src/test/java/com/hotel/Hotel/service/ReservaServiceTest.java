package com.hotel.Hotel.service;

import com.hotel.Hotel.domain.*;
import com.hotel.Hotel.dto.request.CrearReservaRequest;
import com.hotel.Hotel.dto.response.ReservaResponse;
import com.hotel.Hotel.exception.ReglaNegocioException;
import com.hotel.Hotel.mapper.ReservaMapper;
import com.hotel.Hotel.repository.ClienteRepository;
import com.hotel.Hotel.repository.HabitacionRepository;
import com.hotel.Hotel.repository.ReservaRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReservaServiceTest {

    @Mock
    private ReservaRepository reservaRepository;

    @Mock
    private ClienteRepository clienteRepository;

    @Mock
    private HabitacionRepository habitacionRepository;

    @Mock
    private ReservaMapper reservaMapper;

    @InjectMocks
    private ReservaService reservaService;

    @Nested
    @DisplayName("Operaciones de Creación")
    class CreacionTests {

        @Test
        @DisplayName("Debe crear exitosamente una reserva cuando los datos son válidos y el cliente está activo")
        void debeCrearReservaExitosamente() {
            // Arrange
            UUID clienteId = UUID.randomUUID();
            UUID habitacionId = UUID.randomUUID();
            Cliente cliente = new Cliente("Beatriz Morales", "beatriz@empresa.com");
            Habitacion habitacion = new HabitacionEstandar("201", 2, 200.0, 1);

            LocalDateTime fechaInicio = LocalDateTime.now().plusDays(1);
            LocalDateTime fechaFin = LocalDateTime.now().plusDays(3);
            CrearReservaRequest request = new CrearReservaRequest(clienteId, habitacionId, fechaInicio, fechaFin);

            ReservaResponse responseEsperado = new ReservaResponse(
                    UUID.randomUUID(),
                    "Beatriz Morales",
                    "201",
                    fechaInicio,
                    fechaFin,
                    400.0,
                    "PENDIENTE");

            // Stubs
            when(clienteRepository.findById(clienteId)).thenReturn(Optional.of(cliente));
            when(habitacionRepository.findById(habitacionId)).thenReturn(Optional.of(habitacion));
            when(reservaRepository.save(any(Reserva.class))).thenAnswer(invocation -> invocation.getArgument(0));
            when(reservaMapper.toResponse(any(Reserva.class))).thenReturn(responseEsperado);

            // Act
            ReservaResponse resultado = reservaService.crear(request);

            // Assert
            assertNotNull(resultado);
            assertEquals("PENDIENTE", resultado.estado());
            assertEquals("Beatriz Morales", resultado.nombreHuesped());
            assertEquals("201", resultado.habitacionNumero());

            // Verificación de comportamiento (Mocks)
            verify(clienteRepository, times(1)).findById(clienteId);
            verify(habitacionRepository, times(1)).findById(habitacionId);
            verify(reservaRepository, times(1)).save(any(Reserva.class));
            verify(reservaMapper, times(1)).toResponse(any(Reserva.class));
        }

        @Test
        @DisplayName("Debe lanzar ReglaNegocioException cuando la fecha de fin es anterior a la fecha de inicio")
        void debeLanzarExcepcionCuandoFechaFinEsAnteriorAFechaInicio() {
            // Arrange
            UUID clienteId = UUID.randomUUID();
            UUID habitacionId = UUID.randomUUID();
            LocalDateTime fechaInicio = LocalDateTime.now().plusDays(3);
            LocalDateTime fechaFin = LocalDateTime.now().plusDays(1);
            CrearReservaRequest request = new CrearReservaRequest(clienteId, habitacionId, fechaInicio, fechaFin);

            // Act & Assert
            ReglaNegocioException excepcion = assertThrows(
                    ReglaNegocioException.class,
                    () -> reservaService.crear(request));

            assertEquals("La fecha de salida no puede ser anterior a la fecha de entrada.", excepcion.getMessage());

            // Verificación de comportamiento (Mocks)
            verifyNoInteractions(clienteRepository);
            verifyNoInteractions(habitacionRepository);
            verifyNoInteractions(reservaRepository);
            verifyNoInteractions(reservaMapper);
        }

        @Test
        @DisplayName("Debe lanzar ReglaNegocioException cuando el cliente está inactivo o penalizado")
        void debeLanzarExcepcionCuandoClienteEstaInactivo() {
            // Arrange
            UUID clienteId = UUID.randomUUID();
            UUID habitacionId = UUID.randomUUID();
            Cliente cliente = new Cliente("Beatriz Morales", "beatriz@empresa.com");
            cliente.registrarPenalizacion();
            cliente.registrarPenalizacion();
            cliente.registrarPenalizacion(); // 3 penalizaciones desactivan al cliente (activo = false)

            Habitacion habitacion = new HabitacionEstandar("201", 2, 200.0, 1);
            CrearReservaRequest request = new CrearReservaRequest(
                    clienteId,
                    habitacionId,
                    LocalDateTime.now().plusDays(1),
                    LocalDateTime.now().plusDays(3));

            // Stubs
            when(clienteRepository.findById(clienteId)).thenReturn(Optional.of(cliente));
            when(habitacionRepository.findById(habitacionId)).thenReturn(Optional.of(habitacion));

            // Act & Assert
            ReglaNegocioException excepcion = assertThrows(
                    ReglaNegocioException.class,
                    () -> reservaService.crear(request));

            assertEquals("El cliente está inactivo o penalizado y no puede crear reservas.", excepcion.getMessage());

            // Verificación de comportamiento (Mocks)
            verify(clienteRepository, times(1)).findById(clienteId);
            verify(habitacionRepository, times(1)).findById(habitacionId);
            verify(reservaRepository, never()).save(any(Reserva.class));
            verifyNoInteractions(reservaMapper);
        }

    }

    @Nested
    @DisplayName("Operaciones de Confirmación")
    class ConfirmacionTests {

        @Test
        @DisplayName("Debe confirmar exitosamente cuando la reserva existe y está PENDIENTE")
        void debeConfirmarReservaExitosamente() {
            // Arrange
            UUID reservaId = UUID.randomUUID();
            Cliente cliente = new Cliente("Beatriz Morales", "beatriz@empresa.com");
            Habitacion habitacion = new HabitacionEstandar("201", 2, 200.0, 1);

            RangoFechas fechas = new RangoFechas(
                    LocalDateTime.now().plusDays(1),
                    LocalDateTime.now().plusDays(3));
            Reserva reservaExistente = new Reserva(cliente, habitacion, fechas);

            ReservaResponse responseEsperado = new ReservaResponse(
                    reservaId,
                    "Beatriz Morales",
                    "201",
                    fechas.fechaInicio(),
                    fechas.fechaFin(),
                    400.0,
                    "CONFIRMADA");

            // Stubs
            when(reservaRepository.findById(reservaId)).thenReturn(Optional.of(reservaExistente));
            when(reservaRepository.save(any(Reserva.class))).thenAnswer(invocation -> invocation.getArgument(0));
            when(reservaMapper.toResponse(any(Reserva.class))).thenReturn(responseEsperado);

            // Act
            ReservaResponse resultado = reservaService.confirmar(reservaId);

            // Assert
            assertNotNull(resultado);
            assertEquals("CONFIRMADA", resultado.estado());
            assertEquals(EstadoReserva.CONFIRMADA, reservaExistente.getEstado());

            // Verificación de comportamiento (Mocks)
            verify(reservaRepository, times(1)).findById(reservaId);
            verify(reservaRepository, times(1)).save(reservaExistente);
            verify(reservaMapper, times(1)).toResponse(reservaExistente);
        }

        @Test
        @DisplayName("Debe lanzar IllegalArgumentException al confirmar cuando la reserva no existe")
        void debeLanzarExcepcionCuandoReservaNoExisteAlConfirmar() {
            // Arrange
            UUID reservaId = UUID.randomUUID();

            // Stubs
            when(reservaRepository.findById(reservaId)).thenReturn(Optional.empty());

            // Act & Assert
            IllegalArgumentException excepcion = assertThrows(
                    IllegalArgumentException.class,
                    () -> reservaService.confirmar(reservaId));

            assertEquals("Reserva no encontrada", excepcion.getMessage());

            // Verificación de comportamiento (Mocks)
            verify(reservaRepository, times(1)).findById(reservaId);
            verify(reservaRepository, never()).save(any(Reserva.class));
            verifyNoInteractions(reservaMapper);
        }

    }

    @Nested
    @DisplayName("Operaciones de Cancelación")
    class CancelacionTests {

        @Test
        @DisplayName("Debe cancelar exitosamente una reserva y liberar la habitación")
        void debeCancelarReservaExitosamente() {
            // Arrange
            UUID reservaId = UUID.randomUUID();
            Cliente cliente = new Cliente("Beatriz Morales", "beatriz@empresa.com");
            Habitacion habitacion = new HabitacionEstandar("201", 2, 200.0, 1);

            RangoFechas fechas = new RangoFechas(
                    LocalDateTime.now().plusDays(1),
                    LocalDateTime.now().plusDays(3));
            Reserva reservaExistente = new Reserva(cliente, habitacion, fechas);
            reservaExistente.confirmar(); // La reserva pasa a CONFIRMADA y habitación a OCUPADA

            ReservaResponse responseEsperado = new ReservaResponse(
                    reservaId,
                    "Beatriz Morales",
                    "201",
                    fechas.fechaInicio(),
                    fechas.fechaFin(),
                    400.0,
                    "CANCELADA");

            // Stubs
            when(reservaRepository.findById(reservaId)).thenReturn(Optional.of(reservaExistente));
            when(reservaRepository.save(any(Reserva.class))).thenAnswer(invocation -> invocation.getArgument(0));
            when(reservaMapper.toResponse(any(Reserva.class))).thenReturn(responseEsperado);

            // Act
            ReservaResponse resultado = reservaService.cancelar(reservaId);

            // Assert
            assertNotNull(resultado);
            assertEquals("CANCELADA", resultado.estado());
            assertEquals(EstadoReserva.CANCELADA, reservaExistente.getEstado());
            assertEquals(EstadoHabitacion.DISPONIBLE, habitacion.getEstado());

            // Verificación de comportamiento (Mocks)
            verify(reservaRepository, times(1)).findById(reservaId);
            verify(reservaRepository, times(1)).save(reservaExistente);
            verify(reservaMapper, times(1)).toResponse(reservaExistente);
        }

    }
}