package com.academia.puntomarcial.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.academia.puntomarcial.model.Clase;
import com.academia.puntomarcial.service.ClaseService;

@RestController
@RequestMapping("/clases")
public class ClaseController {

    private final ClaseService claseService;

    public ClaseController(ClaseService claseService) {
        this.claseService = claseService;
    }


    @GetMapping
    public ResponseEntity<List<Clase>> listarClases() {

        return ResponseEntity.ok(
                claseService.listarClases()
        );
    }


    // Clases que el cliente todavía puede reservar
    @GetMapping("/disponibles")
    public ResponseEntity<List<Clase>> listarDisponibles() {
        return ResponseEntity.ok(claseService.listarDisponibles());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarClase(
            @PathVariable Integer id) {

        try {

            return ResponseEntity.ok(
                    claseService.buscarPorId(id)
            );

        } catch (IllegalArgumentException e) {

            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<?> registrarClase(
            @RequestBody Clase clase) {

        try {

            Clase nueva = claseService.registrarClase(clase);

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(nueva);

        } catch (IllegalArgumentException e) {

            return ResponseEntity
                    .badRequest()
                    .body(e.getMessage());
        }
    }


    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarClase(
            @PathVariable Integer id,
            @RequestBody Clase clase) {

        try {

            Clase actualizada =
                    claseService.actualizarClase(id, clase);

            return ResponseEntity.ok(actualizada);

        } catch (IllegalArgumentException e) {

            return ResponseEntity
                    .badRequest()
                    .body(e.getMessage());
        }
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarClase(
            @PathVariable Integer id) {

        try {

            claseService.eliminarClase(id);

            return ResponseEntity.ok(
                    "Clase eliminada correctamente"
            );

        } catch (IllegalArgumentException e) {

            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());

        } catch (IllegalStateException e) {

            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(e.getMessage());
        }
    }

    @GetMapping("/disponibilidad")
    public ResponseEntity<Boolean> verificarDisponibilidad(
            @RequestParam LocalDateTime horarioInicio,
            @RequestParam LocalDateTime horarioFin,
            @RequestParam String sala,
            @RequestParam Integer idInstructor) {

        boolean disponible =
                claseService.verificarDisponibilidadHorario(
                        horarioInicio,
                        horarioFin,
                        sala,
                        idInstructor
                );

        return ResponseEntity.ok(disponible);
    }
}