import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export type Categoria = 'ROPA' | 'PROTECCION' | 'EQUIPAMIENTO' | 'ACCESORIOS' | 'SUPLEMENTOS';
export type Disciplina = 'MMA' | 'MUAY_THAI' | 'BOX' | 'GENERAL';

export interface Producto {
  idProducto?: number;
  nombre: string;
  categoria: Categoria;
  disciplina: Disciplina;
  talla: string;
  precio: number;
  stock: number;
  stockMinimo: number;
  estado?: string;
  stockBajo?: boolean;
}

export const CATEGORIAS: { valor: Categoria; etiqueta: string }[] = [
  { valor: 'ROPA', etiqueta: 'Ropa' },
  { valor: 'PROTECCION', etiqueta: 'Protección' },
  { valor: 'EQUIPAMIENTO', etiqueta: 'Equipamiento' },
  { valor: 'ACCESORIOS', etiqueta: 'Accesorios' },
  { valor: 'SUPLEMENTOS', etiqueta: 'Suplementos' }
];

export const DISCIPLINAS: { valor: Disciplina; etiqueta: string }[] = [
  { valor: 'MMA', etiqueta: 'MMA' },
  { valor: 'MUAY_THAI', etiqueta: 'Muay Thai' },
  { valor: 'BOX', etiqueta: 'Box' },
  { valor: 'GENERAL', etiqueta: 'General (todas)' }
];

@Injectable({
  providedIn: 'root'
})
export class ProductoService {

  private readonly apiUrl = 'http://localhost:8081/productos';

  constructor(private http: HttpClient) {}

  listarProductos(): Observable<Producto[]> {
    return this.http.get<Producto[]>(this.apiUrl);
  }

  registrarProducto(producto: Producto): Observable<Producto> {
    return this.http.post<Producto>(this.apiUrl, producto);
  }

  actualizarProducto(id: number, producto: Producto): Observable<Producto> {
    return this.http.put<Producto>(`${this.apiUrl}/${id}`, producto);
  }
}
