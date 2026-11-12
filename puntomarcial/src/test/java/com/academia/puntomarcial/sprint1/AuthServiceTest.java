package com.academia.puntomarcial.sprint1;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.academia.puntomarcial.dto.LoginResponse;
import com.academia.puntomarcial.model.Cliente;
import com.academia.puntomarcial.repository.UsuarioRepository;
import com.academia.puntomarcial.service.AuthService;

/**
 * Sprint 1 – HU-01: Inicio de sesión.
 * Los repositorios se reemplazan por "mocks" (Mockito): la prueba no usa la base de datos.
 */
public class AuthServiceTest {

    //CP01
    @Test
    void login_correoNoRegistrado_debeLanzarExcepcion()
    {
        //Preparar datos
        UsuarioRepository repositorio = mock(UsuarioRepository.class);
        AuthService servicio = new AuthService(repositorio, mock(PasswordEncoder.class));

        when(repositorio.findByCorreo("noexiste@example.com")).thenReturn(Optional.empty());

        //Ejecutar
        IllegalArgumentException error = assertThrows(IllegalArgumentException.class,
                                                      () -> servicio.login("noexiste@example.com", "Cliente123"));

        //Verificar
        assertEquals("Credenciales incorrectas", error.getMessage());
    }

    //CP02
    @Test
    void login_cuentaInactiva_debeLanzarExcepcion()
    {
        //Preparar datos
        UsuarioRepository repositorio = mock(UsuarioRepository.class);
        AuthService servicio = new AuthService(repositorio, mock(PasswordEncoder.class));

        Cliente cliente = new Cliente();
        cliente.setCorreo("camila.chavez@example.com");
        cliente.setEstado("INACTIVO");

        when(repositorio.findByCorreo("camila.chavez@example.com")).thenReturn(Optional.of(cliente));

        //Ejecutar
        IllegalArgumentException error = assertThrows(IllegalArgumentException.class,
                                                      () -> servicio.login("camila.chavez@example.com", "Cliente123"));

        //Verificar
        assertEquals("La cuenta está inactiva", error.getMessage());
    }

    //CP03
    @Test
    void login_contrasenaIncorrecta_debeLanzarExcepcion()
    {
        //Preparar datos
        UsuarioRepository repositorio = mock(UsuarioRepository.class);
        PasswordEncoder encriptador = mock(PasswordEncoder.class);
        AuthService servicio = new AuthService(repositorio, encriptador);

        Cliente cliente = new Cliente();
        cliente.setCorreo("maria.flores@example.com");
        cliente.setPassword("contraseña-encriptada");
        cliente.setEstado("ACTIVO");

        when(repositorio.findByCorreo("maria.flores@example.com")).thenReturn(Optional.of(cliente));
        when(encriptador.matches("ClaveEquivocada1", "contraseña-encriptada")).thenReturn(false);

        //Ejecutar
        IllegalArgumentException error = assertThrows(IllegalArgumentException.class,
                                                      () -> servicio.login("maria.flores@example.com", "ClaveEquivocada1"));

        //Verificar
        assertEquals("Credenciales incorrectas", error.getMessage());
    }

    //CP04
    @Test
    void login_credencialesCorrectas_debeDevolverRolCliente()
    {
        //Preparar datos
        UsuarioRepository repositorio = mock(UsuarioRepository.class);
        PasswordEncoder encriptador = mock(PasswordEncoder.class);
        AuthService servicio = new AuthService(repositorio, encriptador);

        Cliente cliente = new Cliente();
        cliente.setIdUsuario(1001);
        cliente.setNombre("María");
        cliente.setCorreo("maria.flores@example.com");
        cliente.setPassword("contraseña-encriptada");
        cliente.setEstado("ACTIVO");

        when(repositorio.findByCorreo("maria.flores@example.com")).thenReturn(Optional.of(cliente));
        when(encriptador.matches("Cliente123", "contraseña-encriptada")).thenReturn(true);

        //Ejecutar
        LoginResponse respuesta = servicio.login("maria.flores@example.com", "Cliente123");

        //Verificar
        assertEquals("CLIENTE", respuesta.getRol());
        assertEquals(1001, respuesta.getIdUsuario());
    }
}