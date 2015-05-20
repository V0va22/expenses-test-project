package com.toptal.test.expenses.controller;


import com.toptal.test.expenses.model.Credentials;
import com.toptal.test.expenses.model.UserAccount;
import com.toptal.test.expenses.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.List;

@RestController
public class LoginController {
    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    public PasswordEncoder passwordEncoder;

    @Autowired
    public UsersController usersController;

    @RequestMapping(method = RequestMethod.POST, value = "/log-in", produces = "text/plain")
    public ResponseEntity<String> login(@RequestBody Credentials credentials) {
        String name = credentials.getUsername();
        UserAccount user = usersRepository.findOne(name);

        String password = credentials.getPassword();
        if (user != null && passwordEncoder.matches(password, user.getPassword())) {
            SecurityContextHolder.getContext().setAuthentication(
                    new UsernamePasswordAuthenticationToken(name, password, AuthorityUtils.createAuthorityList("ROLE_" + user.getRole().toUpperCase())));
            return new ResponseEntity<>(user.getRole(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>("bad credentials for user " + name, HttpStatus.FORBIDDEN);
        }
    }

    @RequestMapping(method = RequestMethod.GET, value = "/log-in", produces = "text/plain")
    public String checkRole() {
        Collection<? extends GrantedAuthority> roles = SecurityContextHolder.getContext().getAuthentication().getAuthorities();
        return roles.isEmpty() ? "" : roles.iterator().next().getAuthority().substring(5);
    }

    @RequestMapping(method = RequestMethod.POST, value = "register", produces = "text/plain")
    public ResponseEntity<String> register(@RequestBody UserAccount userAccount){
        userAccount.setRole("USER");
        ResponseEntity<String> response = usersController.store(userAccount);
        if (response.getStatusCode() == HttpStatus.OK){
            SecurityContextHolder.getContext().setAuthentication(
                    new UsernamePasswordAuthenticationToken(userAccount.getId(), userAccount.getPassword(), AuthorityUtils.createAuthorityList("ROLE_USER")));
        }
        return response;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/log-out")
    public void logout() {
        SecurityContextHolder.getContext().setAuthentication(null);
    }
}
