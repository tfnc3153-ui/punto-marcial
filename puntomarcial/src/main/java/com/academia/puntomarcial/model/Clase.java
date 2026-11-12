package com.academia.puntomarcial.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

@Entity
@Table(name = "clase")
public class Clase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idClase;

    // MMA, MUAY_THAI, BOX
    @Column(nullable = false)
    private String disciplina;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_instructor", nullable = false)
    private Instructor instructor;

    @Column(nullable = false)
    private LocalDateTime horarioInicio;

    @Column(nullable = false)
    private LocalDateTime horarioFin;

    @Column(nullable = false)
    private Integer aforoMaximo;

    @Column(nullable = false)
    private Integer aforoDisponible;

    // A, B, C, D
    @Column(nullable = false)
    private String sala;

    // Reservas registradas (activas o canceladas). No se guarda: lo calcula ClaseService.
    // Si es mayor a 0, la clase no se puede eliminar
    @Transient
    private Integer totalReservas;

    public Clase() {
    }

    public Integer getIdClase() {
        return idClase;
    }

    public void setIdClase(Integer idClase) {
        this.idClase = idClase;
    }

    public String getDisciplina() {
        return disciplina;
    }

    public void setDisciplina(String disciplina) {
        this.disciplina = disciplina;
    }

    public Instructor getInstructor() {
        return instructor;
    }

    public void setInstructor(Instructor instructor) {
        this.instructor = instructor;
    }

    public Integer getTotalReservas() {
        return totalReservas;
    }

    public void setTotalReservas(Integer totalReservas) {
        this.totalReservas = totalReservas;
    }

    public LocalDateTime getHorarioInicio() {
        return horarioInicio;
    }

    public void setHorarioInicio(LocalDateTime horarioInicio) {
        this.horarioInicio = horarioInicio;
    }

    public LocalDateTime getHorarioFin() {
        return horarioFin;
    }

    public void setHorarioFin(LocalDateTime horarioFin) {
        this.horarioFin = horarioFin;
    }

    public Integer getAforoMaximo() {
        return aforoMaximo;
    }

    public void setAforoMaximo(Integer aforoMaximo) {
        this.aforoMaximo = aforoMaximo;
    }

    public Integer getAforoDisponible() {
        return aforoDisponible;
    }

    public void setAforoDisponible(Integer aforoDisponible) {
        this.aforoDisponible = aforoDisponible;
    }

    public String getSala() {
        return sala;
    }

    public void setSala(String sala) {
        this.sala = sala;
    }

    public boolean validarCupo() {
        return aforoDisponible != null && aforoDisponible > 0;
    }


    /**
     * Reduce en uno el número de cupos disponibles.
     *
     * @return 
     */
    public boolean decrementarAforo() {

        if (!validarCupo()) {
            return false;
        }

        aforoDisponible--;

        return true;
    }
    public void incrementarAforo() {

        if (aforoDisponible < aforoMaximo) {
            aforoDisponible++;
        }
    }
}