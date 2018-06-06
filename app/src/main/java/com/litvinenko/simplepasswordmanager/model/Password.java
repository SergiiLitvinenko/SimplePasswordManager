package com.litvinenko.simplepasswordmanager.model;

public class Password {

    private int mId;
    private String mLogin;
    private String mPassword;

    public int getId() {
        return mId;
    }

    public void setId(int mId) {
        this.mId = mId;
    }

    public String getLogin() {
        return mLogin;
    }

    public void setLogin(String mLogin) {
        this.mLogin = mLogin;
    }

    public String getPassword() {
        return mPassword;
    }

    public void setPassword(String mPassword) {
        this.mPassword = mPassword;
    }
}
