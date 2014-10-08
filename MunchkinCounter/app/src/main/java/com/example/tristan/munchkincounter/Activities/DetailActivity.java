package com.example.tristan.munchkincounter.Activities;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.tristan.munchkincounter.Data;
import com.example.tristan.munchkincounter.FontCache;
import com.example.tristan.munchkincounter.Player;
import com.example.tristan.munchkincounter.R;
import com.example.tristan.munchkincounter.SoundPlayer;
import com.example.tristan.munchkincounter.SoundPoolPlayer;


public class DetailActivity extends ActionBarActivity {
    private TextView strength;
    private TextView level;
    private TextView gear;
    private TextView bonus;

    private Player player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle extras = getIntent().getExtras();

        // get the player
        int pos = extras.getInt("position");
        player = Data.adapter.getItem(pos);
        getSupportActionBar().setTitle(player.getName());

        // store textview and buttons to variables
        strength = (TextView)findViewById(R.id.totalNumberText);
        level = (TextView)findViewById(R.id.levelNumberText);
        gear = (TextView)findViewById(R.id.gearNumberText);
        bonus = (TextView)findViewById(R.id.bonusNumberText);

        Typeface tf = FontCache.get("fonts/quasimodo.ttf", this.getBaseContext());
        strength.setTypeface(tf);

        // assign values to screen
        refreshValues();

        // keep screen on
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                saveData();
                NavUtils.navigateUpFromSameTask(this);
                overridePendingTransition(R.anim.push_right_out, R.anim.push_left_in);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        saveData();
        finish();
        overridePendingTransition(R.anim.push_right_out, R.anim.push_left_in);
    }

    /**
     * Transition to the calculator
     */
    public void btnCalculator(View v) {
        Intent intent = new Intent(this, CalculatorActivity.class);
        intent.putExtra("strength", player.getTotal());
        intent.putExtra("name", player.getName());
        startActivity(intent);
    }

    public void btnGearIncrement(View v) {
        SoundPlayer.playSound(R.raw.gearup1);
        player.incrementGear();
        refreshValues();
    }

    public void btnGearDecrement(View v) {
        SoundPlayer.playSound(R.raw.geardown1);
        player.decrementGear();
        refreshValues();
    }

    public void btnLevelIncrement(View v) {
        SoundPlayer.playSound(R.raw.levelup1);
        player.incrementLevel();
        refreshValues();
    }

    public void btnLevelDecrement(View v) {
        SoundPlayer.playSound(R.raw.leveldown1);
        player.decrementLevel();
        refreshValues();
    }

    public void btnBonusIncrement(View v) {
        SoundPlayer.playSound(R.raw.tick);
        player.incrementBonus();
        refreshValues();
    }

    public void btnBonusDecrement(View v) {
        SoundPlayer.playSound(R.raw.tick);
        player.decrementBonus();
        refreshValues();
    }

    private void refreshValues() {
        gear.setText(Integer.toString(player.getGear()));
        level.setText((Integer.toString(player.getLevel())));
        strength.setText(Integer.toString(player.getTotal()));
        bonus.setText(Integer.toString(player.getBonus()));
    }

    private void saveData() {
        Data.adapter.notifyDataSetChanged();
        Data.adapter.saveData();
        Data.adapter.sortByLevel();
    }
}
