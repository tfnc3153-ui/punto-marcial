package com.academia.puntomarcial.model;

import java.math.BigDecimal;

import com.academia.puntomarcial.service.ReglasVenta;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * Producto de la tienda de la academia (HU-08).
 * La tabla "producto" la crea Hibernate al arrancar (ddl-auto=update).
 */
@Entity
@Table(name = "producto")
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idProducto;

    @Column(nullable = false, length = 100)
    private String nombre;

    // ROPA, PROTECCION, EQUIPAMIENTO, ACCESORIOS, SUPLEMENTOS
    @Column(nullable = false, length = 30)
    private String categoria;

    // MMA, MUAY_THAI, BOX, GENERAL (sirve para todas)
    @Column(nullable = false, length = 30)
    private String disciplina;

    // Talla, onzas o medida (S, M, L, 12 oz, 4 m...). "Única" si no aplica
    @Column(nullable = false, length = 20)
    private String talla;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal precio;

    @Column(nullable = false)
    private Integer stock;

    @Column(nullable = false)
    private Integer stockMinimo;

    // ACTIVO, INACTIVO
    @Column(nullable = false, length = 20)
    private String estado;

    public Producto() {
    }

    public Integer getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(Integer idProducto) {
        this.idProducto = idProducto;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getDisciplina() {
        return disciplina;
    }

    public void setDisciplina(String disciplina) {
        this.disciplina = disciplina;
    }

    public String getTalla() {
        return talla;
    }

    public void setTalla(String talla) {
        this.talla = talla;
    }

    public BigDecimal getPrecio() {
        return precio;
    }

    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public Integer getStockMinimo() {
        return stockMinimo;
    }

    public void setStockMinimo(Integer stockMinimo) {
        this.stockMinimo = stockMinimo;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    // Se envía en el JSON como "stockBajo" para que el frontend resalte el producto
    public boolean isStockBajo() {
        return stock != null && stockMinimo != null
                && ReglasVenta.stockBajo(stock, stockMinimo);
    }
}
