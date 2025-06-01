package com.example.microservicio1_pruebatecnica.Unit;

import com.example.microservicio1_pruebatecnica.Model.Clientes;
import com.example.microservicio1_pruebatecnica.Model.Persona;
import com.example.microservicio1_pruebatecnica.Repository.ClientesRepository;
import com.example.microservicio1_pruebatecnica.Repository.PersonaRepository;
import com.example.microservicio1_pruebatecnica.Service.ClientesService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ClientesTest {

    private ClientesService clientesService;
    private ClientesRepository clientesRepository;
    private PersonaRepository personaRepository;

    @BeforeEach
    public void setUp() {
        clientesRepository = mock(ClientesRepository.class);
        personaRepository = mock(PersonaRepository.class);

        clientesService = new ClientesService();
        clientesService.setClientesRepository(clientesRepository);
        clientesService.setPersonaRepository(personaRepository);
    }

    @Test
    public void testCrearCliente() {
        Persona persona = new Persona(1L, "Sebastian", "Masculino", 22, "12345", "Quito", 987654321);
        Clientes cliente = new Clientes(null, "1234", true, persona);

        when(personaRepository.existsByIdentificacion("12345")).thenReturn(false);
        when(personaRepository.save(any(Persona.class))).thenReturn(persona);
        when(clientesRepository.save(any(Clientes.class))).thenReturn(cliente);

        Clientes creado = clientesService.crearUsuario(cliente);

        assertNotNull(creado);
        assertEquals("Sebastian", creado.getPersona().getNombre());
        assertEquals("12345", creado.getPersona().getIdentificacion());
        assertEquals("1234", creado.getContrasena());
        assertTrue(creado.isEstado());

        verify(personaRepository).existsByIdentificacion("12345");
        verify(personaRepository).save(persona);
        verify(clientesRepository).save(cliente);

        verifyNoMoreInteractions(personaRepository, clientesRepository);
    }

    @Test
    public void testCrearClienteConIdentificacionDuplicada() {
        Persona persona = new Persona();
        persona.setIdentificacion("12345");

        Clientes cliente = new Clientes();
        cliente.setPersona(persona);

        when(personaRepository.existsByIdentificacion("12345")).thenReturn(true);

        assertThrows(DataIntegrityViolationException.class, () -> {
            clientesService.crearUsuario(cliente);
        });

        verify(personaRepository).existsByIdentificacion("12345");
        verify(personaRepository, never()).save(any(Persona.class));
        verify(clientesRepository, never()).save(any(Clientes.class));
    }

    @Test
    public void testActualizarCliente() {
        Persona personaExistente = new Persona(1L, "Sebastian", "Masculino", 22, "12345", "Quito", 987654321);
        Clientes clienteExistente = new Clientes(1L, "1234", true, personaExistente);

        Persona personaActualizada = new Persona(1L, "Sebastian Actualizado", "Masculino", 23, "54321", "Ambato", 987654321);
        Clientes clienteActualizado = new Clientes(1L, "5678", false, personaActualizada);

        when(clientesRepository.findById(1L)).thenReturn(Optional.of(clienteExistente));
        when(personaRepository.save(any(Persona.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(clientesRepository.save(any(Clientes.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Clientes actualizado = clientesService.actualizarUsuario(1L, clienteActualizado);

        assertEquals("Sebastian Actualizado", actualizado.getPersona().getNombre());
        assertEquals("54321", actualizado.getPersona().getIdentificacion());
        assertEquals("5678", actualizado.getContrasena());
        assertFalse(actualizado.isEstado());

        verify(clientesRepository).findById(1L);
        verify(personaRepository).save(personaExistente);
        verify(clientesRepository).save(clienteExistente);
        verifyNoMoreInteractions(personaRepository, clientesRepository);
    }

    @Test
    public void testEliminarCliente() {
        Persona persona = new Persona(1L, "Sebastian", "Masculino", 22, "12345", "Quito", 987654321);
        Clientes cliente = new Clientes(1L, "1234", true, persona);

        when(clientesRepository.findById(1L)).thenReturn(Optional.of(cliente));
        doNothing().when(clientesRepository).delete(cliente);
        doNothing().when(personaRepository).delete(persona);

        clientesService.eliminarUsuario(1L);

        verify(clientesRepository).findById(1L);
        verify(clientesRepository).delete(cliente);
        verify(personaRepository).delete(persona);
        verifyNoMoreInteractions(personaRepository, clientesRepository);
    }
}
