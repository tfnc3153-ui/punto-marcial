import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import {
  CATEGORIAS,
  Categoria,
  DISCIPLINAS,
  Disciplina,
  Producto,
  ProductoService
} from '../services/productoService';
import { CarritoService, ItemCarrito } from '../services/carritoService';
import { VentaService, VerificacionCarrito } from '../services/ventaService';
import { PaginacionComponent, paginar } from '../shared/paginacion/paginacion.component';

@Component({
  selector: 'app-comprar-productos',
  standalone: true,
  imports: [CommonModule, FormsModule, PaginacionComponent],
  templateUrl: './comprar-productos.component.html',
  styleUrl: './comprar-productos.component.scss'
})
export class ComprarProductosComponent implements OnInit {
  readonly categorias = CATEGORIAS;
  readonly disciplinas = DISCIPLINAS;

  productos: Producto[] = [];
  cantidades: Record<number, number> = {};

  filtroDisciplina: Disciplina | '' = '';
  filtroCategoria: Categoria | '' = '';

  cargando = true;
  verificando = false;
  error = '';
  aviso = '';
  verificacion: VerificacionCarrito | null = null;

  constructor(
    private productoService: ProductoService,
    private carritoService: CarritoService,
    private ventaService: VentaService,
    private router: Router,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.cargarProductos();
  }

  get carrito(): ItemCarrito[] {
    return this.carritoService.obtenerItems();
  }

  readonly porPagina = 6;
  pagina = 1;

  get productosDePagina(): Producto[] {
    return paginar(this.productosFiltrados, this.pagina, this.porPagina);
  }

  get productosFiltrados(): Producto[] {
    return this.productos.filter(producto =>
      (!this.filtroDisciplina || producto.disciplina === this.filtroDisciplina) &&
      (!this.filtroCategoria || producto.categoria === this.filtroCategoria)
    );
  }

  // Cálculo para mostrar mientras se arma el carrito; el total oficial lo da el backend al verificar
  subtotal(item: ItemCarrito): number {
    return Math.round(item.producto.precio * item.cantidad * 100) / 100;
  }

  get total(): number {
    return Math.round(this.carrito.reduce((suma, item) => suma + this.subtotal(item), 0) * 100) / 100;
  }

  get cantidadArticulos(): number {
    return this.carrito.reduce((suma, item) => suma + item.cantidad, 0);
  }

  etiquetaDisciplina(valor: string): string {
    return this.disciplinas.find(d => d.valor === valor)?.etiqueta ?? valor;
  }

  etiquetaCategoria(valor: string): string {
    return this.categorias.find(c => c.valor === valor)?.etiqueta ?? valor;
  }

  enCarrito(producto: Producto): number {
    return this.carrito.find(i => i.producto.idProducto === producto.idProducto)?.cantidad ?? 0;
  }

  disponibleParaAgregar(producto: Producto): number {
    return Math.max(producto.stock - this.enCarrito(producto), 0);
  }

  cargarProductos(): void {
    this.cargando = true;

    this.productoService.listarProductos().subscribe({
      next: productos => {
        this.productos = productos.filter(p => p.estado === 'ACTIVO');
        this.productos.forEach(p => (this.cantidades[p.idProducto!] = 1));
        this.cargando = false;
        this.cdr.detectChanges();
      },
      error: () => {
        this.error = 'No se pudieron cargar los productos.';
        this.cargando = false;
        this.cdr.detectChanges();
      }
    });
  }

  agregar(producto: Producto): void {
    this.error = '';
    this.aviso = '';
    this.verificacion = null;

    const cantidad = Number(this.cantidades[producto.idProducto!]);
    const disponible = this.disponibleParaAgregar(producto);

    if (!Number.isInteger(cantidad) || cantidad < 1) {
      this.error = 'La cantidad debe ser un número entero de 1 o más.';
      return;
    }

    if (cantidad > disponible) {
      this.error = disponible === 0
        ? `No hay más stock de ${producto.nombre} para agregar.`
        : `Solo puedes agregar ${disponible} unidad(es) más de ${producto.nombre}.`;
      return;
    }

    this.carritoService.agregar(producto, cantidad);
    this.cantidades[producto.idProducto!] = 1;
    this.aviso = `${producto.nombre} (x${cantidad}) se agregó al carrito.`;
  }

  cambiarCantidad(item: ItemCarrito, valor: number): void {
    this.verificacion = null;
    const cantidad = Number(valor);

    if (Number.isInteger(cantidad) && cantidad >= 1) {
      this.carritoService.cambiarCantidad(item.producto.idProducto!, cantidad);
    }
  }

  quitar(item: ItemCarrito): void {
    this.verificacion = null;
    this.carritoService.quitar(item.producto.idProducto!);
    this.aviso = `${item.producto.nombre} se quitó del carrito.`;
  }

  // HU-09: verifica el stock en el servidor; si falta stock no se permite continuar
  continuarCompra(): void {
    this.error = '';
    this.aviso = '';
    this.verificacion = null;

    if (!this.carrito.length) {
      this.error = 'Agrega al menos un producto al carrito.';
      return;
    }

    this.verificando = true;

    const datos = this.carrito.map(item => ({
      idProducto: item.producto.idProducto!,
      cantidad: item.cantidad
    }));

    this.ventaService.verificarCarrito(datos).subscribe({
      next: resultado => {
        this.verificacion = resultado;
        this.verificando = false;

        if (resultado.puedeContinuar) {
          this.router.navigate(['/pago']);
          return;
        } else {
          // El stock cambió desde que se cargó la pantalla: se actualiza el catálogo
          this.cargarProductos();
        }

        this.cdr.detectChanges();
        document.getElementById('resultado-verificacion')?.focus();
      },
      error: err => {
        this.verificando = false;
        this.error = typeof err.error === 'string' ? err.error : 'No se pudo verificar el carrito.';
        this.cdr.detectChanges();
      }
    });
  }

  volver(): void {
    this.router.navigate(['/menu']);
  }
}
