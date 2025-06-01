package com.example.microservicio1_pruebatecnica.Model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "personas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Persona {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private String genero;
    private Integer edad;

    @Column(unique = true, nullable = false)
    private String identificacion;
    private String direccion;
    private Integer telefono;
}
