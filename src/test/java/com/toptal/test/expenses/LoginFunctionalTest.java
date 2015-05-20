package com.toptal.test.expenses;

import com.toptal.test.expenses.model.Credentials;
import com.toptal.test.expenses.model.UserAccount;
import com.toptal.test.expenses.repository.UsersRepository;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;

public class LoginFunctionalTest extends AbstractFunctionalTest {

    @Autowired
    private UsersRepository usersRepository;

    @Test
    public void loginTest() {
        ResponseEntity<String> response = send("log-in", HttpMethod.POST, new Credentials("user", "user"), String.class, ROLE.ANONYMOUS);
        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assert.assertEquals("USER", response.getBody());
    }
    @Test
    public void checkRoleAnonymousTest() {
        ResponseEntity<String> anonymousResponse = send("log-in", HttpMethod.GET, String.class, ROLE.ANONYMOUS);
        Assert.assertEquals(HttpStatus.OK, anonymousResponse.getStatusCode());
        Assert.assertEquals("ANONYMOUS", anonymousResponse.getBody());
    }
    @Test
    public void checkRoleAdminTest() {
        ResponseEntity<String> adminResponse = send("log-in", HttpMethod.GET, String.class, ROLE.ADMIN);
        Assert.assertEquals(HttpStatus.OK, adminResponse.getStatusCode());
        Assert.assertEquals("ADMIN", adminResponse.getBody());
    }
    @Test
    public void registerTest() {
        ResponseEntity<String> response = send("register", HttpMethod.POST, new UserAccount("test", "test", "USER"), String.class, ROLE.ANONYMOUS);
        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assert.assertEquals("USER", response.getBody());
        Assert.assertEquals(3, usersRepository.findAll().size());

    }
    @Test (expected = HttpClientErrorException.class)
    public void registerNegativeTest() {
        send("register", HttpMethod.POST, new UserAccount("user", "user", "USER"), String.class, ROLE.ANONYMOUS);
    }
}
