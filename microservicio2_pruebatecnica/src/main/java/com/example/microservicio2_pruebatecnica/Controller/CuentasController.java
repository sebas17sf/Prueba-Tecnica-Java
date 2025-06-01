package com.example.microservicio2_pruebatecnica.Controller;

import com.example.microservicio2_pruebatecnica.Model.Cuentas;
import com.example.microservicio2_pruebatecnica.Service.CuentasService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cuentas")
public class CuentasController {

    @Autowired
    private CuentasService cuentasService;


    @PostMapping("/{clienteId}")
    public ResponseEntity<?> crearCuenta(@PathVariable Long clienteId, @RequestBody Cuentas cuenta) {
        try {
            Cuentas nuevaCuenta = cuentasService.crearCuenta(clienteId, cuenta);
            return ResponseEntity.ok(nuevaCuenta);
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al crear la cuenta: " + e.getMessage());
        }
    }



    @GetMapping
    public ResponseEntity<?> obtenerTodasCuentas() {
        try {
            List<Cuentas> cuentas = cuentasService.obtenerTodasCuentas();
            return ResponseEntity.ok(cuentas);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error al obtener las cuentas: " + e.getMessage());
        }
    }


    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerCuentaPorId(@PathVariable Long id) {
        try {
            Cuentas cuenta = cuentasService.obtenerCuentaPorId(id)
                    .orElseThrow(() -> new RuntimeException("Cuenta no encontrada con id: " + id));
            return ResponseEntity.ok(cuenta);
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error al obtener la cuenta: " + e.getMessage());
        }
    }


    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarCuenta(@PathVariable Long id, @RequestBody Cuentas cuentaActualizada) {
        try {
            Cuentas cuenta = cuentasService.actualizarCuenta(id, cuentaActualizada);
            return ResponseEntity.ok(cuenta);
        } catch (DataIntegrityViolationException e) {
             return ResponseEntity.badRequest().body("El número de cuenta ya existe. Por favor use otro número.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error al actualizar la cuenta: " + e.getMessage());
        }
    }



    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarCuenta(@PathVariable Long id) {
        try {
            cuentasService.eliminarCuenta(id);
            return ResponseEntity.ok("Cuenta eliminada correctamente");
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.badRequest().body("Error: No se puede eliminar la cuenta porque tiene movimientos asociados.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error al eliminar la cuenta: " + e.getMessage());
        }
    }
}
