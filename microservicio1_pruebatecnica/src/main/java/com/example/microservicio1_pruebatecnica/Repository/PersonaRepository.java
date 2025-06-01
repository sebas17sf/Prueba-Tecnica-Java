package com.example.microservicio1_pruebatecnica.Repository;

import com.example.microservicio1_pruebatecnica.Model.Persona;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonaRepository extends JpaRepository<Persona, Long> {
 boolean existsByIdentificacion(String identificacion);

}
