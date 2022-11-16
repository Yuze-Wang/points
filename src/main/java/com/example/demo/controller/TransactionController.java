package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import com.example.demo.service.TransactionService;

@RestController
public class TransactionController {
    @Autowired
    TransactionService transactionService;

    @PostMapping(value = "/transaction")
    public HttpStatus newTransaction(
            @RequestParam(name = "payer") String payer,
            @RequestParam(name = "points") String points,
            @RequestParam(name = "timestamp") String timestamp

    ){
        return transactionService.saveTransaction(payer, points, timestamp);
    }

}
