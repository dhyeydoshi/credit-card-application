import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { tap } from 'rxjs/operators';
import { AuthService } from './auth.service';
import { UserRegistration, CreditApplication, AuthResponse } from '../models/user.model';

@Injectable({ providedIn: 'root' })
export class ApiService {
  private baseUrl = 'http://localhost:8081/api';
  public baseDocumentUrl = 'http://localhost:8082/api/documents';

  constructor(private http: HttpClient, private authService: AuthService) {}

  register(userData: UserRegistration): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${this.baseUrl}/auth/register`, userData)
      .pipe(tap(response => this.authService.setCurrentUser(response)));
  }

  login(credentials: {email: string, password: string}): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${this.baseUrl}/auth/login`, credentials)
      .pipe(tap(response => this.authService.setCurrentUser(response)));
  }

  submitApplication(application: CreditApplication): Observable<any> {
    return this.http.post(`${this.baseUrl}/applications/submit`, application);
  }

  getUserApplications(): Observable<any[]> {
    return this.http.get<any[]>(`${this.baseUrl}/applications/user`);
  }

  uploadDocument(formData: FormData): Observable<any> {
    return this.http.post(`${this.baseDocumentUrl}/upload`, formData);
  }
  getUserDocuments(userId: number): Observable<any[]> {
    return this.http.get<any[]>(`${this.baseDocumentUrl}/user/${userId}`);
  }
  getApplicationDocuments(applicationId: number): Observable<any[]> {
    return this.http.get<any[]>(`${this.baseDocumentUrl}/application/${applicationId}`);
  }

}
