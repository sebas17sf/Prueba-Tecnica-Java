package com.example.microservicio2_pruebatecnica.Controller;

import com.example.microservicio2_pruebatecnica.Model.Movimientos;
import com.example.microservicio2_pruebatecnica.Service.MovimientosService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/movimientos")
public class MovimientosController {

    @Autowired
    private MovimientosService movimientosService;

    @Autowired
    private RabbitTemplate rabbitTemplate;

     private static final String EXCHANGE = "direct_exchange";
    private static final String ROUTING_KEY = "routing.Movimientos";


    @PostMapping("/{cuentaId}")
    public ResponseEntity<?> crearMovimiento(@PathVariable Long cuentaId, @RequestBody Movimientos movimiento) {
        try {
             movimientosService.crearMovimiento(cuentaId, movimiento);

             Map<String, Object> payload = new HashMap<>();
            payload.put("cuentaId", cuentaId);
            payload.put("movimiento", movimiento);

             rabbitTemplate.convertAndSend(EXCHANGE, ROUTING_KEY, payload);

            return ResponseEntity.ok("Movimiento enviado a la cola correctamente. Se procesará en breve.");
        } catch (RuntimeException e) {
             return ResponseEntity.badRequest().body("Error al crear el movimiento: " + e.getMessage());
        } catch (Exception e) {
             return ResponseEntity.internalServerError().body("Error al procesar la solicitud: " + e.getMessage());
        }
    }



    @GetMapping
    public ResponseEntity<?> obtenerTodosMovimientos() {
        try {
            List<Movimientos> movimientos = movimientosService.obtenerTodosMovimientos();
            return ResponseEntity.ok(movimientos);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error al obtener los movimientos: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerMovimientoPorId(@PathVariable Long id) {
        try {
            Movimientos movimiento = movimientosService.obtenerMovimientoPorId(id)
                    .orElseThrow(() -> new RuntimeException("Movimiento no encontrado con id: " + id));
            return ResponseEntity.ok(movimiento);
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error al obtener el movimiento: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarMovimiento(@PathVariable Long id) {
        try {
            movimientosService.eliminarMovimiento(id);
            return ResponseEntity.ok("Movimiento eliminado correctamente y saldo revertido.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error al eliminar el movimiento: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarMovimiento(@PathVariable Long id, @RequestBody Movimientos movimiento) {
        return ResponseEntity.status(405).body("Actualizar movimientos no está permitido.");
    }
}
