package com.example.notesapp.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.notesapp.modal.Note;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    // Database Version
    private static final int DATABASE_VERSION = 4;

    // Database Name
    public static final String DATABASE_NAME = "NoteManager.db";

    //Note table name
    public static final String TABLE_NOTE = "note";

    //Note Table Columns names
    public static final String COLUMN_NOTE_ID = "note_id";
    public static final String COLUMN_NOTE_TITLE = "note_title";
    public static final String COLUMN_NOTE_DATE = "note_date";
    public static final String COLUMN_NOTE_CONTENT = "note_content";

    /**
     * Constructor
     *
     * @param context
     */
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //create note table sql query
        String CREATE_NOTE_TABLE = "CREATE TABLE " + TABLE_NOTE + "("
                + COLUMN_NOTE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_NOTE_TITLE + " TEXT, "
                + COLUMN_NOTE_DATE + " TEXT, " + COLUMN_NOTE_CONTENT + " TEXT" + ")";
        db.execSQL(CREATE_NOTE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //Drop Note Table if exist
        //drop table sql query
        String DROP_NOTE_TABLE = "DROP TABLE IF EXISTS "+ TABLE_NOTE;
        db.execSQL(DROP_NOTE_TABLE);

        // Create tables again
        onCreate(db);
    }

    /**
     * This method is to create note record
     *
     * @param note note record to create
     */
    public void addNote(Note note){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NOTE_TITLE, note.getTitle());
        values.put(COLUMN_NOTE_DATE, note.getDate());
        values.put(COLUMN_NOTE_CONTENT, note.getContent());

        // Inserting Row
        db.insert(TABLE_NOTE, null, values);
        db.close();
    }

    /**
     * This method is to fetch all notes and return the list of note records
     *
     * @return list
     */
    public List<Note> getAllNotes() {
        // array of columns to fetch
        String[] columns = {
                COLUMN_NOTE_ID,
                COLUMN_NOTE_TITLE,
                COLUMN_NOTE_DATE,
                COLUMN_NOTE_CONTENT
        };

        // sorting orders
        String sortOrder =
                COLUMN_NOTE_DATE + " ASC";
        List<Note> noteList = new ArrayList<Note>();
        SQLiteDatabase db = this.getReadableDatabase();

        // query the user table
        /*
         * Here query function is used to fetch records from note table this function works like we use sql query.
         * SQL query equivalent to this query function is
         * SELECT note_id,note_tittle,note_date,note_content FROM note ORDER BY note_date;
         */
        Cursor cursor = db.query(TABLE_NOTE, //Table to query
                columns,    //columns to return
                null,        //columns for the WHERE clause
                null,        //The values for the WHERE clause
                null,       //group the rows
                null,       //filter by row groups
                sortOrder); //The sort order

        // Traversing through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Note note = new Note();
                note.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_NOTE_ID))));
                note.setTitle(cursor.getString(cursor.getColumnIndex(COLUMN_NOTE_TITLE)));
                note.setDate(cursor.getString(cursor.getColumnIndex(COLUMN_NOTE_DATE)));
                note.setContent(cursor.getString(cursor.getColumnIndex(COLUMN_NOTE_CONTENT)));

                // Adding user record to list
                noteList.add(note);
            } while (cursor.moveToNext());
        }
        cursor.close();

        // return user list
        return noteList;
    }

    /**
     * This method to update note record
     *
     * @param note the note record to be updated
     */
    public void updateNote(Note note) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NOTE_TITLE, note.getTitle());
        values.put(COLUMN_NOTE_DATE, note.getDate());
        values.put(COLUMN_NOTE_CONTENT, note.getContent());

        // updating row
        db.update(TABLE_NOTE, values, COLUMN_NOTE_ID + " = ?",
                new String[]{String.valueOf(note.getId())});
        db.close();
    }

    /**
     * This method is to delete note record
     *
     * @param note note record to be deleted
     */
    public void deleteNote(Note note) {
        SQLiteDatabase db = this.getWritableDatabase();

        // delete user record by id
        db.delete(TABLE_NOTE, COLUMN_NOTE_ID + " = ?",
                new String[]{String.valueOf(note.getId())});
        db.close();
    }

    /**
     * function used for fetching data for exporting database
     */
    public Cursor raw() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_NOTE , new String[]{});
    }
}