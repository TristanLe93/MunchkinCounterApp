package com.example.tristan.munchkincounter.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.TextView;

import com.example.tristan.munchkincounter.Activities.CalculatorActivity;
import com.example.tristan.munchkincounter.Data;
import com.example.tristan.munchkincounter.Player;
import com.example.tristan.munchkincounter.R;
import com.example.tristan.munchkincounter.SoundPoolPlayer;


public class DetailActivity extends ActionBarActivity {
    private TextView strength;
    private TextView level;
    private TextView gear;
    private TextView bonus;

    private Player player;
    private SoundPoolPlayer sound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // load sound player
        sound = new SoundPoolPlayer(this);

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

        // assign values to screen
        refreshValues();
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
        sound.playGearUpSound();
        player.incrementGear();
        refreshValues();
    }

    public void btnGearDecrement(View v) {
        player.decrementGear();
        refreshValues();
    }

    public void btnLevelIncrement(View v) {
        sound.playSound(R.raw.levelup1);
        player.incrementLevel();
        refreshValues();
    }

    public void btnLevelDecrement(View v) {
        sound.playSound(R.raw.leveldown1);
        player.decrementLevel();
        refreshValues();
    }

    public void btnBonusIncrement(View v) {
        player.incrementBonus();
        refreshValues();
    }

    public void btnBonusDecrement(View v) {
        player.decrementBonus();
        refreshValues();
    }

    private void refreshValues() {
        gear.setText(Integer.toString(player.getGear()));
        level.setText((Integer.toString(player.getLevel())));
        strength.setText(Integer.toString(player.getTotal()));
        bonus.setText(Integer.toString(player.getBonus()));

        Data.adapter.notifyDataSetChanged();
        Data.adapter.saveData();
    }
}
