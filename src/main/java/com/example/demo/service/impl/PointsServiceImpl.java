package com.example.demo.service.impl;

import com.example.demo.model.Payers;
import com.example.demo.model.Transactions;
import com.example.demo.repository.TransactionRepository;
import com.example.demo.service.PointsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class PointsServiceImpl implements PointsService {

    @Autowired
    private TransactionRepository transactionRepository;

    public List<Payers> getPayersBalance(){
        return transactionRepository.getPointsGroupByPayer();
    }

    public HashMap<String, Integer> spendPoints(String pointsAsString){
        int spentPoints;
        HashMap<String, Integer> result = new HashMap<>();
        try{
            spentPoints = Integer.parseInt(pointsAsString);
        }catch (Exception e){
            return null;
        }
        //check whether the total points in db is bigger than the points spent;
        if(checkTotalPoints(spentPoints)) return null;
        while(spentPoints > 0){
            Transactions curr = transactionRepository.findAll(Sort.by("timestamp")).get(0);
            Integer points = result.getOrDefault(curr.getPayer(), 0);
            if(curr.getPoints() > spentPoints){
                curr.setPoints(curr.getPoints() - spentPoints);
                transactionRepository.save(curr);
                result.put(curr.getPayer(), points - spentPoints);
                break;
            }
            else{
                result.put(curr.getPayer(), points - curr.getPoints());
                spentPoints -= curr.getPoints();
                transactionRepository.delete(curr);
            }
        }
        return result;
    }

    private boolean checkTotalPoints(Integer points){
        Integer result = transactionRepository.getPointsSum();
        return result == null || result < points;
    }
}
