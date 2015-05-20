package com.toptal.test.expenses;

import com.toptal.test.expenses.model.Credentials;
import com.toptal.test.expenses.model.UserAccount;
import com.toptal.test.expenses.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.servlet.configuration.EnableWebMvcSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Configuration
@EnableWebMvcSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    public PasswordEncoder passwordEncoder;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.ALWAYS)
            .and()
                .authorizeRequests()
                .antMatchers("/user").hasRole("ADMIN")
                .antMatchers("/expense").authenticated()
                .antMatchers("/report").authenticated()
                .anyRequest().permitAll()
            .and()
                .addFilterAfter(new MongoBasicAuthFilter(), UsernamePasswordAuthenticationFilter.class)
                .csrf().disable();
    }


    /**
     * Performs Basic authentication for mongo user store
     */
    public class MongoBasicAuthFilter extends GenericFilterBean {

        @Override
        public void doFilter(ServletRequest req, ServletResponse res,
                             FilterChain chain) throws IOException, ServletException {
            HttpServletRequest request = (HttpServletRequest) req;
            String authorization = request.getHeader("Authorization");
            if (authorization != null) {
                Credentials credentials = new Credentials(authorization);
                String name = credentials.getUsername();
                UserAccount user = usersRepository.findOne(name);
                String password = credentials.getPassword();
                if (user != null && passwordEncoder.matches(password, user.getPassword())) {
                    SecurityContextHolder.getContext().setAuthentication(
                            new UsernamePasswordAuthenticationToken(name, password, AuthorityUtils.createAuthorityList("ROLE_" + user.getRole().toUpperCase())));
                } else {
                    throw new BadCredentialsException("bad credentials for user " + name);
                }
            }
            chain.doFilter(request, res);
        }

    }

}
