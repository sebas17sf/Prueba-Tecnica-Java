package com.example.microservicio2_pruebatecnica.Service;

import com.example.microservicio2_pruebatecnica.Model.Movimientos;
import com.example.microservicio2_pruebatecnica.Model.Cuentas;
import com.example.microservicio2_pruebatecnica.Repository.MovimientosRepository;
import com.example.microservicio2_pruebatecnica.Repository.CuentaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class MovimientosService {

    @Autowired
    private MovimientosRepository movimientosRepository;

    @Autowired
    private CuentaRepository cuentaRepository;


    public Movimientos crearMovimiento(Long cuentaId, Movimientos movimiento) {
        Cuentas cuenta = cuentaRepository.findById(cuentaId)
                .orElseThrow(() -> new RuntimeException("Cuenta no encontrada con id: " + cuentaId));

        if (movimiento.getValor() < 0 && (cuenta.getSaldoInicial() + movimiento.getValor() < 0)) {
            throw new RuntimeException("Saldo no disponible");
        }

         Double saldoActualizado = cuenta.getSaldoInicial() + movimiento.getValor();
        cuenta.setSaldoInicial(saldoActualizado);
        cuentaRepository.save(cuenta);

         movimiento.setCuentas(cuenta);
        movimiento.setSaldo(saldoActualizado);
        movimiento.setFecha(LocalDateTime.now());

        return movimientosRepository.save(movimiento);
    }


    public List<Movimientos> obtenerTodosMovimientos() {
        return movimientosRepository.findAll();
    }


    public Optional<Movimientos> obtenerMovimientoPorId(Long id) {
        return movimientosRepository.findById(id);
    }


    public void eliminarMovimiento(Long id) {
        Movimientos movimiento = movimientosRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Movimiento no encontrado con id: " + id));

         Cuentas cuenta = movimiento.getCuentas();
        Double saldoActualizado = cuenta.getSaldoInicial() - movimiento.getValor();
        cuenta.setSaldoInicial(saldoActualizado);
        cuentaRepository.save(cuenta);

         movimientosRepository.delete(movimiento);
    }

}
