import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { AuthService } from '../services/auth';
import { ClaseInstructor, InstructorService, enCurso } from '../services/instructorService';
import { etiquetaDisciplina } from '../services/claseService';

@Component({
  selector: 'app-instructor-panel',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './instructor-panel.component.html',
  styleUrl: './instructor.scss'
})
export class InstructorPanelComponent implements OnInit {
  readonly etiquetaDisciplina = etiquetaDisciplina;

  nombre = '';
  proximas: ClaseInstructor[] = [];
  claseEnCurso: ClaseInstructor | null = null;
  cargando = true;

  constructor(
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

    this.nombre = usuario.nombre;

    this.instructorService.listarClases(usuario.idUsuario, false).subscribe({
      next: clases => {
        this.proximas = clases;
        this.claseEnCurso = clases.find(c => enCurso(c.clase)) ?? null;
        this.cargando = false;
        this.cdr.detectChanges();
      },
      error: () => {
        this.cargando = false;
        this.cdr.detectChanges();
      }
    });
  }

  ir(ruta: string): void {
    this.router.navigate([ruta]);
  }

  tomarAsistencia(): void {
    if (this.claseEnCurso) {
      this.router.navigate(['/instructor/clases', this.claseEnCurso.clase.idClase]);
    }
  }

  cerrarSesion(): void {
    this.authService.cerrarSesion();
    this.router.navigate(['/login']);
  }
}
