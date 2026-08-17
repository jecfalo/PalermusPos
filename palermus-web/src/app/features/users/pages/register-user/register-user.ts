import { Component, inject, signal } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { UserService } from '../../services/user-service';
import { InputField } from '../../../../shared/components/input-field/input-field';
import { ButtonComponent } from '../../../../shared/components/button/button';
import { FormCard } from '../../../../shared/components/form-card/form-card';

@Component({
  selector: 'app-register-user',
  imports: [ReactiveFormsModule, RouterLink, InputField, ButtonComponent, FormCard],
  templateUrl: './register-user.html',
  styleUrl: './register-user.css',
})
export class RegisterUser {
  private fb = inject(FormBuilder);
  private userService = inject(UserService);
  private router = inject(Router);

  loading = signal(false);
  errorMessage = signal('');
  successMessage = signal('');

  registerForm: FormGroup = this.fb.group({
    username: [''],
    password: [''],
    document: ['', [Validators.required]],
    names: ['', [Validators.required, Validators.minLength(2)]],
    surnames: ['', [Validators.required, Validators.minLength(2)]],
    email: ['', [Validators.required, Validators.email]]
  });

  onSubmit(): void {
    if (this.registerForm.invalid) {
      this.registerForm.markAllAsTouched();
      return;
    }

    this.loading.set(true);
    this.errorMessage.set('');
    this.successMessage.set('');

    this.userService.addUser(this.registerForm.value).subscribe({
      next: () => {
        this.loading.set(false);
        this.successMessage.set('¡Cuenta creada exitosamente! Redirigiendo al login...');
        setTimeout(() => {
          this.router.navigate(['/login']);
        }, 2000);
      },
      error: (err) => {
        this.loading.set(false);
        if (err.status === 409) {
          this.errorMessage.set('El usuario o documento ya existe.');
        } else {
          this.errorMessage.set('Error al registrar. Intenta de nuevo.');
        }
      }
    });
  }
}
