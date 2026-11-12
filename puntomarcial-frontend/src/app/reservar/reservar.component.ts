import { Component } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-reservar',
  standalone: true,
  templateUrl: './reservar.component.html',
  styleUrl: './reservar.component.scss'
})
export class ReservarComponent {

  constructor(private router: Router) {}

  verClases(): void {
    this.router.navigate(['/clases']);
  }

  volverMenu(): void {
    this.router.navigate(['/menu']);
  }
}