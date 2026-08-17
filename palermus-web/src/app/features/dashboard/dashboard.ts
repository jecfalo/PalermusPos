import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-dashboard',
  imports: [CommonModule],
  template: `
    <div class="dashboard-container">
      <h1>Bienvenido al Panel de Control</h1>
      <p>Selecciona una opción del menú superior para comenzar.</p>
    </div>
  `,
  styles: [`
    .dashboard-container {
      padding: 40px;
      text-align: center;
      background-color: var(--bg-card);
      border-radius: var(--radius-lg);
      box-shadow: var(--shadow-sm);
    }
    h1 { color: var(--text-primary); margin-bottom: 10px; }
    p { color: var(--text-secondary); }
  `]
})
export class DashboardComponent {}
