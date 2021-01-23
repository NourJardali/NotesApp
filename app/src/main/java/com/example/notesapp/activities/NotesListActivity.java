package com.example.notesapp.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notesapp.R;
import com.example.notesapp.adapters.NotesListAdapter;
import com.example.notesapp.helpers.CustomDialogClass;
import com.example.notesapp.helpers.DividerItemDecoration;
import com.example.notesapp.listeners.ExporterListener;
import com.example.notesapp.listeners.NoteOnClickListener;
import com.example.notesapp.modal.Note;
import com.example.notesapp.sqlite.DatabaseHelper;
import com.example.notesapp.util.Constants;
import com.example.notesapp.util.ExportDbUtil;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.example.notesapp.util.Constants.NOTE_REQUEST_CODE;
import static com.example.notesapp.util.Constants.PICKFILE_RESULT_CODE;
import static com.example.notesapp.util.Constants.POSITION;

public class NotesListActivity extends BaseActivity implements NoteOnClickListener, ExporterListener {
    private final AppCompatActivity activity = NotesListActivity.this;
    public static List<Note> listNotes;
    public static NotesListAdapter notesListAdapter;
    private RecyclerView recyclerViewNotes;
    public static DatabaseHelper databaseHelper;
    private ExportDbUtil exportDbUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_list);

        initViews();
        initObjects();
    }

    /**
     * This method is to initialize views
     */
    private void initViews() {
        recyclerViewNotes = findViewById(R.id.recyclerViewNotes);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Going from NotesListActivity to NotesEditorActivity
                Intent intent = new Intent(getApplicationContext(), NoteEditorActivity.class);
                intent.putExtra(POSITION, -1);
                startActivityForResult(intent, Constants.NOTE_REQUEST_CODE);
            }
        });
    }

    /**
     * This method is to initialize objects to be used
     */
    private void initObjects() {
        listNotes = new ArrayList<>();
        notesListAdapter = new NotesListAdapter(listNotes, this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerViewNotes.setLayoutManager(mLayoutManager);
        recyclerViewNotes.setItemAnimator(new DefaultItemAnimator());
        recyclerViewNotes.addItemDecoration(new DividerItemDecoration(this));
        recyclerViewNotes.setHasFixedSize(true);
        recyclerViewNotes.setAdapter(notesListAdapter);
        databaseHelper = new DatabaseHelper(activity);
        getDataFromSQLite();
        exportDbUtil = new ExportDbUtil( "notes", this, databaseHelper);
    }

    /**
     * This method is to fetch all user records from SQLite
     */
    private void getDataFromSQLite() {
        // AsyncTask is used that SQLite operation not blocks the UI Thread.
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                listNotes.clear();
                listNotes.addAll(databaseHelper.getAllNotes());
                return null;
            }
            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                notesListAdapter.notifyDataSetChanged();
            }
        }.execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()){
            case R.id.action_export:
                exportData();
                break;
            case R.id.action_import:
                importData();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void exportData(){
        exportDbUtil.exportSingleTable("note", "notes.csv");
    }

    private void importData(){
        Intent chooseFile = new Intent(Intent.ACTION_GET_CONTENT);
        chooseFile.setType("text/csv");
        chooseFile = Intent.createChooser(chooseFile, "Open CSV");
        startActivityForResult(chooseFile, PICKFILE_RESULT_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICKFILE_RESULT_CODE) {
            if (data != null) {
                String path = data.getData().getPath();
                Log.d("File path", path);
                if (path.contains("/root_path"))
                    path = path.replace("/root_path", "");
                Log.d("New File Path", path);
                try {
                    if (resultCode == RESULT_OK) {
                        Log.d("RESULT CODE", "OK");
                        FileReader fileReader = new FileReader(path);
                        BufferedReader bufferedReader = new BufferedReader(fileReader);
                        String line;
                        while ((line = bufferedReader.readLine()) != null) {
                            Log.d("line", line);
                            String[] str = line.split(",", 4); //defining 4 columns
                            //ID, Date, Title, Content
                            String id = str[0];
                            String title = str[1];
                            String date = str[2];
                            String content = str[3];
                            Note note = new Note();
                            listNotes.add(note);
                            note.setId(Integer.parseInt(id));
                            note.setTitle(title);
                            note.setDate(date);
                            note.setContent(content);
                            databaseHelper.addNote(note);
                            listNotes.set(note.getId(), note);
                        }
                        Toast.makeText(NotesListActivity.this, "Successfully Updated Database", Toast.LENGTH_LONG).show();
                    } else {
                        Log.e("RESULT CODE", "InValid");
                        Toast.makeText(NotesListActivity.this, "Only CSV files allowed.", Toast.LENGTH_LONG).show();
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    Log.e("FileNotFound", "file not found");
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e("IOException", e.getMessage());
                }
            }
        }
        else{
            if(requestCode == NOTE_REQUEST_CODE){
               initObjects();
            }
        }
    }

    @Override
    public void displayContent(int pos) {
        //Going from NotesListActivity to ContentActivity
        Intent intent = new Intent(activity, ContentActivity.class);
        intent.putExtra(Constants.TITLE, listNotes.get(pos).getTitle());
        intent.putExtra(Constants.CONTENT, listNotes.get(pos).getContent());
        activity.startActivity(intent);
    }

    @Override
    public void openDialog(int pos) {
        CustomDialogClass customDialogClass = new CustomDialogClass(this, pos);
        customDialogClass.show();
    }

    @Override
    public void success(String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void fail(String message, String exception) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}