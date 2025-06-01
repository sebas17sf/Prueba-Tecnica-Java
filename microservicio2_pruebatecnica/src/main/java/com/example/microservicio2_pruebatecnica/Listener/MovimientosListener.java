package com.example.microservicio2_pruebatecnica.Listener;

import com.example.microservicio2_pruebatecnica.Model.Movimientos;
import com.example.microservicio2_pruebatecnica.Service.MovimientosService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class MovimientosListener {

    @Autowired
    private MovimientosService movimientosService;

    @RabbitListener(queues = "Movimientos")
    public void procesarMovimiento(Map<String, Object> payload) {
        try {
            Long cuentaId = Long.valueOf(payload.get("cuentaId").toString());
            Object movimientoObj = payload.get("movimiento");

            Movimientos movimiento = new ObjectMapper().convertValue(movimientoObj, Movimientos.class);

            movimientosService.crearMovimiento(cuentaId, movimiento);

            System.out.println("Movimiento procesado y guardado en la BD.");

        } catch (Exception e) {
            System.err.println("Error al procesar el movimiento: " + e.getMessage());
            throw e;
        }
    }
}
