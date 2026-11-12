import { Component, signal } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { AccesibilidadComponent } from './accesibilidad/accesibilidad.component';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, AccesibilidadComponent],
  templateUrl: './app.html',
  styleUrl: './app.scss'
})
export class App {
  protected readonly title = signal('puntomarcial-frontend');
}
