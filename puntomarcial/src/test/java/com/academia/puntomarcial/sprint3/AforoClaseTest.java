package com.academia.puntomarcial.sprint3;

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
 * Sprint 3 – HU-07: Supervisión y control del aforo de las clases.
 */
public class AforoClaseTest {

    //CP34
    @Test
    void actualizar_aforoMenorQueLasReservas_debeLanzarExcepcion()
    {
        //Preparar datos
        ClaseRepository clases = mock(ClaseRepository.class);
        InstructorRepository instructores = mock(InstructorRepository.class);
        ClaseService servicio = new ClaseService(clases, mock(ReservaRepository.class), instructores);

        Instructor sofia = new Instructor();
        sofia.setIdUsuario(1106);
        sofia.setDisciplina("BOX");
        sofia.setEstado("ACTIVO");

        // Clase guardada: aforo 10 con 4 cupos libres, o sea 6 alumnos ya reservaron
        Clase guardada = new Clase();
        guardada.setIdClase(1010);
        guardada.setAforoMaximo(10);
        guardada.setAforoDisponible(4);

        // El administrador intenta bajar el aforo a 5
        Clase cambios = new Clase();
        cambios.setDisciplina("BOX");
        cambios.setInstructor(sofia);
        cambios.setSala("B");
        cambios.setHorarioInicio(LocalDateTime.of(2030, 6, 1, 19, 0));
        cambios.setHorarioFin(LocalDateTime.of(2030, 6, 1, 20, 0));
        cambios.setAforoMaximo(5);

        when(clases.findById(1010)).thenReturn(Optional.of(guardada));
        when(instructores.findById(1106)).thenReturn(Optional.of(sofia));
        when(clases.buscarPorSalaOInstructor("B", 1106, 1010)).thenReturn(List.of());

        //Ejecutar
        IllegalArgumentException error = assertThrows(IllegalArgumentException.class,
                                                      () -> servicio.actualizarClase(1010, cambios));

        //Verificar
        assertEquals("El aforo máximo no puede ser menor que las reservas existentes", error.getMessage());
    }

    //CP35
    @Test
    void actualizar_aforoMayor_debeRecalcularLosCuposDisponibles()
    {
        //Preparar datos
        ClaseRepository clases = mock(ClaseRepository.class);
        InstructorRepository instructores = mock(InstructorRepository.class);
        ClaseService servicio = new ClaseService(clases, mock(ReservaRepository.class), instructores);

        Instructor sofia = new Instructor();
        sofia.setIdUsuario(1106);
        sofia.setDisciplina("BOX");
        sofia.setEstado("ACTIVO");

        // 6 alumnos reservados (aforo 10, quedan 4)
        Clase guardada = new Clase();
        guardada.setIdClase(1010);
        guardada.setAforoMaximo(10);
        guardada.setAforoDisponible(4);

        // Se sube el aforo a 15
        Clase cambios = new Clase();
        cambios.setDisciplina("BOX");
        cambios.setInstructor(sofia);
        cambios.setSala("B");
        cambios.setHorarioInicio(LocalDateTime.of(2030, 6, 1, 19, 0));
        cambios.setHorarioFin(LocalDateTime.of(2030, 6, 1, 20, 0));
        cambios.setAforoMaximo(15);

        when(clases.findById(1010)).thenReturn(Optional.of(guardada));
        when(instructores.findById(1106)).thenReturn(Optional.of(sofia));
        when(clases.buscarPorSalaOInstructor("B", 1106, 1010)).thenReturn(List.of());
        when(clases.save(any(Clase.class))).thenAnswer(invocacion -> invocacion.getArgument(0));

        //Ejecutar
        Clase actualizada = servicio.actualizarClase(1010, cambios);

        //Verificar: 15 de aforo - 6 reservados = 9 cupos libres
        assertEquals(15, actualizada.getAforoMaximo());
        assertEquals(9, actualizada.getAforoDisponible());
    }
}