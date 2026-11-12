package com.academia.puntomarcial.dto;

public class LoginResponse {
    private Integer idUsuario;
    private String nombre;
    private String apellido;
    private String correo;
    private String rol;

    public LoginResponse(Integer idUsuario, String nombre, String apellido, String correo, String rol) {
        this.idUsuario = idUsuario;
        this.nombre = nombre;
        this.apellido = apellido;
        this.correo = correo;
        this.rol = rol;
    }

    public Integer getIdUsuario() { return idUsuario; }
    public String getNombre() { return nombre; }
    public String getApellido() { return apellido; }
    public String getCorreo() { return correo; }
    public String getRol() { return rol; }
}