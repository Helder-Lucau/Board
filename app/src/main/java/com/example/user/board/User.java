package com.example.user.board;


public class User {
    public String user_type,admission,email,username;

    public User()
    {

    }

    public User(String user_type,String admission, String email, String username) {
        this.user_type  = user_type;
        this.admission = admission;
        this.email = email;
        this.username = username;
    }
}