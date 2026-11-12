package com.academia.puntomarcial.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PruebaController {

    @GetMapping("/")
    public String inicio() {
        return "PuntoMarcial correctamente";
    }
}