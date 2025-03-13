import { inject } from '@angular/core';
import { Router, ActivatedRouteSnapshot, CanActivateFn } from '@angular/router';
import { AuthService } from '../service/auth.service';

export const roleGuard: CanActivateFn = (route: ActivatedRouteSnapshot) => {
  const authService = inject(AuthService);
  const router = inject(Router);

  // First check if user is logged in
  if (!authService.isAuthenticated()) {
    router.navigate(['/login']);
    return false;
  }

  // Get the required roles from the route data
  const requiredRole = route.data['requiredRole'] as string;
  
  // If no specific role is required, just being authenticated is enough
  if (!requiredRole) {
    return true;
  }

  // Get user role from auth service
  const userRole = authService.getUserRole();
  
  // Check if user has the required role
  if (userRole === requiredRole) {
    return true;
  }

  // If user doesn't have the role, redirect to appropriate page
  // For non-admin users, redirect to products page or show access denied
  router.navigate(['/products']);
  return false;
};