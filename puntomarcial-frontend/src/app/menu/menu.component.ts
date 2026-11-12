import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { AuthService } from '../services/auth';

@Component({
  selector: 'app-menu',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './menu.component.html',
  styleUrl: './menu.component.scss'
})
export class MenuComponent {

  constructor(
    private router: Router,
    private authService: AuthService
  ) {}

  reservar(): void {
    this.router.navigate(['/reservar']);
  }

  comprar(): void {
    this.router.navigate(['/comprar-productos']);
  }

  misReservas(): void {
    this.router.navigate(['/mis-reservas']);
  }

  misCompras(): void {
    this.router.navigate(['/mis-compras']);
  }

  cerrarSesion(): void {
    this.authService.cerrarSesion();
    this.router.navigate(['/login']);
  }
}