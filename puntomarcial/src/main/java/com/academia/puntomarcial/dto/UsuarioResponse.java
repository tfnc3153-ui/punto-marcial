package com.academia.puntomarcial.dto;

import java.time.LocalDate;

import com.academia.puntomarcial.model.Administrador;
import com.academia.puntomarcial.model.Cliente;
import com.academia.puntomarcial.model.Instructor;
import com.academia.puntomarcial.model.Usuario;

/** Usuario con su rol y los datos propios de ese rol. Nunca incluye la contraseña. */
public class UsuarioResponse {
    private Integer idUsuario;
    private String rol;
    private String nombre;
    private String apellido;
    private String tipoDoc;
    private String numDoc;
    private String correo;
    private String telefono;
    private String estado;
    private String estadoMembresia;
    private LocalDate fechaVencimiento;
    private String disciplina;
    private String codigoEmpleado;

    public static String rolDe(Usuario usuario) {
        if (usuario instanceof Administrador) {
            return "ADMINISTRADOR";
        }
        if (usuario instanceof Instructor) {
            return "INSTRUCTOR";
        }
        return "CLIENTE";
    }

    public static UsuarioResponse de(Usuario usuario) {
        UsuarioResponse r = new UsuarioResponse();
        r.idUsuario = usuario.getIdUsuario();
        r.rol = rolDe(usuario);
        r.nombre = usuario.getNombre();
        r.apellido = usuario.getApellido();
        r.tipoDoc = usuario.getTipoDoc();
        r.numDoc = usuario.getNumDoc();
        r.correo = usuario.getCorreo();
        r.telefono = usuario.getTelefono();
        r.estado = usuario.getEstado();

        if (usuario instanceof Cliente cliente) {
            r.estadoMembresia = cliente.getEstadoMembresia();
            r.fechaVencimiento = cliente.getFechaVencimiento();
        } else if (usuario instanceof Instructor instructor) {
            r.disciplina = instructor.getDisciplina();
        } else if (usuario instanceof Administrador administrador) {
            r.codigoEmpleado = administrador.getCodigoEmpleado();
        }
        return r;
    }

    public Integer getIdUsuario() { return idUsuario; }
    public String getRol() { return rol; }
    public String getNombre() { return nombre; }
    public String getApellido() { return apellido; }
    public String getTipoDoc() { return tipoDoc; }
    public String getNumDoc() { return numDoc; }
    public String getCorreo() { return correo; }
    public String getTelefono() { return telefono; }
    public String getEstado() { return estado; }
    public String getEstadoMembresia() { return estadoMembresia; }
    public LocalDate getFechaVencimiento() { return fechaVencimiento; }
    public String getDisciplina() { return disciplina; }
    public String getCodigoEmpleado() { return codigoEmpleado; }
}
