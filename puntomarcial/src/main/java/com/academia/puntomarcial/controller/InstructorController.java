package com.academia.puntomarcial.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.academia.puntomarcial.model.Instructor;
import com.academia.puntomarcial.repository.InstructorRepository;
import com.academia.puntomarcial.service.InstructorService;

@RestController
@RequestMapping("/instructores")
public class InstructorController {

    private final InstructorRepository instructorRepository;
    private final InstructorService instructorService;

    public InstructorController(InstructorRepository instructorRepository, InstructorService instructorService) {
        this.instructorRepository = instructorRepository;
        this.instructorService = instructorService;
    }

    // Instructores activos, para la lista de opciones al registrar una clase
    @GetMapping
    public ResponseEntity<List<Instructor>> listarInstructores() {
        return ResponseEntity.ok(instructorRepository.findByEstadoOrderByDisciplinaAscApellidoAsc("ACTIVO"));
    }

    // Clases del instructor: próximas (por defecto) o pasadas (?pasadas=true)
    @GetMapping("/{id}/clases")
    public ResponseEntity<?> listarClases(
            @PathVariable Integer id,
            @RequestParam(defaultValue = "false") boolean pasadas) {
        return ResponseEntity.ok(instructorService.listarClases(id, pasadas));
    }

    // Detalle de una clase con sus alumnos inscritos
    @GetMapping("/{id}/clases/{idClase}")
    public ResponseEntity<?> detalleClase(@PathVariable Integer id, @PathVariable Integer idClase) {
        try {
            return ResponseEntity.ok(instructorService.detalleClase(id, idClase));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Tomar asistencia. Cuerpo: { "asistencia": "ASISTIO" | "NO_ASISTIO" }
    @PutMapping("/{id}/reservas/{idReserva}/asistencia")
    public ResponseEntity<?> marcarAsistencia(
            @PathVariable Integer id,
            @PathVariable Integer idReserva,
            @RequestBody Map<String, String> cuerpo) {
        try {
            return ResponseEntity.ok(instructorService.marcarAsistencia(id, idReserva, cuerpo.get("asistencia")));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
