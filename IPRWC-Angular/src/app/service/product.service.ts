import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, map } from 'rxjs';
import { ProductDTO } from '../DTO/ProductDTO';
import { ProductResponse } from '../DTO/Response/productResponse';
import { ProductRequest } from '../DTO/Request/productRequest';

@Injectable({
  providedIn: 'root'
})
export class ProductService {
    // private apiUrl = 'http://localhost:8080/api/v1/product';
    private apiUrl = 'http://167.71.69.170/api/v1/product';
    private imageApiUrl = 'http://167.71.69.170:8080/api/v1/product/image';

    constructor(
        private http: HttpClient
    ) {}

    getAllProducts(): Observable<ProductDTO[]> {
        return this.http.get<ProductResponse[]>(this.apiUrl).pipe(
            map((products: ProductResponse[]) => {
                return products.map(product => {
                    product.image = this.getProductImage(product.image);
                    return product;
                });
            })
        );
    }

    getProductById(id: string): Observable<ProductDTO> {
        return this.http.get<ProductResponse>(`${this.apiUrl}/${id}`).pipe(
            map((product: ProductResponse) => {
                product.image = this.getProductImage(product.image);
                return product;
            }
        ));
    }

    getProductByName(name: string): Observable<ProductDTO> {
        return this.http.get<ProductResponse>(`${this.apiUrl}/name/${name}`).pipe(
            map((product: ProductResponse) => {
                product.image = this.getProductImage(product.image);
                return product;
            }
        ));    
    }

    addProduct(product: FormData): Observable<ProductDTO> {
        return this.http.post<ProductDTO>(this.apiUrl, product);
    }

    updateProduct(product: FormData, id: string): Observable<ProductDTO> {
        return this.http.put<ProductDTO>(`${this.apiUrl}/${id}`, product);
    }

    deleteProduct(id: string): Observable<void> {
        return this.http.delete<void>(`${this.apiUrl}/${id}`);
    }

    getProductImage(fileName: string): string {
        if (!(fileName).includes(this.imageApiUrl)) {
            return `${this.apiUrl}/${fileName}`;
        }
        return fileName;
    }
}