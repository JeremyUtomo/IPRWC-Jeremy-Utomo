import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable } from 'rxjs';
import { CategoryDTO } from '../DTO/CategoryDTO';

@Injectable({
  providedIn: 'root'
})
export class CategoryService {
    // private apiUrl = 'http://localhost:8080/api/v1/category';
    private apiUrl = 'http://167.71.69.170/api/v1/category';

    constructor(
        private http: HttpClient
    ) {}

    getAllCategories(): Observable<CategoryDTO[]> {
        return this.http.get<CategoryDTO[]>(this.apiUrl);
    }

    getCategoryById(id: string): Observable<CategoryDTO> {
        return this.http.get<CategoryDTO>(`${this.apiUrl}/${id}`);
    }

}
