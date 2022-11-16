package com.example.demo.controller;

import com.example.demo.service.PointsService;
import com.example.demo.service.TransactionService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestTransactionController {

    @Autowired
    TransactionController transactionController;

    TransactionService transactionService;

    @Before
    public void setUp(){
        transactionService = Mockito.mock(TransactionService.class);
        ReflectionTestUtils.setField(transactionController, "transactionService", transactionService);
    }

    @After
    public void tearDown(){

    }

    /*
        PreConditions: No data in db
        Execution Step: Call newTransaction("DANNON", "300", "2022-10-31T10:00:00Z"}
        PostConditions: Return value is Https.OK
    */
    @Test
    public void TestNewTransactionWithPositiveTransaction(){
        Mockito.when(transactionService.saveTransaction("DANNON", "300", "2022-10-31T10:00:00Z")).thenReturn(HttpStatus.OK);
        assertEquals(transactionController.newTransaction("DANNON", "300", "2022-10-31T10:00:00Z"), HttpStatus.OK);
    }

    /*
        PreConditions: No data in db
        Execution Step: Call newTransaction("DANNON", "-300", "2022-10-31T10:00:00Z"}
        PostConditions: Return value is HttpStatus.BAD_REQUEST
    */
    @Test
    public void TestNewTransactionWithNegativeTransaction(){
        Mockito.when(transactionService.saveTransaction("DANNON", "-300", "2022-10-31T10:00:00Z")).thenReturn(HttpStatus.BAD_REQUEST);
        assertEquals(transactionController.newTransaction("DANNON", "-300", "2022-10-31T10:00:00Z"), HttpStatus.BAD_REQUEST);
    }

    /*
        PreConditions: No data in db
        Execution Step: Call newTransaction("DANNON", "300", "2022-10-31T10:00:00Z"}
                        Call newTransaction("DANNON", "-400", "2022-10-31T10:00:00Z"}
        PostConditions: Return value is Https.Ok first and HttpStatus.BAD_REQUEST second
    */
    @Test
    public void TestNewTransactionWithPositiveThenNegativeTransaction(){
        Mockito.when(transactionService.saveTransaction("DANNON", "300", "2022-10-31T10:00:00Z")).thenReturn(HttpStatus.OK);
        Mockito.when(transactionService.saveTransaction("DANNON", "-400", "2022-10-31T10:00:00Z")).thenReturn(HttpStatus.BAD_REQUEST);
        assertEquals(transactionController.newTransaction("DANNON", "300", "2022-10-31T10:00:00Z"), HttpStatus.OK);
        Mockito.when(transactionService.saveTransaction("DANNON", "-400", "2022-10-31T10:00:00Z")).thenReturn(HttpStatus.BAD_REQUEST);
    }
}
