import { Component, inject, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators, AbstractControl, ValidationErrors } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { UserService } from '../../services/user-service';
import { AuthService } from '../../services/auth-service';
import { ReferenceProfile } from '../../models/profile/ReferenceProfile';

@Component({
  selector: 'app-profile-page',
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './profile.html',
  styleUrl: './profile.css'
})
export class ProfileComponent implements OnInit {
  private userService = inject(UserService);
  private authService = inject(AuthService);
  private fb = inject(FormBuilder);
  private route = inject(ActivatedRoute);
  
  profile = signal<ReferenceProfile | null>(null);
  loading = signal(true);
  error = signal('');
  
  isSaving = signal(false);
  successMessage = signal('');
  requireUpdateMsg = signal('');

  editForm: FormGroup = this.fb.group({
    username: ['', [Validators.required, Validators.minLength(3)]],
    email: ['', [Validators.required, Validators.email]],
    currentPassword: [''],
    password: ['', [Validators.minLength(4)]],
    confirmPassword: ['']
  }, { validators: this.passwordMatchValidator });

  passwordMatchValidator(control: AbstractControl): ValidationErrors | null {
    const password = control.get('password')?.value;
    const confirmPassword = control.get('confirmPassword')?.value;
    const currentPassword = control.get('currentPassword')?.value;

    if (password && !currentPassword) {
      control.get('currentPassword')?.setErrors({ required: true });
      return { currentPasswordRequired: true };
    }

    if (password && password !== confirmPassword) {
      control.get('confirmPassword')?.setErrors({ mismatch: true });
      return { mismatch: true };
    }

    if (!password) {
      control.get('confirmPassword')?.setErrors(null);
      control.get('currentPassword')?.setErrors(null);
    }
    
    return null;
  }

  ngOnInit(): void {
    this.route.queryParams.subscribe(params => {
      if (params['requireUpdate']) {
        this.requireUpdateMsg.set('Por seguridad, por favor actualiza tu nombre de usuario y cambia tu contraseña temporal.');
      }
    });
    this.loadProfile();
  }

  loadProfile(): void {
    this.loading.set(true);
    this.error.set('');
    const username = this.authService.getUsername();
    
    if (username) {
      this.userService.getProfileByUsername(username).subscribe({
        next: (data) => {
          this.profile.set(data);
          this.editForm.patchValue({
            username: data.username,
            email: data.email,
            currentPassword: '',
            password: '',
            confirmPassword: ''
          });
          this.loading.set(false);
        },
        error: (err) => {
          console.error(err);
          this.error.set('No se pudo cargar la información del perfil.');
          this.loading.set(false);
        }
      });
    } else {
      this.error.set('Usuario no autenticado.');
      this.loading.set(false);
    }
  }

  saveChanges(): void {
    if (this.editForm.invalid || !this.profile()) return;

    this.isSaving.set(true);
    this.error.set('');
    this.successMessage.set('');
    const formVals = this.editForm.value;
    const pId = this.profile()!.id;

    let requiresRelogin = false;
    let requests = [];

    if (formVals.email !== this.profile()!.email) {
      requests.push(this.userService.updateProfileEmail(pId, formVals.email).toPromise());
    }
    
    if (formVals.username !== this.profile()!.username) {
      requests.push(this.userService.updateProfileUsername(pId, formVals.username).toPromise());
      requiresRelogin = true;
    }

    if (formVals.password && formVals.password.trim() !== '') {
      const passPayload = { currentPassword: formVals.currentPassword, password: formVals.password };
      requests.push(this.userService.updateProfilePassword(pId, passPayload).toPromise());
      requiresRelogin = true;
    }

    if (requests.length === 0) {
      this.isSaving.set(false);
      this.successMessage.set('No hay cambios para guardar.');
      return;
    }

    Promise.all(requests)
      .then(() => {
        this.isSaving.set(false);
        if (requiresRelogin) {
          alert('Datos sensibles actualizados. Por seguridad, debes iniciar sesión de nuevo.');
          this.authService.logout();
        } else {
          this.successMessage.set('Cambios guardados correctamente.');
          this.loadProfile();
        }
      })
      .catch(err => {
        console.error(err);
        this.isSaving.set(false);
        if (err.status === 400 && err.error?.message) {
           this.error.set(err.error.message);
        } else {
           this.error.set('Ocurrió un error al guardar los cambios.');
        }
      });
  }
}
