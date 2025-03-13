import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable } from 'rxjs';
import { ProductDTO } from '../DTO/ProductDTO';
import { CookieService } from 'ngx-cookie-service';

// Define interface for cart items
export interface CartItem {
  product: ProductDTO;
  quantity: number;
}

// Define interface for the cart
export interface Cart {
  items: CartItem[];
}

@Injectable({
  providedIn: 'root'
})
export class CartService {
  private cartKey = 'shopping_cart';
  private cartSubject = new BehaviorSubject<Cart>({ items: [] });
  public cart$ = this.cartSubject.asObservable();
  
  constructor(
    private router: Router,
    private cookieService: CookieService
  ) {
    this.loadCartFromCookies();
  }
  
  // Load cart data from cookies on service initialization
  private loadCartFromCookies(): void {
    if (this.cookieService.check(this.cartKey)) {
      try {
        const cartData = this.cookieService.get(this.cartKey);
        const parsedCart = JSON.parse(cartData);
        this.cartSubject.next(parsedCart);
      } catch (error) {
        console.error('Error parsing cart from cookies:', error);
        this.resetCart();
      }
    }
  }
  
  // Save current cart to cookies
  private saveCartToCookies(cart: Cart): void {
    // Set expiry for 30 days
    const expiryDate = new Date();
    expiryDate.setDate(expiryDate.getDate() + 30);
    
    // Convert cart to JSON string
    const cartData = JSON.stringify(cart);
    
    // Save to cookie with appropriate options
    this.cookieService.set(
      this.cartKey,
      cartData,
      expiryDate,
      '/', // path
      undefined, // domain (use current domain)
      false, // secure flag (set to true in production with HTTPS)
      'Lax' // sameSite policy
    );
  }
  
  // Get current cart value
  getCart(): Cart {
    return this.cartSubject.value;
  }
  
  // Add product to cart
  addToCart(product: ProductDTO, quantity: number = 1): void {
    const currentCart = this.getCart();
    const existingItemIndex = currentCart.items.findIndex(item => item.product.id === product.id);
    
    if (existingItemIndex !== -1) {
      // Product already in cart, update quantity
      const updatedItems = [...currentCart.items];
      const newQuantity = updatedItems[existingItemIndex].quantity + quantity;
      
      // Check if we have enough stock
      if (newQuantity > product.stock) {
        console.warn(`Cannot add more items. Stock limit reached (${product.stock})`);
        updatedItems[existingItemIndex].quantity = product.stock;
      } else {
        updatedItems[existingItemIndex].quantity = newQuantity;
      }
      
      const updatedCart = { items: updatedItems };
      this.cartSubject.next(updatedCart);
      this.saveCartToCookies(updatedCart);
    } else {
      // New product, add to cart
      const quantityToAdd = Math.min(quantity, product.stock);
      const updatedCart = { 
        items: [...currentCart.items, { product, quantity: quantityToAdd }] 
      };
      this.cartSubject.next(updatedCart);
      this.saveCartToCookies(updatedCart);
    }
  }
  
  // Update quantity of an existing item
  updateQuantity(productId: string, quantity: number): void {
    const currentCart = this.getCart();
    const itemIndex = currentCart.items.findIndex(item => item.product.id === productId);
    
    if (itemIndex !== -1) {
      const product = currentCart.items[itemIndex].product;
      
      // Check if quantity is valid
      if (quantity <= 0) {
        this.removeFromCart(productId);
        return;
      }
      
      // Check stock limit
      if (quantity > product.stock) {
        console.warn(`Cannot set quantity above stock limit (${product.stock})`);
        quantity = product.stock;
      }
      
      // Update quantity
      const updatedItems = [...currentCart.items];
      updatedItems[itemIndex].quantity = quantity;
      
      const updatedCart = { items: updatedItems };
      this.cartSubject.next(updatedCart);
      this.saveCartToCookies(updatedCart);
    }
  }
  
  // Remove item from cart
  removeFromCart(productId: string): void {
    const currentCart = this.getCart();
    const updatedItems = currentCart.items.filter(item => item.product.id !== productId);
    
    const updatedCart = { items: updatedItems };
    this.cartSubject.next(updatedCart);
    this.saveCartToCookies(updatedCart);
  }
  
  // Clear the entire cart
  clearCart(): void {
    this.resetCart();
  }
  
  // Reset cart to empty state
  private resetCart(): void {
    const emptyCart = { items: [] };
    this.cartSubject.next(emptyCart);
    this.cookieService.delete(this.cartKey, '/');
  }
  
  // Get total number of items in cart
  getItemCount(): number {
    return this.getCart().items.reduce((count, item) => count + item.quantity, 0);
  }
  
  // Calculate subtotal (sum of all items)
  getSubtotal(): number {
    return this.getCart().items.reduce(
      (total, item) => total + (item.product.price * item.quantity), 
      0
    );
  }
  
  // Check if a product is already in the cart
  isInCart(productId: string): boolean {
    return this.getCart().items.some(item => item.product.id === productId);
  }
  
  // Get quantity of a specific product in cart (0 if not in cart)
  getQuantity(productId: string): number {
    const item = this.getCart().items.find(item => item.product.id === productId);
    return item ? item.quantity : 0;
  }
  
  // Proceed to checkout
  proceedToCheckout(): void {
    this.router.navigate(['/checkout']);
  }
}