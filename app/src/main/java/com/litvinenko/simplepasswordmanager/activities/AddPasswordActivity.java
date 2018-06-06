package com.litvinenko.simplepasswordmanager.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import com.litvinenko.simplepasswordmanager.R;

import java.util.Objects;

public class AddPasswordActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Enable Transitions
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        setContentView(R.layout.activity_password_add);
        setupToolbar();
        setupWindowAnimations();

        final EditText etLogin = findViewById(R.id.etAddLogin);
        final EditText etPassword = findViewById(R.id.etAddPassword);

        Button btnAdd = findViewById(R.id.btnAddNewEntry);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!etLogin.getText().toString().matches("")
                        && !etPassword.getText().toString().matches("")) {
                    Intent intent = new Intent();
                    intent.putExtra("login", String.valueOf(etLogin.getText()));
                    intent.putExtra("password", String.valueOf(etPassword.getText()));
                    setResult(RESULT_OK, intent);
                    onBackPressed(); // finish() not triggers transition animations, so in this case use onBackPressed()
                } else {
                Snackbar.make(getWindow().getDecorView(), getString(R.string.snackbar_input_error), Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                }
            }
        });
    }

    void setupToolbar() {
        (Objects.requireNonNull(getSupportActionBar())).setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void setupWindowAnimations() {
        Transition slide = TransitionInflater.from(this).inflateTransition(R.transition.activity_slide);
        getWindow().setEnterTransition(slide);
    }
}
