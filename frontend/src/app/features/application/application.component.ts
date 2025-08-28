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
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';

@Component({
  standalone: true,
  selector: 'app-application',
  templateUrl: './application.component.html',
  // styleUrls: ['./application.component.css'],
  imports: [
    CommonModule,
    ReactiveFormsModule,
    RouterModule,
    MatCardModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatSelectModule,
    MatSnackBarModule,
    MatProgressSpinnerModule
  ]
})
export class ApplicationComponent {
  applicationForm: FormGroup;
  loading = false;

  employmentOptions = [
    { value: 'FULL_TIME', label: 'Full Time' },
    { value: 'PART_TIME', label: 'Part Time' },
    { value: 'SELF_EMPLOYED', label: 'Self Employed' },
    { value: 'UNEMPLOYED', label: 'Unemployed' }
  ];

  housingOptions = [
    { value: 'OWN', label: 'Own' },
    { value: 'RENT', label: 'Rent' },
    { value: 'MORTGAGE', label: 'Mortgage' },
    { value: 'OTHER', label: 'Other' }
  ];

  constructor(
    private fb: FormBuilder,
    private apiService: ApiService,
    private router: Router,
    private snackBar: MatSnackBar
  ) {
    this.applicationForm = this.fb.group({
      annualIncome: ['', [Validators.required, Validators.min(1)]],
      employmentStatus: ['', Validators.required],
      employerName: ['', Validators.required],
      yearsAtJob: ['', [Validators.required, Validators.min(0)]],
      monthlyRent: ['', [Validators.required, Validators.min(0)]],
      housingStatus: ['', Validators.required]
    });
  }

  onSubmit(): void {
    if (this.applicationForm.valid) {
      this.loading = true;
      this.apiService.submitApplication(this.applicationForm.value).subscribe({
        next: () => {
          this.loading = false;
          this.snackBar.open('Application submitted successfully!', 'Close', { duration: 3000 });
          this.router.navigate(['/dashboard']);
        },
        error: (error) => {
          this.loading = false;
          this.snackBar.open('Application submission failed', 'Close', { duration: 3000 });
        }
      });
    }
  }
}
