package com.example.lb1.Models;

public class User {
    String Firstname;
    String Lastname;
    String Username;
    String Email;
    String Password;

    public User(String username, String email, String password) {
        Username = username;
        Email = email;
        Password = password;

    }

    public User(String firstname, String lastname, String username, String email, String password) {
        Firstname = firstname;
        Lastname = lastname;
        Username = username;
        Email = email;
        Password = password;
    }

    public User(String username, String password) {
        Username = username;
        Password = password;
    }

    public String getFirstname() {
        return Firstname;
    }

    public void setFirstname(String firstname) {
        Firstname = firstname;
    }

    public String getLastname() {
        return Lastname;
    }

    public void setLastname(String lastname) {
        Lastname = lastname;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }




}
