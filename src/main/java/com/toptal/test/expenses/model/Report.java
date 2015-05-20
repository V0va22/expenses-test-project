package com.toptal.test.expenses.model;

import java.util.List;

/**
 * Created by VK on 19.05.2015.
 */
public class Report {
    private List<Expense> expenses;
    private Double total;

    public Report() {
    }

    public Report(List<Expense> expenses, double total) {
        this.expenses = expenses;
        this.total = total;
    }

    public List<Expense> getExpenses() {
        return expenses;
    }

    public void setExpenses(List<Expense> expenses) {
        this.expenses = expenses;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }
}
