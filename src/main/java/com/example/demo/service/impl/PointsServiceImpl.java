package com.example.demo.service.impl;

import com.example.demo.component.PayerCache;
import com.example.demo.component.TransactionCache;
import com.example.demo.model.Payers;
import com.example.demo.model.Transactions;
import com.example.demo.repository.PayerRepository;
import com.example.demo.repository.TransactionRepository;
import com.example.demo.service.PointsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;


@Service
public class PointsServiceImpl implements PointsService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private PayerRepository payerRepository;
    
    @Autowired
    private TransactionCache transactionCache;

    @Autowired
    private PayerCache payerCache;

    /**
     *
     * @return the list of payers in db
     */
    public List<Payers> getPayersBalance(){
        return payerRepository.findAll();
    }

    /**
     * the class use to spend points.
     *
     * @param pointsAsString points want to spend
     * @return a hash map: {key: payer's name, value: spend points}
     */
    public HashMap<String, Integer> spendPoints(String pointsAsString){
        int spentPoints;
        Payers payer;
        //the return value: {key: payer's name, value: spend points}
        HashMap<String, Integer> result = new HashMap<>();
        try{
            spentPoints = Integer.parseInt(pointsAsString);
        }catch (Exception e){
            return null;
        }
        if(!checkTotalPoints(spentPoints)) return null;
        while(spentPoints > 0){
            //get hundred oldest transaction from db
            if(transactionCache.getCache().isEmpty()){
                transactionCache.getCache().addAll(transactionRepository.getHundredOldestTransaction());
            }
            //get the oldest transaction
            Transactions curr = transactionCache.getCache().peek();
            //get current points from the result
            Integer points = result.getOrDefault(curr.getPayerName(), 0);
            //get payer for current transaction
            payer = payerRepository.findByName(curr.getPayerName()).get(0);
            //if points in current transaction bigger than points we want to spend
            if(curr.getAvailable() > spentPoints){
                //modify the points left in current transaction
                curr.setAvailable(curr.getAvailable() - spentPoints);
                //modify payer's balance
                payer.setBalance(payer.getBalance() - spentPoints);
                //modify total points, so we do not need to calculate from db each time
                payerCache.modifyTotalPoint(-spentPoints);
                //update return value
                result.put(curr.getPayerName(), points - spentPoints);
                //modify points we want to spend to 0
                spentPoints = 0;

            }
            else{
                //update points we want to spend
                spentPoints -= curr.getAvailable();
                //save the transaction back to db
                transactionRepository.save(curr);
                //delete the transaction from cache
                transactionCache.getCache().poll();
                //modify payer's balance
                payer.setBalance(payer.getBalance() - curr.getAvailable());
                //modify total points, so we do not need to calculate from db each time
                payerCache.modifyTotalPoint(-curr.getAvailable());
                //update return value
                result.put(curr.getPayerName(), points - curr.getAvailable());
                //modify the points left in current transaction
                curr.setAvailable(0);
            }
            payerRepository.save(payer);
        }
        return result;
    }

    /**
     *
     * @param requiredPoints points required
     * @return a boolean value. if there is enough points to be spent, return true. If not, return false
     */
    private boolean checkTotalPoints(Integer requiredPoints){
        return payerCache.getTotalPoint() >= requiredPoints;
    }
}
