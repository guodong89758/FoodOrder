package com.foodorder.logic;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by guodong on 2016/9/19 17:12.
 */
public class UserManager {

    private volatile static UserManager instance;

    private List<String> userList;
    private String name;
    private String address;
    private String postCode;
    private String city;
    private String username;
    private String password;

    private UserManager() {
        userList = new ArrayList<>();
    }

    public static UserManager getInstance() {
        if (instance == null) {
            synchronized (UserManager.class) {
                if (instance == null) {
                    instance = new UserManager();
                }
            }
        }
        return instance;
    }

    public List<String> getUserList() {
        return userList;
    }

    public void addUsername(String username) {
        userList.add(username);
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPostCode() {
        return postCode;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }
}
