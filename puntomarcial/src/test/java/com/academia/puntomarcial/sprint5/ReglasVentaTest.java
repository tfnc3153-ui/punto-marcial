package com.academia.puntomarcial.sprint5;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import com.academia.puntomarcial.service.ReglasVenta;

/**
 * Sprint 5 – HU-09, HU-10, HU-11 y HU-12: casos borde de las reglas de venta.
 * Añadidas en la PC 12 para cubrir las líneas de ReglasVenta que JaCoCo marcaba sin probar.
 * Los casos fueron sugeridos con apoyo de IA y revisados contra las reglas del sistema.
 */
public class ReglasVentaTest {

    //CP66
    @Test
    void calcularSubtotal_precioNulo_debeLanzarExcepcion()
    {
        //Preparar datos
        BigDecimal precio = null;

        //Ejecutar
        IllegalArgumentException error = assertThrows(IllegalArgumentException.class,
                                                      () -> ReglasVenta.calcularSubtotal(precio, 2));

        //Verificar
        assertEquals("El precio es obligatorio", error.getMessage());
    }

    //CP67
    @Test
    void calcularSubtotal_cantidadCero_debeLanzarExcepcion()
    {
        //Preparar datos
        BigDecimal precio = new BigDecimal("19.90");

        //Ejecutar
        IllegalArgumentException error = assertThrows(IllegalArgumentException.class,
                                                      () -> ReglasVenta.calcularSubtotal(precio, 0));

        //Verificar
        assertEquals("La cantidad debe ser 1 o más", error.getMessage());
    }

    //CP68
    @Test
    void descontarStock_cantidadMayorAlStock_debeLanzarExcepcion()
    {
        //Preparar datos: hay 2 unidades y se intentan descontar 5
        int stock = 2;
        int cantidad = 5;

        //Ejecutar
        IllegalArgumentException error = assertThrows(IllegalArgumentException.class,
                                                      () -> ReglasVenta.descontarStock(stock, cantidad));

        //Verificar
        assertEquals("El stock no puede quedar negativo", error.getMessage());
    }

    //CP69
    @Test
    void vencimientoValido_mes13_debeSerInvalido()
    {
        //Preparar datos: el mes 13 no existe, aunque el año sea futuro
        String vencimiento = "13/29";
        LocalDate hoy = LocalDate.of(2026, 11, 12);

        //Ejecutar
        boolean valido = ReglasVenta.vencimientoValido(vencimiento, hoy);

        //Verificar
        assertFalse(valido);
    }

    //CP70
    @Test
    void numeroBoleta_idValido_debeTenerFormatoB001()
    {
        //Preparar datos
        int idVenta = 123;

        //Ejecutar
        String numero = ReglasVenta.numeroBoleta(idVenta);

        //Verificar
        assertEquals("B001-000123", numero);
    }

    //CP71
    @Test
    void numeroBoleta_idCero_debeLanzarExcepcion()
    {
        //Preparar datos
        int idVenta = 0;

        //Ejecutar
        IllegalArgumentException error = assertThrows(IllegalArgumentException.class,
                                                      () -> ReglasVenta.numeroBoleta(idVenta));

        //Verificar
        assertEquals("El id de la venta debe ser 1 o más", error.getMessage());
    }
}
