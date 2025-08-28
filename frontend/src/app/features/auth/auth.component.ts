import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { MatSnackBar } from '@angular/material/snack-bar';
import { ApiService } from '../../shared/services/api.service';

import { CommonModule } from '@angular/common';
import { ReactiveFormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';

import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatSelectModule } from '@angular/material/select';
import { MatTabsModule } from '@angular/material/tabs';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { MatError } from '@angular/material/form-field';


@Component({
  standalone: true,
  selector: 'app-auth',
  templateUrl: './auth.component.html',
  styleUrls: ['./auth.component.css'],
  imports: [
    CommonModule,
    ReactiveFormsModule,
    RouterModule,
    MatCardModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatSelectModule,
    MatTabsModule,
    MatProgressSpinnerModule,
    MatSnackBarModule
  ]
})
export class AuthComponent {
  loginForm: FormGroup;
  registerForm: FormGroup;
  loading = false;
  selectedIndex = 0;

  states = ['AL','AK','AZ','AR','CA','CO','CT','DE','FL','GA','HI','ID','IL','IN','IA','KS','KY','LA','ME','MD','MA','MI','MN','MS','MO','MT','NE','NV','NH','NJ','NM','NY','NC','ND','OH','OK','OR','PA','RI','SC','SD','TN','TX','UT','VT','VA','WA','WV','WI','WY'];

  constructor(
    private fb: FormBuilder,
    private apiService: ApiService,
    private router: Router,
    private snackBar: MatSnackBar
  ) {
    this.loginForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(6)]]
    });

    this.registerForm = this.fb.group({
      firstName: ['', [Validators.required]],
      lastName: ['', [Validators.required]],
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(6)]],
      phoneNumber: ['', [Validators.required, Validators.pattern(/^\d{10}$/)]],
      socialSecurityNumber: ['', [Validators.required, Validators.pattern(/^\d{3}-?\d{2}-?\d{4}$/)]],
      dateOfBirth: ['', [Validators.required]],
      streetAddress: ['', [Validators.required]],
      city: ['', [Validators.required]],
      state: ['', [Validators.required]],
      postalCode: ['', [Validators.required, Validators.pattern(/^\d{5}(-\d{4})?$/)]],
    });
  }

  onLogin(): void {
    if (this.loginForm.valid) {
      this.loading = true;
      this.apiService.login(this.loginForm.value).subscribe({
        next: () => {
          this.loading = false;
          this.showMessage('Login successful!');
          this.router.navigate(['/dashboard']);
        },
        error: (error) => this.handleError(error, 'Login failed')
      });
    }
  }

  onRegister(): void {
    if (this.registerForm.valid) {
      this.loading = true;
      this.apiService.register(this.registerForm.value).subscribe({
        next: () => {
          this.loading = false;
          this.showMessage('Registration successful!');
          this.router.navigate(['/dashboard']);
        },
        error: (error) => this.handleError(error, 'Registration failed')
      });
    }
  }

  private handleError(error: any, defaultMessage: string): void {
    this.loading = false;
    this.showMessage(error.error?.error || defaultMessage);
  }

  private showMessage(message: string): void {
    this.snackBar.open(message, 'Close', { duration: 3000 });
  }
}
