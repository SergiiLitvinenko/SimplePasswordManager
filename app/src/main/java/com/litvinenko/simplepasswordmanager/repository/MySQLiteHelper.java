package com.litvinenko.simplepasswordmanager.repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.litvinenko.simplepasswordmanager.model.Password;

import java.util.ArrayList;
import java.util.List;

import static com.litvinenko.simplepasswordmanager.repository.DBContract.DATABASE_NAME;
import static com.litvinenko.simplepasswordmanager.repository.DBContract.DATABASE_VERSION;
import static com.litvinenko.simplepasswordmanager.repository.DBContract.KEY_ID;
import static com.litvinenko.simplepasswordmanager.repository.DBContract.KEY_LOGIN;
import static com.litvinenko.simplepasswordmanager.repository.DBContract.KEY_PASSWORD;
import static com.litvinenko.simplepasswordmanager.repository.DBContract.TABLE_PASSWORDS;

public class MySQLiteHelper extends SQLiteOpenHelper {

    public MySQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE passwords ( " +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "login TEXT, " +
                "password TEXT )");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS passwords");

        this.onCreate(db);
    }

    public void addPassword(Password password) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_LOGIN, password.getLogin());
        values.put(KEY_PASSWORD, password.getPassword());

        int id = (int) db.insert(TABLE_PASSWORDS,
                null,
                values);

        password.setId(id);
        db.close();
    }

    public List<Password> getAllPasswords() {
        List<Password> passwords = new ArrayList<>();

        String query = "SELECT * FROM " + TABLE_PASSWORDS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        Password password;
        if (cursor.moveToFirst()) {
            do {
                password = new Password();
                password.setId(Integer.parseInt(cursor.getString(0)));
                password.setLogin(cursor.getString(1));
                password.setPassword(cursor.getString(2));
                passwords.add(password);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return passwords;
    }

    public void updatePassword(Password password) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_LOGIN, password.getLogin());
        values.put(KEY_PASSWORD, password.getPassword());

        db.update(TABLE_PASSWORDS,
                values,
                KEY_ID + " = ?",
                new String[]{String.valueOf(password.getId())});

        db.close();
    }

    public void deletePassword(Password password) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TABLE_PASSWORDS,
                KEY_ID + " = ?",
                new String[]{String.valueOf(password.getId())});
        db.close();
    }

}