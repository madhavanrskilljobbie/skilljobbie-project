import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface Student {
  name: string;
  email: string;
  phone: string;
  course: string;
}

@Injectable({
  providedIn: 'root'
})
export class StudentService {
  private apiUrl = 'http://localhost:8080/api/students';

  constructor(private http: HttpClient) { }

  registerStudent(student: Student): Observable<any> {
    return this.http.post(`${this.apiUrl}/register`, student);
  }
}
