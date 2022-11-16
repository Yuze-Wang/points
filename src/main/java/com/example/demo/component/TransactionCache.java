package com.example.demo.component;

import com.example.demo.model.Transactions;
import com.example.demo.repository.TransactionRepository;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Comparator;
import java.util.PriorityQueue;

@Component
public class TransactionCache {

    @Autowired
    private TransactionRepository transactionRepository;

    @Getter
    @Setter
    private PriorityQueue<Transactions> cache;

    @PostConstruct
    public void init(){
        cache = new PriorityQueue<>(new TransactionComparator());
        cache.addAll(transactionRepository.getHundredOldestTransaction());
    }

    static class TransactionComparator implements Comparator<Transactions>{
        @Override
        public int compare(Transactions transaction1, Transactions transaction2){
            if(transaction1.getTimestamp().before(transaction2.getTimestamp())){
                return -1;
            }
            else if(transaction1.getTimestamp().after(transaction2.getTimestamp())){
                return 1;
            }
            else{
                return 0;
            }
        }
    }

}
