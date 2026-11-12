import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router } from '@angular/router';
import { AuthService } from '../services/auth';
import { ClaseInstructor, InstructorService, enCurso } from '../services/instructorService';
import { etiquetaDisciplina } from '../services/claseService';

/** Mis clases (próximas) o Historial (pasadas), según la ruta. */
@Component({
  selector: 'app-instructor-clases',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './instructor-clases.component.html',
  styleUrl: './instructor.scss'
})
export class InstructorClasesComponent implements OnInit {
  readonly etiquetaDisciplina = etiquetaDisciplina;
  readonly enCurso = enCurso;

  pasadas = false;
  clases: ClaseInstructor[] = [];
  cargando = true;
  error = '';

  constructor(
    private route: ActivatedRoute,
    private authService: AuthService,
    private instructorService: InstructorService,
    private router: Router,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.pasadas = this.route.snapshot.data['pasadas'] === true;
    const usuario = this.authService.obtenerSesion();

    if (!usuario) {
      this.router.navigate(['/login']);
      return;
    }

    this.instructorService.listarClases(usuario.idUsuario, this.pasadas).subscribe({
      next: clases => {
        this.clases = clases;
        this.cargando = false;
        this.cdr.detectChanges();
      },
      error: () => {
        this.error = 'No se pudieron cargar tus clases.';
        this.cargando = false;
        this.cdr.detectChanges();
      }
    });
  }

  get totalAsistentes(): number {
    return this.clases.reduce((suma, c) => suma + c.asistentes, 0);
  }

  verClase(item: ClaseInstructor): void {
    this.router.navigate(['/instructor/clases', item.clase.idClase]);
  }

  volver(): void {
    this.router.navigate(['/instructor']);
  }
}
