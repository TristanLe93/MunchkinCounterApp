package com.example.tristan.munchkincounter.Activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
import android.view.WindowManager;

import com.example.tristan.munchkincounter.Data;
import com.example.tristan.munchkincounter.R;

/**
 * Created by Tristan on 15/10/2014.
 */
public class UserSettingActivity extends PreferenceActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setVolumeControlStream(AudioManager.STREAM_MUSIC);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            getActionBar().setDisplayHomeAsUpEnabled(true);
        }

        addPreferencesFromResource(R.xml.settings);
        setPreferenceListener();

        Preference about = (Preference)findPreference("pref_about");
        about.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                dialogAbout();
                return false;
            }
        });

        Preference disclaimer = (Preference)findPreference("pref_disclaimer");
        disclaimer.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                dialogDisclaimer();
                return false;
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                overridePendingTransition(R.anim.push_right_in, R.anim.push_left_out);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        NavUtils.navigateUpFromSameTask(this);
        overridePendingTransition(R.anim.push_right_in, R.anim.push_left_out);
    }

    private void setPreferenceListener() {
        ListPreference deathOption = (ListPreference) findPreference("pref_deathPenalty");
        deathOption.setSummary(deathOption.getEntry());

        deathOption.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                String nv = (String) newValue;

                if (preference.getKey().equals("pref_deathPenalty")) {
                    ListPreference deathOption = (ListPreference) preference;
                    int i = deathOption.findIndexOfValue(nv);
                    deathOption.setSummary(deathOption.getEntries()[i]);
                }
                return true;
            }
        });
    }

    private void dialogAbout() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("About");
        builder.setMessage("Developed by Tristan Le.");

        // dismiss
        builder.setNegativeButton("You got it!", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void dialogDisclaimer() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Disclaimer");
        builder.setMessage("Some disclaimer message saying that this app is not endorsed or associated with Steve Jackson Games.");

        // dismiss
        builder.setNegativeButton("You got it!", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
