package com.academia.puntomarcial.dto;

/** Producto y cantidad que el cliente puso en el carrito. */
public class ItemCarritoRequest {
    private Integer idProducto;
    private Integer cantidad;

    public ItemCarritoRequest() {
    }

    public ItemCarritoRequest(Integer idProducto, Integer cantidad) {
        this.idProducto = idProducto;
        this.cantidad = cantidad;
    }

    public Integer getIdProducto() { return idProducto; }
    public void setIdProducto(Integer idProducto) { this.idProducto = idProducto; }
    public Integer getCantidad() { return cantidad; }
    public void setCantidad(Integer cantidad) { this.cantidad = cantidad; }
}
