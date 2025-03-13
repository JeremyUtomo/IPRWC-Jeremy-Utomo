import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { ProductService } from '../service/product.service';
import { ProductDTO } from '../DTO/ProductDTO';

@Component({
  selector: 'app-admin-overview',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './admin-overview.component.html'
})
export class AdminOverviewComponent implements OnInit {
  loading: boolean = true;
  productCount: number = 0;
  outOfStockCount: number = 0;
  products: ProductDTO[] = [];
  
  constructor(private productService: ProductService) {}
  
  ngOnInit(): void {
    this.loadProducts();
  }
  
  loadProducts(): void {
    this.loading = true;
    
    this.productService.getAllProducts().subscribe({
      next: (products) => {
        this.loading = false;
        this.products = products;
        this.productCount = products.length;
        this.outOfStockCount = products.filter(p => p.stock === 0).length;
      },
      error: (error) => {
        console.error('Error loading products:', error);
        this.loading = false;
      }
    });
  }
  
  confirmDelete(product: ProductDTO): void {
      this.productService.deleteProduct(product.id).subscribe({
        next: () => this.loadProducts(),
        error: (error) => console.error('Error deleting product:', error)
      });
  }
}