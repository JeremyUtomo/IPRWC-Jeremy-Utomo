import { Component, OnInit, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, Router } from '@angular/router';
import { AuthService } from '../../service/auth.service';
import { CartService } from '../../service/cart.service';
import { combineLatest, Observable, Subscription } from 'rxjs';
import { map } from 'rxjs/operators';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  standalone: true,
  imports: [CommonModule, RouterModule]
})
export class NavbarComponent implements OnInit, OnDestroy {
  isLoggedIn: Observable<boolean>;
  isAdmin: boolean = false;
  cartItemCount: number = 0;
  cartTotal: number = 0;
  private cartSubscription: Subscription | undefined;
  private authSubscription: Subscription | undefined;

  constructor(
    private authService: AuthService,
    private cartService: CartService,
    private router: Router
  ) {
    this.isLoggedIn = this.authService.isLoggedIn;
  }

  ngOnInit(): void {
    // Subscribe to cart changes to update item count and total
    this.cartSubscription = this.cartService.cart$.subscribe(cart => {
      this.cartItemCount = cart.items.reduce((count, item) => count + item.quantity, 0);
      this.cartTotal = cart.items.reduce((total, item) => total + (item.product.price * item.quantity), 0);
    });
    
    // Check if user is admin
    this.authSubscription = combineLatest([
      this.authService.isLoggedIn, 
      this.authService.role
    ]).subscribe(([isLoggedIn, role]) => {
      this.isAdmin = isLoggedIn && role === 'ADMIN';
    })
  }

  ngOnDestroy(): void {
    // Clean up subscriptions to prevent memory leaks
    if (this.cartSubscription) {
      this.cartSubscription.unsubscribe();
    }
    
    if (this.authSubscription) {
      this.authSubscription.unsubscribe();
    }
  }

  logout(): void {
    this.authService.logout();
    this.router.navigate(['/login']);
  }
}