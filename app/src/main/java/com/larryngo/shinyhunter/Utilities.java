package com.larryngo.shinyhunter;

import android.app.Activity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.SearchView;

import static android.content.Context.INPUT_METHOD_SERVICE;

public class Utilities {
    public static void closeKeyboard(Activity activity) {
        try {
            InputMethodManager imm = (InputMethodManager)activity.getSystemService(INPUT_METHOD_SERVICE);
            View keyboardView = activity.getCurrentFocus();
            if(keyboardView != null) {
                imm.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
