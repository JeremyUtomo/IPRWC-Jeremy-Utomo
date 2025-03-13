import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable } from 'rxjs';
import { CookieService } from 'ngx-cookie-service';
import { ProductDTO } from '../DTO/ProductDTO';
import { CategoryDTO } from '../DTO/CategoryDTO';
import { ProductRequest } from '../DTO/Request/productRequest';

@Injectable({
  providedIn: 'root'
})
export class ProductService {
    private apiUrl = 'http://localhost:8080/api/v1/product';

    constructor(
        private http: HttpClient,
        private cookieService: CookieService
    ) {}

    getAllProducts(): Observable<ProductDTO[]> {
        return this.http.get<ProductDTO[]>(this.apiUrl);
    }

    getProductById(id: string): Observable<ProductDTO> {
        return this.http.get<ProductDTO>(`${this.apiUrl}/${id}`);
    }

    getProductByName(name: string): Observable<ProductDTO> {
        return this.http.get<ProductDTO>(`${this.apiUrl}/name/${name}`);
    }

    addProduct(product: ProductRequest): Observable<ProductDTO> {
        return this.http.post<ProductDTO>(this.apiUrl, product);
    }

    updateProduct(product: ProductRequest, id: string): Observable<ProductDTO> {
        return this.http.put<ProductDTO>(`${this.apiUrl}/${id}`, product);
    }

    deleteProduct(id: string): Observable<void> {
        return this.http.delete<void>(`${this.apiUrl}/${id}`);
    }

    addProductWithImage(formData: FormData): Observable<ProductDTO> {
        return this.http.post<ProductDTO>(`${this.apiUrl}/upload`, formData, { withCredentials: true});
    }

    updateProductWithImage(id: string, formData: FormData): Observable<ProductDTO> {
        return this.http.put<ProductDTO>(`${this.apiUrl}/upload/${id}`, formData, { withCredentials: true});
    }
}
