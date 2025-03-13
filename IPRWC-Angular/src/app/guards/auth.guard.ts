import { inject } from '@angular/core';
import { Router, CanActivateFn } from '@angular/router';
import { AuthService } from '../service/auth.service';
import { map, take } from 'rxjs/operators';

export const authGuard: CanActivateFn = () => {
  const authService = inject(AuthService);
  const router = inject(Router);
  
  return authService.isLoggedIn.pipe(
    take(1),
    map(isLoggedIn => {
      if (isLoggedIn) {
        return true;
      }
      
      // Not logged in, redirect to login page with return URL
      router.navigate(['/login'], { 
        queryParams: { returnUrl: router.url } 
      });
      return false;
    })
  );
};