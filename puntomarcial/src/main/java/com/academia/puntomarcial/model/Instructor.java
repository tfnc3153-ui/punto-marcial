package com.academia.puntomarcial.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

/**
 * Instructor de la academia. Es un usuario (inicia sesión con correo y contraseña)
 * y dicta una sola disciplina: MMA, MUAY_THAI o BOX.
 */
@Entity
@Table(name = "instructor")
public class Instructor extends Usuario {

    @Column(nullable = false, length = 30)
    private String disciplina;

    public String getDisciplina() {
        return disciplina;
    }

    public void setDisciplina(String disciplina) {
        this.disciplina = disciplina;
    }
}
