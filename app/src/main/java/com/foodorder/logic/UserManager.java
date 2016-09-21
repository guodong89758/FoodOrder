package com.foodorder.logic;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by guodong on 2016/9/19 17:12.
 */
public class UserManager {

    private volatile static UserManager instance;

    private List<String> userList;

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
}
