package com.hotel.Hotel.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CrearClienteRequest(
    @NotBlank(message = "El nombre del cliente no puede estar vacío.")
    @Size(min = 3, max = 100, message = "El nombre debe tener entre 3 y 100 caracteres.")
    String nombre,

    @NotBlank(message = "El correo electrónico es obligatorio.")
    @Email(message = "El formato del correo electrónico es inválido.")
    String email
) {}
