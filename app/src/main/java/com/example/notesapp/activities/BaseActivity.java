package com.example.notesapp.activities;

import android.Manifest;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.notesapp.util.Constants;

import java.util.ArrayList;
import java.util.List;

import static com.example.notesapp.util.Constants.PERMISSION_REQUEST_CODE;

public class BaseActivity extends AppCompatActivity {
    public SharedPreferences preferences;
    public SharedPreferences.Editor editor;
    String[] permissions = new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferences = getSharedPreferences(Constants.MEMO_APP, MODE_PRIVATE);

        checkPermissions();
    }

    public boolean isRegistered(){
        //false is the default value
        return preferences.getBoolean("isRegistered", false);
    }

    private boolean checkPermissions(){
        int result;
        List<String> listPermissionsNeeded = new ArrayList<>();
        for(String p : permissions){
            result = ContextCompat.checkSelfPermission(BaseActivity.this, p);
            if(result != PackageManager.PERMISSION_GRANTED){
                listPermissionsNeeded.add(p);
            }
        }
        if(!listPermissionsNeeded.isEmpty()){
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), PERMISSION_REQUEST_CODE);
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == PERMISSION_REQUEST_CODE){
            //cheking if the permission is granted
            if(grantResults.length <= 0 || grantResults[0] != PackageManager.PERMISSION_GRANTED){
                //permission not granted
                //Display your message to let the user know that it requires permission to access the app
                Toast.makeText(this, "You denied the permission", Toast.LENGTH_LONG).show();
            }
        }
    }
}
