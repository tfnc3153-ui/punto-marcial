package com.academia.puntomarcial.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.academia.puntomarcial.dto.AlumnoResponse;
import com.academia.puntomarcial.dto.ClaseInstructorResponse;
import com.academia.puntomarcial.model.Clase;
import com.academia.puntomarcial.model.Reserva;
import com.academia.puntomarcial.repository.ClaseRepository;
import com.academia.puntomarcial.repository.ReservaRepository;

/**
 * HU-13: el instructor consulta sus clases asignadas, ve a sus alumnos y toma asistencia.
 * El instructor solo ve sus clases: no las crea ni las edita.
 */
@Service
public class InstructorService {

    private final ClaseRepository claseRepository;
    private final ReservaRepository reservaRepository;

    public InstructorService(ClaseRepository claseRepository, ReservaRepository reservaRepository) {
        this.claseRepository = claseRepository;
        this.reservaRepository = reservaRepository;
    }

    /** Próximas (incluye las que están en curso) o pasadas (historial). */
    public List<ClaseInstructorResponse> listarClases(Integer idInstructor, boolean pasadas) {
        LocalDateTime ahora = LocalDateTime.now();

        List<Clase> clases = pasadas
                ? claseRepository.findByInstructorIdUsuarioAndHorarioFinLessThanEqualOrderByHorarioInicioDesc(idInstructor, ahora)
                : claseRepository.findByInstructorIdUsuarioAndHorarioFinAfterOrderByHorarioInicioAsc(idInstructor, ahora);

        return clases.stream()
                .map(clase -> resumen(clase, ahora, false))
                .toList();
    }

    public ClaseInstructorResponse detalleClase(Integer idInstructor, Integer idClase) {
        Clase clase = claseDelInstructor(idInstructor, idClase);
        return resumen(clase, LocalDateTime.now(), true);
    }

    @Transactional
    public ClaseInstructorResponse marcarAsistencia(Integer idInstructor, Integer idReserva, String asistencia) {
        if (!ReglasReserva.asistenciaValida(asistencia)) {
            throw new IllegalArgumentException("La asistencia debe ser ASISTIO o NO_ASISTIO");
        }

        Reserva reserva = reservaRepository.findById(idReserva)
                .orElseThrow(() -> new IllegalArgumentException("La reserva no existe"));

        Clase clase = claseDelInstructor(idInstructor, reserva.getClase().getIdClase());

        if (!"ACTIVA".equals(reserva.getEstado())) {
            throw new IllegalArgumentException("No se puede tomar asistencia de una reserva cancelada");
        }
        if (!ReglasReserva.puedeTomarAsistencia(clase.getHorarioInicio(), LocalDateTime.now())) {
            throw new IllegalArgumentException("La asistencia se toma cuando la clase ya empezó");
        }

        reserva.setAsistencia(asistencia);
        reservaRepository.save(reserva);

        return resumen(clase, LocalDateTime.now(), true);
    }

    private Clase claseDelInstructor(Integer idInstructor, Integer idClase) {
        Clase clase = claseRepository.findById(idClase)
                .orElseThrow(() -> new IllegalArgumentException("La clase no existe"));

        if (idInstructor == null || !idInstructor.equals(clase.getInstructor().getIdUsuario())) {
            throw new IllegalArgumentException("Esta clase no está asignada a usted");
        }
        return clase;
    }

    private ClaseInstructorResponse resumen(Clase clase, LocalDateTime ahora, boolean incluirAlumnos) {
        List<AlumnoResponse> alumnos = reservaRepository
                .findByClaseIdClaseAndEstadoOrderByClienteApellidoAsc(clase.getIdClase(), "ACTIVA")
                .stream()
                .map(AlumnoResponse::de)
                .toList();

        clase.setTotalReservas(alumnos.size());

        return new ClaseInstructorResponse(
                clase, alumnos, ReglasReserva.puedeTomarAsistencia(clase.getHorarioInicio(), ahora), incluirAlumnos);
    }
}
