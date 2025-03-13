import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { OrderRequest } from '../DTO/Request/OrderRequest';
import { Observable } from 'rxjs';
import { OrderDTO } from '../DTO/OrderDTO';
import { AuthService } from './auth.service';


@Injectable({
  providedIn: 'root'
})
export class OrderService {
    private apiUrl = 'http://localhost:8080/api/v1/order';
    private userId: string = '';

    constructor(
        private http: HttpClient,
        private AuthService: AuthService
    ) {
        this.userId = this.AuthService.getUserId();
     }


    createOrder(order: OrderRequest): Observable<OrderDTO> {
        return this.http.post<OrderDTO>(this.apiUrl, order);
    }

    getOrderByUserId(): Observable<OrderDTO[]> {
        return this.http.get<OrderDTO[]>(`${this.apiUrl}/${this.userId}`);
    }

}
