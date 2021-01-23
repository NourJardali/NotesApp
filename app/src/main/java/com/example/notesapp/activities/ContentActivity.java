package com.example.notesapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.notesapp.R;

import static com.example.notesapp.util.Constants.CONTENT;
import static com.example.notesapp.util.Constants.TITLE;

public class ContentActivity extends AppCompatActivity {
    private TextView title;
    private TextView content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);

        initViews();
        initObjects();
    }

    /**
     * This method is to initialize objects to be used
     */
    private void initObjects() {
        Intent intent = getIntent();
        title.setText(intent.getStringExtra(TITLE));
        content.setText(intent.getStringExtra(CONTENT));
    }

    /**
     * This method is to initialize views
     */
    private void initViews() {
        title = findViewById(R.id.tvTitle);
        content = findViewById(R.id.tvContent);
    }
}