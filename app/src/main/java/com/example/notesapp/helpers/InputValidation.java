package com.example.notesapp.helpers;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

public class InputValidation {
    private Context context;

    /**
     * constructor
     * @param context
     */
    public InputValidation(Context context){
        this.context = context;
    }

    /**
     * method to check EditText filled or not
     *
     * @param editText
     * @return true if filled, else false
     */
    public boolean isEditTextFilled(EditText editText){
        String value = editText.getText().toString().trim();
        if(value.isEmpty()){
            hideKeyboardFrom(editText);
            return false;
        }
        return true;
    }

    /**
     * Method used to check if password matches
     * @param editText1 Password value
     * @param editText2 Password confirmation
     * @return true is they match, else false
     */
    public boolean isInputEditTextMatches(EditText editText1, EditText editText2){
        String value1 = editText1.getText().toString().trim();
        String value2 = editText2.getText().toString().trim();
        if(!value1.contentEquals(value2)){
            hideKeyboardFrom(editText2);
            return false;
        }
        return true;
    }

    /**
     * Method used to check if name or password exists
     * @param editText
     * @param data
     * @return
     */
    public boolean checkValue(EditText editText, String data){
        String value = editText.getText().toString().trim();
        if(!value.contentEquals(data)){
            hideKeyboardFrom(editText);
            return false;
        }
        return true;
    }

    public boolean isPasswordValid(EditText editText){
        String value = editText.getText().toString().trim();
        return value.length() >= 8;
    }

    /**
     * method to hide keyboard
     *
     * @param view
     */
    public void hideKeyboardFrom(View view){
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }
}
