import { HttpErrorResponse, HttpHandlerFn, HttpInterceptorFn, HttpRequest } from '@angular/common/http';
import { inject } from '@angular/core';
import { catchError, throwError } from 'rxjs';
import { AuthService } from './service/auth.service';
import { Router } from '@angular/router';

export const AuthInterceptor: HttpInterceptorFn = (
  request: HttpRequest<unknown>, 
  next: HttpHandlerFn
) => {
  const authService = inject(AuthService);
  const router = inject(Router);
  
  // Get the auth token from the service
  const authToken = authService.getToken();
  
  // Skip adding the token for authentication endpoints
  if (request.url.includes('/auth/authenticate') || request.url.includes('/auth/register')) {
    return next(request);
  }
  
  // Clone the request and add the authorization header if token exists
  if (authToken) {
    const authRequest = request.clone({
      setHeaders: {
        Authorization: `Bearer ${authToken}`
      }
    });
    
    // Pass the cloned request with the auth header to the next handler
    return next(authRequest).pipe(
      catchError((error: HttpErrorResponse) => {
        // Handle 401 Unauthorized errors by logging out and redirecting to login
        if (error.status === 401) {
          authService.logout();
          router.navigate(['/login']);
        }
        return throwError(() => error);
      })
    );
  }
  
  // If no token, proceed with the original request
  return next(request);
};