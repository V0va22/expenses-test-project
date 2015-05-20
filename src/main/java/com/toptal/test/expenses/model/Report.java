package com.toptal.test.expenses.model;

import java.util.*;
import java.util.stream.Collectors;

public class Report {
    private List<DayReport> dayReports = new ArrayList<>();
    private Double total;

    public Report() {
    }

    public Report(Map<String, List<Expense>> groupedExpences, double total) {
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

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public static class DayReport {
        private List<Expense> expenses;
        private Double average;

        public DayReport() {
        }

        public DayReport(List<Expense> expenses) {
            this.expenses = expenses;
            this.average = expenses.stream().collect(Collectors.averagingLong((e -> new Double(e.getAmount() * 100d).longValue())))/100;
        }

        public List<Expense> getExpenses() {
            return expenses;
        }

        public void setExpenses(List<Expense> expenses) {
            this.expenses = expenses;
        }

        public Double getAverage() {
            return average;
        }

        public void setAverage(Double average) {
            this.average = average;
        }
    }
}
