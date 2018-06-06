package com.litvinenko.simplepasswordmanager.activities;

import android.app.ActivityOptions;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;

import com.litvinenko.simplepasswordmanager.Constants;
import com.litvinenko.simplepasswordmanager.model.Password;
import com.litvinenko.simplepasswordmanager.adapter.PasswordAdapter;
import com.litvinenko.simplepasswordmanager.R;
import com.litvinenko.simplepasswordmanager.repository.MySQLiteHelper;

import java.util.List;

public class MainActivity extends AppCompatActivity implements PasswordAdapter.IOnPasswordActionsListener {

    private List<Password> mPasswords;
    private RecyclerView.Adapter mAdapter;
    private MySQLiteHelper mSQLiteHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Enable Transitions
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setupWindowAnimations();

        // Init simple SQLite helper
        mSQLiteHelper = new MySQLiteHelper(this);

        // Init RecyclerView
        RecyclerView rvPasswords = findViewById(R.id.rvPasswords);
        // Setting FixedSize to improve performance
        rvPasswords.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        rvPasswords.setLayoutManager(layoutManager);

        // Load passwords from DB and pass them to Adapter
        loadAllPasswords();
        mAdapter = new PasswordAdapter(this, mPasswords, this);
        rvPasswords.setAdapter(mAdapter);

        FloatingActionButton fab = findViewById(R.id.fabAdd);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddPasswordActivity.class);
                startAddActivity(intent);
            }
        });
    }

    private void setupWindowAnimations() {
        // Set up Transition animation on Activity change
        Transition slide = TransitionInflater.from(this)
                .inflateTransition(R.transition.activity_explode);
        getWindow().setExitTransition(slide);
    }

    private void loadAllPasswords() {
        mPasswords = mSQLiteHelper.getAllPasswords();
    }

    private void savePassword(Password password) {
        mSQLiteHelper.addPassword(password);
    }

    private void deletePassword(Password password) {
        mSQLiteHelper.deletePassword(password);
    }

    /**
     * Interface implementation from PasswordAdapter
     * @param password String password to save to Clipboard
     */
    @Override
    public void onCopyToClipboardClicked(String password) {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("Password", password);
        if (clipboard != null) {
            clipboard.setPrimaryClip(clip);
            View parentLayout = findViewById(R.id.fabAdd);
            Snackbar.make(parentLayout, getString(R.string.snackbar_saved), Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }
    }

    /**
     * Interface implementation from PasswordAdapter
     * @param adapterPosition element position
     * @param password String password to update in Password model
     */
    @Override
    public void onPasswordFinishedEditing(int adapterPosition, String password) {
        Password passwordToUpdate = mPasswords.get(adapterPosition);
        passwordToUpdate.setPassword(password);
        mSQLiteHelper.updatePassword(passwordToUpdate);
    }

    /**
     * Interface implementation from PasswordAdapter
     * @param adapterPosition element position
     */
    @Override
    public void onDeleteClicked(int adapterPosition) {
        Password passwordToDelete = mPasswords.get(adapterPosition);
        mPasswords.remove(passwordToDelete);
        deletePassword(passwordToDelete);
        mAdapter.notifyItemRemoved(adapterPosition);
    }

    private void startAddActivity(Intent intent) {
        // Start Activity with Transition
        ActivityCompat.startActivityForResult(MainActivity.this, intent, Constants.RC_ADD_ITEM,
                ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null) {
            return;
        }
        String login = data.getStringExtra("login");
        String pass = data.getStringExtra("password");

        // Create new Password, insert it to Recycler and save it to DB
        Password password = new Password();
        password.setLogin(login);
        password.setPassword(pass);
        mPasswords.add(password);
        savePassword(password);
        mAdapter.notifyItemInserted(mPasswords.size());
    }
}
