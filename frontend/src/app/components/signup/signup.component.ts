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
  selector: 'app-signup',
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
  templateUrl: './signup.component.html',
  styleUrl: './signup.component.scss'
})
export class SignupComponent {
  signupForm: FormGroup;
  loading = signal(false);
  errorMessage = signal<string | null>(null);
  hidePassword = signal(true);
  hideConfirmPassword = signal(true);

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private router: Router
  ) {
    this.signupForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(6)]],
      confirmPassword: ['', [Validators.required]]
    }, { validators: this.passwordMatchValidator });
  }

  /**
   * Validateur personnalisé pour vérifier que les mots de passe correspondent
   */
  private passwordMatchValidator(form: FormGroup): { [key: string]: boolean } | null {
    const password = form.get('password');
    const confirmPassword = form.get('confirmPassword');
    
    if (!password || !confirmPassword) {
      return null;
    }

    return password.value === confirmPassword.value ? null : { passwordMismatch: true };
  }

  onSubmit(): void {
    if (this.signupForm.invalid) {
      this.signupForm.markAllAsTouched();
      return;
    }

    this.loading.set(true);
    this.errorMessage.set(null);

    const { email, password } = this.signupForm.value;

    this.authService.register({ email, password }).subscribe({
      next: () => {
        // Après inscription réussie, on connecte automatiquement l'utilisateur
        this.authService.login({ email, password }).subscribe({
          next: () => {
            this.router.navigate(['/']);
          },
          error: (error) => {
            this.loading.set(false);
            console.error('Auto-login error:', error);
            // Même si la connexion échoue, on redirige vers la page de login
            this.router.navigate(['/login']);
          }
        });
      },
      error: (error) => {
        this.loading.set(false);
        console.error('Signup error:', error);
        
        if (error.status === 409) {
          this.errorMessage.set('Cet email est déjà utilisé');
        } else if (error.status === 400) {
          this.errorMessage.set('Données invalides');
        } else if (error.status === 0) {
          this.errorMessage.set('Impossible de contacter le serveur');
        } else {
          this.errorMessage.set('Une erreur est survenue lors de l\'inscription');
        }
      }
    });
  }

  getEmailError(): string {
    const emailControl = this.signupForm.get('email');
    if (emailControl?.hasError('required')) {
      return 'L\'email est requis';
    }
    if (emailControl?.hasError('email')) {
      return 'Email invalide';
    }
    return '';
  }

  getPasswordError(): string {
    const passwordControl = this.signupForm.get('password');
    if (passwordControl?.hasError('required')) {
      return 'Le mot de passe est requis';
    }
    if (passwordControl?.hasError('minlength')) {
      return 'Le mot de passe doit contenir au moins 6 caractères';
    }
    return '';
  }

  getConfirmPasswordError(): string {
    const confirmPasswordControl = this.signupForm.get('confirmPassword');
    if (confirmPasswordControl?.hasError('required')) {
      return 'Veuillez confirmer votre mot de passe';
    }
    if (this.signupForm.hasError('passwordMismatch') && confirmPasswordControl?.touched) {
      return 'Les mots de passe ne correspondent pas';
    }
    return '';
  }

  togglePasswordVisibility(): void {
    this.hidePassword.set(!this.hidePassword());
  }

  toggleConfirmPasswordVisibility(): void {
    this.hideConfirmPassword.set(!this.hideConfirmPassword());
  }
}
