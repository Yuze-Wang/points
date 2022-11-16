package com.example.demo.service;

import com.example.demo.model.Payers;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@RunWith(SpringRunner.class)
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class TestService {

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private PointsService pointsService;

    private Payers payer1;

    private Payers payer2;

    private Payers payer3;

    @Before
    public void setUp(){

    }

    @After
    public void tearDown(){

    }

    /*
        PreConditions: No data in db
        Execution Step: 1. Call transactionService.saveTransaction("DANNON", "300", "2022-10-31T10:00:00Z")
                        2. Call pointsService.getPayersBalance()
        PostConditions: first call should return Https.Ok
                        second call should return An array with 1 payer with id: 1, name: "DANNON", and balance: 300 in it
    */
    @Test
    public void TestSaveAndReadPositiveTransaction(){
        payer1 = Payers.builder().id(1L).name("DANNON").balance(300).build();
        assertEquals(HttpStatus.OK, transactionService.saveTransaction("DANNON", "300", "2018-09-01 09:01:15.0"));
        assertEquals(new ArrayList<Payers>(){{add(payer1);}}, pointsService.getPayersBalance());
    }

    /*
        PreConditions: No data in db
        Execution Step: 1. Call transactionService.saveTransaction("DANNON", "300", "2022-10-31T10:00:00Z")
                        2. Call pointsService.getPayersBalance()
        PostConditions: first call should return Https.Ok
                        second call should return An array with 1 payer with id: 1, name: "DANNON", and balance: 300 in it
    */
    @Test
    public void TestSaveNegativeTransaction(){
        assertEquals(HttpStatus.BAD_REQUEST, transactionService.saveTransaction("DANNON", "-300", "2018-09-01 09:01:15.0"));
    }

    /*
        PreConditions: No data in db
        Execution Step: 1. Call transactionService.saveTransaction("DANNON", "300", "2022-10-31T10:00:00Z")
                        2. Call pointsService.getPayersBalance()
        PostConditions: first call should return Https.Ok
                        second call should return An array with 1 payer with id: 1, name: "DANNON", and balance: 300 in it
    */
    @Test
    public void TestSaveAndReadPositiveThenNegativeTransaction(){
        payer1 = Payers.builder().id(1L).name("DANNON").balance(100).build();
        assertEquals(HttpStatus.OK, transactionService.saveTransaction("DANNON", "300", "2018-09-01 09:01:15.0"));
        assertEquals(HttpStatus.OK, transactionService.saveTransaction("DANNON", "-200", "2018-09-01 09:01:15.0"));
        assertEquals(new ArrayList<Payers>(){{add(payer1);}}, pointsService.getPayersBalance());
    }

    /*
        PreConditions: There are 1 transaction in db: payer_name is "DANNON", points is 300, and timestamp is "2018-09-01 09:01:15.0"
        Execution Step: 1. Call transactionService.pointsService.spendPoints("300")
                        2. Call pointsService.getPayersBalance()
        PostConditions: first call should return a Hashmap with ("DANNON", -300) in it
                        second call should return An array with 1 payer with id: 1, name: "DANNON", and balance: 300 in it
    */
    @Test
    public void TestSpendPointsWithSinglePayer(){
        payer1 = Payers.builder().id(1L).name("DANNON").balance(0).build();
        assertEquals(HttpStatus.OK, transactionService.saveTransaction("DANNON", "300", "2018-09-01 09:01:15.0"));
        assertEquals(new HashMap<String, Integer>(){{put("DANNON", -300);}}, pointsService.spendPoints("300"));
        assertEquals(new ArrayList<Payers>(){{add(payer1);}}, pointsService.getPayersBalance());
    }

    /*
        PreConditions: There are 5 transactions in db: 1. payer_name is "DANNON", points is 300, and timestamp is "2018-09-01 09:01:15.0"
                                                       2. payer_name is "DANNON", points is 300, and timestamp is "2018-09-01 09:01:15.0"
                                                       3. payer_name is "DANNON", points is 300, and timestamp is "2018-09-01 09:01:15.0"
        Execution Step: 1. Call transactionService.pointsService.spendPoints("300")
                        2. Call pointsService.getPayersBalance()
        PostConditions: first call should return a Hashmap with ("DANNON", -300) in it
                        second call should return An array with 1 payer with id: 1, name: "DANNON", and balance: 300 in it
    */
    @Test
    public void TestSpendPointsWithMultiplePayer(){
        payer1 = Payers.builder().id(1L).name("DANNON").balance(1000).build();
        payer2 = Payers.builder().id(2L).name("UNILEVER").balance(0).build();
        payer3 = Payers.builder().id(3L).name("MILLER COORS").balance(5300).build();
        assertEquals(HttpStatus.OK, transactionService.saveTransaction("DANNON", "300", "2022-10-31 10:00:00"));
        assertEquals(HttpStatus.OK, transactionService.saveTransaction("UNILEVER", "200", "2022-10-31 11:00:00"));
        assertEquals(HttpStatus.OK, transactionService.saveTransaction("DANNON", "-200", "2022-10-31 15:00:00"));
        assertEquals(HttpStatus.OK, transactionService.saveTransaction("MILLER COORS", "10000", "2022-11-01 14:00:00"));
        assertEquals(HttpStatus.OK, transactionService.saveTransaction("DANNON", "1000", "2022-11-02 14:00:00"));
        assertEquals(new HashMap<String, Integer>(){{put("UNILEVER", -200); put("MILLER COORS", -4700); put("DANNON", -100);}}, pointsService.spendPoints("5000"));
        assertEquals(new ArrayList<Payers>(){{add(payer1); add(payer2); add(payer3); }}, pointsService.getPayersBalance());
    }

    @Test
    public void TestSpendPointsWithInvalidInput(){
        assertNull(pointsService.spendPoints("5000"));
        assertEquals(new ArrayList<Payers>(), pointsService.getPayersBalance());
    }
}
