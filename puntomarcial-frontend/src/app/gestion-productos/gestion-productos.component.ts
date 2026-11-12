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
import { PaginacionComponent, paginar } from '../shared/paginacion/paginacion.component';

@Component({
  selector: 'app-gestion-productos',
  standalone: true,
  imports: [CommonModule, FormsModule, PaginacionComponent],
  templateUrl: './gestion-productos.component.html',
  styleUrl: './gestion-productos.component.scss'
})
export class GestionProductosComponent implements OnInit {
  readonly categorias = CATEGORIAS;
  readonly disciplinas = DISCIPLINAS;

  productos: Producto[] = [];
  formulario: Producto = this.nuevoProducto();

  filtroDisciplina: Disciplina | '' = '';
  filtroCategoria: Categoria | '' = '';
  soloStockBajo = false;

  editandoId: number | null = null;
  mensaje = '';
  error = '';
  guardando = false;
  cargando = true;

  constructor(
    private productoService: ProductoService,
    private router: Router,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.cargarProductos();
  }

  readonly porPagina = 8;
  pagina = 1;

  get productosDePagina(): Producto[] {
    return paginar(this.productosFiltrados, this.pagina, this.porPagina);
  }

  get productosFiltrados(): Producto[] {
    return this.productos.filter(producto =>
      (!this.filtroDisciplina || producto.disciplina === this.filtroDisciplina) &&
      (!this.filtroCategoria || producto.categoria === this.filtroCategoria) &&
      (!this.soloStockBajo || producto.stockBajo)
    );
  }

  get cantidadStockBajo(): number {
    return this.productos.filter(producto => producto.stockBajo).length;
  }

  etiquetaCategoria(valor: string): string {
    return this.categorias.find(c => c.valor === valor)?.etiqueta ?? valor;
  }

  etiquetaDisciplina(valor: string): string {
    return this.disciplinas.find(d => d.valor === valor)?.etiqueta ?? valor;
  }

  private nuevoProducto(): Producto {
    return {
      nombre: '',
      categoria: 'EQUIPAMIENTO',
      disciplina: 'GENERAL',
      talla: '',
      precio: 0,
      stock: 0,
      stockMinimo: 0
    };
  }

  cargarProductos(): void {
    this.cargando = true;

    this.productoService.listarProductos().subscribe({
      next: productos => {
        this.productos = productos;
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

  editar(producto: Producto): void {
    this.formulario = { ...producto };
    this.editandoId = producto.idProducto ?? null;
    this.mensaje = '';
    this.error = '';

    window.scrollTo({ top: 0, behavior: 'smooth' });
    document.getElementById('campo-nombre')?.focus();
  }

  cancelar(): void {
    this.formulario = this.nuevoProducto();
    this.editandoId = null;
    this.error = '';
  }

  guardar(): void {
    this.mensaje = '';
    this.error = '';

    const precio = Number(this.formulario.precio);
    const stock = Number(this.formulario.stock);
    const stockMinimo = Number(this.formulario.stockMinimo);

    if (!this.formulario.nombre.trim()) {
      this.error = 'El nombre del producto es obligatorio.';
      return;
    }

    if (!(precio > 0)) {
      this.error = 'El precio debe ser mayor a 0.';
      return;
    }

    if (!Number.isInteger(stock) || stock < 0 || !Number.isInteger(stockMinimo) || stockMinimo < 0) {
      this.error = 'El stock y el stock mínimo deben ser números enteros iguales o mayores a 0.';
      return;
    }

    this.guardando = true;

    const datos: Producto = { ...this.formulario, precio, stock, stockMinimo };

    const peticion =
      this.editandoId === null
        ? this.productoService.registrarProducto(datos)
        : this.productoService.actualizarProducto(this.editandoId, datos);

    peticion.subscribe({
      next: guardado => {
        this.guardando = false;

        this.mensaje =
          (this.editandoId === null ? 'Producto registrado: ' : 'Producto actualizado: ') +
          guardado.nombre +
          (guardado.stockBajo ? '. Atención: el stock está en el mínimo o por debajo.' : '.');

        this.cancelar();
        this.cargarProductos();
        this.cdr.detectChanges();
      },

      error: err => {
        this.guardando = false;

        this.error =
          typeof err.error === 'string'
            ? err.error
            : 'No se pudo guardar el producto.';

        this.cdr.detectChanges();
      }
    });
  }

  volver(): void {
    this.router.navigate(['/administrador']);
  }
}
