package com.shopping_cart.service.product;

import com.shopping_cart.model.Product;
import com.shopping_cart.request.AddProductRequest;
import com.shopping_cart.request.ProductUpdateRequest;

import java.util.List;

public interface IProductService {

    // Adds a new product
    Product addProduct(AddProductRequest product);

    // Retrieves all products
    List<Product> getAllProducts();

    // Retrieves a product by its ID
    Product getProductById(Long id);

    // Deletes a product by its ID
    void deleteProductById(Long id);

    // Updates a product's details by its ID
    Product updateProduct(ProductUpdateRequest product, Long productId);

    // Retrieves products by category
    List<Product> getProductsByCategory(String category);

    // Retrieves products by brand
    List<Product> getProductsByBrand(String brand);

    // Retrieves products by category and brand
    List<Product> getProductsByCategoryAndBrand(String category, String brand);

    // Retrieves products by name
    List<Product> getProductsByName(String name);

    // Retrieves products by brand and name
    List<Product> getProductsByBrandAndName(String brand, String name);

    // Counts the number of products by brand and name
    Long countProductsByBrandAndName(String brand, String name);
}
