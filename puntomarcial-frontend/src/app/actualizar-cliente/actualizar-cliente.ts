import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';

import { DISCIPLINAS_CLASE } from '../services/claseService';
import {
  ROLES,
  Rol,
  Usuario,
  UsuarioService,
  etiquetaRol,
  reglasUsuario
} from '../services/usuarioService';

@Component({
  selector: 'app-actualizar-cliente',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule
  ],
  templateUrl: './actualizar-cliente.html',
  styleUrl: './actualizar-cliente.scss'
})
export class ActualizarCliente implements OnInit {

  readonly roles = ROLES;
  readonly disciplinas = DISCIPLINAS_CLASE;
  readonly etiquetaRol = etiquetaRol;

  usuarios: Usuario[] = [];
  usuarioSeleccionado: Usuario | null = null;

  busqueda = '';
  filtroRol: Rol | '' = '';
  filtroEstado: '' | 'ACTIVO' | 'INACTIVO' = '';

  cargando = false;
  guardando = false;
  cambiandoEstado = false;

  mensaje = '';
  error = '';

  constructor(
    private usuarioService: UsuarioService,
    private router: Router,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.cargarUsuarios();
  }

  get usuariosFiltrados(): Usuario[] {
    const texto = this.busqueda.trim().toLowerCase();

    return this.usuarios.filter(usuario => {
      const coincideTexto = !texto
        || `${usuario.nombre} ${usuario.apellido}`.toLowerCase().includes(texto)
        || (usuario.numDoc || '').toLowerCase().includes(texto)
        || (usuario.correo || '').toLowerCase().includes(texto);

      return coincideTexto
        && (!this.filtroRol || usuario.rol === this.filtroRol)
        && (!this.filtroEstado || usuario.estado === this.filtroEstado);
    });
  }

  cargarUsuarios(idSeleccionar?: number): void {

    this.cargando = true;

    this.usuarioService.listarUsuarios().subscribe({

      next: usuarios => {
        this.usuarios = usuarios;
        this.cargando = false;

        if (idSeleccionar) {
          const actualizado = usuarios.find(u => u.idUsuario === idSeleccionar);
          this.usuarioSeleccionado = actualizado ? { ...actualizado } : null;
        }

        this.cdr.detectChanges();
      },

      error: () => {
        this.error = 'No se pudieron cargar los usuarios.';
        this.cargando = false;
        this.cdr.detectChanges();
      }

    });
  }

  seleccionarUsuario(usuario: Usuario): void {
    // Copia para no modificar directamente el listado
    this.usuarioSeleccionado = { ...usuario };
    this.mensaje = '';
    this.error = '';

    setTimeout(() => document.getElementById('editar-nombre')?.focus());
  }

  actualizarUsuario(): void {

    const u = this.usuarioSeleccionado;

    if (!u || !u.idUsuario) {
      this.error = 'Seleccione un usuario.';
      return;
    }

    if (!u.nombre?.trim() || !u.apellido?.trim() || !u.correo?.trim() || !u.telefono?.trim()) {
      this.error = 'Complete los campos obligatorios.';
      return;
    }

    if (!reglasUsuario.correoValido(u.correo)) {
      this.error = 'El correo no es válido.';
      return;
    }

    if (!reglasUsuario.telefonoValido(u.telefono)) {
      this.error = 'El teléfono debe tener 9 dígitos.';
      return;
    }

    this.guardando = true;
    this.mensaje = '';
    this.error = '';

    this.usuarioService.actualizarUsuario(u.idUsuario, u).subscribe({

      next: actualizado => {
        this.guardando = false;
        this.mensaje = 'Datos actualizados correctamente.';
        this.cargarUsuarios(actualizado.idUsuario);
      },

      error: (err: any) => {
        this.guardando = false;
        this.error = this.mensajeError(err, 'No se pudieron actualizar los datos.');
        this.cdr.detectChanges();
      }

    });
  }

  // Un usuario inactivo no puede iniciar sesión
  cambiarEstado(): void {

    const u = this.usuarioSeleccionado;

    if (!u || !u.idUsuario) {
      return;
    }

    const nuevoEstado = u.estado === 'INACTIVO' ? 'ACTIVO' : 'INACTIVO';
    const accion = nuevoEstado === 'INACTIVO' ? 'desactivar' : 'activar';

    if (!confirm(`¿Seguro que deseas ${accion} a ${u.nombre} ${u.apellido}?`)) {
      return;
    }

    this.cambiandoEstado = true;
    this.mensaje = '';
    this.error = '';

    this.usuarioService.cambiarEstado(u.idUsuario, nuevoEstado).subscribe({

      next: actualizado => {
        this.cambiandoEstado = false;
        this.mensaje = nuevoEstado === 'INACTIVO'
          ? 'Usuario desactivado: ya no podrá iniciar sesión.'
          : 'Usuario activado: ya puede iniciar sesión.';
        this.cargarUsuarios(actualizado.idUsuario);
      },

      error: (err: any) => {
        this.cambiandoEstado = false;
        this.error = this.mensajeError(err, 'No se pudo cambiar el estado.');
        this.cdr.detectChanges();
      }

    });
  }

  private mensajeError(err: any, porDefecto: string): string {
    if (err.status === 0) {
      return 'No se pudo conectar con el servidor.';
    }
    return typeof err.error === 'string' && err.error.trim() ? err.error : porDefecto;
  }

  volver(): void {
    this.router.navigate(['/administrador']);
  }
}
