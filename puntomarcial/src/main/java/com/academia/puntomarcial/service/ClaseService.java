package com.academia.puntomarcial.service;

import com.academia.puntomarcial.model.Clase;
import com.academia.puntomarcial.model.Instructor;
import com.academia.puntomarcial.repository.ClaseRepository;
import com.academia.puntomarcial.repository.InstructorRepository;
import com.academia.puntomarcial.repository.ReservaRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ClaseService {

    private final ClaseRepository claseRepository;
    private final ReservaRepository reservaRepository;
    private final InstructorRepository instructorRepository;

    public ClaseService(
            ClaseRepository claseRepository,
            ReservaRepository reservaRepository,
            InstructorRepository instructorRepository) {
        this.claseRepository = claseRepository;
        this.reservaRepository = reservaRepository;
        this.instructorRepository = instructorRepository;
    }

    public List<Clase> listarClases() {
        List<Clase> clases = claseRepository.findAll();

        Map<Integer, Integer> reservasPorClase = new HashMap<>();
        for (Object[] fila : reservaRepository.contarReservasPorClase()) {
            reservasPorClase.put((Integer) fila[0], ((Long) fila[1]).intValue());
        }
        clases.forEach(c -> c.setTotalReservas(reservasPorClase.getOrDefault(c.getIdClase(), 0)));

        return clases;
    }

    // Clases que el cliente puede reservar (todavía no empiezan)
    public List<Clase> listarDisponibles() {
        return claseRepository.findByHorarioInicioAfterOrderByHorarioInicioAsc(LocalDateTime.now());
    }

    public Clase buscarPorId(Integer id) {
        Clase clase = claseRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Clase no encontrada"));
        clase.setTotalReservas((int) reservaRepository.countByClaseIdClase(id));
        return clase;
    }

    public Clase registrarClase(Clase clase) {
        validarDatos(clase);
        clase.setInstructor(buscarInstructorDeLaDisciplina(clase));
        validarSinConflicto(clase, -1);

        clase.setAforoDisponible(clase.getAforoMaximo());
        return claseRepository.save(clase);
    }

    public Clase actualizarClase(Integer id, Clase datos) {
        Clase clase = buscarPorId(id);

        validarDatos(datos);
        datos.setInstructor(buscarInstructorDeLaDisciplina(datos));
        validarSinConflicto(datos, id);

        clase.setDisciplina(datos.getDisciplina());
        clase.setInstructor(datos.getInstructor());
        clase.setHorarioInicio(datos.getHorarioInicio());
        clase.setHorarioFin(datos.getHorarioFin());
        clase.setSala(datos.getSala());
        int reservados = clase.getAforoMaximo() - clase.getAforoDisponible();

        if (datos.getAforoMaximo() < reservados) {
        throw new IllegalArgumentException(
        "El aforo máximo no puede ser menor que las reservas existentes"
        );
        }

        clase.setAforoMaximo(datos.getAforoMaximo());
        clase.setAforoDisponible(datos.getAforoMaximo() - reservados);

        return claseRepository.save(clase);
    }

    public void eliminarClase(Integer id) {
        Clase clase = buscarPorId(id);

        long reservas = reservaRepository.countByClaseIdClase(id);
        if (!ReglasReserva.puedeEliminarClase((int) reservas)) {
            throw new IllegalStateException(
                    "No se puede eliminar la clase porque tiene " + reservas + " reserva(s) registrada(s)"
            );
        }

        claseRepository.delete(clase);
    }

    public boolean verificarDisponibilidadHorario(
            LocalDateTime horarioInicio, LocalDateTime horarioFin,
            String sala, Integer idInstructor) {
        return contarConflictos(horarioInicio, horarioFin, sala, idInstructor, -1) == 0;
    }

    private void validarDatos(Clase clase) {
        if (!ReglasReserva.aforoValido(clase.getAforoMaximo())) {
            throw new IllegalArgumentException(
                    "El aforo máximo debe estar entre 1 y " + ReglasReserva.AFORO_MAXIMO_PERMITIDO
            );
        }
        if (clase.getHorarioInicio() == null
        || clase.getHorarioFin() == null
        || !clase.getHorarioFin().isAfter(clase.getHorarioInicio())) {
        throw new IllegalArgumentException(
        "El fin de la clase debe ser posterior al inicio"
        );
        }

        if (!ReglasReserva.disciplinaValida(clase.getDisciplina())) {
            throw new IllegalArgumentException("La disciplina debe ser MMA, Muay Thai o Box");
        }
        if (!ReglasReserva.salaValida(clase.getSala())) {
            throw new IllegalArgumentException("La sala debe ser A, B, C o D");
        }
    }

    // El instructor llega solo con su id: se busca en la BD y se valida que dicte la disciplina
    private Instructor buscarInstructorDeLaDisciplina(Clase clase) {
        if (clase.getInstructor() == null || clase.getInstructor().getIdUsuario() == null) {
            throw new IllegalArgumentException("Debe elegir un instructor");
        }

        Instructor instructor = instructorRepository.findById(clase.getInstructor().getIdUsuario())
                .orElseThrow(() -> new IllegalArgumentException("El instructor no existe"));

        if (!"ACTIVO".equals(instructor.getEstado())) {
            throw new IllegalArgumentException("El instructor no está activo");
        }
        if (!ReglasReserva.instructorDictaDisciplina(instructor.getDisciplina(), clase.getDisciplina())) {
            throw new IllegalArgumentException(
                    "El instructor " + instructor.getNombre() + " " + instructor.getApellido()
                            + " no dicta esa disciplina");
        }

        return instructor;
    }

    private void validarSinConflicto(Clase clase, Integer idExcluir) {
        long conflictos = contarConflictos(
                clase.getHorarioInicio(), clase.getHorarioFin(),
                clase.getSala(), clase.getInstructor().getIdUsuario(), idExcluir
        );

        if (conflictos > 0) {
            throw new IllegalArgumentException(
                    "Ya existe una clase en esa sala o con ese instructor en el horario indicado"
            );
        }
    }

    private long contarConflictos(LocalDateTime horarioInicio, LocalDateTime horarioFin,
                                  String sala, Integer idInstructor, Integer idExcluir) {
        return claseRepository.buscarPorSalaOInstructor(sala, idInstructor, idExcluir)
                .stream()
                .filter(otra -> ReglasReserva.hayTraslape(
                        horarioInicio, horarioFin,
                        otra.getHorarioInicio(), otra.getHorarioFin()))
                .count();
    }
}
