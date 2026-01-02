import { Component, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatIconModule } from '@angular/material/icon';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    RouterModule,
    MatCardModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatProgressSpinnerModule,
    MatIconModule
  ],
  templateUrl: './login.component.html',
  styleUrl: './login.component.scss'
})
export class LoginComponent {
  loginForm: FormGroup;
  loading = signal(false);
  errorMessage = signal<string | null>(null);
  hidePassword = signal(true);
  
  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private router: Router
  ) {
    this.loginForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(6)]]
    });
  }

  onSubmit(): void {
    if (this.loginForm.invalid) {
      this.loginForm.markAllAsTouched();
      return;
    }

    this.loading.set(true);
    this.errorMessage.set(null);

    this.authService.login(this.loginForm.value).subscribe({
      next: () => {
        this.router.navigate(['/']);
      },
      error: (error) => {
        this.loading.set(false);
        console.error('Login error:', error);
        
        if (error.status === 401) {
          this.errorMessage.set('Email ou mot de passe incorrect');
        } else if (error.status === 0) {
          this.errorMessage.set('Impossible de contacter le serveur');
        } else {
          this.errorMessage.set('Une erreur est survenue lors de la connexion');
        }
      }
    });
  }

  getEmailError(): string {
    const emailControl = this.loginForm.get('email');
    if (emailControl?.hasError('required')) {
      return 'L\'email est requis';
    }
    if (emailControl?.hasError('email')) {
      return 'Email invalide';
    }
    return '';
  }

  getPasswordError(): string {
    const passwordControl = this.loginForm.get('password');
    if (passwordControl?.hasError('required')) {
      return 'Le mot de passe est requis';
    }
    if (passwordControl?.hasError('minlength')) {
      return 'Le mot de passe doit contenir au moins 6 caractères';
    }
    return '';
  }

  togglePasswordVisibility(): void {
    this.hidePassword.set(!this.hidePassword());
  }
}
