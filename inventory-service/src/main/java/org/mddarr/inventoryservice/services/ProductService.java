package org.mddarr.inventoryservice.services;



import org.mddarr.inventoryservice.dto.Product;
import org.mddarr.inventoryservice.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository){
        this.productRepository = productRepository;
    }

    public List<Product> fetchAllProducts() {
        return productRepository.fetchAllProducts().stream().map(ProductService::map).collect(Collectors.toList());
    }

    public List<Product> fetchAllProductsByBrand(String brand){
        return productRepository.fetchAllProductsByBrand(brand).stream().map(ProductService::map).collect(Collectors.toList());
    }

    private static Product map(ProductRepository.ProductEntity entity) {
        return new Product(entity.getBrandID(), entity.getProductName(), entity.getPrice(), entity.getImageURL());
    }

}
