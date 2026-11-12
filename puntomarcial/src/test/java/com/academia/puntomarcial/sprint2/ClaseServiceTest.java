package com.academia.puntomarcial.sprint2;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import com.academia.puntomarcial.model.Clase;
import com.academia.puntomarcial.model.Instructor;
import com.academia.puntomarcial.repository.ClaseRepository;
import com.academia.puntomarcial.repository.InstructorRepository;
import com.academia.puntomarcial.repository.ReservaRepository;
import com.academia.puntomarcial.service.ClaseService;

/**
 * Sprint 2 – HU-04: Gestión y programación de clases.
 */
public class ClaseServiceTest {

    //CP17
    @Test
    void registrar_aforoCero_debeLanzarExcepcion()
    {
        //Preparar datos
        Clase clase = new Clase();
        ClaseService servicio = new ClaseService(mock(ClaseRepository.class),
                                                 mock(ReservaRepository.class),
                                                 mock(InstructorRepository.class));

        clase.setDisciplina("MMA");
        clase.setSala("A");
        clase.setHorarioInicio(LocalDateTime.of(2030, 6, 1, 18, 0));
        clase.setHorarioFin(LocalDateTime.of(2030, 6, 1, 19, 0));
        clase.setAforoMaximo(0);

        //Ejecutar
        IllegalArgumentException error = assertThrows(IllegalArgumentException.class,
                                                      () -> servicio.registrarClase(clase));

        //Verificar
        assertEquals("El aforo máximo debe estar entre 1 y 30", error.getMessage());
    }

    //CP18
    @Test
    void registrar_aforoMayorA30_debeLanzarExcepcion()
    {
        //Preparar datos
        Clase clase = new Clase();
        ClaseService servicio = new ClaseService(mock(ClaseRepository.class),
                                                 mock(ReservaRepository.class),
                                                 mock(InstructorRepository.class));

        clase.setDisciplina("MMA");
        clase.setSala("A");
        clase.setHorarioInicio(LocalDateTime.of(2030, 6, 1, 18, 0));
        clase.setHorarioFin(LocalDateTime.of(2030, 6, 1, 19, 0));
        clase.setAforoMaximo(31);

        //Ejecutar
        IllegalArgumentException error = assertThrows(IllegalArgumentException.class,
                                                      () -> servicio.registrarClase(clase));

        //Verificar
        assertEquals("El aforo máximo debe estar entre 1 y 30", error.getMessage());
    }

    //CP19
    @Test
    void registrar_finAntesDelInicio_debeLanzarExcepcion()
    {
        //Preparar datos
        Clase clase = new Clase();
        ClaseService servicio = new ClaseService(mock(ClaseRepository.class),
                                                 mock(ReservaRepository.class),
                                                 mock(InstructorRepository.class));

        clase.setDisciplina("MMA");
        clase.setSala("A");
        clase.setHorarioInicio(LocalDateTime.of(2030, 6, 1, 19, 0));
        clase.setHorarioFin(LocalDateTime.of(2030, 6, 1, 18, 0));
        clase.setAforoMaximo(12);

        //Ejecutar
        IllegalArgumentException error = assertThrows(IllegalArgumentException.class,
                                                      () -> servicio.registrarClase(clase));

        //Verificar
        assertEquals("El fin de la clase debe ser posterior al inicio", error.getMessage());
    }

    //CP20
    @Test
    void registrar_salaQueNoExiste_debeLanzarExcepcion()
    {
        //Preparar datos
        Clase clase = new Clase();
        ClaseService servicio = new ClaseService(mock(ClaseRepository.class),
                                                 mock(ReservaRepository.class),
                                                 mock(InstructorRepository.class));

        clase.setDisciplina("MMA");
        clase.setSala("E");
        clase.setHorarioInicio(LocalDateTime.of(2030, 6, 1, 18, 0));
        clase.setHorarioFin(LocalDateTime.of(2030, 6, 1, 19, 0));
        clase.setAforoMaximo(12);

        //Ejecutar
        IllegalArgumentException error = assertThrows(IllegalArgumentException.class,
                                                      () -> servicio.registrarClase(clase));

        //Verificar
        assertEquals("La sala debe ser A, B, C o D", error.getMessage());
    }

    //CP21
    @Test
    void registrar_disciplinaQueNoSeDicta_debeLanzarExcepcion()
    {
        //Preparar datos
        Clase clase = new Clase();
        ClaseService servicio = new ClaseService(mock(ClaseRepository.class),
                                                 mock(ReservaRepository.class),
                                                 mock(InstructorRepository.class));

        clase.setDisciplina("KARATE");
        clase.setSala("A");
        clase.setHorarioInicio(LocalDateTime.of(2030, 6, 1, 18, 0));
        clase.setHorarioFin(LocalDateTime.of(2030, 6, 1, 19, 0));
        clase.setAforoMaximo(12);

        //Ejecutar
        IllegalArgumentException error = assertThrows(IllegalArgumentException.class,
                                                      () -> servicio.registrarClase(clase));

        //Verificar
        assertEquals("La disciplina debe ser MMA, Muay Thai o Box", error.getMessage());
    }

    //CP22
    @Test
    void registrar_instructorDeOtraDisciplina_debeLanzarExcepcion()
    {
        //Preparar datos
        Clase clase = new Clase();
        InstructorRepository instructores = mock(InstructorRepository.class);
        ClaseService servicio = new ClaseService(mock(ClaseRepository.class),
                                                 mock(ReservaRepository.class),
                                                 instructores);

        Instructor luis = new Instructor();
        luis.setIdUsuario(1103);
        luis.setNombre("Luis");
        luis.setApellido("Paredes");
        luis.setDisciplina("MUAY_THAI");
        luis.setEstado("ACTIVO");

        clase.setDisciplina("MMA");
        clase.setInstructor(luis);
        clase.setSala("A");
        clase.setHorarioInicio(LocalDateTime.of(2030, 6, 1, 18, 0));
        clase.setHorarioFin(LocalDateTime.of(2030, 6, 1, 19, 0));
        clase.setAforoMaximo(12);

        when(instructores.findById(1103)).thenReturn(Optional.of(luis));

        //Ejecutar
        IllegalArgumentException error = assertThrows(IllegalArgumentException.class,
                                                      () -> servicio.registrarClase(clase));

        //Verificar
        assertEquals("El instructor Luis Paredes no dicta esa disciplina", error.getMessage());
    }

    //CP23
    @Test
    void registrar_horarioCruzadoEnLaMismaSala_debeLanzarExcepcion()
    {
        //Preparar datos
        Clase clase = new Clase();
        ClaseRepository clases = mock(ClaseRepository.class);
        InstructorRepository instructores = mock(InstructorRepository.class);
        ClaseService servicio = new ClaseService(clases, mock(ReservaRepository.class), instructores);

        Instructor diego = new Instructor();
        diego.setIdUsuario(1101);
        diego.setDisciplina("MMA");
        diego.setEstado("ACTIVO");

        // Clase que ya existe en la sala A de 18:00 a 19:00
        Clase existente = new Clase();
        existente.setHorarioInicio(LocalDateTime.of(2030, 6, 1, 18, 0));
        existente.setHorarioFin(LocalDateTime.of(2030, 6, 1, 19, 0));

        // Clase nueva de 18:30 a 19:30 en la misma sala: se cruza
        clase.setDisciplina("MMA");
        clase.setInstructor(diego);
        clase.setSala("A");
        clase.setHorarioInicio(LocalDateTime.of(2030, 6, 1, 18, 30));
        clase.setHorarioFin(LocalDateTime.of(2030, 6, 1, 19, 30));
        clase.setAforoMaximo(12);

        when(instructores.findById(1101)).thenReturn(Optional.of(diego));
        when(clases.buscarPorSalaOInstructor("A", 1101, -1)).thenReturn(List.of(existente));

        //Ejecutar
        IllegalArgumentException error = assertThrows(IllegalArgumentException.class,
                                                      () -> servicio.registrarClase(clase));

        //Verificar
        assertEquals("Ya existe una clase en esa sala o con ese instructor en el horario indicado", error.getMessage());
    }

    //CP24
    @Test
    void registrar_datosCorrectos_debeGuardarConTodosLosCupos()
    {
        //Preparar datos
        Clase clase = new Clase();
        ClaseRepository clases = mock(ClaseRepository.class);
        InstructorRepository instructores = mock(InstructorRepository.class);
        ClaseService servicio = new ClaseService(clases, mock(ReservaRepository.class), instructores);

        Instructor diego = new Instructor();
        diego.setIdUsuario(1101);
        diego.setDisciplina("MMA");
        diego.setEstado("ACTIVO");

        clase.setDisciplina("MMA");
        clase.setInstructor(diego);
        clase.setSala("A");
        clase.setHorarioInicio(LocalDateTime.of(2030, 6, 1, 18, 0));
        clase.setHorarioFin(LocalDateTime.of(2030, 6, 1, 19, 0));
        clase.setAforoMaximo(12);

        when(instructores.findById(1101)).thenReturn(Optional.of(diego));
        when(clases.buscarPorSalaOInstructor("A", 1101, -1)).thenReturn(List.of());   // sin cruces
        when(clases.save(any(Clase.class))).thenAnswer(invocacion -> invocacion.getArgument(0));

        //Ejecutar
        Clase guardada = servicio.registrarClase(clase);

        //Verificar
        assertEquals(12, guardada.getAforoDisponible());    // empieza con todos los cupos libres
        verify(clases).save(clase);
    }
}