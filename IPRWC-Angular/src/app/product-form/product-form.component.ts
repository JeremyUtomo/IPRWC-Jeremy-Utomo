import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { Router, ActivatedRoute, RouterModule } from '@angular/router';
import { ProductService } from '../service/product.service';
import { CategoryService } from '../service/category.service';
import { ProductDTO } from '../DTO/ProductDTO';
import { CategoryDTO } from '../DTO/CategoryDTO';
import { ProductRequest } from '../DTO/Request/productRequest';

@Component({
  selector: 'app-product-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterModule],
  templateUrl: './product-form.component.html'
})
export class ProductFormComponent implements OnInit {
  productForm!: FormGroup;
  isSubmitting = false;
  isEdit = false;
  productId: string | null = null;
  formError: string | null = null;
  categories: CategoryDTO[] = [];
  isLoadingCategories = true;
  
  // For file upload
  selectedFile: File | null = null;
  selectedFileName: string | null = null;
  imagePreview: string | null = null;
  
  constructor(
    private fb: FormBuilder,
    private productService: ProductService,
    private categoryService: CategoryService,
    private router: Router,
    private route: ActivatedRoute
  ) {}
  
  ngOnInit(): void {
    // Initialize the form
    this.initForm();
    
    // Load categories
    this.loadCategories();
    
    // Check if we're editing an existing product
    this.productId = this.route.snapshot.paramMap.get('id');
    
    if (this.productId) {
      this.isEdit = true;
      this.loadProduct(this.productId);
    }
  }
  
  initForm(): void {
    this.productForm = this.fb.group({
      name: ['', [Validators.required, Validators.minLength(3)]],
      description: ['', [Validators.required, Validators.minLength(10)]],
      price: [0, [Validators.required, Validators.min(0.01)]],
      stock: [0, [Validators.required, Validators.min(0)]],
      image: [null, [Validators.required]],
      categoryId: ['', Validators.required]
    });
  }
  
  loadCategories(): void {
    this.isLoadingCategories = true;
    this.categoryService.getAllCategories().subscribe({
      next: (categories) => {
        this.categories = categories;
        this.isLoadingCategories = false;
      },
      error: (error) => {
        console.error('Error loading categories:', error);
        this.formError = 'Failed to load categories. Please refresh and try again.';
        this.isLoadingCategories = false;
      }
    });
  }
  
  loadProduct(id: string): void {
    this.productService.getProductById(id).subscribe({
      next: (product) => {
        this.productForm.patchValue({
          name: product.name,
          description: product.description,
          price: product.price,
          stock: product.stock,
          categoryId: product.categoryId
        });
      },
      error: (error) => {
        console.error('Error loading product:', error);
        this.formError = 'Failed to load product details.';
      }
    });
  }
  
  // Handle file selection
  onFileSelected(event: Event): void {
    const input = event.target as HTMLInputElement;
    
    if (input.files && input.files.length > 0) {
      this.selectedFile = input.files[0];
      this.selectedFileName = this.selectedFile.name;
      
      // Update the form control value
      this.productForm.patchValue({ image: this.selectedFile });
      this.productForm.get('image')?.updateValueAndValidity();
      
      // Create a FileReader to read the file as data URL (base64)
      const reader = new FileReader();
      reader.onload = () => {
        // Get the base64 string (remove data:image/... prefix)
        const base64String = reader.result as string;
        
        // Set the preview
        this.imagePreview = base64String;
      };
      
      reader.readAsDataURL(this.selectedFile);
    }
  }
  
  onSubmit(): void {
    if (this.productForm.invalid || this.isSubmitting) {
      // Mark all fields as touched to trigger validation messages
      Object.keys(this.productForm.controls).forEach(key => {
        const control = this.productForm.get(key);
        control?.markAsTouched();
      });
      return;
    }
    
    this.isSubmitting = true;
    this.formError = null;
    
    const productData: ProductRequest = this.productForm.value;
    
    // Create FormData object to send the file and other product details
    const formData = new FormData();
    formData.append('name', productData.name);
    formData.append('description', productData.description);
    formData.append('price', productData.price.toString());
    formData.append('stock', productData.stock.toString());
    formData.append('categoryId', productData.categoryId);
    if (this.selectedFile) {
      formData.append('image', this.selectedFile);
    }
    
    // Choose between add or update
    const saveOperation = this.isEdit && this.productId
      ? this.productService.updateProduct(formData, this.productId)
      : this.productService.addProduct(formData);
      
    saveOperation.subscribe({
      next: (savedProduct) => {
        console.log('Product saved:', savedProduct);
        this.router.navigate(['/admin-overview']);
      },
      error: (error) => {
        console.error('Error saving product:', error);
        this.formError = 'Failed to save product. Please try again.';
        this.isSubmitting = false;
      }
    });
  }
}