import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { ProductDTO } from '../DTO/ProductDTO';
import { ProductService } from '../service/product.service';
import { CategoryService } from '../service/category.service';
import { CartService } from '../service/cart.service';
import { CategoryDTO } from '../DTO/CategoryDTO';

@Component({
  selector: 'app-products',
  templateUrl: './products.component.html',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule]
})
export class ProductsComponent implements OnInit {
  // Products
  products: ProductDTO[] = [];
  filteredProducts: ProductDTO[] = [];
  
  // Categories
  categories: CategoryDTO[] = [];
  
  // Filtering and Sorting
  searchTerm: string = '';
  selectedCategory: string = '';
  sortOption: string = 'nameAsc';
  
  // Pagination
  itemsPerPage: number = 12;
  currentPage: number = 1;
  totalPages: number = 1;
  
  // UI State
  loading: boolean = true;
  error: boolean = false;
  
  constructor(
    private productService: ProductService,
    private categoryService: CategoryService,
    private cartService: CartService
  ) {}
  
  ngOnInit(): void {
    this.loading = true;
    this.error = false;
    
    // Load categories
    this.categoryService.getAllCategories().subscribe({
      next: (categories) => {
        this.categories = categories;
      },
      error: (err) => {
        console.error('Error loading categories:', err);
      }
    });
    
    // Load products
    this.productService.getAllProducts().subscribe({
      next: (products) => {
        this.products = products;
        this.applyFilters();
        this.loading = false;
      },
      error: (err) => {
        console.error('Error loading products:', err);
        this.error = true;
        this.loading = false;
      }
    });
  }
  
  // Apply filters, sorting, and pagination
  applyFilters(): void {
    let result = [...this.products];
    
    // Apply search filter
    if (this.searchTerm.trim() !== '') {
      const term = this.searchTerm.toLowerCase();
      result = result.filter(product => 
        product.name.toLowerCase().includes(term) || 
        product.description.toLowerCase().includes(term)
      );
    }
    
    // Apply category filter
    if (this.selectedCategory) {
      result = result.filter(product => product.categoryId === this.selectedCategory);
    }
    
    // Apply sorting
    switch (this.sortOption) {
      case 'nameAsc':
        result.sort((a, b) => a.name.localeCompare(b.name));
        break;
      case 'nameDesc':
        result.sort((a, b) => b.name.localeCompare(a.name));
        break;
      case 'priceAsc':
        result.sort((a, b) => a.price - b.price);
        break;
      case 'priceDesc':
        result.sort((a, b) => b.price - a.price);
        break;
    }
    
    // Update total pages
    this.totalPages = Math.max(1, Math.ceil(result.length / this.itemsPerPage));
    
    // If current page is now invalid, reset to page 1
    if (this.currentPage > this.totalPages) {
      this.currentPage = 1;
    }
    
    // Apply pagination
    const startIndex = (this.currentPage - 1) * this.itemsPerPage;
    this.filteredProducts = result.slice(startIndex, startIndex + this.itemsPerPage);
  }
  
  // Change page
  changePage(page: number): void {
    this.currentPage = page;
    this.applyFilters();
  }
  
  // Get array of page numbers for pagination
  getPageNumbers(): number[] {
    const pages: number[] = [];
    const maxDisplayedPages = 5;
    
    let startPage = Math.max(1, this.currentPage - Math.floor(maxDisplayedPages / 2));
    let endPage = Math.min(this.totalPages, startPage + maxDisplayedPages - 1);
    
    if (endPage - startPage + 1 < maxDisplayedPages) {
      startPage = Math.max(1, endPage - maxDisplayedPages + 1);
    }
    
    for (let i = startPage; i <= endPage; i++) {
      pages.push(i);
    }
    
    return pages;
  }
  
  // Get category name by ID
  getCategoryName(categoryId: string): string {
    const category = this.categories.find(c => c.id === categoryId);
    return category ? category.name : 'Unknown';
  }
  
  // Add product to cart
  addToCart(product: ProductDTO): void {
    this.cartService.addToCart(product, 1);
    // You could add a toast notification here
  }

  handleImageError(event: Event): void {
    const imgElement = event.target as HTMLImageElement;
    if (imgElement && imgElement.src) {
      imgElement.src = 'assets/placeholder-image.jpg';
    }
  }
}