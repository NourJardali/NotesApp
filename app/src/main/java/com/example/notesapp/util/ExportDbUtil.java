package com.example.notesapp.util;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

import com.example.notesapp.helpers.CSVWriter;
import com.example.notesapp.listeners.ExporterListener;
import com.example.notesapp.sqlite.DatabaseHelper;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class ExportDbUtil {
    private final ExporterListener exporterListener;
    private final SQLiteDatabase database;
    private final File exportDir;

    public ExportDbUtil(String directoryName, ExporterListener exporterListener, DatabaseHelper databaseHelper){
        this.exporterListener = exporterListener;
        database = databaseHelper.getReadableDatabase();
        exportDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + directoryName);
        if(!exportDir.exists())
            exportDir.mkdirs();
    }

    /**
     *
     * @param tableName export table name
     * @param csvFileName filename that you want to show after export
     */
    public void exportSingleTable(String tableName, String csvFileName){
        File f = new File(exportDir, csvFileName);
        try{
            f.createNewFile();
            CSVWriter csvWriter = new CSVWriter(new FileWriter(f));
            exportTable(tableName, csvWriter);
            csvWriter.close();
            exporterListener.success( "Notes Successfully Exported in notes folder");
        } catch (IOException e) {
            e.printStackTrace();
            exporterListener.fail("Exporting " + tableName + "failed", e.getMessage());
        }
    }

    private void exportTable(String tableName, CSVWriter csvWriter){
        Cursor cursor = database.rawQuery("SELECT * FROM " + tableName , new String[]{});
        csvWriter.writeNext(cursor.getColumnNames());
        while (cursor.moveToNext()){
            //which column you want to export
            String[] mySecondStringArray = new String[cursor.getColumnNames().length];
            for(int i=0;i<cursor.getColumnNames().length;i++){
                mySecondStringArray[i] = cursor.getString(i);
            }
            csvWriter.writeNext(mySecondStringArray);
        }
        cursor.close();
    }
}