import { Component, Input, Optional, Self, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import {
  ControlValueAccessor,
  NgControl,
  ReactiveFormsModule
} from '@angular/forms';

export interface SelectOption {
  value: any;
  label: string;
}

@Component({
  selector: 'app-select-field',
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './select-field.html',
  styleUrl: './select-field.css'
})
export class SelectField implements ControlValueAccessor {
  @Input() label = '';
  @Input() options: SelectOption[] = [];
  @Input() placeholder = 'Seleccione...';
  @Input() icon = '';
  @Input() errorMessages: Record<string, string> = {};

  value: any = '';
  disabled = false;
  focused = signal(false);
  touched = false;

  private onChange: (value: any) => void = () => {};
  private onTouched: () => void = () => {};

  constructor(@Optional() @Self() public ngControl: NgControl) {
    if (this.ngControl) {
      this.ngControl.valueAccessor = this;
    }
  }

  writeValue(value: any): void {
    this.value = value ?? '';
  }

  registerOnChange(fn: (value: any) => void): void {
    this.onChange = fn;
  }

  registerOnTouched(fn: () => void): void {
    this.onTouched = fn;
  }

  setDisabledState(isDisabled: boolean): void {
    this.disabled = isDisabled;
  }

  onSelectChange(event: Event): void {
    const target = event.target as HTMLSelectElement;
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

  hasErrors(): boolean {
    return !!(this.ngControl?.control?.errors);
  }

  getFirstError(): string {
    if (!this.ngControl?.control?.errors) return '';
    const errors = this.ngControl.control.errors;
    const firstKey = Object.keys(errors)[0];

    if (this.errorMessages[firstKey]) {
      return this.errorMessages[firstKey];
    }

    const defaultMessages: Record<string, string> = {
      required: `${this.label || 'Este campo'} es obligatorio`,
    };

    return defaultMessages[firstKey] || 'Selección inválida';
  }
}
