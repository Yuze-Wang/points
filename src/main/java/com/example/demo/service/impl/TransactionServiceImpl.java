package com.example.demo.service.impl;

import com.example.demo.model.Transactions;
import com.example.demo.repository.TransactionRepository;
import com.example.demo.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

@Service
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;
    public HttpStatus saveTransaction(String payer, String pointsAsString, String timestampAsString){
        int points;
        Timestamp timestamp;
        try{
            points = Integer.parseInt(pointsAsString);
            timestamp = Timestamp.valueOf(timestampAsString);
        }catch (Exception e){
            return HttpStatus.BAD_REQUEST;
        }
        transactionRepository.save(new Transactions(payer, points, timestamp));
        return HttpStatus.OK;
    }
}
