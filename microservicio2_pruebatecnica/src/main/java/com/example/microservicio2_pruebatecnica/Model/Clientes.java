package com.example.microservicio2_pruebatecnica.Model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "clientes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Clientes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String contrasena;
    private boolean estado;

    @OneToOne
    @JoinColumn(name = "persona_id", referencedColumnName = "id", nullable = false, unique = true)
    private Persona persona;
}