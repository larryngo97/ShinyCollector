package com.larryngo.shinyhunter;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

public class HomeActivity extends AppCompatActivity {
    protected static FragmentManager fm;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_layout);

        fm = getSupportFragmentManager();
        fm.beginTransaction().replace(R.id.home_fragment_container, new HomeBaseFragment()).commit();

    }

    @Override
    public void onBackPressed() {
        int backStackCount = fm.getBackStackEntryCount(); //gets count of the back stack
        Toast.makeText(this, "Stack Count: " + backStackCount, Toast.LENGTH_SHORT).show();
        if (backStackCount != 0) //if theres a current back stack
        {
            fm.popBackStack(); //pops the stack, reformats the layout
        }
        else
        {
            super.onBackPressed();
        }
    }
}
