package com.academia.puntomarcial.sprint6;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.academia.puntomarcial.model.Administrador;
import com.academia.puntomarcial.model.Instructor;
import com.academia.puntomarcial.repository.ClaseRepository;
import com.academia.puntomarcial.repository.UsuarioRepository;
import com.academia.puntomarcial.service.UsuarioService;

/**
 * Sprint 6 – HU-03 (ajuste por roles): activar y desactivar usuarios.
 */
public class EstadoUsuarioTest {

    //CP63
    @Test
    void desactivar_instructorConClasesFuturas_debeLanzarExcepcion()
    {
        //Preparar datos
        UsuarioRepository usuarios = mock(UsuarioRepository.class);
        ClaseRepository clases = mock(ClaseRepository.class);
        UsuarioService servicio = new UsuarioService(usuarios, clases, mock(PasswordEncoder.class));

        Instructor diego = new Instructor();
        diego.setIdUsuario(1101);
        diego.setEstado("ACTIVO");

        when(usuarios.findById(1101)).thenReturn(Optional.of(diego));
        // Diego tiene 3 clases programadas a futuro
        when(clases.countByInstructorIdUsuarioAndHorarioInicioAfter(eq(1101), any(LocalDateTime.class))).thenReturn(3L);

        //Ejecutar
        IllegalArgumentException error = assertThrows(IllegalArgumentException.class,
                                                      () -> servicio.cambiarEstado(1101, "INACTIVO"));

        //Verificar
        assertEquals("No se puede desactivar: el instructor tiene clases futuras asignadas", error.getMessage());
    }

    //CP64
    @Test
    void desactivar_ultimoAdministrador_debeLanzarExcepcion()
    {
        //Preparar datos
        UsuarioRepository usuarios = mock(UsuarioRepository.class);
        UsuarioService servicio = new UsuarioService(usuarios, mock(ClaseRepository.class), mock(PasswordEncoder.class));

        Administrador admin = new Administrador();
        admin.setIdUsuario(1);
        admin.setEstado("ACTIVO");

        when(usuarios.findById(1)).thenReturn(Optional.of(admin));
        when(usuarios.contarAdministradoresActivos()).thenReturn(1L);   // es el único administrador activo

        //Ejecutar
        IllegalArgumentException error = assertThrows(IllegalArgumentException.class,
                                                      () -> servicio.cambiarEstado(1, "INACTIVO"));

        //Verificar
        assertEquals("Debe quedar al menos un administrador activo", error.getMessage());
    }

    //CP65
    @Test
    void cambiarEstado_valorInvalido_debeLanzarExcepcion()
    {
        //Preparar datos
        UsuarioService servicio = new UsuarioService(mock(UsuarioRepository.class),
                                                     mock(ClaseRepository.class),
                                                     mock(PasswordEncoder.class));

        //Ejecutar
        IllegalArgumentException error = assertThrows(IllegalArgumentException.class,
                                                      () -> servicio.cambiarEstado(1001, "BLOQUEADO"));

        //Verificar
        assertEquals("El estado debe ser ACTIVO o INACTIVO", error.getMessage());
    }
}