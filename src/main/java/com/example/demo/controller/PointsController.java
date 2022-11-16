package com.example.demo.controller;

import com.example.demo.model.Payers;
import com.example.demo.service.PointsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;

@RestController
public class PointsController {

    @Autowired
    PointsService pointsService;

    @GetMapping(value = "/balance")
    public ResponseEntity<List<Payers>> payerBalance(){
        return new ResponseEntity<>(pointsService.getPayersBalance(), HttpStatus.OK);
    }

    @PostMapping(value = "/spend")
    public ResponseEntity<HashMap<String, Integer>> spend(
            @RequestParam("points") String points
    ){
        HashMap<String, Integer> result = pointsService.spendPoints(points);
        return result == null ? new ResponseEntity<>(HttpStatus.BAD_REQUEST) : new ResponseEntity<>(result, HttpStatus.OK);
    }
}
