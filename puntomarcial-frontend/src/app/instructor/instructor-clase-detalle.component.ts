import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router } from '@angular/router';
import { AuthService } from '../services/auth';
import { Alumno, ClaseInstructor, InstructorService, enCurso } from '../services/instructorService';
import { etiquetaDisciplina } from '../services/claseService';

/** HU-13: alumnos inscritos en una clase y toma de asistencia. */
@Component({
  selector: 'app-instructor-clase-detalle',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './instructor-clase-detalle.component.html',
  styleUrl: './instructor.scss'
})
export class InstructorClaseDetalleComponent implements OnInit {
  readonly etiquetaDisciplina = etiquetaDisciplina;
  readonly enCurso = enCurso;

  detalle: ClaseInstructor | null = null;
  idInstructor = 0;
  cargando = true;
  guardandoId: number | null = null;
  error = '';
  mensaje = '';

  constructor(
    private route: ActivatedRoute,
    private authService: AuthService,
    private instructorService: InstructorService,
    private router: Router,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    const usuario = this.authService.obtenerSesion();

    if (!usuario) {
      this.router.navigate(['/login']);
      return;
    }

    this.idInstructor = usuario.idUsuario;
    const idClase = Number(this.route.snapshot.paramMap.get('id'));

    this.instructorService.detalleClase(this.idInstructor, idClase).subscribe({
      next: detalle => {
        this.detalle = detalle;
        this.cargando = false;
        this.cdr.detectChanges();
      },
      error: err => {
        this.error = typeof err.error === 'string' ? err.error : 'No se pudo cargar la clase.';
        this.cargando = false;
        this.cdr.detectChanges();
      }
    });
  }

  textoAsistencia(alumno: Alumno): string {
    return alumno.asistencia === 'ASISTIO' ? 'Asistió'
      : alumno.asistencia === 'NO_ASISTIO' ? 'No asistió'
      : 'Pendiente';
  }

  marcar(alumno: Alumno, asistencia: 'ASISTIO' | 'NO_ASISTIO'): void {
    if (!this.detalle?.puedeTomarAsistencia || alumno.asistencia === asistencia) {
      return;
    }

    this.guardandoId = alumno.idReserva;
    this.error = '';
    this.mensaje = '';

    this.instructorService.marcarAsistencia(this.idInstructor, alumno.idReserva, asistencia).subscribe({
      next: detalle => {
        this.detalle = detalle;
        this.guardandoId = null;
        this.mensaje = `${alumno.nombre} ${alumno.apellido}: ${asistencia === 'ASISTIO' ? 'asistió' : 'no asistió'}.`;
        this.cdr.detectChanges();
      },
      error: err => {
        this.guardandoId = null;
        this.error = typeof err.error === 'string' ? err.error : 'No se pudo guardar la asistencia.';
        this.cdr.detectChanges();
      }
    });
  }

  volver(): void {
    const pasada = this.detalle && new Date(this.detalle.clase.horarioFin).getTime() <= Date.now();
    this.router.navigate([pasada ? '/instructor/historial' : '/instructor/clases']);
  }
}
