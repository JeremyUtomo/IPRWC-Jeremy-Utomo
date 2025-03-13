import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule, Router } from '@angular/router';
import { CartService, CartItem } from '../service/cart.service';
import { OrderService } from '../service/order.service';
import { AuthService } from '../service/auth.service';
import { OrderRequest } from '../DTO/Request/OrderRequest';

@Component({
  selector: 'app-cart',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './cart.component.html',
  styles: [`
    /* Hide number input arrows */
    input[type=number].no-spinner::-webkit-inner-spin-button, 
    input[type=number].no-spinner::-webkit-outer-spin-button { 
      -webkit-appearance: none; 
      margin: 0; 
    }
    
    input[type=number].no-spinner {
      -moz-appearance: textfield; /* Firefox */
    }
  `]
})
export class CartComponent implements OnInit {
  cartItems: CartItem[] = [];
  subtotal: number = 0;
  isPlacingOrder: boolean = false;
  
  constructor(
    private cartService: CartService,
    private orderService: OrderService,
    private authService: AuthService,
    private router: Router
  ) {}
  
  ngOnInit(): void {
    this.cartService.cart$.subscribe(cart => {
      this.cartItems = cart.items;
      this.subtotal = this.calculateSubtotal();
    });
  }
  
  calculateSubtotal(): number {
    return this.cartItems.reduce((total, item) => 
      total + (item.product.price * item.quantity), 0);
  }
  
  updateQuantity(productId: string, quantity: number): void {
    this.cartService.updateQuantity(productId, quantity);
  }
  
  removeItem(productId: string): void {
    this.cartService.removeFromCart(productId);
  }
  
  clearCart(): void {
    this.cartService.clearCart();
  }
  
  placeOrder(): void {
    if (this.cartItems.length === 0) {
      return;
    }
    
    this.isPlacingOrder = true;

    // Prepare order request
    const userId = this.authService.getUserId();
    
    // Create an array of product IDs with repeats based on quantity
    const productIds: string[] = [];
    this.cartItems.forEach(item => {
      // Add product ID for each quantity
      for (let i = 0; i < item.quantity; i++) {
        productIds.push(item.product.id);
      }
    });

    const orderRequest: OrderRequest = {
      userId: userId,
      productIds: productIds
    };

    // Call order service
    this.orderService.createOrder(orderRequest).subscribe({
      next: () => {
        this.cartService.clearCart();
        this.router.navigate(['/orders']);
      },
      error: (error) => {
        console.error('Error creating order:', error);
        this.isPlacingOrder = false;
        // You might want to add error handling UI here
        alert('There was an error placing your order. Please try again.');
      },
      complete: () => {
        this.isPlacingOrder = false;
      }
    });
  }

  handleImageError(event: Event): void {
    const img = event.target as HTMLImageElement;
    if (img) {
      img.src = 'assets/placeholder-image.jpg';
    }
  }
}