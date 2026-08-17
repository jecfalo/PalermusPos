import { Component, inject, signal } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../../services/auth-service';
import { UserService } from '../../services/user-service';
import { InputField } from '../../../../shared/components/input-field/input-field';
import { ButtonComponent } from '../../../../shared/components/button/button';
import { FormCard } from '../../../../shared/components/form-card/form-card';

@Component({
  selector: 'app-login-user',
  imports: [ReactiveFormsModule, RouterLink, InputField, ButtonComponent, FormCard],
  templateUrl: './login-user.html',
  styleUrl: './login-user.css',
})
export class LoginUser {
  private fb = inject(FormBuilder);
  private authService = inject(AuthService);
  private userService = inject(UserService);
  private router = inject(Router);

  loading = signal(false);
  errorMessage = signal('');

  loginForm: FormGroup = this.fb.group({
    username: ['', [Validators.required, Validators.minLength(3)]],
    password: ['', [Validators.required, Validators.minLength(4)]]
  });

  onSubmit(): void {
    if (this.loginForm.invalid) {
      this.loginForm.markAllAsTouched();
      return;
    }

    this.loading.set(true);
    this.errorMessage.set('');

    const { username, password } = this.loginForm.value;

    this.authService.login(username, password).subscribe({
      next: (response) => {
        this.authService.saveToken(response.token);
        
        // Verificar si es el primer inicio de sesión (usuario == documento)
        this.userService.getProfileByUsername(username).subscribe({
          next: (profile) => {
            this.loading.set(false);
            const role = this.authService.getUserRole();
            
            if (profile.username === profile.document) {
              // Si el usuario es igual al documento, forzar actualización
              this.router.navigate(['/profile'], { queryParams: { requireUpdate: true }});
            } else if (role === 'CLIENT') {
              this.router.navigate(['/purchases']);
            } else {
              this.router.navigate(['/dashboard']);
            }
          },
          error: () => {
            // Si falla la obtención del perfil, navegar por defecto
            this.loading.set(false);
            const role = this.authService.getUserRole();
            if (role === 'CLIENT') {
              this.router.navigate(['/purchases']);
            } else {
              this.router.navigate(['/dashboard']);
            }
          }
        });
      },
      error: (err) => {
        this.loading.set(false);
        if (err.status === 401) {
          this.errorMessage.set('Usuario o contraseña incorrectos');
        } else {
          this.errorMessage.set('Error de conexión. Intenta de nuevo.');
        }
      }
    });
  }
}
