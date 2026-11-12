import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { Clase, ClaseService, etiquetaDisciplina, nombreInstructor } from '../services/claseService';
import { PaginacionComponent, paginar } from '../shared/paginacion/paginacion.component';

@Component({
  selector: 'app-supervisar-aforo',
  standalone: true,
  imports: [CommonModule, PaginacionComponent],
  templateUrl: './supervisar-aforo.component.html',
  styleUrl: '../shared/operaciones.scss'
})
export class SupervisarAforoComponent implements OnInit {

  readonly etiquetaDisciplina = etiquetaDisciplina;
  readonly nombreInstructor = nombreInstructor;
  clases: Clase[] = [];
  readonly porPagina = 6;
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
    this.actualizar();
  }

  actualizar(): void {
    this.cargando = true;
    this.error = '';

    this.claseService.listarClases().subscribe({
      next: clases => {
        this.clases = clases;
        this.cargando = false;
        this.cdr.detectChanges();
      },
      error: () => {
        this.error = 'No se pudo consultar el aforo.';
        this.cargando = false;
        this.cdr.detectChanges();
      }
    });
  }

  ocupados(clase: Clase): number {
    return clase.aforoMaximo - clase.aforoDisponible;
  }

  porcentaje(clase: Clase): number {
    return clase.aforoMaximo > 0
      ? Math.round(this.ocupados(clase) * 100 / clase.aforoMaximo)
      : 0;
  }

  volver(): void {
    this.router.navigate(['/administrador']);
  }
}