package com.litvinenko.simplepasswordmanager.repository;

public class DBContract {

    static final int DATABASE_VERSION = 1;
    static final String DATABASE_NAME = "PasswordsDB";
    static final String TABLE_PASSWORDS = "Passwords";
    static final String KEY_ID = "id";
    static final String KEY_LOGIN = "login";
    static final String KEY_PASSWORD = "password";
    static final String[] COLUMNS = {KEY_ID, KEY_LOGIN, KEY_PASSWORD};

}
