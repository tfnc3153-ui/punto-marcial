import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../services/auth';

@Component({
  selector: 'app-administrador',
  standalone: true,
  imports: [],
  templateUrl: './administrador.html',
  styleUrl: './administrador.scss'
})
export class Administrador {

  constructor(
    private router: Router,
    private authService: AuthService
  ) {}

  registrarUsuario(): void {
    this.router.navigate(['/registrar-usuario']);
  }
  actualizarUsuario(): void {
  this.router.navigate(['/actualizar-usuario']);
  }
  gestionarClases(): void {
  this.router.navigate(['/gestion-clases']);
}
gestionarProductos(): void {
  this.router.navigate(['/gestion-productos']);
}
supervisarAforo(): void {
  this.router.navigate(['/supervisar-aforo']);
}

  cerrarSesion(): void {
    this.authService.cerrarSesion();
    this.router.navigate(['/login']);
  }
}