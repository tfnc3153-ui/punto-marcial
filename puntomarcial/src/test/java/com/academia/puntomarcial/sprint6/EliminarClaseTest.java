package com.academia.puntomarcial.sprint6;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;

import com.academia.puntomarcial.model.Clase;
import com.academia.puntomarcial.repository.ClaseRepository;
import com.academia.puntomarcial.repository.InstructorRepository;
import com.academia.puntomarcial.repository.ReservaRepository;
import com.academia.puntomarcial.service.ClaseService;

/**
 * Sprint 6 – HU-04 (ajuste): el administrador solo elimina clases sin reservas.
 */
public class EliminarClaseTest {

    //CP61
    @Test
    void eliminar_claseConReservas_debeLanzarExcepcion()
    {
        //Preparar datos
        ClaseRepository clases = mock(ClaseRepository.class);
        ReservaRepository reservas = mock(ReservaRepository.class);
        ClaseService servicio = new ClaseService(clases, reservas, mock(InstructorRepository.class));

        Clase clase = new Clase();
        clase.setIdClase(1004);

        when(clases.findById(1004)).thenReturn(Optional.of(clase));
        when(reservas.countByClaseIdClase(1004)).thenReturn(3L);   // la clase tiene 3 reservas

        //Ejecutar
        IllegalStateException error = assertThrows(IllegalStateException.class,
                                                   () -> servicio.eliminarClase(1004));

        //Verificar
        assertEquals("No se puede eliminar la clase porque tiene 3 reserva(s) registrada(s)", error.getMessage());
        verify(clases, never()).delete(clase);   // no se borró
    }

    //CP62
    @Test
    void eliminar_claseSinReservas_debeEliminarla()
    {
        //Preparar datos
        ClaseRepository clases = mock(ClaseRepository.class);
        ReservaRepository reservas = mock(ReservaRepository.class);
        ClaseService servicio = new ClaseService(clases, reservas, mock(InstructorRepository.class));

        Clase clase = new Clase();
        clase.setIdClase(1013);

        when(clases.findById(1013)).thenReturn(Optional.of(clase));
        when(reservas.countByClaseIdClase(1013)).thenReturn(0L);

        //Ejecutar
        servicio.eliminarClase(1013);

        //Verificar
        verify(clases).delete(clase);
    }
}