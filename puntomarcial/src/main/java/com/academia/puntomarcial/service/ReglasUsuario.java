package com.academia.puntomarcial.service;

import java.util.List;

/**
 * Reglas de negocio de los usuarios (registro, roles y desactivación).
 * No depende de Spring ni de la base de datos: se puede probar solo con JUnit 5.
 */
public final class ReglasUsuario {

    public static final List<String> ROLES = List.of("ADMINISTRADOR", "INSTRUCTOR", "CLIENTE");
    public static final List<String> ESTADOS = List.of("ACTIVO", "INACTIVO");

    private ReglasUsuario() {
    }

    /** true si el rol es ADMINISTRADOR, INSTRUCTOR o CLIENTE. */
    public static boolean rolValido(String rol) {
        return rol != null && ROLES.contains(rol);
    }

    /** true si el estado es ACTIVO o INACTIVO. */
    public static boolean estadoValido(String estado) {
        return estado != null && ESTADOS.contains(estado);
    }

    /** true si el correo tiene la forma texto@dominio.ext */
    public static boolean correoValido(String correo) {
        return correo != null && correo.matches("[^\\s@]+@[^\\s@]+\\.[^\\s@]+");
    }

    /** true si el teléfono tiene exactamente 9 dígitos. */
    public static boolean telefonoValido(String telefono) {
        return telefono != null && telefono.matches("\\d{9}");
    }

    /** true si la contraseña tiene 8 caracteres o más, con al menos una letra y un número. */
    public static boolean passwordValida(String password) {
        return password != null
                && password.length() >= 8
                && password.matches(".*[A-Za-z].*")
                && password.matches(".*\\d.*");
    }

    /** Un instructor solo se puede desactivar si no tiene clases futuras asignadas. */
    public static boolean puedeDesactivarInstructor(long clasesFuturas) {
        return clasesFuturas == 0;
    }

    /** Siempre debe quedar al menos un administrador activo. */
    public static boolean puedeDesactivarAdministrador(long administradoresActivos) {
        return administradoresActivos > 1;
    }
}
