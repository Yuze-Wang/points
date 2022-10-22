package com.example.demo.service;

import org.springframework.http.HttpStatus;

import java.sql.Timestamp;

public interface TransactionService{
    public HttpStatus saveTransaction(String payer, String pointsAsString, String timestampAsString);

}
