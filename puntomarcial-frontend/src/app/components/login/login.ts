import { ChangeDetectorRef, Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth';

@Component({
  selector: 'app-login',
  imports: [FormsModule],
  templateUrl: './login.html',
  styleUrl: './login.scss',
})
export class LoginComponent {

  correo: string = '';
  password: string = '';
  mensaje: string = '';
  error: boolean = false;
  cargando: boolean = false;

  constructor(
    private authService: AuthService,
    private router: Router,
    private cdr: ChangeDetectorRef
  ) {}

  onSubmit(): void {

    this.cargando = true;
    this.mensaje = '';
    this.error = false;

    this.authService.login({
      correo: this.correo,
      password: this.password
    }).subscribe({

      next: (respuesta) => {
        this.cargando = false;
        this.mensaje = 'Inicio de sesión correcto';
        this.error = false;

        this.authService.guardarSesion(respuesta);

        // Cada rol entra a su propia página
        const inicioPorRol = {
          ADMINISTRADOR: '/administrador',
          INSTRUCTOR: '/instructor',
          CLIENTE: '/menu'
        };
        this.router.navigate([inicioPorRol[respuesta.rol]]);
      },
      error: (err) => {

        this.cargando = false;
        this.error = true;

        this.mensaje =
          typeof err.error === 'string' && err.error
            ? err.error
            : err.status === 0
              ? 'No se pudo conectar con el servidor.'
              : 'Ocurrió un error al iniciar sesión';

        // Sin esto el mensaje de error no se mostraba en pantalla
        this.cdr.detectChanges();
      }

    });
  }
}