package com.example.ctl_manageer;

public class Users {

    private String email, name, password;

    public Users(){

    }

    public Users(String email, String name, String password) {
        this.name = email;
        this.email = name;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
