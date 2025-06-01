package com.example.microservicio2_pruebatecnica.Repository;

import com.example.microservicio2_pruebatecnica.Model.Cuentas;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CuentaRepository extends JpaRepository<Cuentas, Long> {
 }
