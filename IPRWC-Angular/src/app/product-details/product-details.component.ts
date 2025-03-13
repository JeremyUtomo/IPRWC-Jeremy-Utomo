import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { Location } from '@angular/common';
import { ProductDTO } from '../DTO/ProductDTO';
import { ProductService } from '../service/product.service';
import { CategoryService } from '../service/category.service';
import { CategoryDTO } from '../DTO/CategoryDTO';
import { CartService } from '../service/cart.service';

@Component({
  selector: 'app-product-details',
  templateUrl: './product-details.component.html',
  standalone: true,
  imports: [CommonModule, FormsModule]
})
export class ProductDetailsComponent implements OnInit {
  product: ProductDTO | null = null;
  categoryName: string = '';
  quantity: number = 1;
  loading: boolean = true;
  error: boolean = false;

  constructor(
    private route: ActivatedRoute,
    private productService: ProductService,
    private categoryService: CategoryService,
    private cartService: CartService,
    private location: Location
  ) {}

  ngOnInit(): void {
    this.loading = true;
    this.error = false;
    
    // Get product ID from route parameters
    this.route.paramMap.subscribe(params => {
      const productName = params.get('name');
      if (productName) {
        this.loadProductDetails(productName);
      } else {
        this.error = true;
        this.loading = false;
      }
    });
  }

  loadProductDetails(productName: string): void {
    this.productService.getProductByName(productName).subscribe({
      next: (product) => {
        this.product = product;
        this.loadCategoryName(product.categoryId);
        this.loading = false;
      },
      error: (err) => {
        console.error('Error loading product:', err);
        this.error = true;
        this.loading = false;
      }
    });
  }

  loadCategoryName(categoryId: string): void {
    this.categoryService.getCategoryById(categoryId).subscribe({
      next: (category: CategoryDTO) => {
        this.categoryName = category.name;
      },
      error: (error) => {
        console.error('Error loading category:', error);
        this.categoryName = 'Unknown Category';
      }
    });
  }

  incrementQuantity(): void {
    if (this.product && this.quantity < this.product.stock) {
      this.quantity++;
    }
  }

  decrementQuantity(): void {
    if (this.quantity > 1) {
      this.quantity--;
    }
  }

  addToCart(): void {
    if (this.product) {
      this.cartService.addToCart(this.product, this.quantity);
      // Show toast or notification
    }
  }

  goBack(): void {
    this.location.back();
  }

  handleImageError(event: Event): void {
    const img = event.target as HTMLImageElement;
    if (img) {
      img.src = 'assets/placeholder-image.jpg';
    }
  }
}