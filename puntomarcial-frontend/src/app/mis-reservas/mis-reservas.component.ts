import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { Reserva, ReservaService } from '../services/reservaService';
import { AuthService } from '../services/auth';
import { etiquetaDisciplina, nombreInstructor } from '../services/claseService';
import { PaginacionComponent, paginar } from '../shared/paginacion/paginacion.component';

@Component({
  selector: 'app-mis-reservas',
  standalone: true,
  imports: [CommonModule, PaginacionComponent],
  templateUrl: './mis-reservas.component.html',
  styleUrl: '../shared/operaciones.scss'
})
export class MisReservasComponent implements OnInit {

  readonly etiquetaDisciplina = etiquetaDisciplina;
  readonly nombreInstructor = nombreInstructor;
  reservas: Reserva[] = [];
  readonly porPagina = 4;
  pagina = 1;

  get reservasDePagina(): Reserva[] {
    return paginar(this.reservas, this.pagina, this.porPagina);
  }
  cargando = true;
  error = '';
  mensaje = '';
  cancelandoId: number | null = null;

  constructor(
    private reservaService: ReservaService,
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

    this.reservaService.listarPorCliente(usuario.idUsuario).subscribe({
      next: reservas => {
        this.reservas = reservas;
        this.cargando = false;
        this.cdr.detectChanges();
      },
      error: err => {
        this.error =
          typeof err.error === 'string'
            ? err.error
            : 'No se pudieron cargar tus reservas.';
        this.cargando = false;
        this.cdr.detectChanges();
      }
    });
  }

  volver(): void {
    this.router.navigate(['/menu']);
  }

  yaEmpezo(reserva: Reserva): boolean {
    return new Date(reserva.clase.horarioInicio).getTime() <= Date.now();
  }

  textoAsistencia(reserva: Reserva): string {
    return reserva.asistencia === 'ASISTIO' ? 'Asististe'
      : reserva.asistencia === 'NO_ASISTIO' ? 'No asististe'
      : 'Asistencia pendiente';
  }

  cancelar(reserva: Reserva): void {
    const usuario = this.authService.obtenerSesion();

    if (!usuario) {
      this.router.navigate(['/login']);
      return;
    }

    if (!confirm(`¿Deseas cancelar la reserva #${reserva.idReserva}?`)) {
      return;
    }

    this.error = '';
    this.mensaje = '';
    this.cancelandoId = reserva.idReserva;

    this.reservaService
      .cancelarReserva(reserva.idReserva, usuario.idUsuario)
      .subscribe({
        next: () => {
          // La reserva queda CANCELADA en la base de datos, pero ya no se muestra al cliente
          this.reservas = this.reservas.filter(r => r.idReserva !== reserva.idReserva);
          this.mensaje = `La reserva #${reserva.idReserva} fue cancelada correctamente.`;
          this.cancelandoId = null;
          this.cdr.detectChanges();
        },
        error: err => {
          this.error =
            typeof err.error === 'string'
              ? err.error
              : 'No se pudo cancelar la reserva.';
          this.cancelandoId = null;
          this.cdr.detectChanges();
        }
      });
  }
}
