package com.hotel.Hotel.repository;

import com.hotel.Hotel.domain.Habitacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface HabitacionRepository extends JpaRepository<Habitacion, UUID> {
    Optional<Habitacion> findByNumero(String numero);
}
