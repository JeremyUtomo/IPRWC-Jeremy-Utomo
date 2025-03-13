import { Routes } from '@angular/router';
import { LoginComponent } from './login/login.component';
import { RegisterComponent } from './register/register.component';
import { ProductDetailsComponent } from './product-details/product-details.component';
import { ProductsComponent } from './products/products.component';
import { authGuard } from './guards/auth.guard';
import { publicGuard } from './guards/public.guard';
import { OrdersComponent } from './orders/orders.component';
import { AdminOverviewComponent } from './admin-overview/admin-overview.component';
import { roleGuard } from './guards/role.guard';
import { CartComponent } from './cart/cart.component';
import { ProductFormComponent } from './product-form/product-form.component';

export const routes: Routes = [
  // Redirect empty path to products
  { path: '', redirectTo: '/products', pathMatch: 'full' },
  
  // Public routes
  { path: 'products', component: ProductsComponent },
  { path: 'products/:name', component: ProductDetailsComponent },
  
  // Auth routes (prevented for logged-in users)
  { path: 'login', component: LoginComponent, canActivate: [publicGuard] },
  { path: 'register', component: RegisterComponent, canActivate: [publicGuard] },

  { path: 'orders', component: OrdersComponent, canActivate: [authGuard] },
  { path: 'cart', component: CartComponent, canActivate: [authGuard] },
  
  { path: 'admin-overview', component: AdminOverviewComponent, canActivate: [roleGuard], data: { requiredRole: 'ADMIN' } },  
  { path: 'admin/products/create', component: ProductFormComponent, canActivate: [roleGuard], data: { requiredRole: 'ADMIN' } },
  { path: 'admin/products/edit/:id', component: ProductFormComponent, canActivate: [roleGuard], data: { requiredRole: 'ADMIN' } },

  // Wildcard route
  { path: '**', redirectTo: '/products' }
];