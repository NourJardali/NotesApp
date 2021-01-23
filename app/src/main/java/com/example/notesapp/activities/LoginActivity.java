package com.example.notesapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;

import com.example.notesapp.R;
import com.example.notesapp.helpers.InputValidation;

import static com.example.notesapp.util.Constants.USER_NAME;
import static com.example.notesapp.util.Constants.USER_PASSWORD;

public class LoginActivity extends BaseActivity implements View.OnClickListener {
    private final AppCompatActivity activity = LoginActivity.this;
    private NestedScrollView nestedScrollView;
    private EditText etName;
    private EditText etPassword;
    private Button buttonLogin;
    private Button buttonRegister;
    private InputValidation inputValidation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

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
        etName = findViewById(R.id.etName);
        etPassword = findViewById(R.id.etPassword);
        buttonLogin = findViewById(R.id.buttonLogin);
        buttonRegister = findViewById(R.id.buttonRegister);
    }

    /**
     * This method is to initialize listeners
     */
    private void initListeners() {
        buttonLogin.setOnClickListener(this);
        buttonRegister.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        //check if registered or not
        if(isRegistered()){
            buttonRegister.setVisibility(View.GONE);
        }
        else{
            buttonRegister.setVisibility(View.VISIBLE);
        }
    }

    /**
     * This method is to initialize objects to be used
     */
    private void initObjects() {
        inputValidation = new InputValidation(activity);
    }

    /**
     * This implemented method is to listen the click on view
     *
     * @param v button view that was clicked
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonLogin:
                verifyFromSharedPref();
                break;
            case R.id.buttonRegister:
                // Navigate to RegisterActivity
                Intent intentRegister = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intentRegister);
                break;
        }
    }

    /**
     * This method is to validate the input text fields and verify login credentials from SQLite
     */
    private void verifyFromSharedPref() {
        String name = preferences.getString(USER_NAME, "NOT_FOUND");
        String password = preferences.getString(USER_PASSWORD, "NOT_FOUND");

        //check if name filed is filled
        if (!inputValidation.isEditTextFilled(etName)) {
            Toast.makeText(this, getString(R.string.error_message_name), Toast.LENGTH_LONG).show();
            return;
        }

        //check if password field is filled
        if (!inputValidation.isEditTextFilled(etPassword)) {
            Toast.makeText(this, getString(R.string.error_message_password), Toast.LENGTH_LONG).show();
            return;
        }

        //check if name exist in SharedPreferences
        if (!inputValidation.checkValue(etName, name)) {
            Toast.makeText(this, getString(R.string.error_not_found), Toast.LENGTH_LONG).show();
            return;
        }

        //check if password exists in SharedPreferences
        if(!inputValidation.checkValue(etPassword, password)){
            Toast.makeText(this, getString(R.string.error_not_found), Toast.LENGTH_LONG).show();
            return;
        }

        Intent accountsIntent = new Intent(activity, NotesListActivity.class);
        accountsIntent.putExtra("NAME", etName.getText().toString().trim());
        emptyInputEditText();
        startActivity(accountsIntent);
    }

    /**
     * This method is to empty all input edit text
     */
    private void emptyInputEditText() {
        etName.setText(null);
        etPassword.setText(null);
    }
}