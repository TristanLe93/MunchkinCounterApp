package com.example.tristan.munchkincounter.Activities;

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
        ListPreference deathOption = (ListPreference)findPreference("pref_deathPenalty");
        deathOption.setSummary(deathOption.getEntry());

        deathOption.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                String nv = (String)newValue;

                if (preference.getKey().equals("pref_deathPenalty")) {
                    ListPreference deathOption = (ListPreference)preference;
                    int i = deathOption.findIndexOfValue(nv);
                    deathOption.setSummary(deathOption.getEntries()[i]);
                }
                return true;
            }
        });
    }
}
