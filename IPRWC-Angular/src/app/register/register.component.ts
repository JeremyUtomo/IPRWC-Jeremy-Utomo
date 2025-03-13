import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule, Router } from '@angular/router';
import { AuthService } from '../service/auth.service';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule]
})
export class RegisterComponent {
  firstName: string = '';
  lastName: string = '';
  email: string = '';
  password: string = '';
  confirmPassword: string = '';
  isLoading: boolean = false;
  errorMessage: string = '';

  constructor(
    private authService: AuthService,
    private router: Router
  ) {}

  validateEmail(email: string): boolean {
    return email.includes('@') && email.includes('.');
  }

  validatePassword(password: string): boolean {
    const hasUpperCase = /[A-Z]/.test(password);
    const hasLowerCase = /[a-z]/.test(password);
    const hasNumber = /[0-9]/.test(password);
    const hasSpecialChar = /[!@#$%^&*()_+\-=\[\]{};':"\\|,.<>\/?]/.test(password);
    const hasMinLength = password.length >= 8;
    
    return hasUpperCase && hasLowerCase && hasNumber && hasSpecialChar && hasMinLength;
  }

  onSubmit() {
    // Reset error message
    this.errorMessage = '';
    
    // Validate email
    if (!this.validateEmail(this.email)) {
      this.errorMessage = 'Email must contain @ and . characters.';
      return;
    }

    // Validate password
    if (!this.validatePassword(this.password)) {
      this.errorMessage = 'Password must be at least 8 characters and contain uppercase, lowercase, number, and special character.';
      return;
    }
    
    // Validate passwords match
    if (this.password !== this.confirmPassword) {
      this.errorMessage = 'Passwords do not match';
      return;
    }
    
    // Set loading state
    this.isLoading = true;
    
    // Call auth service to register user
    this.authService.register(
      this.firstName,
      this.lastName,
      this.email,
      this.password
    ).subscribe({
      next: () => {
        // Redirect to login page after successful registration
        this.router.navigate(['/login']);
      },
      error: (error) => {
        console.error('Registration error:', error);
        // Handle different error cases
        if (error.status === 409) {
          this.errorMessage = 'Email already exists. Please use a different email.';
        } else {
          this.errorMessage = 'Registration failed. Please try again.';
        }
        this.isLoading = false;
      },
      complete: () => {
        this.isLoading = false;
      }
    });
  }
}