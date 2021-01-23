package com.example.notesapp.helpers;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.example.notesapp.R;
import com.example.notesapp.activities.NoteEditorActivity;
import com.example.notesapp.modal.Note;
import com.example.notesapp.util.Constants;

import static com.example.notesapp.activities.NotesListActivity.databaseHelper;
import static com.example.notesapp.activities.NotesListActivity.listNotes;
import static com.example.notesapp.activities.NotesListActivity.notesListAdapter;
import static com.example.notesapp.util.Constants.NOTE_REQUEST_CODE;

public class CustomDialogClass extends Dialog implements android.view.View.OnClickListener {
    private Activity activity;
    private Dialog dialog;
    private Button edit, delete;
    private int pos;

    public CustomDialogClass(Activity activity, int pos){
        super(activity);
        this.activity = activity;
        this.pos = pos;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.layout_dialog);
        edit = findViewById(R.id.btEdit);
        delete = findViewById(R.id.btDelete);
        edit.setOnClickListener(this);
        delete.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btEdit:
                //Going from NotesListActivity to NotesEditorActivity
                Intent intent = new Intent(activity, NoteEditorActivity.class);
                intent.putExtra(Constants.POSITION, pos);            //to tell us which row of listView was tapped
                activity.startActivityForResult(intent, NOTE_REQUEST_CODE);
                dismiss();
                break;
            case R.id.btDelete:
                Note note = listNotes.get(pos);
                listNotes.remove(pos);
                databaseHelper.deleteNote(note);
                notesListAdapter.notifyDataSetChanged();
                dismiss();
                break;
        }
    }
}