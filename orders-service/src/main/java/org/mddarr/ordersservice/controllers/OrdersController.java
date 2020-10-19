package org.mddarr.ordersservice.controllers;


import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class OrdersController {
    @GetMapping
    public String hello(){
        return "Hello";
    }
}
