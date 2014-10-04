package com.example.tristan.munchkincounter;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;


public class DetailActivity extends ActionBarActivity {
    private Player player;
    private TextView strength;
    private TextView level;
    private TextView gear;
    private SoundPoolPlayer sound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // get the player
        int pos = getIntent().getExtras().getInt("position");
        player = Data.adapter.getItem(pos);
        getSupportActionBar().setTitle(player.getName());

        // store textview and buttons to variables
        strength = (TextView)findViewById(R.id.totalNumberText);
        level = (TextView)findViewById(R.id.levelNumberText);
        gear = (TextView)findViewById(R.id.gearNumberText);

        // assign values to screen
        refreshValues();

        // load sound player
        sound = new SoundPoolPlayer(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.summary_actions, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }

    public void btnGearIncrement(View v) {
        sound.playSound(R.raw.gear1);
        player.incrementGear();
        refreshValues();
    }

    public void btnGearDecrement(View v) {
        player.decrementGear();
        refreshValues();
    }

    public void btnLevelIncrement(View v) {
        sound.playSound(R.raw.levelUp1);
        player.incrementLevel();
        refreshValues();
    }

    public void btnLevelDecrement(View v) {
        player.decrementLevel();
        refreshValues();
    }

    private void refreshValues() {
        gear.setText(Integer.toString(player.getGear()));
        level.setText((Integer.toString(player.getLevel())));
        strength.setText(Integer.toString(player.getTotal()));

        Data.adapter.notifyDataSetChanged();
        Data.adapter.saveData();
    }
}
