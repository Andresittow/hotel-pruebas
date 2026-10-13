package com.hotel.Hotel.mapper;

import com.hotel.Hotel.domain.Reserva;
import com.hotel.Hotel.dto.response.ReservaResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ReservaMapper {

    @Mapping(target = "estado", expression = "java(reserva.getEstado().name())")
    ReservaResponse toResponse(Reserva reserva);

    List<ReservaResponse> toResponseList(List<Reserva> reservas);
}