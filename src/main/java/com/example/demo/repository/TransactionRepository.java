package com.example.demo.repository;

import com.example.demo.model.Transactions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transactions, Long> {
    @Query(nativeQuery = true, value = "SELECT * From Transactions WHERE available > 0 ORDER BY timestamp LIMIT 100")
    public List<Transactions> getHundredOldestTransaction();

    @Query("select t as Transactions from Transactions t where t.payerId = ?1")
    public List<Transactions> getPayerTransaction(Long id);
}
