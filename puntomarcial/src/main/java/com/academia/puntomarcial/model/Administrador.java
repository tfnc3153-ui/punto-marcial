package com.academia.puntomarcial.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity 
@Table (name = "administrador" )

public class Administrador extends Usuario {

    @Column(nullable = false, unique = true)
    private String codigoEmpleado;

    public String getCodigoEmpleado() {
        return codigoEmpleado;
    }

    public void setCodigoEmpleado(String codigoEmpleado) {
        this.codigoEmpleado = codigoEmpleado;
    }
   
}
