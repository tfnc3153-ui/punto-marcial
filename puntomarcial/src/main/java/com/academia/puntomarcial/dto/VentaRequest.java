package com.academia.puntomarcial.dto;

import java.util.List;

/** Carrito + datos de pago que envía el cliente al confirmar la compra. */
public class VentaRequest {
    private Integer idCliente;
    private List<ItemCarritoRequest> items;
    private DatosPagoRequest pago;

    public VentaRequest() {
    }

    public Integer getIdCliente() { return idCliente; }
    public void setIdCliente(Integer idCliente) { this.idCliente = idCliente; }
    public List<ItemCarritoRequest> getItems() { return items; }
    public void setItems(List<ItemCarritoRequest> items) { this.items = items; }
    public DatosPagoRequest getPago() { return pago; }
    public void setPago(DatosPagoRequest pago) { this.pago = pago; }
}
