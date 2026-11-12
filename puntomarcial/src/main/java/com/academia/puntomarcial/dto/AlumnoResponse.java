package com.academia.puntomarcial.dto;

import com.academia.puntomarcial.model.Reserva;

/** Alumno inscrito en una clase (reserva activa), visto por el instructor. */
public class AlumnoResponse {
    private Integer idReserva;
    private String nombre;
    private String apellido;
    private String telefono;
    private String asistencia;

    public static AlumnoResponse de(Reserva reserva) {
        AlumnoResponse a = new AlumnoResponse();
        a.idReserva = reserva.getIdReserva();
        a.nombre = reserva.getCliente().getNombre();
        a.apellido = reserva.getCliente().getApellido();
        a.telefono = reserva.getCliente().getTelefono();
        a.asistencia = reserva.getAsistencia();
        return a;
    }

    public Integer getIdReserva() { return idReserva; }
    public String getNombre() { return nombre; }
    public String getApellido() { return apellido; }
    public String getTelefono() { return telefono; }
    public String getAsistencia() { return asistencia; }
}
