import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class FileUploadService {
  private apiUrl = 'http://localhost:8080/api/v1/upload';
  
  constructor(private http: HttpClient) {}
  
  uploadImage(file: File): Observable<{filename: string}> {
    const formData = new FormData();
    formData.append('image', file);
    
    // This endpoint should save the file to your assets folder
    // and return the filename that was saved
    return this.http.post<{filename: string}>(this.apiUrl, formData);
  }
}