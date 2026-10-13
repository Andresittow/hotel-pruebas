package com.hotel.Hotel.dto.response;

import java.time.LocalDateTime;
import java.util.Map;

public record ErrorResponse(
        LocalDateTime timestamp,
        int codigoHttp,
        String estado,
        String mensaje,
        String ruta,
        Map<String, String> detallesValidacion) {
    // Constructor de conveniencia para errores simples de negocio
    public static ErrorResponse deError(int codigoHttp, String estado, String mensaje, String ruta) {
        return new ErrorResponse(LocalDateTime.now(), codigoHttp, estado, mensaje, ruta, null);
    }

    // Constructor para fallos de Bean Validation en campos múltiples
    public static ErrorResponse deValidacion(int codigoHttp, String estado, String mensaje, String ruta,
            Map<String, String> errores) {
        return new ErrorResponse(LocalDateTime.now(), codigoHttp, estado, mensaje, ruta, errores);
    }
}
