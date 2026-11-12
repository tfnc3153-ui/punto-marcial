import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, catchError, from, map, switchMap, throwError } from 'rxjs';

export interface ItemCarritoRequest {
  idProducto: number;
  cantidad: number;
}

export interface ItemVerificado {
  idProducto: number;
  nombre: string;
  talla: string;
  precioUnitario: number;
  cantidad: number;
  subtotal: number;
  stockDisponible: number;
  stockSuficiente: boolean;
}

export interface DatosPago {
  titular: string;
  numeroTarjeta: string;
  vencimiento: string;
  cvv: string;
}

export interface VentaRequest {
  idCliente: number;
  items: ItemCarritoRequest[];
  pago: DatosPago;
}

export interface DetalleVenta {
  idDetalle: number;
  producto: { idProducto: number; nombre: string; talla: string };
  cantidad: number;
  precioUnitario: number;
  subtotal: number;
}

export interface Venta {
  idVenta: number;
  fecha: string;
  total: number;
  estado: 'PENDIENTE' | 'PAGADA' | 'RECHAZADA';
  ultimos4: string | null;
  detalles: DetalleVenta[];
}

export interface VentaResponse {
  venta: Venta;
  mensaje: string;
  pagada: boolean;
  productosStockMinimo: string[];
  correoEnviado: boolean | null;
  mensajeCorreo: string | null;
}

/** Número de boleta igual al del backend: 12 -> B001-000012 */
export function numeroBoleta(idVenta: number): string {
  return 'B001-' + String(idVenta).padStart(6, '0');
}

export interface VerificacionCarrito {
  items: ItemVerificado[];
  total: number;
  puedeContinuar: boolean;
  productosSinStock: string[];
}

@Injectable({
  providedIn: 'root'
})
export class VentaService {

  private readonly apiUrl = 'http://localhost:8081/ventas';

  constructor(private http: HttpClient) {}

  verificarCarrito(carrito: ItemCarritoRequest[]): Observable<VerificacionCarrito> {
    return this.http.post<VerificacionCarrito>(`${this.apiUrl}/verificar`, carrito);
  }

  registrarVenta(venta: VentaRequest): Observable<VentaResponse> {
    return this.http.post<VentaResponse>(this.apiUrl, venta);
  }

  listarPorCliente(idCliente: number): Observable<Venta[]> {
    return this.http.get<Venta[]>(`${this.apiUrl}/cliente/${idCliente}`);
  }

  // HU-12: descarga la boleta como blob y la guarda en el equipo del cliente
  descargarBoleta(idVenta: number, idCliente: number): Observable<void> {
    return this.http.get(`${this.apiUrl}/${idVenta}/boleta`, {
      params: { idCliente },
      responseType: 'blob'
    }).pipe(
      map(pdf => this.guardarArchivo(pdf, `boleta-${numeroBoleta(idVenta)}.pdf`)),
      // Con responseType 'blob' el mensaje de error también llega como blob: se convierte a texto
      catchError(err =>
        err.error instanceof Blob
          ? from(err.error.text()).pipe(switchMap(texto => throwError(() => new Error(String(texto)))))
          : throwError(() => new Error('No se pudo descargar la boleta.'))
      )
    );
  }

  private guardarArchivo(contenido: Blob, nombre: string): void {
    const url = URL.createObjectURL(contenido);
    const enlace = document.createElement('a');
    enlace.href = url;
    enlace.download = nombre;
    document.body.appendChild(enlace);
    enlace.click();
    enlace.remove();
    setTimeout(() => URL.revokeObjectURL(url), 1000);
  }
}
