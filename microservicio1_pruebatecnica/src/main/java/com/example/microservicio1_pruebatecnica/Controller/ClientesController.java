package com.example.microservicio1_pruebatecnica.Controller;

import com.example.microservicio1_pruebatecnica.Model.Clientes;
import com.example.microservicio1_pruebatecnica.Service.ClientesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/clientes")
public class ClientesController {

    @Autowired
    private ClientesService clientesService;


    @PostMapping
    public ResponseEntity<?> crearClientes(@RequestBody List<Clientes> listaClientes) {
        try {
            List<Clientes> clientesGuardados = new ArrayList<>();
            for (Clientes cliente : listaClientes) {
                clientesGuardados.add(clientesService.crearUsuario(cliente));
            }
            return ResponseEntity.ok(clientesGuardados);
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error al crear los clientes: " + e.getMessage());
        }
    }


    @GetMapping
    public ResponseEntity<?> obtenerTodosClientes() {
        try {
            List<Clientes> clientes = clientesService.obtenerTodosUsuarios();
            return ResponseEntity.ok(clientes);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error al obtener los clientes: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerClientePorId(@PathVariable Long id) {
        try {
            Clientes cliente = clientesService.obtenerUsuarioPorId(id)
                    .orElseThrow(() -> new RuntimeException("Cliente no encontrado con id: " + id));
            return ResponseEntity.ok(cliente);
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error al obtener el cliente: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarCliente(@PathVariable Long id, @RequestBody Clientes clientesActualizado) {
        try {
            Clientes clienteActualizado = clientesService.actualizarUsuario(id, clientesActualizado);
            return ResponseEntity.ok(clienteActualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body("Cliente no encontrado con id: " + id);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error al actualizar el cliente: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarCliente(@PathVariable Long id) {
        try {
            clientesService.eliminarUsuario(id);
            return ResponseEntity.ok("Cliente eliminado correctamente");
        } catch (DataIntegrityViolationException e) {
             return ResponseEntity.badRequest().body("Error: No se puede eliminar el cliente porque tiene cuentas o movimientos asociados.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error al eliminar el cliente: " + e.getMessage());
        }
    }
}
