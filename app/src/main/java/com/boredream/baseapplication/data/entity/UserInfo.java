package com.boredream.baseapplication.data.entity;

import java.io.Serializable;

public class UserInfo implements Serializable {

    private String saleSiebleUid;
    private String password;

    public String getName() {
        return saleSiebleUid;
    }

    public void setName(String name) {
        this.saleSiebleUid = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
