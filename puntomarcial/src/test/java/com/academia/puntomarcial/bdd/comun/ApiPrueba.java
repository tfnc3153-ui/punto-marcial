package com.academia.puntomarcial.bdd.comun;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Prepara los datos de un escenario llamando directamente al backend (sin abrir el navegador).
 * Así cada escenario crea sus propios datos y se puede ejecutar las veces que se quiera:
 * por ejemplo, un cliente nuevo nunca tiene una reserva repetida.
 */
public class ApiPrueba {

    private static final HttpClient HTTP = HttpClient.newHttpClient();
    private static final Random AZAR = new Random();

    /** Datos del cliente creado para el escenario. */
    public record ClientePrueba(int id, String correo, String clave) {
    }

    /** Datos de la clase creada para el escenario. */
    public record ClasePrueba(LocalDateTime inicio, char sala) {
    }

    /** Registra un cliente nuevo con DNI, correo y teléfono que no se repiten. */
    public static ClientePrueba crearCliente() {
        String dni = String.format("%08d", AZAR.nextInt(100_000_000));
        String correo = "selenium" + System.currentTimeMillis() + "@prueba.com";
        String clave = "Prueba123";
        String json = """
                {"rol":"CLIENTE","nombre":"Cliente","apellido":"Selenium","tipoDoc":"DNI",
                 "numDoc":"%s","correo":"%s","telefono":"9%08d","password":"%s",
                 "estadoMembresia":"VIGENTE"}
                """.formatted(dni, correo, AZAR.nextInt(100_000_000), clave);
        String respuesta = enviar("POST", "/usuarios", json, 201);
        return new ClientePrueba(primerNumero(respuesta, "idUsuario"), correo, clave);
    }

    /** Reserva una clase para el cliente y devuelve el número de la reserva. */
    public static int reservar(int idCliente, int idClase) {
        String json = """
                {"cliente":{"idUsuario":%d},"clase":{"idClase":%d}}
                """.formatted(idCliente, idClase);
        String respuesta = enviar("POST", "/reservas", json, 201);
        return primerNumero(respuesta, "idReserva");
    }

    /** Cancela todas las reservas del cliente (limpieza al terminar). Ignora las que no se pueden cancelar. */
    public static void cancelarReservas(int idCliente) {
        String reservas = enviar("GET", "/reservas/cliente/" + idCliente, null, 200);
        Matcher m = Pattern.compile("\"idReserva\"\\s*:\\s*(\\d+)").matcher(reservas);
        while (m.find()) {
            try {
                enviar("PUT", "/reservas/" + m.group(1) + "/cancelar", null, 200);
            } catch (IllegalStateException e) {
                // Faltan menos de 2 horas: se deja como está.
            }
        }
    }

    /**
     * Crea una clase de Box sin reservas en una fecha lejana (entre 200 y 350 días).
     * Si choca con otra clase (traslape), prueba con otra fecha y sala.
     */
    public static ClasePrueba crearClaseSinReservas() {
        for (int intento = 0; intento < 10; intento++) {
            LocalDateTime inicio = LocalDateTime.now().plusDays(200 + AZAR.nextInt(150))
                    .withHour(6).withMinute(0).withSecond(0).withNano(0);
            char sala = "ABCD".charAt(AZAR.nextInt(4));
            String json = """
                    {"disciplina":"BOX","instructor":{"idUsuario":1105},"sala":"%s",
                     "horarioInicio":"%s","horarioFin":"%s","aforoMaximo":10}
                    """.formatted(sala, inicio, inicio.plusHours(1));
            try {
                enviar("POST", "/clases", json, 201);
                return new ClasePrueba(inicio, sala);
            } catch (IllegalStateException e) {
                // Traslape: se intenta con otra fecha.
            }
        }
        throw new IllegalStateException("No se pudo crear la clase de prueba");
    }

    private static String enviar(String metodo, String ruta, String json, int estadoEsperado) {
        HttpRequest pedido = HttpRequest.newBuilder(URI.create(Navegador.API + ruta))
                .header("Content-Type", "application/json")
                .method(metodo, json == null
                        ? HttpRequest.BodyPublishers.noBody()
                        : HttpRequest.BodyPublishers.ofString(json))
                .build();
        try {
            HttpResponse<String> respuesta = HTTP.send(pedido, HttpResponse.BodyHandlers.ofString());
            if (respuesta.statusCode() != estadoEsperado) {
                throw new IllegalStateException(metodo + " " + ruta + " respondió "
                        + respuesta.statusCode() + ": " + respuesta.body());
            }
            return respuesta.body();
        } catch (java.io.IOException e) {
            throw new IllegalStateException("No se pudo conectar con el backend en " + Navegador.API, e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException(e);
        }
    }

    private static int primerNumero(String json, String campo) {
        Matcher m = Pattern.compile("\"" + campo + "\"\\s*:\\s*(\\d+)").matcher(json);
        if (!m.find()) {
            throw new IllegalStateException("La respuesta no tiene " + campo + ": " + json);
        }
        return Integer.parseInt(m.group(1));
    }
}