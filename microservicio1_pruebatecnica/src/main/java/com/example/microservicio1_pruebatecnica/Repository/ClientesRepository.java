package com.example.microservicio1_pruebatecnica.Repository;

import com.example.microservicio1_pruebatecnica.Model.Clientes;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientesRepository extends JpaRepository<Clientes, Long> {

 }
