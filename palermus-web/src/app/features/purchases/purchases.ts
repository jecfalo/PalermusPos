import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-purchases',
  imports: [CommonModule, RouterLink],
  templateUrl: './purchases.html',
  styleUrl: './purchases.css'
})
export class PurchasesComponent {
  // Aquí en el futuro se llamará a un servicio de Ventas/Compras
  // para cargar el historial de compras del cliente logueado.
}
