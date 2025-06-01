package com.example.microservicio2_pruebatecnica.Controller;

import com.example.microservicio2_pruebatecnica.Model.Reporte;
import com.example.microservicio2_pruebatecnica.Service.ReporteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;

import java.util.stream.Collectors;
import java.util.Map;

@RestController
@RequestMapping("/reportes")
public class ReporteController {

    @Autowired
    private ReporteService reporteService;

    @GetMapping
    public ResponseEntity<?> generarReporte(
            @RequestParam Long clienteId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaFin) {

        try {
            List<Reporte> reporte = reporteService.generarReporte(clienteId, fechaInicio, fechaFin);

            if (reporte.isEmpty()) {
                return ResponseEntity.ok("No se encontraron datos para el reporte.");
            }

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

            List<Map<String, Object>> reporteFormateado = reporte.stream().map(r -> {
                Map<String, Object> map = new HashMap<>();
                map.put("fecha", r.getFecha().format(formatter));
                map.put("cliente", r.getCliente());
                map.put("numeroCuenta", r.getNumeroCuenta());
                map.put("tipoCuenta", r.getTipoCuenta());
                map.put("saldoInicial", r.getSaldoInicial());
                map.put("estado", r.getEstado());
                map.put("movimiento", r.getMovimiento());
                map.put("saldoDisponible", r.getSaldoDisponible());
                return map;
            }).collect(Collectors.toList());

            return ResponseEntity.ok(reporteFormateado);

        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error al generar el reporte: " + e.getMessage());
        }
    }


    @GetMapping("/general")
    public ResponseEntity<?> generarReporteGeneral(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaFin) {

        try {
            List<Reporte> reporte = reporteService.generarReporteGeneral(fechaInicio, fechaFin);

            if (reporte.isEmpty()) {
                return ResponseEntity.ok("No se encontraron datos para el reporte general.");
            }

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

            List<Map<String, Object>> reporteFormateado = reporte.stream().map(r -> {
                Map<String, Object> map = new HashMap<>();
                map.put("fecha", r.getFecha().format(formatter));
                map.put("cliente", r.getCliente());
                map.put("numeroCuenta", r.getNumeroCuenta());
                map.put("tipoCuenta", r.getTipoCuenta());
                map.put("saldoInicial", r.getSaldoInicial());
                map.put("estado", r.getEstado());
                map.put("movimiento", r.getMovimiento());
                map.put("saldoDisponible", r.getSaldoDisponible());
                return map;
            }).collect(Collectors.toList());

            return ResponseEntity.ok(reporteFormateado);

        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error al generar el reporte general: " + e.getMessage());
        }
    }



}
