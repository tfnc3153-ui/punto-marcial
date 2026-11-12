package com.academia.puntomarcial.dto;

import java.math.BigDecimal;

/** Línea del carrito ya revisada contra el stock real. */
public class ItemCarritoResponse {
    private Integer idProducto;
    private String nombre;
    private String talla;
    private BigDecimal precioUnitario;
    private Integer cantidad;
    private BigDecimal subtotal;
    private Integer stockDisponible;
    private boolean stockSuficiente;

    public ItemCarritoResponse(Integer idProducto, String nombre, String talla,
                               BigDecimal precioUnitario, Integer cantidad, BigDecimal subtotal,
                               Integer stockDisponible, boolean stockSuficiente) {
        this.idProducto = idProducto;
        this.nombre = nombre;
        this.talla = talla;
        this.precioUnitario = precioUnitario;
        this.cantidad = cantidad;
        this.subtotal = subtotal;
        this.stockDisponible = stockDisponible;
        this.stockSuficiente = stockSuficiente;
    }

    public Integer getIdProducto() { return idProducto; }
    public String getNombre() { return nombre; }
    public String getTalla() { return talla; }
    public BigDecimal getPrecioUnitario() { return precioUnitario; }
    public Integer getCantidad() { return cantidad; }
    public BigDecimal getSubtotal() { return subtotal; }
    public Integer getStockDisponible() { return stockDisponible; }
    public boolean isStockSuficiente() { return stockSuficiente; }
}
