import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../services/auth';
import { Venta, VentaService, numeroBoleta } from '../services/ventaService';

@Component({
  selector: 'app-mis-compras',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './mis-compras.component.html',
  styleUrl: './mis-compras.component.scss'
})
export class MisComprasComponent implements OnInit {
  ventas: Venta[] = [];
  cargando = true;
  error = '';
  descargandoId: number | null = null;
  errorDescarga: { idVenta: number; mensaje: string } | null = null;
  readonly numeroBoleta = numeroBoleta;
  readonly comprasPorPagina = 3;

  // Filtros
  filtroEstado: '' | 'PAGADA' | 'RECHAZADA' = '';
  filtroDesde = '';
  filtroHasta = '';
  filtroProducto = '';

  paginaActual = 1;

  constructor(
    private ventaService: VentaService,
    private authService: AuthService,
    private router: Router,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    const usuario = this.authService.obtenerSesion();

    if (!usuario) {
      this.router.navigate(['/login']);
      return;
    }

    this.ventaService.listarPorCliente(usuario.idUsuario).subscribe({
      next: ventas => {
        this.ventas = ventas;
        this.cargando = false;
        this.cdr.detectChanges();
      },
      error: err => {
        this.error = typeof err.error === 'string' ? err.error : 'No se pudieron cargar tus compras.';
        this.cargando = false;
        this.cdr.detectChanges();
      }
    });
  }

  get comprasPagadas(): number {
    return this.ventas.filter(v => v.estado === 'PAGADA').length;
  }

  get ventasFiltradas(): Venta[] {
    const texto = this.filtroProducto.trim().toLowerCase();

    return this.ventas.filter(venta => {
      const fecha = venta.fecha.slice(0, 10); // AAAA-MM-DD, se compara como texto

      return (!this.filtroEstado || venta.estado === this.filtroEstado)
        && (!this.filtroDesde || fecha >= this.filtroDesde)
        && (!this.filtroHasta || fecha <= this.filtroHasta)
        && (!texto || venta.detalles.some(d => d.producto.nombre.toLowerCase().includes(texto)));
    });
  }

  get totalPaginas(): number {
    return Math.max(1, Math.ceil(this.ventasFiltradas.length / this.comprasPorPagina));
  }

  get paginas(): number[] {
    return Array.from({ length: this.totalPaginas }, (_, i) => i + 1);
  }

  get ventasDeLaPagina(): Venta[] {
    const inicio = (this.paginaActual - 1) * this.comprasPorPagina;
    return this.ventasFiltradas.slice(inicio, inicio + this.comprasPorPagina);
  }

  get rangoMostrado(): string {
    const total = this.ventasFiltradas.length;
    if (!total) {
      return '0 compras';
    }
    const desde = (this.paginaActual - 1) * this.comprasPorPagina + 1;
    const hasta = Math.min(desde + this.comprasPorPagina - 1, total);
    return `${desde}–${hasta} de ${total} compra(s)`;
  }

  get hayFiltros(): boolean {
    return !!(this.filtroEstado || this.filtroDesde || this.filtroHasta || this.filtroProducto.trim());
  }

  // Al cambiar un filtro se vuelve a la primera página
  aplicarFiltros(): void {
    this.paginaActual = 1;
  }

  limpiarFiltros(): void {
    this.filtroEstado = '';
    this.filtroDesde = '';
    this.filtroHasta = '';
    this.filtroProducto = '';
    this.paginaActual = 1;
  }

  irAPagina(pagina: number): void {
    if (pagina < 1 || pagina > this.totalPaginas || pagina === this.paginaActual) {
      return;
    }
    this.paginaActual = pagina;
    // El foco va al inicio de la lista para que el lector de pantalla lea la nueva página
    setTimeout(() => document.getElementById('titulo-lista-compras')?.focus());
  }

  descargarBoleta(venta: Venta): void {
    const usuario = this.authService.obtenerSesion();

    if (!usuario) {
      this.router.navigate(['/login']);
      return;
    }

    this.descargandoId = venta.idVenta;
    this.errorDescarga = null;

    this.ventaService.descargarBoleta(venta.idVenta, usuario.idUsuario).subscribe({
      next: () => {
        this.descargandoId = null;
        this.cdr.detectChanges();
      },
      error: (err: Error) => {
        this.descargandoId = null;
        this.errorDescarga = { idVenta: venta.idVenta, mensaje: err.message };
        this.cdr.detectChanges();
      }
    });
  }

  irATienda(): void {
    this.router.navigate(['/comprar-productos']);
  }

  volver(): void {
    this.router.navigate(['/menu']);
  }
}
