package org.mddarr.ordersservice.controllers;


import lombok.RequiredArgsConstructor;
import org.mddarr.ordersservice.dto.Order;
import org.mddarr.ordersservice.dto.OrderRequest;
import org.mddarr.ordersservice.services.OrdersService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("orders")
public class OrdersController {

    private final OrdersService ordersService;

    @GetMapping("/hello")
    public String hello(@RequestParam(name="name", defaultValue = "World") String name){
        return String.format("Hello, %s", name);
    }
    @GetMapping
    public List<Order> allOrders() {
        return ordersService.getAll();
    }

    @GetMapping("/{order}/{creationDate}")
    public ResponseEntity<Order> getOrder(@PathVariable String order, @PathVariable long creationDate){
        return ResponseEntity.of(ordersService.get(order, creationDate));
    }

    @GetMapping("/customerOrders/{customerID}")
    public List<Order> getCustomerOrders(@PathVariable String customerID){
        return ordersService.fetchCustomersOrders(customerID);
    }

    @GetMapping("customerOrders/{customerID}/{creationDate}")
    public List<Order> getCustomerOrders(@PathVariable String customerID, @PathVariable String creationDate){
        return ordersService.fetchCustomersOrders(customerID, creationDate);
    }


    @DeleteMapping("/{order}/{creationDate}")
    public ResponseEntity<Void> delete(@PathVariable String order,@PathVariable long creationDate){
        ordersService.delete(order, creationDate);
        return ResponseEntity.accepted().build();
    }

    @PutMapping
    public ResponseEntity<Order> postOrder(@RequestBody OrderRequest order){
        Order resp = ordersService.createOrder(order);
        HttpHeaders headers = new HttpHeaders();
        return ResponseEntity.accepted().headers(headers).body(resp);
    }


}
