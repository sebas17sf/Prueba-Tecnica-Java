package com.example.microservicio2_pruebatecnica.Service;

import com.example.microservicio2_pruebatecnica.Model.Reporte;
import com.example.microservicio2_pruebatecnica.Model.Clientes;
import com.example.microservicio2_pruebatecnica.Model.Movimientos;
import com.example.microservicio2_pruebatecnica.Repository.MovimientosRepository;
import com.example.microservicio2_pruebatecnica.Repository.ClientesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReporteService {

    @Autowired
    private MovimientosRepository movimientosRepository;

    @Autowired
    private ClientesRepository clientesRepository;

    public List<Reporte> generarReporte(Long clienteId, LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        Clientes cliente = clientesRepository.findById(clienteId)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado con id: " + clienteId));

        List<Movimientos> movimientos = movimientosRepository.findByCuentasClienteIdAndFechaBetween(
                clienteId, fechaInicio, fechaFin);

        return movimientos.stream()
                .map(mov -> new Reporte(
                        mov.getFecha(),
                        cliente.getPersona().getNombre(),
                        mov.getCuentas().getNumeroCuenta(),
                        mov.getCuentas().getTipoCuenta(),
                        mov.getCuentas().getSaldoInicial(),
                        mov.getCuentas().getEstado(),
                        mov.getValor(),
                        mov.getSaldo()
                ))
                .collect(Collectors.toList());
    }

    public List<Reporte> generarReporteGeneral(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
         List<Movimientos> movimientos = movimientosRepository.findByFechaBetween(fechaInicio, fechaFin);

        return movimientos.stream()
                .map(mov -> {
                    Clientes cliente = clientesRepository.findById(mov.getCuentas().getCliente().getId())
                            .orElseThrow(() -> new RuntimeException("Cliente no encontrado con id: " + mov.getCuentas().getCliente().getId()));
                    return new Reporte(
                            mov.getFecha(),
                            cliente.getPersona().getNombre(),
                            mov.getCuentas().getNumeroCuenta(),
                            mov.getCuentas().getTipoCuenta(),
                            mov.getCuentas().getSaldoInicial(),
                            mov.getCuentas().getEstado(),
                            mov.getValor(),
                            mov.getSaldo()
                    );
                })
                .collect(Collectors.toList());
    }



}