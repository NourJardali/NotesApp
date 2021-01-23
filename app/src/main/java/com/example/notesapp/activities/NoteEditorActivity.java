package com.example.notesapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.notesapp.R;
import com.example.notesapp.modal.Note;
import com.example.notesapp.sqlite.DatabaseHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static com.example.notesapp.activities.NotesListActivity.listNotes;
import static com.example.notesapp.util.Constants.POSITION;

public class NoteEditorActivity extends BaseActivity {
    private final AppCompatActivity activity = NoteEditorActivity.this;
    private int pos;
    private DatabaseHelper databaseHelper;
    private boolean newNote = true;
    private EditText editTextContent;
    private EditText editTextTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_editor);

        initViews();
        initObjects();
        checkNote();
    }

    /**
     * check if note is new or old
     */
    private void checkNote(){
        if(pos != -1){
            editTextContent.setText(listNotes.get(pos).getContent());
            editTextTitle.setText(listNotes.get(pos).getTitle());
            newNote = false;
        }
        else{
            listNotes = new ArrayList<>();
            Note note = new Note();
            note.setId(0);
            note.setTitle("");
            note.setContent("");
            Date c = Calendar.getInstance().getTime();
            SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
            String formattedDate = df.format(c);
            note.setDate(formattedDate);
            listNotes.add(note);
            pos = listNotes.size() - 1;
        }
    }

    /**
     * This method is to initialize views
     */
    private void initViews(){
        editTextContent = findViewById(R.id.editTextContent);
        editTextTitle = findViewById(R.id.editTextTitle);
    }

    /**
     * This method is to initialize objects to be used
     */
    private void initObjects() {
        databaseHelper = new DatabaseHelper(activity);

        //Fetch data that is passed from NotesListActivity
        Intent intent = getIntent();

        //Accessing the data using key and value
        pos = intent.getIntExtra(POSITION, -1);

        editTextTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                listNotes.get(pos).setTitle(String.valueOf(s));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        editTextContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                listNotes.get(pos).setContent(String.valueOf(s));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.save_note_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.save){
            if(newNote){
                databaseHelper.addNote(listNotes.get(pos));
            }
            else{
                databaseHelper.updateNote(listNotes.get(pos));
            }
            Intent intent = getIntent();
            setResult(RESULT_OK, intent);
            finish();
        }
        return true;
    }
}