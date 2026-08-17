import { Component, Input, Optional, Self, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import {
  ControlValueAccessor,
  NgControl,
  ReactiveFormsModule
} from '@angular/forms';

@Component({
  selector: 'app-input-field',
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './input-field.html',
  styleUrl: './input-field.css'
})
export class InputField implements ControlValueAccessor {
  @Input() label = '';
  @Input() type: 'text' | 'password' | 'email' | 'number' | 'search' | 'tel' = 'text';
  @Input() placeholder = '';
  @Input() icon = '';
  @Input() errorMessages: Record<string, string> = {};

  value = '';
  disabled = false;
  focused = signal(false);
  showPassword = signal(false);
  touched = false;

  private onChange: (value: string) => void = () => {};
  private onTouched: () => void = () => {};

  constructor(@Optional() @Self() public ngControl: NgControl) {
    if (this.ngControl) {
      this.ngControl.valueAccessor = this;
    }
  }

  writeValue(value: string): void {
    this.value = value || '';
  }

  registerOnChange(fn: (value: string) => void): void {
    this.onChange = fn;
  }

  registerOnTouched(fn: () => void): void {
    this.onTouched = fn;
  }

  setDisabledState(isDisabled: boolean): void {
    this.disabled = isDisabled;
  }

  onInput(event: Event): void {
    const target = event.target as HTMLInputElement;
    this.value = target.value;
    this.onChange(this.value);
  }

  onBlur(): void {
    this.focused.set(false);
    this.touched = true;
    this.onTouched();
  }

  onFocus(): void {
    this.focused.set(true);
  }

  togglePassword(): void {
    this.showPassword.update(v => !v);
  }

  get currentType(): string {
    if (this.type === 'password' && this.showPassword()) {
      return 'text';
    }
    return this.type;
  }

  hasErrors(): boolean {
    return !!(this.ngControl?.control?.errors && this.ngControl.control.errors);
  }

  getFirstError(): string {
    if (!this.ngControl?.control?.errors) return '';
    const errors = this.ngControl.control.errors;
    const firstKey = Object.keys(errors)[0];

    // Custom error messages take priority
    if (this.errorMessages[firstKey]) {
      return this.errorMessages[firstKey];
    }

    // Default messages
    const defaultMessages: Record<string, string> = {
      required: `${this.label || 'Este campo'} es obligatorio`,
      email: 'Ingrese un correo electrónico válido',
      minlength: `Mínimo ${errors['minlength']?.requiredLength} caracteres`,
      maxlength: `Máximo ${errors['maxlength']?.requiredLength} caracteres`,
      pattern: 'El formato no es válido',
      min: `El valor mínimo es ${errors['min']?.min}`,
      max: `El valor máximo es ${errors['max']?.max}`,
    };

    return defaultMessages[firstKey] || 'Campo inválido';
  }
}
