package com.example.demo.component;


import com.example.demo.repository.PayerRepository;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;



@Component
public class PayerCache {
    @Autowired
    private PayerRepository payerRepository;

    @Getter
    @Setter
    private int totalPoint;

    @PostConstruct
    public void init(){
        Integer sumPoint = payerRepository.getPointsSum();
        totalPoint = sumPoint == null ? 0 : sumPoint;
    }

    public void modifyTotalPoint(int change){
        this.totalPoint += change;
    }

}
