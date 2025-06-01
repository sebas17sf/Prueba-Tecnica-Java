package com.example.microservicio1_pruebatecnica.Service;

import com.example.microservicio1_pruebatecnica.Model.Clientes;
import com.example.microservicio1_pruebatecnica.Model.Persona;
import com.example.microservicio1_pruebatecnica.Repository.ClientesRepository;
import com.example.microservicio1_pruebatecnica.Repository.PersonaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClientesService {

    @Autowired
    private ClientesRepository clientesRepository;

    @Autowired
    private PersonaRepository personaRepository;

    public Clientes crearUsuario(Clientes cliente) {
        if (personaRepository.existsByIdentificacion(cliente.getPersona().getIdentificacion())) {
            throw new DataIntegrityViolationException("La identificación ya está registrada para otra persona.");
        }
         Persona personaGuardada = personaRepository.save(cliente.getPersona());
         cliente.setPersona(personaGuardada);
         return clientesRepository.save(cliente);
    }

     public List<Clientes> obtenerTodosUsuarios() {
        return clientesRepository.findAll();
    }

     public Optional<Clientes> obtenerUsuarioPorId(Long id) {
        return clientesRepository.findById(id);
    }

     public Clientes actualizarUsuario(Long id, Clientes clienteActualizado) {
        Clientes clienteExistente = clientesRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado con id: " + id));

        Persona personaExistente = clienteExistente.getPersona();
        Persona personaActualizada = clienteActualizado.getPersona();

        personaExistente.setNombre(personaActualizada.getNombre());
        personaExistente.setGenero(personaActualizada.getGenero());
        personaExistente.setEdad(personaActualizada.getEdad());
        personaExistente.setIdentificacion(personaActualizada.getIdentificacion());
        personaExistente.setDireccion(personaActualizada.getDireccion());
        personaExistente.setTelefono(personaActualizada.getTelefono());

        personaRepository.save(personaExistente);

        clienteExistente.setContrasena(clienteActualizado.getContrasena());
        clienteExistente.setEstado(clienteActualizado.isEstado());

        return clientesRepository.save(clienteExistente);
    }

     public void eliminarUsuario(Long id) {
        Clientes cliente = clientesRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado con id: " + id));
        clientesRepository.delete(cliente);
        personaRepository.delete(cliente.getPersona());
    }



    ///implementacion para mi test
    public void setClientesRepository(ClientesRepository clientesRepository) {
        this.clientesRepository = clientesRepository;
    }

    public void setPersonaRepository(PersonaRepository personaRepository) {
        this.personaRepository = personaRepository;
    }
}
