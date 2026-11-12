import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { Clase, etiquetaDisciplina, nombreInstructor } from '../services/claseService';
import { ReservaService } from '../services/reservaService';
import { AuthService } from '../services/auth';

@Component({
  selector: 'app-reservar-clase',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './reservar-clase.component.html',
  styleUrl: './reservar-clase.component.scss'
})
export class ReservarClaseComponent implements OnInit {

  readonly etiquetaDisciplina = etiquetaDisciplina;
  readonly nombreInstructor = nombreInstructor;

  clase: Clase | null = null;
  mensaje = '';
  error = '';
  cargando = false;


  constructor(
    private router: Router,
    private reservaService: ReservaService,
    private authService: AuthService,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {

    const navigation = this.router.getCurrentNavigation();

    if (navigation?.extras.state?.['clase']) {
      this.clase = navigation.extras.state['clase'];
    }

    if (!this.clase && history.state?.['clase']) {
      this.clase = history.state['clase'];
    }
  }

      confirmarReserva(): void {

      if (!this.clase || !this.clase.idClase) {
        this.error = 'No se encontró la clase seleccionada.';
        return;
      }

      const usuario = this.authService.obtenerSesion();

      if (!usuario) {
        this.error = 'La sesión ha expirado. Inicie sesión nuevamente.';
        return;
      }

      this.cargando = true;
      this.mensaje = '';
      this.error = '';

      const reserva = {
        cliente: {
          idUsuario: usuario.idUsuario
        },
        clase: {
          idClase: this.clase.idClase
        },
        fechaReserva: new Date().toISOString().split('T')[0],
        estado: 'ACTIVA'
      };

      this.reservaService.registrarReserva(reserva).subscribe({

        next: () => {
          this.cargando = false;
          this.mensaje = '¡Reserva realizada correctamente!';
          if (this.clase) {
            this.clase.aforoDisponible =
            Math.max(0, this.clase.aforoDisponible - 1);
        }
        this.cdr.detectChanges();
      },
        error: (err) => {
          this.cargando = false;

          this.error =
            typeof err.error === 'string'
            ? err.error
            : err.error?.message ||
              `No se pudo registrar la reserva (HTTP ${err.status || 0}). Revisa la consola del backend.`;

    this.cdr.detectChanges();

      }
    });
  }
  volverClases(): void {
    this.router.navigate(['/clases']);
  }
}