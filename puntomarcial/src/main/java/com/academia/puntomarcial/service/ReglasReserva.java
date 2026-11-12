package com.academia.puntomarcial.service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Reglas de negocio de clases, reservas y clientes.
 * No depende de Spring ni de la base de datos: recibe datos y devuelve un resultado,
 * por eso se puede probar solo con JUnit 5.
 */
public final class ReglasReserva {

    public static final int AFORO_MAXIMO_PERMITIDO = 30;
    public static final long HORAS_MINIMAS_CANCELACION = 2;

    public static final List<String> DISCIPLINAS = List.of("MMA", "MUAY_THAI", "BOX");
    public static final List<String> SALAS = List.of("A", "B", "C", "D");

    private ReglasReserva() {
    }

    /** true si la clase todavía tiene al menos un cupo. */
    public static boolean hayCupo(int aforoDisponible) {
        return aforoDisponible > 0;
    }

    /** true si faltan 2 horas o más para el inicio de la clase. */
    public static boolean puedeCancelar(LocalDateTime inicioClase, LocalDateTime ahora) {
        if (inicioClase == null || ahora == null) {
            return false;
        }
        Duration faltante = Duration.between(ahora, inicioClase);
        return faltante.compareTo(Duration.ofHours(HORAS_MINIMAS_CANCELACION)) >= 0;
    }

    /** true si los intervalos [ini1, fin1) y [ini2, fin2) se cruzan. Si son contiguos no hay traslape. */
    public static boolean hayTraslape(LocalDateTime ini1, LocalDateTime fin1,
                                      LocalDateTime ini2, LocalDateTime fin2) {
        return ini1.isBefore(fin2) && fin1.isAfter(ini2);
    }

    /** true si el aforo está entre 1 y 30. */
    public static boolean aforoValido(Integer aforo) {
        return aforo != null && aforo >= 1 && aforo <= AFORO_MAXIMO_PERMITIDO;
    }

    /** true si la disciplina es MMA, MUAY_THAI o BOX. */
    public static boolean disciplinaValida(String disciplina) {
        return disciplina != null && DISCIPLINAS.contains(disciplina);
    }

    /** true si la sala es A, B, C o D. */
    public static boolean salaValida(String sala) {
        return sala != null && SALAS.contains(sala);
    }

    /** true si el instructor dicta la disciplina de la clase. */
    public static boolean instructorDictaDisciplina(String disciplinaInstructor, String disciplinaClase) {
        return disciplinaInstructor != null && disciplinaInstructor.equals(disciplinaClase);
    }

    /** true si la clase ya empezó: ya no se puede reservar. */
    public static boolean claseYaInicio(LocalDateTime inicioClase, LocalDateTime ahora) {
        return inicioClase != null && ahora != null && !ahora.isBefore(inicioClase);
    }

    /** La asistencia se toma desde que la clase empieza. */
    public static boolean puedeTomarAsistencia(LocalDateTime inicioClase, LocalDateTime ahora) {
        return claseYaInicio(inicioClase, ahora);
    }

    /** true si la asistencia es ASISTIO o NO_ASISTIO. */
    public static boolean asistenciaValida(String asistencia) {
        return "ASISTIO".equals(asistencia) || "NO_ASISTIO".equals(asistencia);
    }

    /** true si la clase no tiene ninguna reserva y por lo tanto se puede eliminar. */
    public static boolean puedeEliminarClase(Integer totalReservas) {
        return totalReservas == null || totalReservas == 0;
    }

    /** true si el DNI tiene exactamente 8 dígitos. */
    public static boolean dniValido(String dni) {
        return dni != null && dni.matches("\\d{8}");
    }
}
