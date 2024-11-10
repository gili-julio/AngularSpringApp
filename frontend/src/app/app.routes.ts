import { Routes } from '@angular/router';
import { AppComponent } from './app.component';
import { LoginComponent } from './features/auth/login/login.component';
import { DashboardComponent } from './dashboard/dashboard.component';

export const routes: Routes = [
    {path: '', component: AppComponent},
    {path: 'login', component: LoginComponent, pathMatch: 'full'},
    {path: 'dashboard', component: DashboardComponent, pathMatch: 'full'},
];
