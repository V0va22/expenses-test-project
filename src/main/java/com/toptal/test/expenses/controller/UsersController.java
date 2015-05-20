package com.toptal.test.expenses.controller;

import com.toptal.test.expenses.model.UserAccount;
import com.toptal.test.expenses.repository.ExpensesRepository;
import com.toptal.test.expenses.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UsersController {
    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private ExpensesRepository expensesRepository;

    @Autowired
    public PasswordEncoder passwordEncoder;

    @RequestMapping(method = RequestMethod.GET, value = "user")
    public List<UserAccount> findAll(){
        return usersRepository.findAll();
    }

    @RequestMapping(method = RequestMethod.PUT, value = "user", produces = "text/plain")
    public ResponseEntity<String> store(@RequestBody UserAccount userAccount){
        if (usersRepository.findOne(userAccount.getId()) == null){
            userAccount.setPassword(passwordEncoder.encode(userAccount.getPassword()));
            usersRepository.save(userAccount);
            return new ResponseEntity<>(userAccount.getRole(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>("User already exist", HttpStatus.BAD_REQUEST);
        }

    }
    @RequestMapping(method = RequestMethod.POST, value = "user")
    public void update(@RequestBody UserAccount userAccount){
        usersRepository.save(userAccount);
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "user/{id}")
    public void delete(@PathVariable String  id){
        usersRepository.delete(id);
        expensesRepository.delete(expensesRepository.findByUserId(id));
    }
}
