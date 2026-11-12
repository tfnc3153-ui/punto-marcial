import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import {
  Clase,
  ClaseService,
  DISCIPLINAS_CLASE,
  Instructor,
  SALAS,
  etiquetaDisciplina,
  nombreInstructor
} from '../services/claseService';

@Component({
  selector: 'app-gestion-clases',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './gestion-clases.component.html',
  styleUrl: './gestion-clases.component.scss'
})
export class GestionClasesComponent implements OnInit {
  readonly disciplinas = DISCIPLINAS_CLASE;
  readonly salas = SALAS;
  readonly etiquetaDisciplina = etiquetaDisciplina;
  readonly nombreInstructor = nombreInstructor;

  clases: Clase[] = [];
  instructores: Instructor[] = [];

  formulario: Clase = this.nuevaClase();
  idInstructor: number | null = null;

  editandoId: number | null = null;
  eliminandoId: number | null = null;
  mensaje = '';
  error = '';
  guardando = false;
  cargando = true;

  constructor(
    private claseService: ClaseService,
    private router: Router,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.cargarClases();
    this.cargarInstructores();
  }

  // Solo los instructores que dictan la disciplina elegida
  get instructoresDeLaDisciplina(): Instructor[] {
    return this.instructores.filter(i => i.disciplina === this.formulario.disciplina);
  }

  private nuevaClase(): Clase {
    return {
      disciplina: '',
      instructor: null,
      sala: '',
      horarioInicio: '',
      horarioFin: '',
      aforoMaximo: 1,
      aforoDisponible: 1
    };
  }

  cargarClases(): void {
    this.cargando = true;

    this.claseService.listarClases().subscribe({
      next: clases => {
        this.clases = clases;
        this.cargando = false;
        this.cdr.detectChanges();
      },
      error: () => {
        this.error = 'No se pudieron cargar las clases.';
        this.cargando = false;
        this.cdr.detectChanges();
      }
    });
  }

  cargarInstructores(): void {
    this.claseService.listarInstructores().subscribe({
      next: instructores => {
        this.instructores = instructores;
        this.cdr.detectChanges();
      },
      error: () => {
        this.error = 'No se pudieron cargar los instructores.';
        this.cdr.detectChanges();
      }
    });
  }

  // Al cambiar la disciplina, el instructor elegido ya no corresponde
  cambiarDisciplina(): void {
    if (!this.instructoresDeLaDisciplina.some(i => i.idUsuario === this.idInstructor)) {
      this.idInstructor = null;
    }
  }

  editar(clase: Clase): void {
    this.formulario = {
      ...clase,
      horarioInicio: clase.horarioInicio.slice(0, 16),
      horarioFin: clase.horarioFin.slice(0, 16)
    };

    this.idInstructor = clase.instructor?.idUsuario ?? null;
    this.editandoId = clase.idClase ?? null;
    this.mensaje = '';
    this.error = '';

    window.scrollTo({ top: 0, behavior: 'smooth' });
    document.getElementById('campo-disciplina')?.focus();
  }

  cancelar(): void {
    this.formulario = this.nuevaClase();
    this.idInstructor = null;
    this.editandoId = null;
    this.error = '';
  }

  guardar(): void {
    this.mensaje = '';
    this.error = '';

    const aforo = Number(this.formulario.aforoMaximo);

    if (
      !this.formulario.disciplina ||
      !this.idInstructor ||
      !this.formulario.sala ||
      !this.formulario.horarioInicio ||
      !this.formulario.horarioFin
    ) {
      this.error = 'Elige la disciplina, el instructor, la sala y el horario.';
      return;
    }

    if (this.formulario.horarioFin <= this.formulario.horarioInicio) {
      this.error = 'La hora final debe ser posterior a la inicial.';
      return;
    }

    if (!Number.isInteger(aforo) || aforo < 1 || aforo > 30) {
      this.error = 'El aforo debe ser un número entero entre 1 y 30.';
      return;
    }

    this.guardando = true;

    const datos: Clase = {
      ...this.formulario,
      instructor: { idUsuario: this.idInstructor },

      horarioInicio:
        this.formulario.horarioInicio.length === 16
          ? this.formulario.horarioInicio + ':00'
          : this.formulario.horarioInicio,

      horarioFin:
        this.formulario.horarioFin.length === 16
          ? this.formulario.horarioFin + ':00'
          : this.formulario.horarioFin
    };

    const peticion =
      this.editandoId === null
        ? this.claseService.registrarClase(datos)
        : this.claseService.actualizarClase(this.editandoId, datos);

    peticion.subscribe({
      next: () => {
        this.guardando = false;

        this.mensaje =
          this.editandoId === null
            ? 'Clase registrada.'
            : 'Clase actualizada.';

        this.cancelar();
        this.cargarClases();
        this.cdr.detectChanges();
      },

      error: err => {
        this.guardando = false;

        this.error =
          typeof err.error === 'string'
            ? err.error
            : err.error?.message || 'No se pudo guardar la clase.';

        this.cdr.detectChanges();
      }
    });
  }

  // Solo se pueden eliminar clases que no tienen ninguna reserva
  eliminar(clase: Clase): void {
    if (clase.totalReservas) {
      return;
    }

    const descripcion = `${etiquetaDisciplina(clase.disciplina)} del ${clase.horarioInicio.slice(0, 10)} en la sala ${clase.sala}`;

    if (!confirm(`¿Eliminar la clase de ${descripcion}? Esta acción no se puede deshacer.`)) {
      return;
    }

    this.mensaje = '';
    this.error = '';
    this.eliminandoId = clase.idClase ?? null;

    this.claseService.eliminarClase(clase.idClase!).subscribe({
      next: () => {
        this.eliminandoId = null;
        this.mensaje = `Se eliminó la clase de ${descripcion}.`;

        if (this.editandoId === clase.idClase) {
          this.cancelar();
        }

        this.cargarClases();
        this.cdr.detectChanges();
      },
      error: err => {
        this.eliminandoId = null;
        this.error = typeof err.error === 'string' ? err.error : 'No se pudo eliminar la clase.';
        this.cdr.detectChanges();
      }
    });
  }

  volver(): void {
    this.router.navigate(['/administrador']);
  }
}
