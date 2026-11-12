package com.academia.puntomarcial.dto;

import java.time.LocalDate;

/**
 * Datos para registrar o actualizar un usuario desde el panel del administrador.
 * Según el rol se usan unos campos u otros:
 * CLIENTE -> estadoMembresia y fechaVencimiento; INSTRUCTOR -> disciplina; ADMINISTRADOR -> codigoEmpleado.
 */
public class UsuarioRequest {
    private String rol;
    private String nombre;
    private String apellido;
    private String tipoDoc;
    private String numDoc;
    private String correo;
    private String telefono;
    private String password;
    private String estadoMembresia;
    private LocalDate fechaVencimiento;
    private String disciplina;
    private String codigoEmpleado;

    public UsuarioRequest() {
    }

    public String getRol() { return rol; }
    public void setRol(String rol) { this.rol = rol; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }
    public String getTipoDoc() { return tipoDoc; }
    public void setTipoDoc(String tipoDoc) { this.tipoDoc = tipoDoc; }
    public String getNumDoc() { return numDoc; }
    public void setNumDoc(String numDoc) { this.numDoc = numDoc; }
    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }
    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getEstadoMembresia() { return estadoMembresia; }
    public void setEstadoMembresia(String estadoMembresia) { this.estadoMembresia = estadoMembresia; }
    public LocalDate getFechaVencimiento() { return fechaVencimiento; }
    public void setFechaVencimiento(LocalDate fechaVencimiento) { this.fechaVencimiento = fechaVencimiento; }
    public String getDisciplina() { return disciplina; }
    public void setDisciplina(String disciplina) { this.disciplina = disciplina; }
    public String getCodigoEmpleado() { return codigoEmpleado; }
    public void setCodigoEmpleado(String codigoEmpleado) { this.codigoEmpleado = codigoEmpleado; }
}
