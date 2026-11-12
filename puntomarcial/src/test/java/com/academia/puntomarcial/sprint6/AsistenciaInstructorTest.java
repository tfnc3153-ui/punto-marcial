package com.academia.puntomarcial.sprint6;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import com.academia.puntomarcial.model.Clase;
import com.academia.puntomarcial.model.Instructor;
import com.academia.puntomarcial.model.Reserva;
import com.academia.puntomarcial.repository.ClaseRepository;
import com.academia.puntomarcial.repository.ReservaRepository;
import com.academia.puntomarcial.service.InstructorService;

/**
 * Sprint 6 – HU-13: El instructor registra la asistencia de sus alumnos.
 */
public class AsistenciaInstructorTest {

    //CP56
    @Test
    void marcarAsistencia_valorInvalido_debeLanzarExcepcion()
    {
        //Preparar datos
        InstructorService servicio = new InstructorService(mock(ClaseRepository.class), mock(ReservaRepository.class));

        //Ejecutar
        IllegalArgumentException error = assertThrows(IllegalArgumentException.class,
                                                      () -> servicio.marcarAsistencia(1101, 1017, "TARDE"));

        //Verificar
        assertEquals("La asistencia debe ser ASISTIO o NO_ASISTIO", error.getMessage());
    }

    //CP57
    @Test
    void marcarAsistencia_claseQueAunNoEmpieza_debeLanzarExcepcion()
    {
        //Preparar datos
        ClaseRepository clases = mock(ClaseRepository.class);
        ReservaRepository reservas = mock(ReservaRepository.class);
        InstructorService servicio = new InstructorService(clases, reservas);

        Instructor diego = new Instructor();
        diego.setIdUsuario(1101);

        Clase clase = new Clase();
        clase.setIdClase(1003);
        clase.setInstructor(diego);
        clase.setHorarioInicio(LocalDateTime.now().plusHours(1));   // empieza dentro de 1 hora

        Reserva reserva = new Reserva();
        reserva.setIdReserva(2001);
        reserva.setClase(clase);
        reserva.setEstado("ACTIVA");

        when(reservas.findById(2001)).thenReturn(Optional.of(reserva));
        when(clases.findById(1003)).thenReturn(Optional.of(clase));

        //Ejecutar
        IllegalArgumentException error = assertThrows(IllegalArgumentException.class,
                                                      () -> servicio.marcarAsistencia(1101, 2001, "ASISTIO"));

        //Verificar
        assertEquals("La asistencia se toma cuando la clase ya empezó", error.getMessage());
    }

    //CP58
    @Test
    void marcarAsistencia_claseDeOtroInstructor_debeLanzarExcepcion()
    {
        //Preparar datos
        ClaseRepository clases = mock(ClaseRepository.class);
        ReservaRepository reservas = mock(ReservaRepository.class);
        InstructorService servicio = new InstructorService(clases, reservas);

        Instructor diego = new Instructor();
        diego.setIdUsuario(1101);

        Clase claseDeDiego = new Clase();
        claseDeDiego.setIdClase(1015);
        claseDeDiego.setInstructor(diego);
        claseDeDiego.setHorarioInicio(LocalDateTime.now().minusMinutes(30));

        Reserva reserva = new Reserva();
        reserva.setIdReserva(1017);
        reserva.setClase(claseDeDiego);
        reserva.setEstado("ACTIVA");

        when(reservas.findById(1017)).thenReturn(Optional.of(reserva));
        when(clases.findById(1015)).thenReturn(Optional.of(claseDeDiego));

        //Ejecutar: Carla (1102) intenta tomar asistencia en la clase de Diego
        IllegalArgumentException error = assertThrows(IllegalArgumentException.class,
                                                      () -> servicio.marcarAsistencia(1102, 1017, "ASISTIO"));

        //Verificar
        assertEquals("Esta clase no está asignada a usted", error.getMessage());
    }

    //CP59
    @Test
    void marcarAsistencia_reservaCancelada_debeLanzarExcepcion()
    {
        //Preparar datos
        ClaseRepository clases = mock(ClaseRepository.class);
        ReservaRepository reservas = mock(ReservaRepository.class);
        InstructorService servicio = new InstructorService(clases, reservas);

        Instructor diego = new Instructor();
        diego.setIdUsuario(1101);

        Clase clase = new Clase();
        clase.setIdClase(1015);
        clase.setInstructor(diego);
        clase.setHorarioInicio(LocalDateTime.now().minusMinutes(30));

        Reserva reserva = new Reserva();
        reserva.setIdReserva(1018);
        reserva.setClase(clase);
        reserva.setEstado("CANCELADA");

        when(reservas.findById(1018)).thenReturn(Optional.of(reserva));
        when(clases.findById(1015)).thenReturn(Optional.of(clase));

        //Ejecutar
        IllegalArgumentException error = assertThrows(IllegalArgumentException.class,
                                                      () -> servicio.marcarAsistencia(1101, 1018, "ASISTIO"));

        //Verificar
        assertEquals("No se puede tomar asistencia de una reserva cancelada", error.getMessage());
    }

    //CP60
    @Test
    void marcarAsistencia_claseEnCurso_debeGuardarLaAsistencia()
    {
        //Preparar datos
        ClaseRepository clases = mock(ClaseRepository.class);
        ReservaRepository reservas = mock(ReservaRepository.class);
        InstructorService servicio = new InstructorService(clases, reservas);

        Instructor diego = new Instructor();
        diego.setIdUsuario(1101);

        Clase clase = new Clase();
        clase.setIdClase(1015);
        clase.setInstructor(diego);
        clase.setHorarioInicio(LocalDateTime.now().minusMinutes(30));   // empezó hace 30 minutos
        clase.setHorarioFin(LocalDateTime.now().plusMinutes(30));

        Reserva reserva = new Reserva();
        reserva.setIdReserva(1017);
        reserva.setClase(clase);
        reserva.setEstado("ACTIVA");

        when(reservas.findById(1017)).thenReturn(Optional.of(reserva));
        when(clases.findById(1015)).thenReturn(Optional.of(clase));

        //Ejecutar
        servicio.marcarAsistencia(1101, 1017, "ASISTIO");

        //Verificar
        assertEquals("ASISTIO", reserva.getAsistencia());
        verify(reservas).save(reserva);   // la asistencia se guardó en la base de datos
    }
}