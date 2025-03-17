import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable } from 'rxjs';
import { tap } from 'rxjs/operators';
import { LoginResponse } from '../DTO/Response/LoginResponse';
import { CookieService } from 'ngx-cookie-service';
import { AuthService } from './auth.service';
import { userResponse } from '../DTO/Response/userResponse';
import { userRequest } from '../DTO/Request/userRequest';

@Injectable({
  providedIn: 'root'
})
export class UserService {
    // private apiUrl = 'http://localhost:8080/api/v1/user';
    private apiUrl = 'http://167.71.69.170:8080/api/v1/user';

    private userid: string = '';

    constructor(
        private authService: AuthService,
        private http: HttpClient
    ) {
        this.refreshUserId();
    }

    public getUserById(): Observable<userRequest> {
        this.refreshUserId();
        return this.http.get<userRequest>(`${this.apiUrl}/${this.userid}`);
    }

    public updateUser(user: userRequest): Observable<userResponse> {
        this.refreshUserId();
        return this.http.put<userResponse>(`${this.apiUrl}/${this.userid}`, user);
    }

    private refreshUserId(): string {
        return this.userid = this.authService.getUserId();
    }
}
