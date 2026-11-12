import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../services/auth';
import { Usuario, UsuarioService, reglasUsuario } from '../services/usuarioService';
import { etiquetaDisciplina } from '../services/claseService';

@Component({
  selector: 'app-instructor-perfil',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './instructor-perfil.component.html',
  styleUrl: './instructor.scss'
})
export class InstructorPerfilComponent implements OnInit {
  readonly etiquetaDisciplina = etiquetaDisciplina;

  usuario: Usuario | null = null;
  correo = '';
  telefono = '';
  intentoEnvio = false;
  guardando = false;
  mensaje = '';
  error = '';

  constructor(
    private authService: AuthService,
    private usuarioService: UsuarioService,
    private router: Router,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    const sesion = this.authService.obtenerSesion();

    if (!sesion) {
      this.router.navigate(['/login']);
      return;
    }

    this.usuarioService.buscarUsuario(sesion.idUsuario).subscribe({
      next: usuario => {
        this.usuario = usuario;
        this.correo = usuario.correo;
        this.telefono = usuario.telefono;
        this.cdr.detectChanges();
      },
      error: () => {
        this.error = 'No se pudieron cargar tus datos.';
        this.cdr.detectChanges();
      }
    });
  }

  get errorCorreo(): string | null {
    return this.intentoEnvio && !reglasUsuario.correoValido(this.correo) ? 'Ingresa un correo válido.' : null;
  }

  get errorTelefono(): string | null {
    return this.intentoEnvio && !reglasUsuario.telefonoValido(this.telefono) ? 'El teléfono debe tener 9 dígitos.' : null;
  }

  guardar(): void {
    this.intentoEnvio = true;
    this.mensaje = '';
    this.error = '';

    if (!this.usuario?.idUsuario || this.errorCorreo || this.errorTelefono) {
      this.error = 'Revisa los campos marcados.';
      return;
    }

    this.guardando = true;

    this.usuarioService.actualizarPerfil(this.usuario.idUsuario, this.correo.trim(), this.telefono.trim()).subscribe({
      next: actualizado => {
        this.guardando = false;
        this.usuario = actualizado;
        this.intentoEnvio = false;
        this.mensaje = 'Tus datos se actualizaron correctamente.';

        // El correo de la sesión también se actualiza
        const sesion = this.authService.obtenerSesion();
        if (sesion) {
          this.authService.guardarSesion({ ...sesion, correo: actualizado.correo });
        }
        this.cdr.detectChanges();
      },
      error: err => {
        this.guardando = false;
        this.error = typeof err.error === 'string' ? err.error : 'No se pudieron guardar los cambios.';
        this.cdr.detectChanges();
      }
    });
  }

  volver(): void {
    this.router.navigate(['/instructor']);
  }
}
