import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable } from 'rxjs';
import { tap } from 'rxjs/operators';
import { LoginResponse } from '../DTO/Response/LoginResponse';
import { CookieService } from 'ngx-cookie-service';
import { jwtDecode } from 'jwt-decode';
import { get } from 'http';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
    private apiUrl = 'http://localhost:8080/api/v1/auth/';

    private loggedIn = new BehaviorSubject<boolean>(false);
    private userRole = new BehaviorSubject<string>('');
    private userId = new BehaviorSubject<string>('');

    constructor(
        private router: Router,
        private http: HttpClient,
        private cookieService: CookieService
    ) {
        // Check if token exists on service initialization
        this.loggedIn.next(this.isAuthenticated());
        if (this.cookieService.check('auth_token')) {
            this.userRole.next(this.getUserRole());
            this.userId.next(this.getUserId());
        }
    }

    get isLoggedIn() {
        return this.loggedIn.asObservable();
    }

    get role() {
        return this.userRole.asObservable();
    }

    login(email: string, password: string): Observable<LoginResponse> {
        return this.http.post<LoginResponse>(`${this.apiUrl}authenticate`, { email, password }).pipe(
            tap((response) => {
                const expirationDate = new Date();
                expirationDate.setDate(expirationDate.getDate() + 7);

                this.loggedIn.next(true);
                this.cookieService.set('auth_token', response.token, expirationDate, '/', undefined, true, 'Strict');
                this.userRole.next(this.getUserRole());
                this.userId.next(this.getUserId());
            })
          );
        }

    register(firstName: string, lastName: string, email: string, password: string): Observable<LoginResponse> {
        return this.http.post<LoginResponse>(`${this.apiUrl}register`, { firstName, lastName, email, password });
    }

    isAuthenticated(): boolean {
        return this.cookieService.check('auth_token');
    }

    getToken(): string {
        return this.cookieService.get('auth_token');
    }

    logout(): void {
        this.cookieService.delete('auth_token', '/');
        this.userId.next('');
        this.userRole.next('');
        this.loggedIn.next(false);
        this.router.navigate(['/login']);
    }

    getUserId(): string {
        const token = this.getToken();
    if (token) {
        const decodedToken = jwtDecode<any>(token);
        return decodedToken.id; // Adjust property name if needed based on your token structure
    }
    return '';
    }

    getUserRole(): string {
        const token = this.getToken();
        if (token) {
            const decodedToken = jwtDecode<any>(token);
            return decodedToken.role; // Adjust property name if needed based on your token structure
        }
    return '';
    }
}
