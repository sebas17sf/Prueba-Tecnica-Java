package com.example.microservicio2_pruebatecnica.Repository;

import com.example.microservicio2_pruebatecnica.Model.Movimientos;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDateTime;
import java.util.List;

public interface MovimientosRepository extends JpaRepository<Movimientos, Long> {

    List<Movimientos> findByCuentasClienteIdAndFechaBetween(Long clienteId, LocalDateTime fechaInicio, LocalDateTime fechaFin);

    List<Movimientos> findByFechaBetween(LocalDateTime fechaInicio, LocalDateTime fechaFin);

}
