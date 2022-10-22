package com.example.demo.service;

import com.example.demo.model.Payers;

import java.util.HashMap;
import java.util.List;


public interface PointsService {
    public List<Payers> getPayersBalance();
    public HashMap<String, Integer> spendPoints(String valueAsString);
}
