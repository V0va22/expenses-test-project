package com.toptal.test.expenses.repository;


import com.toptal.test.expenses.model.UserAccount;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

public interface UsersRepository extends MongoRepository<UserAccount, String> {

}
