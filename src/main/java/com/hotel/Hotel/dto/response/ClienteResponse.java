package com.hotel.Hotel.dto.response;

import java.util.UUID;

public record ClienteResponse(
        UUID id,
        String nombre,
        String email,
        boolean activo,
        int penalizaciones) {
}
