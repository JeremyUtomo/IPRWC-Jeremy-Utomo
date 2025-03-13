import { inject } from '@angular/core';
import { Router, CanActivateFn } from '@angular/router';
import { AuthService } from '../service/auth.service';
import { map, take } from 'rxjs/operators';

export const publicGuard: CanActivateFn = () => {
  const authService = inject(AuthService);
  const router = inject(Router);
  
  return authService.isLoggedIn.pipe(
    take(1),
    map(isLoggedIn => {
      if (isLoggedIn) {
        // User is logged in, redirect to products
        router.navigate(['/products']);
        return false;
      }
      
      // Not logged in, allow access to public pages
      return true;
    })
  );
};