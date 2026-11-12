import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Clase } from './claseService';

export interface Alumno {
  idReserva: number;
  nombre: string;
  apellido: string;
  telefono: string;
  asistencia: 'PENDIENTE' | 'ASISTIO' | 'NO_ASISTIO';
}

export interface ClaseInstructor {
  clase: Clase;
  inscritos: number;
  asistentes: number;
  pendientes: number;
  puedeTomarAsistencia: boolean;
  alumnos: Alumno[] | null;
}

@Injectable({
  providedIn: 'root'
})
export class InstructorService {

  private readonly apiUrl = 'http://localhost:8081/instructores';

  constructor(private http: HttpClient) {}

  // Próximas (incluye la que está en curso) o pasadas
  listarClases(idInstructor: number, pasadas: boolean): Observable<ClaseInstructor[]> {
    return this.http.get<ClaseInstructor[]>(`${this.apiUrl}/${idInstructor}/clases`, {
      params: { pasadas }
    });
  }

  detalleClase(idInstructor: number, idClase: number): Observable<ClaseInstructor> {
    return this.http.get<ClaseInstructor>(`${this.apiUrl}/${idInstructor}/clases/${idClase}`);
  }

  marcarAsistencia(
    idInstructor: number,
    idReserva: number,
    asistencia: 'ASISTIO' | 'NO_ASISTIO'
  ): Observable<ClaseInstructor> {
    return this.http.put<ClaseInstructor>(
      `${this.apiUrl}/${idInstructor}/reservas/${idReserva}/asistencia`,
      { asistencia }
    );
  }
}

export function enCurso(clase: Clase): boolean {
  const ahora = Date.now();
  return new Date(clase.horarioInicio).getTime() <= ahora && new Date(clase.horarioFin).getTime() > ahora;
}
