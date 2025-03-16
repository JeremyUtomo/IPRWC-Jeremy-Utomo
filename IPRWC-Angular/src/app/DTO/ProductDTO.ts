export interface ProductDTO {
    id: string;
    name: string;
    description: string;
    price: number;
    stock: number;
    image: File | string;
    categoryId: string;
}
