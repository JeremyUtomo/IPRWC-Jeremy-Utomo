import { ProductDTO } from "./ProductDTO";

export interface OrderDTO {
    id: string;
    userId: string;
    orderDate: string;
    total: number;
    products: ProductDTO[];
}