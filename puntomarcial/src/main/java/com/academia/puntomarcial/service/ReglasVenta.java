package com.academia.puntomarcial.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

/**
 * Reglas de negocio de productos y ventas.
 * No depende de Spring ni de la base de datos: recibe datos y devuelve un resultado,
 * por eso se puede probar solo con JUnit 5.
 */
public final class ReglasVenta {

    public static final List<String> CATEGORIAS =
            List.of("ROPA", "PROTECCION", "EQUIPAMIENTO", "ACCESORIOS", "SUPLEMENTOS");

    public static final List<String> DISCIPLINAS =
            List.of("MMA", "MUAY_THAI", "BOX", "GENERAL");

    private ReglasVenta() {
    }

    /** true si el nombre no está vacío. */
    public static boolean nombreValido(String nombre) {
        return nombre != null && !nombre.isBlank();
    }

    /** true si el precio es mayor que 0. */
    public static boolean precioValido(BigDecimal precio) {
        return precio != null && precio.compareTo(BigDecimal.ZERO) > 0;
    }

    /** true si el stock es 0 o más. */
    public static boolean stockValido(Integer stock) {
        return stock != null && stock >= 0;
    }

    /** true si el stock mínimo es 0 o más. */
    public static boolean stockMinimoValido(Integer stockMinimo) {
        return stockMinimo != null && stockMinimo >= 0;
    }

    /** true si el stock llegó al mínimo o está por debajo. */
    public static boolean stockBajo(int stock, int stockMinimo) {
        return stock <= stockMinimo;
    }

    /** true si la categoría es una de las permitidas. */
    public static boolean categoriaValida(String categoria) {
        return categoria != null && CATEGORIAS.contains(categoria);
    }

    /** true si la disciplina es MMA, MUAY_THAI, BOX o GENERAL. */
    public static boolean disciplinaValida(String disciplina) {
        return disciplina != null && DISCIPLINAS.contains(disciplina);
    }

    /** true si la cantidad pedida es 1 o más. */
    public static boolean cantidadValida(Integer cantidad) {
        return cantidad != null && cantidad >= 1;
    }

    /** true si hay stock para la cantidad pedida. */
    public static boolean stockSuficiente(int stock, int cantidad) {
        return cantidad <= stock;
    }

    /** precio × cantidad, redondeado a 2 decimales. */
    public static BigDecimal calcularSubtotal(BigDecimal precio, int cantidad) {
        if (precio == null) {
            throw new IllegalArgumentException("El precio es obligatorio");
        }
        if (cantidad < 1) {
            throw new IllegalArgumentException("La cantidad debe ser 1 o más");
        }
        return precio.multiply(BigDecimal.valueOf(cantidad)).setScale(2, RoundingMode.HALF_UP);
    }

    /** Stock que queda después de vender. Nunca puede quedar negativo. */
    public static int descontarStock(int stock, int cantidad) {
        if (!stockSuficiente(stock, cantidad)) {
            throw new IllegalArgumentException("El stock no puede quedar negativo");
        }
        return stock - cantidad;
    }

    /** true si la tarjeta tiene exactamente 16 dígitos. */
    public static boolean tarjetaValida(String numeroTarjeta) {
        return numeroTarjeta != null && numeroTarjeta.matches("\\d{16}");
    }

    /**
     * true si el vencimiento tiene el formato MM/AA y no está vencido.
     * La tarjeta vale hasta el último día de su mes, así que el mes actual todavía es válido.
     */
    public static boolean vencimientoValido(String mesAnioVencimiento, LocalDate hoy) {
        if (mesAnioVencimiento == null || hoy == null
                || !mesAnioVencimiento.matches("(0[1-9]|1[0-2])/\\d{2}")) {
            return false;
        }
        int mes = Integer.parseInt(mesAnioVencimiento.substring(0, 2));
        int anio = 2000 + Integer.parseInt(mesAnioVencimiento.substring(3, 5));
        return !YearMonth.of(anio, mes).isBefore(YearMonth.from(hoy));
    }

    /** true si el CVV tiene exactamente 3 dígitos. */
    public static boolean cvvValido(String cvv) {
        return cvv != null && cvv.matches("\\d{3}");
    }

    /** HU-10: el pago simulado es válido si la tarjeta, el vencimiento y el CVV lo son. */
    public static boolean pagoValido(String numeroTarjeta, String mesAnioVencimiento, String cvv, LocalDate hoy) {
        return tarjetaValida(numeroTarjeta)
                && vencimientoValido(mesAnioVencimiento, hoy)
                && cvvValido(cvv);
    }

    /** Número de boleta a partir del id de la venta: 123 -> B001-000123. */
    public static String numeroBoleta(int idVenta) {
        if (idVenta < 1) {
            throw new IllegalArgumentException("El id de la venta debe ser 1 o más");
        }
        return String.format("B001-%06d", idVenta);
    }

    /** Suma de los subtotales, con 2 decimales. Una lista vacía da 0.00. */
    public static BigDecimal calcularTotal(List<BigDecimal> subtotales) {
        BigDecimal total = BigDecimal.ZERO;
        for (BigDecimal subtotal : subtotales) {
            total = total.add(subtotal);
        }
        return total.setScale(2, RoundingMode.HALF_UP);
    }
}
