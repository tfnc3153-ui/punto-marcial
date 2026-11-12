import { Component, ElementRef, HostListener, OnInit, ViewChild } from '@angular/core';

type TamanoTexto = 'normal' | 'grande' | 'muy-grande';

interface PreferenciasAccesibilidad {
  tamano: TamanoTexto;
  altoContraste: boolean;
}

const CLAVE_PREFERENCIAS = 'pm_accesibilidad';

/**
 * Botón de accesibilidad (engranaje) visible en todas las pantallas (RNF-02).
 * Abre un panel con tamaño de texto y modo alto contraste.
 * La preferencia se guarda en localStorage.
 */
@Component({
  selector: 'app-accesibilidad',
  standalone: true,
  templateUrl: './accesibilidad.component.html',
  styleUrl: './accesibilidad.component.scss'
})
export class AccesibilidadComponent implements OnInit {

  readonly tamanos: { valor: TamanoTexto; etiqueta: string; descripcion: string }[] = [
    { valor: 'normal', etiqueta: 'A', descripcion: 'Texto normal' },
    { valor: 'grande', etiqueta: 'A+', descripcion: 'Texto grande' },
    { valor: 'muy-grande', etiqueta: 'A++', descripcion: 'Texto muy grande' }
  ];

  tamano: TamanoTexto = 'normal';
  altoContraste = false;
  abierto = false;

  @ViewChild('botonEngranaje') botonEngranaje?: ElementRef<HTMLButtonElement>;

  constructor(private elemento: ElementRef<HTMLElement>) {}

  ngOnInit(): void {
    const guardadas = this.leerPreferencias();

    if (guardadas) {
      this.tamano = guardadas.tamano;
      this.altoContraste = guardadas.altoContraste;
    }

    this.aplicar();
  }

  alternarPanel(): void {
    this.abierto = !this.abierto;
  }

  cerrar(): void {
    this.abierto = false;
    this.botonEngranaje?.nativeElement.focus();
  }

  // Escape cierra el panel y devuelve el foco al engranaje
  @HostListener('document:keydown.escape')
  alPresionarEscape(): void {
    if (this.abierto) {
      this.cerrar();
    }
  }

  // Un clic fuera del panel lo cierra
  @HostListener('document:click', ['$event'])
  alHacerClic(evento: MouseEvent): void {
    if (this.abierto && !this.elemento.nativeElement.contains(evento.target as Node)) {
      this.abierto = false;
    }
  }

  cambiarTamano(tamano: TamanoTexto): void {
    this.tamano = tamano;
    this.aplicar();
    this.guardarPreferencias();
  }

  alternarContraste(): void {
    this.altoContraste = !this.altoContraste;
    this.aplicar();
    this.guardarPreferencias();
  }

  // Con el router de Angular, un href="#..." cambiaría la ruta; por eso se mueve el foco a mano.
  saltarAlContenido(evento: Event): void {
    evento.preventDefault();
    const contenido = document.getElementById('contenido');

    if (contenido) {
      contenido.focus();
      contenido.scrollIntoView();
    }
  }

  private aplicar(): void {
    const html = document.documentElement;

    html.classList.toggle('texto-grande', this.tamano === 'grande');
    html.classList.toggle('texto-muy-grande', this.tamano === 'muy-grande');
    html.classList.toggle('alto-contraste', this.altoContraste);
  }

  private leerPreferencias(): PreferenciasAccesibilidad | null {
    try {
      const datos = localStorage.getItem(CLAVE_PREFERENCIAS);

      if (!datos) {
        return null;
      }

      const preferencias = JSON.parse(datos) as PreferenciasAccesibilidad;
      const tamanoValido = this.tamanos.some(t => t.valor === preferencias.tamano);

      return {
        tamano: tamanoValido ? preferencias.tamano : 'normal',
        altoContraste: preferencias.altoContraste === true
      };
    } catch {
      return null;
    }
  }

  private guardarPreferencias(): void {
    try {
      const preferencias: PreferenciasAccesibilidad = {
        tamano: this.tamano,
        altoContraste: this.altoContraste
      };
      localStorage.setItem(CLAVE_PREFERENCIAS, JSON.stringify(preferencias));
    } catch {
      // Si el navegador bloquea localStorage, la preferencia solo dura esta visita.
    }
  }
}
