package org.mddarr.inventoryservice.controllers;


import org.mddarr.inventoryservice.dto.Product;
import org.mddarr.inventoryservice.services.ProductService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("products")
@CrossOrigin
public class ProductsController {

    private final ProductService productService;

    public ProductsController(ProductService productService){
        this.productService = productService;
    }

    @GetMapping
    public List<Product> allProducts() {
        return productService.fetchAllProducts();
    }

    @GetMapping("/{brandID}")
    public List<Product> allProducts(@PathVariable String brandID) {
        return productService.fetchAllProductsByBrand(brandID);
    }

//    @GetMapping
//    public List<Product> allBrandsProducts(){
//
//    }

}
