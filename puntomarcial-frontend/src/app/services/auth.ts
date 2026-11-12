import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface LoginRequest {
  correo: string;
  password: string;
}

export interface LoginResponse {
  idUsuario: number;
  nombre: string;
  apellido: string;
  correo: string;
  rol: 'ADMINISTRADOR' | 'INSTRUCTOR' | 'CLIENTE';
}

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private readonly apiUrl = 'http://localhost:8081/auth';
  private readonly STORAGE_KEY = 'pm_usuario';

  constructor(private http: HttpClient) {}

  login(datos: LoginRequest): Observable<LoginResponse> {
    return this.http.post<LoginResponse>(`${this.apiUrl}/login`, datos);
  }

  guardarSesion(usuario: LoginResponse): void {
    sessionStorage.setItem(this.STORAGE_KEY, JSON.stringify(usuario));
  }

  obtenerSesion(): LoginResponse | null {
    const data = sessionStorage.getItem(this.STORAGE_KEY);
    return data ? JSON.parse(data) : null;
  }

  cerrarSesion(): void {
    sessionStorage.removeItem(this.STORAGE_KEY);
  }

  estaAutenticado(): boolean {
    return this.obtenerSesion() !== null;
  }
}