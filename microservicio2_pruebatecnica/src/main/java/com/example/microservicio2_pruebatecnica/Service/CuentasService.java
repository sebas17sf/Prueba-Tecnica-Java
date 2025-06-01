package com.example.microservicio2_pruebatecnica.Service;

import com.example.microservicio2_pruebatecnica.Model.Cuentas;
import com.example.microservicio2_pruebatecnica.Model.Clientes;
import com.example.microservicio2_pruebatecnica.Repository.CuentaRepository;
import com.example.microservicio2_pruebatecnica.Repository.ClientesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CuentasService {

    @Autowired
    private CuentaRepository cuentaRepository;

    @Autowired
    private ClientesRepository clientesRepository;


    public Cuentas crearCuenta(Long clienteId, Cuentas cuenta) {
        Clientes cliente = clientesRepository.findById(clienteId)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado con id: " + clienteId));

        cuenta.setCliente(cliente);
        return cuentaRepository.save(cuenta);
    }


    public List<Cuentas> obtenerTodasCuentas() {
        return cuentaRepository.findAll();
    }


    public Optional<Cuentas> obtenerCuentaPorId(Long id) {
        return cuentaRepository.findById(id);
    }


    public Cuentas actualizarCuenta(Long id, Cuentas cuentaActualizada) {
        Cuentas cuentaExistente = cuentaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cuenta no encontrada con id: " + id));

        cuentaExistente.setNumeroCuenta(cuentaActualizada.getNumeroCuenta());
        cuentaExistente.setTipoCuenta(cuentaActualizada.getTipoCuenta());
        cuentaExistente.setSaldoInicial(cuentaActualizada.getSaldoInicial());
        cuentaExistente.setEstado(cuentaActualizada.getEstado());

        return cuentaRepository.save(cuentaExistente);
    }


    public void eliminarCuenta(Long id) {
        Cuentas cuenta = cuentaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cuenta no encontrada con id: " + id));
        cuentaRepository.delete(cuenta);
    }
}
