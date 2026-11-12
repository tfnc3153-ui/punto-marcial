package com.academia.puntomarcial.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.academia.puntomarcial.model.Reserva;
import com.academia.puntomarcial.service.ReservaService;
import org.springframework.web.bind.annotation.PathVariable;
@RestController
@RequestMapping("/reservas")
public class ReservaController {

    private final ReservaService reservaService;

    public ReservaController(ReservaService reservaService) {
        this.reservaService = reservaService;
    }

    @GetMapping
    public ResponseEntity<List<Reserva>> listarReservas() {
        return ResponseEntity.ok(reservaService.listarReservas());
    }

    @PostMapping
    public ResponseEntity<?> registrarReserva(@RequestBody Reserva reserva) {
        try {
            Reserva nuevaReserva = reservaService.registrarReserva(reserva);
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(nuevaReserva);

        } catch (IllegalArgumentException e) {
            return ResponseEntity
                    .badRequest()
                    .body(e.getMessage());
        }
    }
    @GetMapping("/cliente/{idCliente}")
public ResponseEntity<?> listarPorCliente(
        @PathVariable Integer idCliente
) {
    try {
        return ResponseEntity.ok(
            reservaService.listarPorCliente(idCliente)
        );
    } catch (IllegalArgumentException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}

    @PutMapping("/{id}/cancelar")
    public ResponseEntity<?> cancelarReserva(
            @PathVariable Integer id,
            @RequestParam Integer idCliente
    ) {
        try {
            return ResponseEntity.ok(
                reservaService.cancelarReserva(id, idCliente)
            );
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
