package com.hotel.Hotel.exception;

import com.hotel.Hotel.dto.response.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 1. Manejo polimórfico de Recurso No Encontrado -> HTTP 404
    @ExceptionHandler(RecursoNoEncontradoException.class)
    public ResponseEntity<ErrorResponse> manejarRecursoNoEncontrado(
            RecursoNoEncontradoException ex, HttpServletRequest request) {

        ErrorResponse error = ErrorResponse.deError(
                HttpStatus.NOT_FOUND.value(),
                HttpStatus.NOT_FOUND.getReasonPhrase(),
                ex.getMessage(),
                request.getRequestURI());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    // 2. Manejo de Reglas de Negocio / Invariantes violados -> HTTP 400
    @ExceptionHandler(ReglaNegocioException.class)
    public ResponseEntity<ErrorResponse> manejarReglaNegocio(
            ReglaNegocioException ex, HttpServletRequest request) {

        ErrorResponse error = ErrorResponse.deError(
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                ex.getMessage(),
                request.getRequestURI());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    // 3. Manejo de Conflictos de Disponibilidad -> HTTP 409
    @ExceptionHandler(HabitacionNoDisponibleException.class)
    public ResponseEntity<ErrorResponse> manejarConflictoHabitacion(
            HabitacionNoDisponibleException ex, HttpServletRequest request) {

        ErrorResponse error = ErrorResponse.deError(
                HttpStatus.CONFLICT.value(),
                HttpStatus.CONFLICT.getReasonPhrase(),
                ex.getMessage(),
                request.getRequestURI());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    // 4. Manejo de Validaciones de Formato Bean Validation (@Valid) -> HTTP 400
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> manejarValidacionesDto(
            MethodArgumentNotValidException ex, HttpServletRequest request) {

        Map<String, String> erroresPorCampo = new HashMap<>();
        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            erroresPorCampo.put(fieldError.getField(), fieldError.getDefaultMessage());
        }

        ErrorResponse error = ErrorResponse.deValidacion(
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                "Error en la validación de los datos enviados.",
                request.getRequestURI(),
                erroresPorCampo);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    // 5. Red de seguridad para Errores Inesperados -> HTTP 500 (Ocultando detalles
    // internos)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> manejarErrorGenerico(
            Exception ex, HttpServletRequest request) {

        ErrorResponse error = ErrorResponse.deError(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                "Ocurrió un error interno no controlado. Comuníquese con soporte.",
                request.getRequestURI());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}
