package com.academia.puntomarcial.sprint1;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.academia.puntomarcial.dto.UsuarioRequest;
import com.academia.puntomarcial.dto.UsuarioResponse;
import com.academia.puntomarcial.model.Cliente;
import com.academia.puntomarcial.model.Usuario;
import com.academia.puntomarcial.repository.ClaseRepository;
import com.academia.puntomarcial.repository.UsuarioRepository;
import com.academia.puntomarcial.service.UsuarioService;

/**
 * Sprint 1 – HU-02: Registro de usuarios y HU-03: Actualización de usuarios.
 */
public class UsuarioServiceTest {

    //CP05
    @Test
    void registrar_rolInvalido_debeLanzarExcepcion()
    {
        //Preparar datos
        UsuarioRequest usuario = new UsuarioRequest();
        UsuarioService servicio = new UsuarioService(mock(UsuarioRepository.class),
                                                     mock(ClaseRepository.class),
                                                     mock(PasswordEncoder.class));

        usuario.setRol("SECRETARIA");
        usuario.setNombre("Maria");
        usuario.setApellido("Flores");
        usuario.setTipoDoc("DNI");
        usuario.setNumDoc("45781236");
        usuario.setCorreo("maria@example.com");
        usuario.setTelefono("910582963");
        usuario.setPassword("Cliente123");

        //Ejecutar
        IllegalArgumentException error = assertThrows(IllegalArgumentException.class,
                                                      () -> servicio.registrarUsuario(usuario));

        //Verificar
        assertEquals("El rol debe ser Administrador, Instructor o Cliente", error.getMessage());
    }

    //CP06
    @Test
    void registrar_nombreVacio_debeLanzarExcepcion()
    {
        //Preparar datos
        UsuarioRequest usuario = new UsuarioRequest();
        UsuarioService servicio = new UsuarioService(mock(UsuarioRepository.class),
                                                     mock(ClaseRepository.class),
                                                     mock(PasswordEncoder.class));

        usuario.setRol("CLIENTE");
        usuario.setNombre("");
        usuario.setApellido("Flores");
        usuario.setTipoDoc("DNI");
        usuario.setNumDoc("45781236");
        usuario.setCorreo("maria@example.com");
        usuario.setTelefono("910582963");
        usuario.setPassword("Cliente123");

        //Ejecutar
        IllegalArgumentException error = assertThrows(IllegalArgumentException.class,
                                                      () -> servicio.registrarUsuario(usuario));

        //Verificar
        assertEquals("Complete todos los campos obligatorios", error.getMessage());
    }

    //CP07
    @Test
    void registrar_dniDe7Digitos_debeLanzarExcepcion()
    {
        //Preparar datos
        UsuarioRequest usuario = new UsuarioRequest();
        UsuarioService servicio = new UsuarioService(mock(UsuarioRepository.class),
                                                     mock(ClaseRepository.class),
                                                     mock(PasswordEncoder.class));

        usuario.setRol("CLIENTE");
        usuario.setNombre("Maria");
        usuario.setApellido("Flores");
        usuario.setTipoDoc("DNI");
        usuario.setNumDoc("4578123");
        usuario.setCorreo("maria@example.com");
        usuario.setTelefono("910582963");
        usuario.setPassword("Cliente123");

        //Ejecutar
        IllegalArgumentException error = assertThrows(IllegalArgumentException.class,
                                                      () -> servicio.registrarUsuario(usuario));

        //Verificar
        assertEquals("El DNI debe tener exactamente 8 dígitos", error.getMessage());
    }

    //CP08
    @Test
    void registrar_correoSinArroba_debeLanzarExcepcion()
    {
        //Preparar datos
        UsuarioRequest usuario = new UsuarioRequest();
        UsuarioService servicio = new UsuarioService(mock(UsuarioRepository.class),
                                                     mock(ClaseRepository.class),
                                                     mock(PasswordEncoder.class));

        usuario.setRol("CLIENTE");
        usuario.setNombre("Maria");
        usuario.setApellido("Flores");
        usuario.setTipoDoc("DNI");
        usuario.setNumDoc("45781236");
        usuario.setCorreo("maria.example.com");
        usuario.setTelefono("910582963");
        usuario.setPassword("Cliente123");

        //Ejecutar
        IllegalArgumentException error = assertThrows(IllegalArgumentException.class,
                                                      () -> servicio.registrarUsuario(usuario));

        //Verificar
        assertEquals("El correo no es válido", error.getMessage());
    }

    //CP09
    @Test
    void registrar_telefonoDe8Digitos_debeLanzarExcepcion()
    {
        //Preparar datos
        UsuarioRequest usuario = new UsuarioRequest();
        UsuarioService servicio = new UsuarioService(mock(UsuarioRepository.class),
                                                     mock(ClaseRepository.class),
                                                     mock(PasswordEncoder.class));

        usuario.setRol("CLIENTE");
        usuario.setNombre("Maria");
        usuario.setApellido("Flores");
        usuario.setTipoDoc("DNI");
        usuario.setNumDoc("45781236");
        usuario.setCorreo("maria@example.com");
        usuario.setTelefono("91058296");
        usuario.setPassword("Cliente123");

        //Ejecutar
        IllegalArgumentException error = assertThrows(IllegalArgumentException.class,
                                                      () -> servicio.registrarUsuario(usuario));

        //Verificar
        assertEquals("El teléfono debe tener 9 dígitos", error.getMessage());
    }

    //CP10
    @Test
    void registrar_contrasenaSinNumeros_debeLanzarExcepcion()
    {
        //Preparar datos
        UsuarioRequest usuario = new UsuarioRequest();
        UsuarioService servicio = new UsuarioService(mock(UsuarioRepository.class),
                                                     mock(ClaseRepository.class),
                                                     mock(PasswordEncoder.class));

        usuario.setRol("CLIENTE");
        usuario.setNombre("Maria");
        usuario.setApellido("Flores");
        usuario.setTipoDoc("DNI");
        usuario.setNumDoc("45781236");
        usuario.setCorreo("maria@example.com");
        usuario.setTelefono("910582963");
        usuario.setPassword("ClaveSegura");

        //Ejecutar
        IllegalArgumentException error = assertThrows(IllegalArgumentException.class,
                                                      () -> servicio.registrarUsuario(usuario));

        //Verificar
        assertEquals("La contraseña debe tener al menos 8 caracteres, con letras y números", error.getMessage());
    }

    //CP11
    @Test
    void registrar_dniYaRegistrado_debeLanzarExcepcion()
    {
        //Preparar datos
        UsuarioRequest usuario = new UsuarioRequest();
        UsuarioRepository repositorio = mock(UsuarioRepository.class);
        UsuarioService servicio = new UsuarioService(repositorio,
                                                     mock(ClaseRepository.class),
                                                     mock(PasswordEncoder.class));

        usuario.setRol("CLIENTE");
        usuario.setNombre("Maria");
        usuario.setApellido("Flores");
        usuario.setTipoDoc("DNI");
        usuario.setNumDoc("45781236");
        usuario.setCorreo("maria@example.com");
        usuario.setTelefono("910582963");
        usuario.setPassword("Cliente123");

        // La base de datos "responde" que ese DNI ya existe
        when(repositorio.existsByNumDoc("45781236")).thenReturn(true);

        //Ejecutar
        IllegalArgumentException error = assertThrows(IllegalArgumentException.class,
                                                      () -> servicio.registrarUsuario(usuario));

        //Verificar
        assertEquals("El número de documento ya está registrado", error.getMessage());
    }

    //CP12
    @Test
    void registrar_correoYaRegistrado_debeLanzarExcepcion()
    {
        //Preparar datos
        UsuarioRequest usuario = new UsuarioRequest();
        UsuarioRepository repositorio = mock(UsuarioRepository.class);
        UsuarioService servicio = new UsuarioService(repositorio,
                                                     mock(ClaseRepository.class),
                                                     mock(PasswordEncoder.class));

        usuario.setRol("CLIENTE");
        usuario.setNombre("Maria");
        usuario.setApellido("Flores");
        usuario.setTipoDoc("DNI");
        usuario.setNumDoc("45781236");
        usuario.setCorreo("maria.flores@example.com");
        usuario.setTelefono("910582963");
        usuario.setPassword("Cliente123");

        // El DNI es nuevo, pero el correo ya existe
        when(repositorio.existsByNumDoc("45781236")).thenReturn(false);
        when(repositorio.existsByCorreo("maria.flores@example.com")).thenReturn(true);

        //Ejecutar
        IllegalArgumentException error = assertThrows(IllegalArgumentException.class,
                                                      () -> servicio.registrarUsuario(usuario));

        //Verificar
        assertEquals("El correo ya está registrado", error.getMessage());
    }

    //CP13
    @Test
    void registrar_instructorSinDisciplina_debeLanzarExcepcion()
    {
        //Preparar datos
        UsuarioRequest usuario = new UsuarioRequest();
        UsuarioService servicio = new UsuarioService(mock(UsuarioRepository.class),
                                                     mock(ClaseRepository.class),
                                                     mock(PasswordEncoder.class));

        usuario.setRol("INSTRUCTOR");
        usuario.setNombre("Diego");
        usuario.setApellido("Ramirez");
        usuario.setTipoDoc("DNI");
        usuario.setNumDoc("41235678");
        usuario.setCorreo("diego@puntomarcial.com");
        usuario.setTelefono("911223344");
        usuario.setPassword("Instructor123");
        usuario.setDisciplina(null);

        //Ejecutar
        IllegalArgumentException error = assertThrows(IllegalArgumentException.class,
                                                      () -> servicio.registrarUsuario(usuario));

        //Verificar
        assertEquals("El instructor debe tener una disciplina: MMA, Muay Thai o Box", error.getMessage());
    }

    //CP14
    @Test
    void registrar_datosCorrectos_debeGuardarConContrasenaEncriptada()
    {
        //Preparar datos
        UsuarioRequest usuario = new UsuarioRequest();
        UsuarioRepository repositorio = mock(UsuarioRepository.class);
        PasswordEncoder encriptador = mock(PasswordEncoder.class);
        UsuarioService servicio = new UsuarioService(repositorio, mock(ClaseRepository.class), encriptador);

        usuario.setRol("CLIENTE");
        usuario.setNombre("Maria");
        usuario.setApellido("Flores");
        usuario.setTipoDoc("DNI");
        usuario.setNumDoc("45781236");
        usuario.setCorreo("maria@example.com");
        usuario.setTelefono("910582963");
        usuario.setPassword("Cliente123");

        when(encriptador.encode("Cliente123")).thenReturn("contraseña-encriptada");
        // Al guardar, la base de datos devuelve el mismo usuario
        when(repositorio.save(any(Usuario.class))).thenAnswer(invocacion -> invocacion.getArgument(0));

        //Ejecutar
        UsuarioResponse respuesta = servicio.registrarUsuario(usuario);

        //Verificar
        assertEquals("CLIENTE", respuesta.getRol());
        assertEquals("ACTIVO", respuesta.getEstado());
        verify(encriptador).encode("Cliente123");          // la contraseña se encriptó
        verify(repositorio).save(any(Usuario.class));       // se guardó una sola vez
    }

    //CP15
    @Test
    void actualizar_usuarioQueNoExiste_debeLanzarExcepcion()
    {
        //Preparar datos
        UsuarioRequest usuario = new UsuarioRequest();
        UsuarioRepository repositorio = mock(UsuarioRepository.class);
        UsuarioService servicio = new UsuarioService(repositorio,
                                                     mock(ClaseRepository.class),
                                                     mock(PasswordEncoder.class));

        usuario.setNombre("Maria");
        usuario.setApellido("Flores");
        usuario.setCorreo("maria@example.com");
        usuario.setTelefono("910582963");

        when(repositorio.findById(9999)).thenReturn(Optional.empty());

        //Ejecutar
        IllegalArgumentException error = assertThrows(IllegalArgumentException.class,
                                                      () -> servicio.actualizarUsuario(9999, usuario));

        //Verificar
        assertEquals("El usuario no existe", error.getMessage());
    }

    //CP16
    @Test
    void actualizar_correoDeOtroUsuario_debeLanzarExcepcion()
    {
        //Preparar datos
        UsuarioRequest usuario = new UsuarioRequest();
        UsuarioRepository repositorio = mock(UsuarioRepository.class);
        UsuarioService servicio = new UsuarioService(repositorio,
                                                     mock(ClaseRepository.class),
                                                     mock(PasswordEncoder.class));

        Cliente maria = new Cliente();
        maria.setIdUsuario(1001);

        usuario.setNombre("Maria");
        usuario.setApellido("Flores");
        usuario.setCorreo("carlos.mendoza@example.com");
        usuario.setTelefono("910582963");

        when(repositorio.findById(1001)).thenReturn(Optional.of(maria));
        // Ese correo ya lo usa otro usuario
        when(repositorio.existsByCorreoAndIdUsuarioNot("carlos.mendoza@example.com", 1001)).thenReturn(true);

        //Ejecutar
        IllegalArgumentException error = assertThrows(IllegalArgumentException.class,
                                                      () -> servicio.actualizarUsuario(1001, usuario));

        //Verificar
        assertEquals("El correo ya está en uso por otro usuario", error.getMessage());
    }
}