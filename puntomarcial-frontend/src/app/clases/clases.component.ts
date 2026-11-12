import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';

import { Clase, ClaseService, etiquetaDisciplina, nombreInstructor } from '../services/claseService';
import { PaginacionComponent, paginar } from '../shared/paginacion/paginacion.component';

@Component({
  selector: 'app-clases',
  standalone: true,
  imports: [CommonModule, PaginacionComponent],
  templateUrl: './clases.component.html',
  styleUrl: './clases.component.scss'
})
export class ClasesComponent implements OnInit {

  readonly etiquetaDisciplina = etiquetaDisciplina;
  readonly nombreInstructor = nombreInstructor;

  clases: Clase[] = [];
  readonly porPagina = 5;
  pagina = 1;

  get clasesDePagina(): Clase[] {
    return paginar(this.clases, this.pagina, this.porPagina);
  }
  cargando = true;
  error = '';

  constructor(
  private claseService: ClaseService,
  private router: Router,
  private cdr: ChangeDetectorRef
) {}

  ngOnInit(): void {
    this.listarClases();
  }

  listarClases(): void {
    this.cargando = true;
    this.error = '';

    // Solo las clases que todavía no empiezan
    this.claseService.listarDisponibles().subscribe({
      next: (datos: Clase[]) => {
        this.clases = datos;
        this.cargando = false;

        this.cdr.detectChanges();
      },

      error: (err: any) => {
        console.error('Error al cargar las clases:', err);
        this.error = 'No se pudieron cargar las clases.';
        this.cargando = false;
        this.cdr.detectChanges();
      }
    });
  }

  volver(): void {
    this.router.navigate(['/reservar']);
  }

  seleccionarClase(clase: Clase): void {

  this.router.navigate(['/reservar-clase'], {
    state: {
      clase: clase
    }
  }).catch((error) => {

    console.error('ERROR DE NAVEGACIÓN:', error);

  });
}
}