package com.academia.puntomarcial.dto;

import java.util.List;

import com.academia.puntomarcial.model.Venta;

/**
 * Resultado de la compra: la venta (PAGADA o RECHAZADA), un mensaje para el cliente
 * y los productos que quedaron en stock mínimo después de descontar (alerta HU-11).
 */
public class VentaResponse {
    private Venta venta;
    private String mensaje;
    private List<String> productosStockMinimo;
    private Boolean correoEnviado;
    private String mensajeCorreo;

    public VentaResponse(Venta venta, String mensaje, List<String> productosStockMinimo) {
        this.venta = venta;
        this.mensaje = mensaje;
        this.productosStockMinimo = productosStockMinimo;
    }

    public Venta getVenta() { return venta; }
    public String getMensaje() { return mensaje; }
    public List<String> getProductosStockMinimo() { return productosStockMinimo; }
    public Boolean getCorreoEnviado() { return correoEnviado; }
    public String getMensajeCorreo() { return mensajeCorreo; }

    public void informarCorreo(boolean enviado, String mensaje) {
        this.correoEnviado = enviado;
        this.mensajeCorreo = mensaje;
    }

    public boolean isPagada() {
        return venta != null && "PAGADA".equals(venta.getEstado());
    }
}
