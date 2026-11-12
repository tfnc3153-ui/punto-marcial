import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface ReservaRequest {
  cliente: {
    idUsuario: number;
  };
  clase: {
    idClase: number;
  };
  fechaReserva: string;
  estado?: string;
}

export interface Reserva {
  idReserva: number;
  cliente: any;
  clase: any;
  fechaReserva: string;
  estado: string;
  asistencia?: 'PENDIENTE' | 'ASISTIO' | 'NO_ASISTIO';
}

@Injectable({
  providedIn: 'root'
})
export class ReservaService {

  private readonly apiUrl = 'http://localhost:8081/reservas';

  constructor(private http: HttpClient) {}

  registrarReserva(reserva: ReservaRequest): Observable<Reserva> {
    return this.http.post<Reserva>(this.apiUrl, reserva);
  }

  listarReservas(): Observable<Reserva[]> {
    return this.http.get<Reserva[]>(this.apiUrl);
  }
  listarPorCliente(idCliente: number): Observable<Reserva[]> {
  return this.http.get<Reserva[]>(
    `${this.apiUrl}/cliente/${idCliente}`
  );
}

  cancelarReserva(idReserva: number, idCliente: number): Observable<Reserva> {
    return this.http.put<Reserva>(
      `${this.apiUrl}/${idReserva}/cancelar`,
      null,
      { params: { idCliente } }
    );
  }
}
