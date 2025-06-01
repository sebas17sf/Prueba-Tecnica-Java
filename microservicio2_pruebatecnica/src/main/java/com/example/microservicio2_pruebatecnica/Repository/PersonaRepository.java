package com.example.microservicio2_pruebatecnica.Repository;

import com.example.microservicio2_pruebatecnica.Model.Persona;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonaRepository extends JpaRepository<Persona, Long> {
 boolean existsByIdentificacion(String identificacion);

}
