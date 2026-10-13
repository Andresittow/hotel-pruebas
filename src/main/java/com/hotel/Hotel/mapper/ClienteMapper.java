package com.hotel.Hotel.mapper;

import com.hotel.Hotel.domain.Cliente;
import com.hotel.Hotel.dto.request.CrearClienteRequest;
import com.hotel.Hotel.dto.response.ClienteResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import java.util.List;

@Mapper(componentModel = "spring")
public interface ClienteMapper {

    ClienteResponse toResponse(Cliente cliente);

    List<ClienteResponse> toResponseList(List<Cliente> clientes);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "activo", ignore = true)
    @Mapping(target = "penalizaciones", ignore = true)
    @Mapping(target = "reservas", ignore = true)
    Cliente toEntity(CrearClienteRequest request);
}
