package com.academia.puntomarcial.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.academia.puntomarcial.dto.LoginResponse;
import com.academia.puntomarcial.dto.UsuarioResponse;
import com.academia.puntomarcial.model.Usuario;
import com.academia.puntomarcial.repository.UsuarioRepository;

@Service
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public LoginResponse login(String correo, String passwordPlano) {
        Usuario usuario = usuarioRepository.findByCorreo(correo)
                .orElseThrow(() -> new IllegalArgumentException("Credenciales incorrectas"));

        if (!"ACTIVO".equals(usuario.getEstado())) {
            throw new IllegalArgumentException("La cuenta está inactiva");
        }

        if (!passwordEncoder.matches(passwordPlano, usuario.getPassword())) {
            throw new IllegalArgumentException("Credenciales incorrectas");
        }

        String rol = UsuarioResponse.rolDe(usuario);

        return new LoginResponse(
                usuario.getIdUsuario(),
                usuario.getNombre(),
                usuario.getApellido(),
                usuario.getCorreo(),
                rol
        );
    }
}