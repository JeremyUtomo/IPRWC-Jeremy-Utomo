import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule, Router } from '@angular/router';
import { AuthService } from '../service/auth.service';
import { CookieService } from 'ngx-cookie-service';
import { UserService } from '../service/user.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule]
})
export class LoginComponent {
  email: string = '';
  password: string = '';
  isLoading: boolean = false;
  errorMessage: string = '';

  constructor(
    private authService: AuthService,
    private router: Router,
    private cookieService: CookieService,
    private userService: UserService
  ) {}

  onSubmit() {
    this.isLoading = true;
    this.errorMessage = '';

    this.authService.login(this.email, this.password).subscribe({
      next: (response) => {
        // Store token in cookies
        // Set expiration for 7 days (or your preferred duration)
        const expirationDate = new Date();
        expirationDate.setDate(expirationDate.getDate() + 7);

        this.cookieService.set(
          'auth_token',
          response.token,
          expirationDate,
          '/', // Path
          undefined, // Domain
          true, // Secure flag for HTTPS
          'Strict' // SameSite policy
        );

        // Redirect to home or dashboard
        this.router.navigate(['/products']);
      },
      error: (error) => {
        console.error('Login error:', error);
        this.errorMessage = 'Invalid email or password. Please try again.';
        this.isLoading = false;
      },
      complete: () => {
        this.isLoading = false;
      }
    });
  }
}
