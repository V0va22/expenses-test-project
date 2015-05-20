package com.toptal.test.expenses.repository;


import com.toptal.test.expenses.model.Expense;
import org.springframework.data.mongodb.repository.MongoRepository;


import java.util.List;

public interface ExpensesRepository extends MongoRepository<Expense, String> {

    List<Expense> findByUserId(String userId);

    List<Expense> findByDescription(String s);
}
