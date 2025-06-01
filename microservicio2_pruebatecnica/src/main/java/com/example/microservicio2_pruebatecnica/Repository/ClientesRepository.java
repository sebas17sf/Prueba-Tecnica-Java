package com.example.microservicio2_pruebatecnica.Repository;

import com.example.microservicio2_pruebatecnica.Model.Clientes;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientesRepository extends JpaRepository<Clientes, Long> {

 }
