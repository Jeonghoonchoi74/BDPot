package com.example.baedalpot.model;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class User {
    public String id;
    public String accNum;
    public String email;
    public String password;

    public User() {
    }

    public User(String id, String email, String password, String accNum) {
        this.id = id;
        this.accNum = accNum;
        this.password = password;
        this.email = email;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("Id", id);
        result.put("Email", email);
        result.put("Pw", password);
        result.put("Acc", accNum);
        return result;
    }

    public String getId() {
        return id;
    }

    public void setIdToken(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmailId(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAccNum() {
        return accNum;
    }

    public void setAccNum(String accNum) {
        this.accNum = accNum;
    }

}
