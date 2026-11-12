package com.academia.puntomarcial.dto;

/**
 * Datos de la tarjeta (pago simulado). Solo se usan para validar:
 * nunca se guardan ni el número completo ni el CVV.
 */
public class DatosPagoRequest {
    private String titular;
    private String numeroTarjeta;
    private String vencimiento; // MM/AA
    private String cvv;

    public DatosPagoRequest() {
    }

    public DatosPagoRequest(String titular, String numeroTarjeta, String vencimiento, String cvv) {
        this.titular = titular;
        this.numeroTarjeta = numeroTarjeta;
        this.vencimiento = vencimiento;
        this.cvv = cvv;
    }

    public String getTitular() { return titular; }
    public void setTitular(String titular) { this.titular = titular; }
    public String getNumeroTarjeta() { return numeroTarjeta; }
    public void setNumeroTarjeta(String numeroTarjeta) { this.numeroTarjeta = numeroTarjeta; }
    public String getVencimiento() { return vencimiento; }
    public void setVencimiento(String vencimiento) { this.vencimiento = vencimiento; }
    public String getCvv() { return cvv; }
    public void setCvv(String cvv) { this.cvv = cvv; }
}
