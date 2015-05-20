package com.toptal.test.expenses.model;

import javax.xml.bind.DatatypeConverter;

public class Credentials {

    private String username;
    private String password;

    public Credentials(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public Credentials() {

    }

    public Credentials(String basicHttpHeader) {
        String userPassword = new String(DatatypeConverter.parseBase64Binary(basicHttpHeader.substring(6).trim()));
        username = userPassword.substring(0, userPassword.indexOf(":"));
        password = userPassword.substring(userPassword.indexOf(":") + 1);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
