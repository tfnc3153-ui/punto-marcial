package com.academia.puntomarcial.dto;

import java.math.BigDecimal;
import java.util.List;

/**
 * Resultado de revisar el carrito: subtotales, total y si se puede continuar.
 * Si algún producto no tiene stock suficiente, puedeContinuar es false.
 */
public class VerificacionCarritoResponse {
    private List<ItemCarritoResponse> items;
    private BigDecimal total;
    private boolean puedeContinuar;
    private List<String> productosSinStock;

    public VerificacionCarritoResponse(List<ItemCarritoResponse> items, BigDecimal total,
                                       boolean puedeContinuar, List<String> productosSinStock) {
        this.items = items;
        this.total = total;
        this.puedeContinuar = puedeContinuar;
        this.productosSinStock = productosSinStock;
    }

    public List<ItemCarritoResponse> getItems() { return items; }
    public BigDecimal getTotal() { return total; }
    public boolean isPuedeContinuar() { return puedeContinuar; }
    public List<String> getProductosSinStock() { return productosSinStock; }
}
