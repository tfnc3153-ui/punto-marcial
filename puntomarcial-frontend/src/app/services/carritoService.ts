import { Injectable } from '@angular/core';
import { Producto } from './productoService';

export interface ItemCarrito {
  producto: Producto;
  cantidad: number;
}

const CLAVE_CARRITO = 'pm_carrito';

/**
 * Carrito del cliente. Se guarda en sessionStorage para no perderlo al ir a la pantalla de pago.
 */
@Injectable({
  providedIn: 'root'
})
export class CarritoService {

  private items: ItemCarrito[] = this.leer();

  obtenerItems(): ItemCarrito[] {
    return this.items;
  }

  agregar(producto: Producto, cantidad: number): void {
    const existente = this.items.find(i => i.producto.idProducto === producto.idProducto);

    if (existente) {
      existente.cantidad += cantidad;
      existente.producto = producto;
    } else {
      this.items.push({ producto, cantidad });
    }

    this.guardar();
  }

  cambiarCantidad(idProducto: number, cantidad: number): void {
    const item = this.items.find(i => i.producto.idProducto === idProducto);

    if (item) {
      item.cantidad = cantidad;
      this.guardar();
    }
  }

  quitar(idProducto: number): void {
    this.items = this.items.filter(i => i.producto.idProducto !== idProducto);
    this.guardar();
  }

  vaciar(): void {
    this.items = [];
    this.guardar();
  }

  private leer(): ItemCarrito[] {
    try {
      const datos = sessionStorage.getItem(CLAVE_CARRITO);
      return datos ? JSON.parse(datos) : [];
    } catch {
      return [];
    }
  }

  private guardar(): void {
    try {
      sessionStorage.setItem(CLAVE_CARRITO, JSON.stringify(this.items));
    } catch {
      // Sin sessionStorage el carrito solo dura mientras la pantalla está abierta.
    }
  }
}
