package com.hotel.Hotel.exception;

import java.util.UUID;

public class RecursoNoEncontradoException extends HotelDomainException {
    public RecursoNoEncontradoException(String recurso, UUID id) {
        super(String.format("%s con identificador %s no fue encontrado en el sistema.", recurso, id));
    }

    public RecursoNoEncontradoException(String mensaje) {
        super(mensaje);
    }
}