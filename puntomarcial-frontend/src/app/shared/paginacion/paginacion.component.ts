import { Component, EventEmitter, Input, Output } from '@angular/core';

/**
 * Devuelve solo los elementos de la página indicada.
 * Si la página quedó fuera de rango (por ejemplo, al filtrar), usa la última página válida.
 */
export function paginar<T>(lista: T[], pagina: number, porPagina: number): T[] {
  const totalPaginas = Math.max(1, Math.ceil(lista.length / porPagina));
  const actual = Math.min(Math.max(pagina, 1), totalPaginas);
  const inicio = (actual - 1) * porPagina;
  return lista.slice(inicio, inicio + porPagina);
}

/**
 * Barra de paginación reutilizable: Anterior · 1 2 3 · Siguiente.
 * Accesible: es un <nav> con nombre, la página actual lleva aria-current
 * y al cambiar de página el foco va al título de la lista (idFoco).
 */
@Component({
  selector: 'app-paginacion',
  standalone: true,
  templateUrl: './paginacion.component.html',
  styleUrl: './paginacion.component.scss'
})
export class PaginacionComponent {
  @Input({ required: true }) total = 0;
  @Input({ required: true }) porPagina = 5;
  @Input() pagina = 1;
  @Input() etiqueta = 'Paginación';
  @Input() nombreElementos = 'elemento(s)';
  @Input() idFoco = '';
  @Output() paginaChange = new EventEmitter<number>();

  get totalPaginas(): number {
    return Math.max(1, Math.ceil(this.total / this.porPagina));
  }

  get actual(): number {
    return Math.min(Math.max(this.pagina, 1), this.totalPaginas);
  }

  get paginas(): number[] {
    return Array.from({ length: this.totalPaginas }, (_, i) => i + 1);
  }

  get rango(): string {
    if (!this.total) {
      return `0 ${this.nombreElementos}`;
    }
    const desde = (this.actual - 1) * this.porPagina + 1;
    const hasta = Math.min(desde + this.porPagina - 1, this.total);
    return `${desde}–${hasta} de ${this.total} ${this.nombreElementos}`;
  }

  irA(pagina: number): void {
    if (pagina < 1 || pagina > this.totalPaginas || pagina === this.actual) {
      return;
    }

    this.paginaChange.emit(pagina);

    if (this.idFoco) {
      // El foco vuelve al inicio de la lista para que el lector de pantalla lea la nueva página
      setTimeout(() => {
        const destino = document.getElementById(this.idFoco);
        destino?.focus();
        destino?.scrollIntoView({ block: 'start' });
      });
    }
  }
}
