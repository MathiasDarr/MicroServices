package org.mddarr.inventoryservice.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Product {
    private String brandID;
    private String productName;
    private Double price;
    private String imageURL;
}
