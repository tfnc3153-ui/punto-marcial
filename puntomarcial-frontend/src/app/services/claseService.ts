import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export type DisciplinaClase = 'MMA' | 'MUAY_THAI' | 'BOX';

export interface Instructor {
  idUsuario: number;
  nombre?: string;
  apellido?: string;
  disciplina?: DisciplinaClase;
  estado?: string;
}

export interface Clase {
  idClase?: number;
  disciplina: DisciplinaClase | '';
  instructor: Instructor | null;
  horarioInicio: string;
  horarioFin: string;
  aforoMaximo: number;
  aforoDisponible: number;
  sala: string;
  totalReservas?: number;
}

// Mismas listas que ReglasReserva en el backend
export const DISCIPLINAS_CLASE: { valor: DisciplinaClase; etiqueta: string }[] = [
  { valor: 'MMA', etiqueta: 'MMA' },
  { valor: 'MUAY_THAI', etiqueta: 'Muay Thai' },
  { valor: 'BOX', etiqueta: 'Box' }
];

export const SALAS = ['A', 'B', 'C', 'D'];

export function etiquetaDisciplina(valor: string): string {
  return DISCIPLINAS_CLASE.find(d => d.valor === valor)?.etiqueta ?? valor;
}

export function nombreInstructor(instructor: Instructor | null | undefined): string {
  return instructor ? `${instructor.nombre ?? ''} ${instructor.apellido ?? ''}`.trim() : '';
}

@Injectable({
  providedIn: 'root'
})
export class ClaseService {

  private readonly apiUrl = 'http://localhost:8081/clases';
  private readonly instructoresUrl = 'http://localhost:8081/instructores';

  constructor(private http: HttpClient) {}

  listarClases(): Observable<Clase[]> {
    return this.http.get<Clase[]>(this.apiUrl);
  }

  // Clases que el cliente todavía puede reservar (no han empezado)
  listarDisponibles(): Observable<Clase[]> {
    return this.http.get<Clase[]>(`${this.apiUrl}/disponibles`);
  }

  registrarClase(clase: Clase): Observable<Clase> {
    return this.http.post<Clase>(this.apiUrl, clase);
  }

  actualizarClase(id: number, clase: Clase): Observable<Clase> {
    return this.http.put<Clase>(`${this.apiUrl}/${id}`, clase);
  }

  eliminarClase(id: number): Observable<string> {
    return this.http.delete(`${this.apiUrl}/${id}`, { responseType: 'text' });
  }

  listarInstructores(): Observable<Instructor[]> {
    return this.http.get<Instructor[]>(this.instructoresUrl);
  }
}
