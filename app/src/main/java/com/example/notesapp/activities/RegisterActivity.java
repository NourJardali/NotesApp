package com.example.notesapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;

import com.example.notesapp.R;
import com.example.notesapp.helpers.InputValidation;
import com.example.notesapp.util.Constants;
import com.google.android.material.snackbar.Snackbar;

public class RegisterActivity extends BaseActivity implements View.OnClickListener {
    private final AppCompatActivity activity = RegisterActivity.this;
    private EditText etName;
    private EditText etPassword;
    private EditText etPasswordConfirm;
    private Button buttonRegister;
    private NestedScrollView nestedScrollView;
    private InputValidation inputValidation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        getSupportActionBar().hide();
        initViews();
        initListeners();
        initObjects();
    }

    /**
     * This method is to initialize views
     */
    private void initViews() {
        nestedScrollView = findViewById(R.id.nestedScrollView);
        etName = findViewById(R.id.textName);
        etPassword = findViewById(R.id.textPassword);
        etPasswordConfirm = findViewById(R.id.textPasswordConfirm);
        buttonRegister = findViewById(R.id.buttonRegister);
    }

    /**
     * This method is to initialize com.example.notesapp.listeners
     */
    private void initListeners() {
        buttonRegister.setOnClickListener(this);
    }

    /**
     * This method is to initialize objects to be used
     */
    private void initObjects(){
        inputValidation = new InputValidation(activity);
    }

    /**
     * This implemented method is to listen the click on view
     *
     * @param v view
     */
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.buttonRegister) {
            saveDataInSP();
        }
    }

    /**
     * This method is to validate the input text fields and save data in SharedPreferences
     */
    private void saveDataInSP(){
        //check is name field is filled
        if(!inputValidation.isEditTextFilled(etName)){
            Snackbar.make(nestedScrollView, getString(R.string.error_message_name), Snackbar.LENGTH_LONG).show();
            return;
        }

        //check if password field is filled
        if(!inputValidation.isEditTextFilled(etPassword)){
            Snackbar.make(nestedScrollView, getString(R.string.error_message_password), Snackbar.LENGTH_LONG).show();
            return;
        }

        if(!inputValidation.isPasswordValid(etPassword)){
            Snackbar.make(nestedScrollView, "Password should at least has 8 characters", Snackbar.LENGTH_LONG).show();
            return;
        }

        //check if password field is filled
        if(!inputValidation.isEditTextFilled(etPasswordConfirm)){
            Snackbar.make(nestedScrollView, getString(R.string.error_message_password), Snackbar.LENGTH_LONG).show();
            return;
        }

        //check if password matches or not
        if(!inputValidation.isInputEditTextMatches(etPassword, etPasswordConfirm)){
            Snackbar.make(nestedScrollView, getString(R.string.error_password_match), Snackbar.LENGTH_LONG).show();
            return;
        }

        editor = preferences.edit();
        editor.putString(Constants.USER_NAME, etName.getText().toString().trim());
        editor.putString(Constants.USER_PASSWORD, etPassword.getText().toString().trim());
        editor.putBoolean(Constants.IS_REGISTERED, true);
        editor.apply();

        // Snack Bar to show success message that record saved successfully
        Snackbar.make(nestedScrollView, getString(R.string.success_message), Snackbar.LENGTH_LONG).show();
        emptyInputEditText();

        //go back to LoginActivity
        Intent intent = getIntent();
        setResult(RESULT_OK, intent);
        finish();
    }

    /**
     * This method is to empty all input edit text
     */
    private void emptyInputEditText(){
        etName.setText(null);
        etPassword.setText(null);
        etPasswordConfirm.setText(null);
        ((EditText) findViewById(R.id.textEmail)).setText(null);
    }
}