import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { AuthService } from './auth';

export function roleGuard(rolRequerido: 'ADMINISTRADOR' | 'INSTRUCTOR' | 'CLIENTE'): CanActivateFn {
  return () => {
    const authService = inject(AuthService);
    const router = inject(Router);
    const usuario = authService.obtenerSesion();

    if (!usuario || usuario.rol !== rolRequerido) {
      router.navigate(['/login']);
      return false;
    }

    return true;
  };
}