import { Component, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';

import { DISCIPLINAS_CLASE } from '../services/claseService';
import {
  ROLES,
  Usuario,
  UsuarioService,
  etiquetaRol,
  reglasUsuario
} from '../services/usuarioService';

type CampoConError = 'numDoc' | 'telefono' | 'password' | 'correo';

@Component({
  selector: 'app-registrar-cliente',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule
  ],
  templateUrl: './registrar-cliente.html',
  styleUrl: './registrar-cliente.scss'
})
export class RegistrarClienteComponent {

  readonly roles = ROLES;
  readonly disciplinas = DISCIPLINAS_CLASE;

  usuario: Usuario = this.nuevoUsuario();

  mensaje = '';
  error = '';
  cargando = false;
  intentoEnvio = false;

  constructor(
    private usuarioService: UsuarioService,
    private router: Router,
    private cdr: ChangeDetectorRef
  ) {}

  // Errores por campo, con las mismas reglas que el backend
  get erroresCampo(): Partial<Record<CampoConError, string>> {
    const errores: Partial<Record<CampoConError, string>> = {};

    if (this.usuario.tipoDoc === 'DNI' && !/^\d{8}$/.test(this.usuario.numDoc.trim())) {
      errores.numDoc = 'El DNI debe tener exactamente 8 dígitos.';
    }
    if (!reglasUsuario.correoValido(this.usuario.correo)) {
      errores.correo = 'Ingresa un correo válido, por ejemplo nombre@correo.com.';
    }
    if (!reglasUsuario.telefonoValido(this.usuario.telefono)) {
      errores.telefono = 'El teléfono debe tener 9 dígitos.';
    }
    if (!reglasUsuario.passwordValida(this.usuario.password ?? '')) {
      errores.password = 'Mínimo 8 caracteres, con letras y números.';
    }
    return errores;
  }

  mostrarError(campo: CampoConError): string | null {
    return this.intentoEnvio ? this.erroresCampo[campo] ?? null : null;
  }

  registrarCliente(): void {

    this.mensaje = '';
    this.error = '';
    this.intentoEnvio = true;

    const u = this.usuario;

    if (
      !u.rol ||
      !u.nombre.trim() ||
      !u.apellido.trim() ||
      !u.tipoDoc ||
      !u.numDoc.trim() ||
      !u.correo.trim() ||
      !u.telefono.trim() ||
      !(u.password ?? '').trim() ||
      (u.rol === 'CLIENTE' && !u.estadoMembresia) ||
      (u.rol === 'INSTRUCTOR' && !u.disciplina) ||
      (u.rol === 'ADMINISTRADOR' && !(u.codigoEmpleado ?? '').trim())
    ) {
      this.error = 'Complete todos los campos obligatorios.';
      return;
    }

    if (Object.keys(this.erroresCampo).length) {
      this.error = 'Revise los campos marcados.';
      return;
    }

    this.cargando = true;

    this.usuarioService.registrarUsuario(this.datosDelRol()).subscribe({

      next: (registrado: Usuario) => {
        this.cargando = false;
        this.mensaje = `${etiquetaRol(registrado.rol)} registrado correctamente: ${registrado.nombre} ${registrado.apellido}.`;
        this.usuario = this.nuevoUsuario();
        this.intentoEnvio = false;
        this.cdr.detectChanges();
      },

      error: (err: any) => {
        this.cargando = false;

        if (err.status === 0) {
          this.error = 'No se pudo conectar con el servidor.';
        } else if (typeof err.error === 'string' && err.error.trim()) {
          this.error = err.error;
        } else {
          this.error = 'Ocurrió un error al registrar el usuario.';
        }

        this.cdr.detectChanges();
      }

    });
  }

  // Solo se envían los campos del rol elegido
  private datosDelRol(): Usuario {
    const u = { ...this.usuario };

    if (u.rol !== 'CLIENTE') {
      u.estadoMembresia = null;
      u.fechaVencimiento = null;
    }
    if (u.rol !== 'INSTRUCTOR') {
      u.disciplina = null;
    }
    if (u.rol !== 'ADMINISTRADOR') {
      u.codigoEmpleado = null;
    }
    return u;
  }

  limpiarFormulario(): void {
    this.usuario = this.nuevoUsuario();
    this.intentoEnvio = false;
    this.mensaje = '';
    this.error = '';
  }

  volver(): void {
    this.router.navigate(['/administrador']);
  }

  private nuevoUsuario(): Usuario {

    return {
      rol: 'CLIENTE',
      nombre: '',
      apellido: '',
      tipoDoc: 'DNI',
      numDoc: '',
      correo: '',
      telefono: '',
      password: '',
      estadoMembresia: 'VIGENTE',
      fechaVencimiento: null,
      disciplina: '',
      codigoEmpleado: ''
    };

  }
}
