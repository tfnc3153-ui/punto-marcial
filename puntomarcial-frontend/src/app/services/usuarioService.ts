import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export type Rol = 'ADMINISTRADOR' | 'INSTRUCTOR' | 'CLIENTE';

export interface Usuario {
  idUsuario?: number;
  rol: Rol;
  nombre: string;
  apellido: string;
  tipoDoc: string;
  numDoc: string;
  correo: string;
  telefono: string;
  password?: string;
  estado?: 'ACTIVO' | 'INACTIVO';
  // Cliente
  estadoMembresia?: string | null;
  fechaVencimiento?: string | null;
  // Instructor
  disciplina?: string | null;
  // Administrador
  codigoEmpleado?: string | null;
}

export const ROLES: { valor: Rol; etiqueta: string }[] = [
  { valor: 'CLIENTE', etiqueta: 'Cliente' },
  { valor: 'INSTRUCTOR', etiqueta: 'Instructor' },
  { valor: 'ADMINISTRADOR', etiqueta: 'Administrador' }
];

export function etiquetaRol(rol: string): string {
  return ROLES.find(r => r.valor === rol)?.etiqueta ?? rol;
}

// Mismas reglas que ReglasUsuario en el backend
export const reglasUsuario = {
  telefonoValido: (telefono: string) => /^\d{9}$/.test((telefono || '').trim()),
  correoValido: (correo: string) => /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test((correo || '').trim()),
  passwordValida: (password: string) =>
    (password || '').length >= 8 && /[A-Za-z]/.test(password) && /\d/.test(password)
};

@Injectable({
  providedIn: 'root'
})
export class UsuarioService {

  private readonly apiUrl = 'http://localhost:8081/usuarios';

  constructor(private http: HttpClient) {}

  listarUsuarios(): Observable<Usuario[]> {
    return this.http.get<Usuario[]>(this.apiUrl);
  }

  buscarUsuario(id: number): Observable<Usuario> {
    return this.http.get<Usuario>(`${this.apiUrl}/${id}`);
  }

  registrarUsuario(usuario: Usuario): Observable<Usuario> {
    return this.http.post<Usuario>(this.apiUrl, usuario);
  }

  actualizarUsuario(id: number, usuario: Usuario): Observable<Usuario> {
    return this.http.put<Usuario>(`${this.apiUrl}/${id}`, usuario);
  }

  cambiarEstado(id: number, estado: 'ACTIVO' | 'INACTIVO'): Observable<Usuario> {
    return this.http.put<Usuario>(`${this.apiUrl}/${id}/estado`, { estado });
  }

  actualizarPerfil(id: number, correo: string, telefono: string): Observable<Usuario> {
    return this.http.put<Usuario>(`${this.apiUrl}/${id}/perfil`, { correo, telefono });
  }
}
