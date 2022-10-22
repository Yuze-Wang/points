package com.example.demo.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.sql.Timestamp;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class Transactions {
    private @Id @GeneratedValue Long id;
    private String payer;
    private int points;
    private Timestamp timestamp;

    public Transactions(String payer, int points, Timestamp timestamp) {
        this.payer = payer;
        this.points = points;
        this.timestamp = timestamp;
    }
}
