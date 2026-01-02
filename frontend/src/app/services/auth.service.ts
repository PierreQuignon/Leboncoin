import { Injectable, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { Observable, tap } from 'rxjs';
import { LoginRequest, RegisterRequest, LoginResponse, User } from '../models/auth.model';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private readonly apiUrl = `${environment.apiUrl}/auth`;
  private readonly TOKEN_KEY = 'auth_token';
  
  // Signal pour l'état d'authentification
  public currentUser = signal<User | null>(null);
  public isAuthenticated = signal<boolean>(false);

  constructor(
    private http: HttpClient,
    private router: Router
  ) {
    this.checkAuthStatus();
  }

  /**
   * Vérifie si un token existe au démarrage
   */
  private checkAuthStatus(): void {
    const token = this.getToken();
    if (token) {
      this.isAuthenticated.set(true);
      // TODO: Décoder le token JWT pour récupérer les infos utilisateur si nécessaire
    }
  }

  /**
   * Inscription d'un nouvel utilisateur
   */
  register(request: RegisterRequest): Observable<User> {
    return this.http.post<User>(`${this.apiUrl}/register`, request).pipe(
      tap(user => {
        console.log('User registered successfully:', user);
      })
    );
  }

  /**
   * Connexion d'un utilisateur
   */
  login(request: LoginRequest): Observable<LoginResponse> {
    return this.http.post<LoginResponse>(`${this.apiUrl}/login`, request).pipe(
      tap(response => {
        this.setToken(response.token);
        this.currentUser.set(response.user);
        this.isAuthenticated.set(true);
        console.log('User logged in successfully:', response.user);
      })
    );
  }

  /**
   * Déconnexion de l'utilisateur
   */
  logout(): void {
    this.removeToken();
    this.currentUser.set(null);
    this.isAuthenticated.set(false);
    this.router.navigate(['/login']);
  }

  /**
   * Stocke le token JWT dans le localStorage
   */
  private setToken(token: string): void {
    localStorage.setItem(this.TOKEN_KEY, token);
  }

  /**
   * Récupère le token JWT depuis le localStorage
   */
  getToken(): string | null {
    return localStorage.getItem(this.TOKEN_KEY);
  }

  /**
   * Supprime le token JWT du localStorage
   */
  private removeToken(): void {
    localStorage.removeItem(this.TOKEN_KEY);
  }
}
