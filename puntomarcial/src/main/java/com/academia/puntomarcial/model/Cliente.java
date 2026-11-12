package com.academia.puntomarcial.model;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity 
@Table (name = "cliente")

public class Cliente extends Usuario {

    @Column(nullable = false)
    private String estadoMembresia; //VIGENTE , VENCIDA

    private LocalDate fechaVencimiento;

    public String getEstadoMembresia() {
        return estadoMembresia;
    }

    public void setEstadoMembresia(String estadoMembresia) {
        this.estadoMembresia = estadoMembresia;
    }

    public LocalDate getFechaVencimiento() {
        return fechaVencimiento;
    }

    public void setFechaVencimiento(LocalDate fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }
    
}
