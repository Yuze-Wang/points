package com.example.demo.repository;

import com.example.demo.model.Payers;
import com.example.demo.model.Transactions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;

public interface TransactionRepository extends JpaRepository<Transactions, Long> {

    @Query(value = "select payer as payer, sum(points) as points from Transactions GROUP BY payer")
    List<Payers> getPointsGroupByPayer();


}
