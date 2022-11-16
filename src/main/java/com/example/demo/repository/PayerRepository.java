package com.example.demo.repository;

import com.example.demo.model.Payers;
import com.example.demo.model.Transactions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PayerRepository extends JpaRepository<Payers, Long> {

    @Query("select p as payer from Payers p where p.name = ?1")
    public List<Payers> findByName(String name);

    @Query("select sum(balance) as total from Payers")
    public Integer getPointsSum();

}
