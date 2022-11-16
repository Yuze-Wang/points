package com.example.demo.service.impl;

import com.example.demo.component.PayerCache;
import com.example.demo.model.Payers;
import com.example.demo.model.Transactions;
import com.example.demo.repository.PayerRepository;
import com.example.demo.repository.TransactionRepository;
import com.example.demo.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

@Service
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private PayerRepository payerRepository;

    @Autowired
    private PayerCache payerCache;

    /**
     *
     * @param payerName the payer's name
     * @param pointsAsString points in transaction
     * @param timestampAsString timestamp of the transaction
     * @return a HttpStatus represents whether the transaction been saved successfully
     */
    public HttpStatus saveTransaction(String payerName, String pointsAsString, String timestampAsString){
        int points;
        Payers payer;
        Timestamp timestamp;
        List<Transactions> transactions = null;
        try{
            points = Integer.parseInt(pointsAsString);
            timestamp = Timestamp.valueOf(timestampAsString);
        }catch (Exception e){
            return HttpStatus.BAD_REQUEST;
        }
        List<Payers> result = payerRepository.findByName(payerName);
        if(result.isEmpty()){
            //return bad request since balance could not be negative
            if(points < 0) return HttpStatus.BAD_REQUEST;
            payer = new Payers(payerName, points);
        }
        else{
            payer = result.get(0);
            //return bad request since balance could not be negative
            if(payer.getBalance() + points < 0) return HttpStatus.BAD_REQUEST;
            payer.setBalance(payer.getBalance() + points);

        }
        int templePoints = points;
        while(templePoints < 0){
            if(transactions == null) transactions = transactionRepository.getPayerTransaction(payer.getId());
            Transactions curr = transactions.get(0);
            if(curr.getAvailable() + templePoints > 0){
                curr.setAvailable(curr.getAvailable() + templePoints);
                templePoints = 0;
            }
            else{
                transactions.remove(0);
                templePoints += curr.getAvailable();
                curr.setAvailable(0);
            }
            transactionRepository.save(curr);
        }
        payerCache.modifyTotalPoint(points);
        payerRepository.save(payer);
        transactionRepository.save(new Transactions(payer.getId(), payerName, points, timestamp));
        return HttpStatus.OK;
    }
}
