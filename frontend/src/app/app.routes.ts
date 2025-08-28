import { RouterModule, Routes } from '@angular/router';
import { AuthComponent } from './features/auth/auth.component';
import { ApplicationComponent } from './features/application/application.component';
import { DashboardComponent } from './features/dashboard/dashboard.component';
import { AuthGuard } from './shared/guards/auth.guard';

export const routes: Routes = [
  { path: '', redirectTo: '/auth', pathMatch: 'full' },
  { path: 'auth', component: AuthComponent },
  { path: 'application', component: ApplicationComponent, canActivate: [AuthGuard] },
  { path: 'dashboard', component: DashboardComponent, canActivate: [AuthGuard] }
];

