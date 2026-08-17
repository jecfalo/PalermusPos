import { HttpInterceptorFn, HttpErrorResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { Router } from '@angular/router';
import { catchError, throwError } from 'rxjs';
import { AuthService } from '../../features/users/services/auth-service';

export const errorInterceptor: HttpInterceptorFn = (req, next) => {
  const authService = inject(AuthService);
  const router = inject(Router);

  return next(req).pipe(
    catchError((error: HttpErrorResponse) => {
      if (error.status === 401) {
        // Token expiro o es invalido
        authService.logout();
      } else if (error.status === 403) {
        // No tiene los roles necesarios
        console.warn('Acceso denegado: No tienes los permisos necesarios.');
        router.navigate(['/']); // O alguna pagina de error 403
      }
      return throwError(() => error);
    })
  );
};
