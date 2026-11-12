package com.academia.puntomarcial.dto;

import java.util.List;

import com.academia.puntomarcial.model.Clase;

/**
 * Clase asignada a un instructor, con el resumen de alumnos y asistencia.
 * La lista de alumnos solo se llena en el detalle de una clase.
 */
public class ClaseInstructorResponse {
    private Clase clase;
    private int inscritos;
    private int asistentes;
    private int pendientes;
    private boolean puedeTomarAsistencia;
    private List<AlumnoResponse> alumnos;

    public ClaseInstructorResponse(Clase clase, List<AlumnoResponse> alumnos, boolean puedeTomarAsistencia,
                                   boolean incluirAlumnos) {
        this.clase = clase;
        this.inscritos = alumnos.size();
        this.asistentes = (int) alumnos.stream().filter(a -> "ASISTIO".equals(a.getAsistencia())).count();
        this.pendientes = (int) alumnos.stream().filter(a -> "PENDIENTE".equals(a.getAsistencia())).count();
        this.puedeTomarAsistencia = puedeTomarAsistencia;
        this.alumnos = incluirAlumnos ? alumnos : null;
    }

    public Clase getClase() { return clase; }
    public int getInscritos() { return inscritos; }
    public int getAsistentes() { return asistentes; }
    public int getPendientes() { return pendientes; }
    public boolean isPuedeTomarAsistencia() { return puedeTomarAsistencia; }
    public List<AlumnoResponse> getAlumnos() { return alumnos; }
}
