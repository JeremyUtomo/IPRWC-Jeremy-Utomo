import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { OrderService } from '../service/order.service';
import { OrderDTO } from '../DTO/OrderDTO';

@Component({
  selector: 'app-orders',
  standalone: true,
  imports: [CommonModule, RouterModule, FormsModule],
  templateUrl: './orders.component.html',
})
export class OrdersComponent implements OnInit {
  orders: OrderDTO[] = [];
  filteredOrders: OrderDTO[] = [];
  loading: boolean = true;
  error: boolean = false;
  expandedOrderId: string | null = null;
  dateFilter: string = 'all';

  constructor(
    private orderService: OrderService
  ) {}

  ngOnInit(): void {
    this.loadOrders();
  }

  loadOrders(): void {
    this.loading = true;
    this.error = false;
  
    this.orderService.getOrderByUserId().subscribe({
      next: (response) => {
        console.log('Orders loaded:', response);
        // Filter out orders with no items
        this.orders = response.filter(order => order.products && order.products.length > 0);
        this.applyFilters();
        this.loading = false;
      },
      error: (error) => {
        console.error('Error loading orders:', error);
        this.error = true;
        this.loading = false;
      }
    });
  }

  toggleOrderDetails(orderId: string): void {
    this.expandedOrderId = this.expandedOrderId === orderId ? null : orderId;
  }

  getOrderStatus(order: OrderDTO): string {
    // Provide a default status since it's not in the DTO
    return 'Processing';
  }
  
  getStatusBadge(order: OrderDTO): string {
    const status = this.getOrderStatus(order);
    return status;
  }

  applyFilters(): void {
    // First filter out orders with no items
    let filtered = this.orders.filter(order => {
      return order.products && order.products.length > 0;
    });
    
    // Apply date filter
    if (this.dateFilter !== 'all') {
      const days = parseInt(this.dateFilter);
      const cutoffDate = new Date();
      cutoffDate.setDate(cutoffDate.getDate() - days);
      
      filtered = filtered.filter(order => {
        const orderDate = new Date(order.orderDate);
        return orderDate >= cutoffDate;
      });
    }
    
    // Sort by date (newest first)
    filtered.sort((a, b) => {
      return new Date(b.orderDate).getTime() - new Date(a.orderDate).getTime();
    });
    
    this.filteredOrders = filtered;
  }
  
  // Helper method to calculate number of items in an order
  getTotalItems(order: OrderDTO): number {
    return order.products?.length || 0;
  }

  handleImageError(event: Event): void {
    const img = event.target as HTMLImageElement;
    if (img) {
      img.src = 'assets/placeholder-image.jpg';
    }
  }
}