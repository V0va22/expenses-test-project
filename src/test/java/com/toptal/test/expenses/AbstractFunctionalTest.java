package com.toptal.test.expenses;

import com.toptal.test.expenses.model.UserAccount;
import com.toptal.test.expenses.repository.UsersRepository;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.math.BigInteger;
import java.security.SecureRandom;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@TestPropertySource(properties = "mongo.port=27117")
@IntegrationTest("server.port:8090")
public abstract class AbstractFunctionalTest {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UsersRepository usersRepository;

    @PostConstruct
    public void init(){
        usersRepository.delete(usersRepository.findAll());
        usersRepository.save(new UserAccount("user", passwordEncoder.encode("user"), "USER"));
        usersRepository.save(new UserAccount("admin", passwordEncoder.encode("admin"), "ADMIN"));
    }

    public static final String HTTP_LOCALHOST = "http://localhost:8090/";
    public static final String AUTH_HEADER = "Authorization";


    protected RestTemplate restTemplate = new RestTemplate();

    protected <T> ResponseEntity<T> send(String url, HttpMethod method, Class<T> tClass, ROLE role) {
        return send(url,method, null, tClass, role);
    }
    protected <T> ResponseEntity<T> send(String url, HttpMethod method, Object body, Class<T> tClass, ROLE role){
        HttpEntity<?> requestEntity = new HttpEntity<>(body, role.getAuthHeader());
        return restTemplate.exchange(HTTP_LOCALHOST + url, method, requestEntity, tClass);
    }

    protected String randomString(){
        return new BigInteger(130, new SecureRandom()).toString(32);
    }

    public enum ROLE {
        ADMIN,
        USER,
        ANONYMOUS;

        public MultiValueMap<String, String> getAuthHeader(){
            MultiValueMap<String, String> headers = new HttpHeaders();
            if (this == ADMIN){
                headers.add(AUTH_HEADER, "Basic YWRtaW46YWRtaW4=");
            } else if (this == USER) {
                headers.add(AUTH_HEADER, "Basic dXNlcjp1c2Vy");
            }
            return headers;
        }
    }
}
