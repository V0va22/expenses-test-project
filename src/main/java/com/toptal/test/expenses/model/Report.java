package com.toptal.test.expenses.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class Report {
    public static final String DATE_PATTERN = "yyyy-MM-dd";
    public static final DateFormat DATE_FORMAT = new SimpleDateFormat(DATE_PATTERN);
    private List<DayReport> dayReports = new ArrayList<>();
    private BigDecimal total;

    public Report() {
    }

    public Report(Map<String, List<Expense>> groupedExpenses, BigDecimal total) {
        TreeMap<String, List<Expense>> sortedExpenses = new TreeMap<>((o1, o2) ->  o1.compareTo(o2));
        sortedExpenses.putAll(groupedExpenses);
        sortedExpenses.forEach((k,v) -> dayReports.add(new DayReport(k, v)));
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
        private Date date;
        private BigDecimal total;

        public DayReport() {
        }

        public DayReport(String dateString, List<Expense> expenses) {
            try {
                date = DATE_FORMAT.parse(dateString);
            } catch (ParseException e) {
                date = expenses.isEmpty() ? new Date() : expenses.get(0).getDate();
            }
            BigDecimal total = new BigDecimal(0);
            for (Expense expense: expenses){
                total = total.add(expense.getAmount());
            }
            this.total = total;
            this.expenses = expenses;
        }

        public List<Expense> getExpenses() {
            return expenses;
        }

        public void setExpenses(List<Expense> expenses) {
            this.expenses = expenses;
        }

        public Date getDate() {
            return date;
        }

        public void setDate(Date date) {
            this.date = date;
        }

        public BigDecimal getTotal() {
            return total;
        }

        public void setTotal(BigDecimal total) {
            this.total = total;
        }
    }
}
