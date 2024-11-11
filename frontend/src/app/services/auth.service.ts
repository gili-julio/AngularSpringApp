import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { Observable } from 'rxjs';

interface AuthResponse {
  token: string;
}

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private apiUrl = 'http://localhost:8080/auth';  // Full endpoint path

  constructor(private http: HttpClient, private router: Router) {}

  // Login method, calls the API and returns an observable of the response
  login(username: string, password: string): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(this.apiUrl, { username, password });
  }

  // Logout method to remove the token and redirect to the login page
  logout() {
    localStorage.removeItem('jwtToken');
    this.router.navigate(['/']);  // Redirect to the login or home page
  }

  // Check if the user is authenticated by verifying the presence of a token
  isAuthenticated(): boolean {
    return !!localStorage.getItem('jwtToken');
  }

  // Retrieve the JWT token for use in HTTP headers
  getToken(): string | null {
    return localStorage.getItem('jwtToken');
  }

  // Helper function to store the token
  storeToken(token: string) {
    localStorage.setItem('jwtToken', token);
  }
}
