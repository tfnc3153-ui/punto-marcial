package com.academia.puntomarcial.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.academia.puntomarcial.model.Clase;
import com.academia.puntomarcial.model.Reserva;
import com.academia.puntomarcial.repository.ClaseRepository;
import com.academia.puntomarcial.repository.ReservaRepository;
import com.academia.puntomarcial.repository.ClienteRepository;
import org.springframework.transaction.annotation.Transactional;
@Service
public class ReservaService {

    private final ReservaRepository reservaRepository;
    private final ClaseRepository claseRepository;
    private final ClienteRepository clienteRepository;

    public ReservaService(
        ReservaRepository reservaRepository,
        ClaseRepository claseRepository,
        ClienteRepository clienteRepository
) {
    this.reservaRepository = reservaRepository;
    this.claseRepository = claseRepository;
    this.clienteRepository = clienteRepository;
}

@Transactional
    public Reserva registrarReserva(Reserva reserva) {

    if (reserva.getEstado() == null || reserva.getEstado().isBlank()) {
        reserva.setEstado("ACTIVA");
    }

    // Fase 9 (defecto encontrado por las pruebas): sin fecha la BD rechazaba la reserva con error 500.
    if (reserva.getFechaReserva() == null || reserva.getFechaReserva().isBlank()) {
        reserva.setFechaReserva(LocalDate.now().toString());
    }

    if (reserva.getClase() == null || reserva.getClase().getIdClase() == null) {
        throw new IllegalArgumentException("Debe indicar una clase.");
    }

    if (reserva.getCliente() == null || reserva.getCliente().getIdUsuario() == null) {
        throw new IllegalArgumentException("Debe indicar un cliente.");
    }

    Integer idClase = reserva.getClase().getIdClase();
    Integer idCliente = reserva.getCliente().getIdUsuario();


    Clase clase = claseRepository.buscarParaReserva(idClase)
            .orElseThrow(() -> new IllegalArgumentException("La clase no existe."));

    reserva.setCliente(
        clienteRepository.findById(idCliente)
                .orElseThrow(() ->
                        new IllegalArgumentException("El cliente no existe."))
);


    if (ReglasReserva.claseYaInicio(clase.getHorarioInicio(), LocalDateTime.now())) {
        throw new IllegalArgumentException("La clase ya empezó: no se puede reservar.");
    }

    long cantidadReservas = reservaRepository.contarReserva(idCliente, idClase);

    if (cantidadReservas > 0) {
        throw new IllegalArgumentException(
                "El cliente ya tiene una reserva para esta clase."
        );
    }

    if (clase.getAforoDisponible() == null) {
        throw new IllegalArgumentException(
                "La clase no tiene aforo disponible configurado."
        );
    }

    if (!ReglasReserva.hayCupo(clase.getAforoDisponible())) {
        throw new IllegalArgumentException(
                "La clase no tiene cupos disponibles."
        );
    }

    clase.setAforoDisponible(clase.getAforoDisponible() - 1);
    claseRepository.save(clase);

    reserva.setClase(clase);

    return reservaRepository.save(reserva);
}
    public List<Reserva> listarReservas() {
        return reservaRepository.findAll();
    }
    public List<Reserva> listarPorCliente(Integer idCliente) {
    if (!clienteRepository.existsById(idCliente)) {
        throw new IllegalArgumentException("El cliente no existe.");
    }

    return reservaRepository.buscarPorCliente(idCliente);
}

    /**
     * HU-06: cancela una reserva del cliente si faltan 2 horas o más para la clase
     * y devuelve el cupo a la clase.
     */
    @Transactional
    public Reserva cancelarReserva(Integer idReserva, Integer idCliente) {
        Reserva reserva = reservaRepository.findById(idReserva)
                .orElseThrow(() -> new IllegalArgumentException("La reserva no existe."));

        if (idCliente == null || !idCliente.equals(reserva.getCliente().getIdUsuario())) {
            throw new IllegalArgumentException("Solo el cliente dueño de la reserva puede cancelarla.");
        }

        if ("CANCELADA".equals(reserva.getEstado())) {
            throw new IllegalArgumentException("La reserva ya estaba cancelada.");
        }

        Clase clase = claseRepository.buscarParaReserva(reserva.getClase().getIdClase())
                .orElseThrow(() -> new IllegalArgumentException("La clase no existe."));

        if (!ReglasReserva.puedeCancelar(clase.getHorarioInicio(), LocalDateTime.now())) {
            throw new IllegalArgumentException(
                    "Solo se puede cancelar con al menos 2 horas de anticipación."
            );
        }

        reserva.setEstado("CANCELADA");
        clase.incrementarAforo();
        claseRepository.save(clase);
        reserva.setClase(clase);

        return reservaRepository.save(reserva);
    }
}