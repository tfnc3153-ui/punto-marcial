import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../services/auth';
import { CarritoService } from '../services/carritoService';
import {
  DatosPago,
  VentaResponse,
  VentaService,
  VerificacionCarrito,
  numeroBoleta
} from '../services/ventaService';

type CampoPago = 'titular' | 'numeroTarjeta' | 'vencimiento' | 'cvv';

@Component({
  selector: 'app-pago',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './pago.component.html',
  styleUrl: './pago.component.scss'
})
export class PagoComponent implements OnInit {
  resumen: VerificacionCarrito | null = null;
  pago: DatosPago = { titular: '', numeroTarjeta: '', vencimiento: '', cvv: '' };
  erroresCampo: Partial<Record<CampoPago, string>> = {};

  cargando = true;
  procesando = false;
  error = '';
  resultado: VentaResponse | null = null;
  descargando = false;
  errorDescarga = '';

  constructor(
    private carritoService: CarritoService,
    private ventaService: VentaService,
    private authService: AuthService,
    private router: Router,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.cargarResumen();
  }

  private get itemsCarrito() {
    return this.carritoService.obtenerItems().map(item => ({
      idProducto: item.producto.idProducto!,
      cantidad: item.cantidad
    }));
  }

  // El resumen se pide al servidor para mostrar el total real antes de pagar
  cargarResumen(): void {
    const items = this.itemsCarrito;

    if (!items.length) {
      this.cargando = false;
      return;
    }

    this.ventaService.verificarCarrito(items).subscribe({
      next: resumen => {
        this.resumen = resumen;
        this.cargando = false;
        this.cdr.detectChanges();
      },
      error: err => {
        this.error = typeof err.error === 'string' ? err.error : 'No se pudo cargar el resumen de la compra.';
        this.cargando = false;
        this.cdr.detectChanges();
      }
    });
  }

  // Agrupa los dígitos de 4 en 4 mientras se escribe: 4111 1111 1111 1111
  formatearTarjeta(valor: string): void {
    const digitos = (valor || '').replace(/\D/g, '').slice(0, 16);
    this.pago.numeroTarjeta = digitos.replace(/(\d{4})(?=\d)/g, '$1 ');
  }

  // Agrega la barra del vencimiento: 1229 -> 12/29
  formatearVencimiento(valor: string): void {
    const digitos = (valor || '').replace(/\D/g, '').slice(0, 4);
    this.pago.vencimiento = digitos.length > 2 ? digitos.slice(0, 2) + '/' + digitos.slice(2) : digitos;
  }

  // Mismas reglas que ReglasVenta.pagoValido en el backend
  private validar(): boolean {
    const errores: Partial<Record<CampoPago, string>> = {};
    const numero = this.pago.numeroTarjeta.replace(/\s/g, '');

    if (!this.pago.titular.trim()) {
      errores.titular = 'Ingresa el nombre que aparece en la tarjeta.';
    }

    if (!/^\d{16}$/.test(numero)) {
      errores.numeroTarjeta = 'El número de tarjeta debe tener 16 dígitos.';
    }

    const coincidencia = /^(0[1-9]|1[0-2])\/(\d{2})$/.exec(this.pago.vencimiento);
    if (!coincidencia) {
      errores.vencimiento = 'Usa el formato MM/AA, por ejemplo 08/28.';
    } else {
      const hoy = new Date();
      const mes = Number(coincidencia[1]);
      const anio = 2000 + Number(coincidencia[2]);
      if (anio < hoy.getFullYear() || (anio === hoy.getFullYear() && mes < hoy.getMonth() + 1)) {
        errores.vencimiento = 'La tarjeta está vencida.';
      }
    }

    if (!/^\d{3}$/.test(this.pago.cvv)) {
      errores.cvv = 'El CVV debe tener 3 dígitos.';
    }

    this.erroresCampo = errores;
    return Object.keys(errores).length === 0;
  }

  pagar(): void {
    this.error = '';

    if (!this.validar()) {
      this.error = 'Revisa los datos de la tarjeta marcados abajo.';
      const primero = (['titular', 'numeroTarjeta', 'vencimiento', 'cvv'] as CampoPago[])
        .find(campo => this.erroresCampo[campo]);
      setTimeout(() => document.getElementById('pago-' + primero)?.focus());
      return;
    }

    const usuario = this.authService.obtenerSesion();

    if (!usuario) {
      this.router.navigate(['/login']);
      return;
    }

    this.procesando = true;

    this.ventaService.registrarVenta({
      idCliente: usuario.idUsuario,
      items: this.itemsCarrito,
      pago: { ...this.pago, numeroTarjeta: this.pago.numeroTarjeta.replace(/\s/g, '') }
    }).subscribe({
      next: respuesta => {
        this.procesando = false;
        this.resultado = respuesta;
        this.carritoService.vaciar();
        this.limpiarTarjeta();
        this.cdr.detectChanges();
        document.getElementById('resultado-pago')?.focus();
      },
      error: err => {
        this.procesando = false;

        if (err.error && typeof err.error === 'object' && err.error.venta) {
          // Pago rechazado: la venta quedó RECHAZADA y el stock no cambió
          this.error = err.error.mensaje;
        } else if (typeof err.error === 'string') {
          this.error = err.error;
        } else {
          this.error = 'No se pudo procesar el pago. Inténtalo de nuevo.';
        }

        this.cdr.detectChanges();
        document.getElementById('error-pago')?.focus();
      }
    });
  }

  // Los datos de la tarjeta no se guardan en ningún lado del navegador
  private limpiarTarjeta(): void {
    this.pago = { titular: '', numeroTarjeta: '', vencimiento: '', cvv: '' };
  }

  get numeroBoleta(): string {
    return this.resultado ? numeroBoleta(this.resultado.venta.idVenta) : '';
  }

  descargarBoleta(): void {
    const usuario = this.authService.obtenerSesion();

    if (!this.resultado || !usuario) {
      return;
    }

    this.descargando = true;
    this.errorDescarga = '';

    this.ventaService.descargarBoleta(this.resultado.venta.idVenta, usuario.idUsuario).subscribe({
      next: () => {
        this.descargando = false;
        this.cdr.detectChanges();
      },
      error: (err: Error) => {
        this.descargando = false;
        this.errorDescarga = err.message;
        this.cdr.detectChanges();
      }
    });
  }

  volverAlCarrito(): void {
    this.router.navigate(['/comprar-productos']);
  }

  irAMisCompras(): void {
    this.router.navigate(['/mis-compras']);
  }

  volverMenu(): void {
    this.router.navigate(['/menu']);
  }
}
