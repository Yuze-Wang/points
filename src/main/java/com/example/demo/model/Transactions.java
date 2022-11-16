package com.example.demo.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.sql.Timestamp;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class Transactions {
    private @Id @GeneratedValue(strategy = GenerationType.IDENTITY) Long id;
    private Long payerId;

    private String payerName;
    private int points;
    private Timestamp timestamp;

    private int available;

    public Transactions(Long payerId, String payerName, int points, Timestamp timestamp) {
        this.payerId = payerId;
        this.payerName = payerName;
        this.points = points;
        this.timestamp = timestamp;
        this.available = points;
    }

}
