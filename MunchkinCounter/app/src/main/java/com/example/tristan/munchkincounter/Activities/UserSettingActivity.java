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
        builder.setMessage(
            "This application is developed by Tristan Le.\n\nYour feedback are welcome!" +
            "\n\nPlease send your feedback to:\ntristan.le6@gmail.com"
        );

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
        builder.setMessage(
            "Munchkin is a trademark of Steve Jackson Games, and its rules and art " +
            "are copyrighted by Steve Jackson Games. All rights are reserved by " +
            "Steve Jackson Games. This application is the original creation of Tristan Le and is " +
            "released for free distribution, and not for resale, under the permissions granted " +
            "in the Steve Jackson Games Online Policy.\n\nThis application is not endorsed or " +
            "associated with SJ Games."
        );

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
