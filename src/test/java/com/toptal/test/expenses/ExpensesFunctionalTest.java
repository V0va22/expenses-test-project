package com.toptal.test.expenses;


import com.toptal.test.expenses.model.Expense;
import com.toptal.test.expenses.model.Report;
import com.toptal.test.expenses.repository.ExpensesRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;


public class ExpensesFunctionalTest extends AbstractFunctionalTest {
    public static final long WEEK_IN_MS = 168000l;
    @Autowired
    private ExpensesRepository expensesRepository;

    @Before
    public void before(){
        generateExpenses(10);
    }


    @Test
    public void testExpenses() throws Exception {
        ResponseEntity<ArrayList> expenses = send("expense", HttpMethod.GET, ArrayList.class, ROLE.USER);
        Assert.assertEquals(HttpStatus.OK, expenses.getStatusCode());
        Assert.assertEquals(10, expenses.getBody().size());
    }
    @Test
    public void testAddExpense() throws Exception {
        Expense expense = getExpense();
        expense.setDescription("description for add");
        ResponseEntity<String> expenses = send("expense", HttpMethod.POST, expense, String.class, ROLE.USER);
        Assert.assertEquals(HttpStatus.OK, expenses.getStatusCode());
        Assert.assertEquals(1, expensesRepository.findByDescription("description for add").size());
    }

    @Test
    public void testDeleteExpense() throws Exception {
        Expense expense = getExpense();
        expense.setDescription("description for delete");
        expensesRepository.save(expense);
        String expenseId = expensesRepository.findByDescription("description for delete").get(0).getId();
        ResponseEntity<String> expenses = send("expense/" + expenseId, HttpMethod.DELETE, String.class, ROLE.USER);
        Assert.assertEquals(HttpStatus.OK, expenses.getStatusCode());
        Assert.assertEquals(0, expensesRepository.findByDescription("description for delete").size());
    }

    @Test
    public void testReport() throws Exception {
        Expense expenseTooOld = getExpense();
        expenseTooOld.setDate(new Date(new Date().getTime() - WEEK_IN_MS * 2));
        expensesRepository.save(expenseTooOld);
        Expense expenseTooNew = getExpense();
        expenseTooOld.setDate(new Date(new Date().getTime() + WEEK_IN_MS));
        expensesRepository.save(expenseTooNew);
        long start = new Date().getTime() - WEEK_IN_MS;
        long end = new Date().getTime();
        ResponseEntity<Report> report = send("report?start=" + start + "&end=" + end, HttpMethod.GET, Report.class, ROLE.USER);
        Assert.assertEquals(HttpStatus.OK, report.getStatusCode());
        Assert.assertEquals(1, report.getBody().getDayReports().size());
    }

    private void generateExpenses(int count){
        expensesRepository.delete(expensesRepository.findAll());
        for (int i =0; i< count;i++){
            Expense expense = getExpense();
            expensesRepository.save(expense);
        }
    }

    private Expense getExpense() {
        Expense expense = new Expense();
        expense.setUserId("user");
        expense.setAmount(new BigDecimal(new Random().nextInt(10000)/ 100d));
        expense.setCategory(randomString());
        expense.setDescription(randomString());
        expense.setDate(new Date());
        return expense;
    }
}