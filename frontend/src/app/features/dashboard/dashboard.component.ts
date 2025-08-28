import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { ApiService } from '../../shared/services/api.service';
import { AuthService } from '../../shared/services/auth.service';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';

import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { MatSnackBar } from '@angular/material/snack-bar';
import { MatSelectModule } from '@angular/material/select';
import { FormsModule } from '@angular/forms';



@Component({
  standalone: true,
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css'],
  imports: [
    CommonModule,
    RouterModule,
    FormsModule,
    MatCardModule,
    MatButtonModule,
    MatProgressSpinnerModule,
    MatSnackBarModule,
    MatSelectModule,

  ]
})
export class DashboardComponent implements OnInit {
  applications: any[] = [];
  userDocuments: any[] = [];
  loading = false;
  uploading = false;
  selectedFile: File | null = null;
  selectedApplicationId: number | null = null;
  documentType: string = '';
  userId: number | undefined;
  applicationDocuments: Record<number, any[]> = {};

  constructor(
    private apiService: ApiService,
    public authService: AuthService,
    private snackBar: MatSnackBar,
    private router: Router
  ) {}
  private allowedFileTypes = [
    'image/jpeg',
    'image/png',
    'image/jpg',
    'application/pdf',
    'application/vnd.openxmlformats-officedocument.wordprocessingml.document' // .docx
  ];


  ngOnInit(): void {
    this.loadApplications();
  }

  loadApplications(): void {
    this.loading = true;
    this.authService.currentUser$.subscribe(user => {
      console.log('Current user from authService:', user);
      if (user?.id !== undefined) {
        this.userId = user.id;
        this.loadUserDocuments();
      }
    });
    this.apiService.getUserApplications().subscribe({
      next: (apps) => {
        console.log('Applications:', apps);
        this.applications = apps;
        this.loading = false;
      },
      error: () => {
        this.loading = false;
      }
    });
  }
  loadUserDocuments(): void {
    if (this.userId == null) {
      console.warn('User ID is not available yet.');
      return;
    }
    this.apiService.getUserDocuments(this.userId).subscribe({
      next: docs => {
        console.log('User documents:', docs);
        this.userDocuments = docs;
      },
      error: err => {
        console.error('Error loading user documents:', err);
      }
    });
  }
  loadApplicationDocuments(applicationId: number): void {
    this.apiService.getApplicationDocuments(applicationId).subscribe({
      next: docs => {
        console.log(`Documents for application ${applicationId}:`, docs);
        this.applicationDocuments[applicationId] = docs;
      },
      error: err => {
        console.error(`Error loading documents for application ${applicationId}:`, err);
      }
    });
  }
  getDocumentUrl(documentId: number): string {
    return `${this.apiService.baseDocumentUrl}/download/${documentId}`;
  }

  applyForCard(): void {
    this.router.navigate(['/application']);
  }
  onFileSelected(event: Event): void {
    const input = event.target as HTMLInputElement;
    if (input.files && input.files.length > 0) {
      this.selectedFile = input.files[0];
    }
    else {
      this.snackBar.open('No file selected', 'Close', { duration: 3000 });
      return;
    }
    if (!this.allowedFileTypes.includes(this.selectedFile.type)) {
      this.snackBar.open('Invalid file type. Allowed: JPG, PNG, PDF, DOCX.', 'Close', { duration: 3000 });
      this.selectedFile = null;
      input.value = '';
      return;
    }
    const fileSizeInMB = this.selectedFile.size / (1024 * 1024);
    if (fileSizeInMB > 10) {
      this.snackBar.open('File size exceeds 10 MB limit', 'Close', { duration: 3000 });
      this.selectedFile = null;
      input.value = '';
      return; }

  }

  uploadDocument(): void {
    if (!this.selectedFile) {
      this.snackBar.open('Please select a file first', 'Close', { duration: 3000 });
      return;
    }
    if (!this.selectedApplicationId) {
      this.snackBar.open('Please select an application', 'Close', { duration: 3000 });
      return;
    }
    if (!this.documentType) {
      this.snackBar.open('Please select a document type', 'Close', { duration: 3000 });
      return;
    }

    const formData = new FormData();
    formData.append('file', this.selectedFile);
    formData.append('applicationId', this.selectedApplicationId.toString());
    formData.append('userId', this.userId?.toString() || '');
    formData.append('documentType', this.documentType);

    console.log('FormData to upload:');
    formData.forEach((value, key) => console.log(key, value));

    this.uploading = true;
    this.apiService.uploadDocument(formData).subscribe({
      next: () => {
        this.uploading = false;
        this.snackBar.open('Document uploaded successfully!', 'Close', { duration: 3000 });
        this.selectedFile = null;
      },
      error: () => {
        this.uploading = false;
        this.snackBar.open('Failed to upload document', 'Close', { duration: 3000 });
      }
    });
  }

}
