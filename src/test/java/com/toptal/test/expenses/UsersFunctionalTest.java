package com.toptal.test.expenses;

import com.toptal.test.expenses.model.UserAccount;
import com.toptal.test.expenses.repository.UsersRepository;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;

import java.util.ArrayList;

public class UsersFunctionalTest extends AbstractFunctionalTest {
    @Autowired
    private UsersRepository usersRepository;

    @Test
    public void testFindAll() throws Exception {
        ResponseEntity<ArrayList> users = send("user", HttpMethod.GET, ArrayList.class, ROLE.ADMIN);
        Assert.assertEquals(HttpStatus.OK, users.getStatusCode());
        Assert.assertEquals(2, users.getBody().size());
    }

    @Test
    public void testStore() throws Exception {
        ResponseEntity<String> response = send("user", HttpMethod.PUT, new UserAccount("test", "test", "USER"), String.class, ROLE.ADMIN);
        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assert.assertEquals(3, usersRepository.findAll().size());
        Assert.assertNotEquals("test", usersRepository.findOne("test").getPassword());
    }

    @Test(expected = HttpClientErrorException.class)
    public void testStoreNegative() throws Exception {
        send("user", HttpMethod.PUT, new UserAccount("admin", "admin", "USER"), String.class, ROLE.ADMIN);
    }


    @Test
    public void testUpdate() throws Exception {
        UserAccount user = usersRepository.findOne("user");
        user.setRole("ADMIN");
        ResponseEntity<String> response = send("user", HttpMethod.POST, user, String.class, ROLE.ADMIN);
        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assert.assertEquals("ADMIN", usersRepository.findOne("user").getRole());
    }

    @Test
    public void testDelete() throws Exception {
        ResponseEntity<String> response = send("user/user", HttpMethod.DELETE, String.class, ROLE.ADMIN);
        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assert.assertEquals(1, usersRepository.findAll().size());
    }
}