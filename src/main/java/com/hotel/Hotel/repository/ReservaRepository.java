package com.hotel.Hotel.repository;

import com.hotel.Hotel.domain.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.UUID;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva, UUID> {
    List<Reserva> findByClienteId(UUID clienteId);
}
