package com.academia.puntomarcial.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.academia.puntomarcial.dto.UsuarioRequest;
import com.academia.puntomarcial.dto.UsuarioResponse;
import com.academia.puntomarcial.service.UsuarioService;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping
    public ResponseEntity<List<UsuarioResponse>> listarUsuarios() {
        return ResponseEntity.ok(usuarioService.listarUsuarios());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarUsuario(@PathVariable Integer id) {
        try {
            return ResponseEntity.ok(usuarioService.buscarPorId(id));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<?> registrarUsuario(@RequestBody UsuarioRequest datos) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(usuarioService.registrarUsuario(datos));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarUsuario(@PathVariable Integer id, @RequestBody UsuarioRequest datos) {
        try {
            return ResponseEntity.ok(usuarioService.actualizarUsuario(id, datos));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Cuerpo: { "estado": "ACTIVO" | "INACTIVO" }
    @PutMapping("/{id}/estado")
    public ResponseEntity<?> cambiarEstado(@PathVariable Integer id, @RequestBody Map<String, String> cuerpo) {
        try {
            return ResponseEntity.ok(usuarioService.cambiarEstado(id, cuerpo.get("estado")));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Mi perfil. Cuerpo: { "correo": "...", "telefono": "..." }
    @PutMapping("/{id}/perfil")
    public ResponseEntity<?> actualizarPerfil(@PathVariable Integer id, @RequestBody Map<String, String> cuerpo) {
        try {
            return ResponseEntity.ok(
                    usuarioService.actualizarPerfil(id, cuerpo.get("correo"), cuerpo.get("telefono")));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
