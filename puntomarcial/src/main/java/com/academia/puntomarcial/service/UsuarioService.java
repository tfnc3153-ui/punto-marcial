package com.academia.puntomarcial.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.academia.puntomarcial.dto.UsuarioRequest;
import com.academia.puntomarcial.dto.UsuarioResponse;
import com.academia.puntomarcial.model.Administrador;
import com.academia.puntomarcial.model.Cliente;
import com.academia.puntomarcial.model.Instructor;
import com.academia.puntomarcial.model.Usuario;
import com.academia.puntomarcial.repository.ClaseRepository;
import com.academia.puntomarcial.repository.UsuarioRepository;

/**
 * Gestión de usuarios desde el panel del administrador:
 * registrar (con rol), actualizar, activar/desactivar, y el perfil de cada usuario.
 */
@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final ClaseRepository claseRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(
            UsuarioRepository usuarioRepository,
            ClaseRepository claseRepository,
            PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.claseRepository = claseRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<UsuarioResponse> listarUsuarios() {
        return usuarioRepository.findAllByOrderByApellidoAscNombreAsc().stream()
                .map(UsuarioResponse::de)
                .toList();
    }

    public UsuarioResponse buscarPorId(Integer id) {
        return UsuarioResponse.de(buscarUsuario(id));
    }

    @Transactional
    public UsuarioResponse registrarUsuario(UsuarioRequest datos) {
        if (!ReglasUsuario.rolValido(datos.getRol())) {
            throw new IllegalArgumentException("El rol debe ser Administrador, Instructor o Cliente");
        }

        validarDatosComunes(datos);

        if (!ReglasUsuario.passwordValida(datos.getPassword())) {
            throw new IllegalArgumentException(
                    "La contraseña debe tener al menos 8 caracteres, con letras y números");
        }
        if (usuarioRepository.existsByNumDoc(datos.getNumDoc().trim())) {
            throw new IllegalArgumentException("El número de documento ya está registrado");
        }
        if (usuarioRepository.existsByCorreo(datos.getCorreo().trim())) {
            throw new IllegalArgumentException("El correo ya está registrado");
        }

        Usuario usuario = switch (datos.getRol()) {
            case "ADMINISTRADOR" -> nuevoAdministrador(datos);
            case "INSTRUCTOR" -> nuevoInstructor(datos);
            default -> nuevoCliente(datos);
        };

        usuario.setNombre(datos.getNombre().trim());
        usuario.setApellido(datos.getApellido().trim());
        usuario.setTipoDoc(datos.getTipoDoc());
        usuario.setNumDoc(datos.getNumDoc().trim());
        usuario.setCorreo(datos.getCorreo().trim());
        usuario.setTelefono(datos.getTelefono().trim());
        usuario.setPassword(passwordEncoder.encode(datos.getPassword()));
        usuario.setEstado("ACTIVO");

        return UsuarioResponse.de(usuarioRepository.save(usuario));
    }

    /** Actualiza los datos de contacto y los propios del rol. El rol y el documento no cambian. */
    @Transactional
    public UsuarioResponse actualizarUsuario(Integer id, UsuarioRequest datos) {
        Usuario usuario = buscarUsuario(id);

        if (datos.getNombre() == null || datos.getNombre().isBlank()
                || datos.getApellido() == null || datos.getApellido().isBlank()) {
            throw new IllegalArgumentException("Nombre y apellido son obligatorios");
        }
        validarContacto(id, datos.getCorreo(), datos.getTelefono());

        usuario.setNombre(datos.getNombre().trim());
        usuario.setApellido(datos.getApellido().trim());
        usuario.setCorreo(datos.getCorreo().trim());
        usuario.setTelefono(datos.getTelefono().trim());

        if (usuario instanceof Cliente cliente) {
            if (datos.getEstadoMembresia() != null) {
                validarMembresia(datos.getEstadoMembresia());
                cliente.setEstadoMembresia(datos.getEstadoMembresia());
                cliente.setFechaVencimiento(datos.getFechaVencimiento());
            }
        } else if (usuario instanceof Instructor instructor) {
            if (datos.getDisciplina() != null && !datos.getDisciplina().equals(instructor.getDisciplina())) {
                if (!ReglasReserva.disciplinaValida(datos.getDisciplina())) {
                    throw new IllegalArgumentException("La disciplina debe ser MMA, Muay Thai o Box");
                }
                if (clasesFuturas(id) > 0) {
                    throw new IllegalArgumentException(
                            "No se puede cambiar la disciplina: el instructor tiene clases futuras asignadas");
                }
                instructor.setDisciplina(datos.getDisciplina());
            }
        } else if (usuario instanceof Administrador administrador) {
            if (datos.getCodigoEmpleado() != null && !datos.getCodigoEmpleado().isBlank()) {
                administrador.setCodigoEmpleado(datos.getCodigoEmpleado().trim());
            }
        }

        return UsuarioResponse.de(usuarioRepository.save(usuario));
    }

    /** Activa o desactiva un usuario. Un usuario inactivo no puede iniciar sesión. */
    @Transactional
    public UsuarioResponse cambiarEstado(Integer id, String estado) {
        if (!ReglasUsuario.estadoValido(estado)) {
            throw new IllegalArgumentException("El estado debe ser ACTIVO o INACTIVO");
        }

        Usuario usuario = buscarUsuario(id);

        if ("INACTIVO".equals(estado) && !"INACTIVO".equals(usuario.getEstado())) {
            if (usuario instanceof Instructor
                    && !ReglasUsuario.puedeDesactivarInstructor(clasesFuturas(id))) {
                throw new IllegalArgumentException(
                        "No se puede desactivar: el instructor tiene clases futuras asignadas");
            }
            if (usuario instanceof Administrador
                    && !ReglasUsuario.puedeDesactivarAdministrador(usuarioRepository.contarAdministradoresActivos())) {
                throw new IllegalArgumentException("Debe quedar al menos un administrador activo");
            }
        }

        usuario.setEstado(estado);
        return UsuarioResponse.de(usuarioRepository.save(usuario));
    }

    /** El propio usuario actualiza su teléfono y su correo (Mi perfil). */
    @Transactional
    public UsuarioResponse actualizarPerfil(Integer id, String correo, String telefono) {
        Usuario usuario = buscarUsuario(id);
        validarContacto(id, correo, telefono);

        usuario.setCorreo(correo.trim());
        usuario.setTelefono(telefono.trim());
        return UsuarioResponse.de(usuarioRepository.save(usuario));
    }

    private Usuario buscarUsuario(Integer id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("El usuario no existe"));
    }

    private long clasesFuturas(Integer idInstructor) {
        return claseRepository.countByInstructorIdUsuarioAndHorarioInicioAfter(idInstructor, LocalDateTime.now());
    }

    private void validarDatosComunes(UsuarioRequest datos) {
        if (datos.getNombre() == null || datos.getNombre().isBlank()
                || datos.getApellido() == null || datos.getApellido().isBlank()
                || datos.getTipoDoc() == null || datos.getNumDoc() == null || datos.getNumDoc().isBlank()) {
            throw new IllegalArgumentException("Complete todos los campos obligatorios");
        }
        if ("DNI".equals(datos.getTipoDoc()) && !ReglasReserva.dniValido(datos.getNumDoc().trim())) {
            throw new IllegalArgumentException("El DNI debe tener exactamente 8 dígitos");
        }
        validarContacto(null, datos.getCorreo(), datos.getTelefono());
    }

    private void validarContacto(Integer idUsuario, String correo, String telefono) {
        if (!ReglasUsuario.correoValido(correo == null ? null : correo.trim())) {
            throw new IllegalArgumentException("El correo no es válido");
        }
        if (!ReglasUsuario.telefonoValido(telefono == null ? null : telefono.trim())) {
            throw new IllegalArgumentException("El teléfono debe tener 9 dígitos");
        }
        if (idUsuario != null && usuarioRepository.existsByCorreoAndIdUsuarioNot(correo.trim(), idUsuario)) {
            throw new IllegalArgumentException("El correo ya está en uso por otro usuario");
        }
    }

    private void validarMembresia(String estadoMembresia) {
        if (!"VIGENTE".equals(estadoMembresia) && !"VENCIDA".equals(estadoMembresia)) {
            throw new IllegalArgumentException("La membresía debe ser VIGENTE o VENCIDA");
        }
    }

    private Cliente nuevoCliente(UsuarioRequest datos) {
        Cliente cliente = new Cliente();
        validarMembresia(datos.getEstadoMembresia() == null ? "VIGENTE" : datos.getEstadoMembresia());
        cliente.setEstadoMembresia(datos.getEstadoMembresia() == null ? "VIGENTE" : datos.getEstadoMembresia());
        cliente.setFechaVencimiento(datos.getFechaVencimiento());
        return cliente;
    }

    private Instructor nuevoInstructor(UsuarioRequest datos) {
        if (!ReglasReserva.disciplinaValida(datos.getDisciplina())) {
            throw new IllegalArgumentException("El instructor debe tener una disciplina: MMA, Muay Thai o Box");
        }
        Instructor instructor = new Instructor();
        instructor.setDisciplina(datos.getDisciplina());
        return instructor;
    }

    private Administrador nuevoAdministrador(UsuarioRequest datos) {
        if (datos.getCodigoEmpleado() == null || datos.getCodigoEmpleado().isBlank()) {
            throw new IllegalArgumentException("El administrador debe tener un código de empleado");
        }
        Administrador administrador = new Administrador();
        administrador.setCodigoEmpleado(datos.getCodigoEmpleado().trim());
        return administrador;
    }
}
