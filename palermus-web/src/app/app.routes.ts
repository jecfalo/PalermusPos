import { Routes } from '@angular/router';
import { LoginUser } from './features/users/pages/login-user/login-user';
import { RegisterUser } from './features/users/pages/register-user/register-user';
import { DashboardComponent } from './features/dashboard/dashboard';
import { SalesComponent } from './features/sales/sales';
import { InventoryComponent } from './features/inventory/inventory';
import { CustomersComponent } from './features/customers/customers';
import { SettingsComponent } from './features/settings/settings';
import { ProfileComponent } from './features/users/pages/profile/profile';
import { PurchasesComponent } from './features/purchases/purchases';

export const routes: Routes = [
  { path: '', redirectTo: '/login', pathMatch: 'full' },
  { path: 'login', component: LoginUser },
  { path: 'register', component: RegisterUser },
  { path: 'dashboard', component: DashboardComponent },
  { path: 'sales', component: SalesComponent },
  { path: 'inventory', component: InventoryComponent },
  { path: 'customers', component: CustomersComponent },
  { path: 'settings', component: SettingsComponent },
  { path: 'profile', component: ProfileComponent },
  { path: 'purchases', component: PurchasesComponent }
];
