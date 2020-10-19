package org.mddarr.paymentsservice.controllers;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PaymentsController {

    @GetMapping("/hello")
    public String hello(){
        return "Hello";
    }

//    @PostMapping(path="/sendMoney/{sourceAccountID/{targetAccountId}/{amount}")
//    ResponseEntity sendMoney(@PathVariable("sourceAccountID") Long sourceAccountId, @PathVariable("targetAccountID") Long targetAccountID, @PathVariable("amount") Integer amount){
//
//    }
}
