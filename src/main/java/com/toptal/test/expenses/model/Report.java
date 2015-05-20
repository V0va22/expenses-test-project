package com.toptal.test.expenses.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

public class Report {
    private List<DayReport> dayReports = new ArrayList<>();
    private BigDecimal total;

    public Report() {
    }

    public Report(Map<String, List<Expense>> groupedExpences, BigDecimal total) {
        TreeMap<String, List<Expense>> sortedExpenses = new TreeMap<>((o1, o2) ->  o1.compareTo(o2));
        sortedExpenses.putAll(groupedExpences);
        sortedExpenses.forEach((k,v) -> dayReports.add(new DayReport(v)));
        this.total = total;
    }

    public List<DayReport> getDayReports() {
        return dayReports;
    }

    public void setDayReports(List<DayReport> dayReports) {
        this.dayReports = dayReports;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public static class DayReport {
        private List<Expense> expenses;
        private BigDecimal average;

        public DayReport() {
        }

        public DayReport(List<Expense> expenses) {
            BigDecimal total = new BigDecimal(0);
            for (Expense expense: expenses){
                total = total.add(expense.getAmount());
            }
            this.average = total.divide(new BigDecimal(expenses.size()), 2, RoundingMode.HALF_UP);
            this.expenses = expenses;
        }

        public List<Expense> getExpenses() {
            return expenses;
        }

        public void setExpenses(List<Expense> expenses) {
            this.expenses = expenses;
        }

        public BigDecimal getAverage() {
            return average;
        }

        public void setAverage(BigDecimal average) {
            this.average = average;
        }
    }
}
