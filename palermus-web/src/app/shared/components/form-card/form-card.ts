import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-form-card',
  imports: [CommonModule],
  templateUrl: './form-card.html',
  styleUrl: './form-card.css'
})
export class FormCard {
  @Input() title = '';
  @Input() subtitle = '';
  @Input() icon = '';
}
