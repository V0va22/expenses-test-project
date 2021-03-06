package com.toptal.test.expenses.controller;


import com.toptal.test.expenses.model.Expense;
import com.toptal.test.expenses.model.Report;
import com.toptal.test.expenses.repository.ExpensesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.data.mongodb.core.query.Criteria.*;

@RestController
public class ExpensesController {
    @Autowired
    private ExpensesRepository expensesRepository;
    @Autowired
    private MongoOperations mongo;

    @RequestMapping(method = RequestMethod.GET, value = "/expense")
    public List<Expense> expenses() {
        String userId = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        return expensesRepository.findByUserId(userId);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/expense")
    public void addExpenses(@RequestBody Expense expense) {
        expense.setUserId(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
        expensesRepository.save(expense);
    }


    @RequestMapping(method = RequestMethod.DELETE, value = "/expense/{id}")
    public void delete(@PathVariable String  id){
        expensesRepository.delete(id);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/report")
    public Report report(@RequestParam Long start, @RequestParam Long end) throws Exception {
        Object user = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Expense> expenses = mongo.find(new Query().addCriteria(where("date").gte(new Date(start)).lte(new Date(end)).and("userId").is(user)), Expense.class);
        // grouping by day average
        Map<String, List<Expense>> groupedExpenses = expenses.stream().collect(Collectors.groupingBy(e -> Report.DATE_FORMAT.format(e.getDate())));
        // calculating total
        BigDecimal sumLong = calculateExpensesSum(expenses);
        return new Report(groupedExpenses,  sumLong);

    }
    // calculating sum via long. It is forbidden to calculate money with float or double
    private BigDecimal calculateExpensesSum(List<Expense> expenses) {
        BigDecimal total = new BigDecimal(0);
        for (Expense expense: expenses){
            total = total.add(expense.getAmount());
        }
        return total;
    }
}
