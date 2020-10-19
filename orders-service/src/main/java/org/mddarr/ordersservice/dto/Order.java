package org.mddarr.ordersservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Order {
    String orderID;
    long creationDate;
    String customerID;
    List<String> productBrands;
    List<String> productsNames;
    List<Long> quantities;
    List<Double> prices;
    String orderState;
}
