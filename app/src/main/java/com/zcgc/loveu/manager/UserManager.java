package com.zcgc.loveu.manager;

import com.zcgc.loveu.po.User;

public class UserManager {
    private static volatile UserManager INSTANCE;
    private static User user;

    public static UserManager getInstance() {
        if (INSTANCE == null) {
            synchronized (UserManager.class) {
                if (INSTANCE == null) {
                    INSTANCE = new UserManager();
                }
            }
        }
        return INSTANCE;
    }

    public User getUser() {
        return user;
    }

    public UserManager setUser(User user) {
        UserManager.user = user;
        return this;
    }

    public boolean isLogin() {
        return user != null;
    }
}
