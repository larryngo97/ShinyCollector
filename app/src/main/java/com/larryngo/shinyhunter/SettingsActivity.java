package com.larryngo.shinyhunter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.larryngo.shinyhunter.app.App;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.preference.CheckBoxPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

public class SettingsActivity extends AppCompatActivity {

    public static String SETTING_NIGHTMODE_KEY = "dark_mode";
    public static String SETTING_VIBRATEMODE_KEY = "vibrate_mode";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if(isDarkModeOn()) {
            setTheme(R.style.HomeThemeDark);
        } else {
            setTheme(R.style.HomeTheme);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);

        Toolbar toolbar = findViewById(R.id.settings_toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations(R.anim.slide_in_up, R.anim.slide_out_down, R.anim.slide_in_down, R.anim.slide_out_up)
                    .replace(R.id.settings, new SettingsFragment())
                    .commit();
        }
    }

    public static class SettingsFragment extends PreferenceFragmentCompat {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);

            CheckBoxPreference nightModePreference = findPreference(getResources().getString(R.string.setting_category_nightmode_key));
            if(nightModePreference != null) {
                if(isDarkModeOn()) {
                    nightModePreference.setChecked(true);
                }
                nightModePreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                    @Override
                    public boolean onPreferenceClick(Preference preference) {
                        if(nightModePreference.isChecked()) {
                            System.out.println("Turning on dark mode");
                            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                            saveDarkMode(true);
                        } else {
                            System.out.println("Turning off dark mode");
                            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                            saveDarkMode(false);
                        }
                        if(getActivity() != null) {
                            System.out.println("Restarting activity");
                            getActivity().recreate();
                        }
                        return true;
                    }
                });
            }

            CheckBoxPreference vibrateModePreference = findPreference(getResources().getString(R.string.setting_category_vibrate_key));
            if(vibrateModePreference != null) {
                vibrateModePreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                    @Override
                    public boolean onPreferenceClick(Preference preference) {
                        if(vibrateModePreference.isChecked()) {
                            System.out.println("Turning on vibration");
                            saveVibrateMode(true);
                        } else {
                            System.out.println("Turning off vibration");
                            saveVibrateMode(false);
                        }
                        return true;
                    }
                });
            }

        }
    }

    public static boolean isDarkModeOn() {
        return App.getTinyDB().getBoolean(SETTING_NIGHTMODE_KEY, false);
    }

    public static void saveDarkMode(boolean value) {
        App.getTinyDB().putBoolean(SETTING_NIGHTMODE_KEY, value);
    }

    public static boolean isVibrateModeOn () {
        return App.getTinyDB().getBoolean(SETTING_VIBRATEMODE_KEY, true);
    }

    public static void saveVibrateMode(boolean value) {
        App.getTinyDB().putBoolean(SETTING_VIBRATEMODE_KEY, value);
    }
}