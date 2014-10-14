package com.example.tristan.munchkincounter.Activities;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.tristan.munchkincounter.Data;
import com.example.tristan.munchkincounter.FontCache;
import com.example.tristan.munchkincounter.Player;
import com.example.tristan.munchkincounter.R;
import com.example.tristan.munchkincounter.SoundPlayer;


public class DetailActivity extends BaseActivity {
    private Player player;

    /**
     * Initialise the Activity
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setup();
        setOnClickListeners();
        updateUI();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.actions_detail, menu);
        return true;
    }

    /**
     * Handle the back button input.
     * Transition back and save player data.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.kill_player:
                player.setGearZero();
                updateUI();
                return true;
            case R.id.reset_player:
                player.reset();
                updateUI();
                return true;
            case R.id.remove_player:
                int pos = getIntent().getExtras().getInt("position");
                Data.adapter.deletePlayer(pos);
                transitionBack();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Set the player data and TextView text fonts.
     */
    private void setup() {
        // get the player from list
        Bundle extras = getIntent().getExtras();
        player = Data.adapter.getItem(extras.getInt("position"));
        getSupportActionBar().setTitle(player.getName());

        // set strength textfont
        Typeface tf = FontCache.get("fonts/quasimodo.ttf", this.getBaseContext());
        TextView strength = (TextView)findViewById(R.id.txt_strength);
        strength.setTypeface(tf);
    }

    /**
     * Set all buttons OnClickListeners
     */
    private void setOnClickListeners() {
        findViewById(R.id.btn_level_up).setOnClickListener(onClickListener);
        findViewById(R.id.btn_level_down).setOnClickListener(onClickListener);
        findViewById(R.id.btn_gear_up).setOnClickListener(onClickListener);
        findViewById(R.id.btn_gear_down).setOnClickListener(onClickListener);
        findViewById(R.id.btn_mod_up).setOnClickListener(onClickListener);
        findViewById(R.id.btn_mod_down).setOnClickListener(onClickListener);
        findViewById(R.id.btn_battle).setOnClickListener(onClickListener);
    }

    /**
     * Set an int value to a TextView.
     * @param resId The resource Id
     * @param num The number value
     */
    private void setTextValue(int resId, int num) {
        TextView tv = (TextView)findViewById(resId);
        tv.setText(Integer.toString(num));
    }

    /**
     * Update the UI TextView's with player information.
     */
    private void updateUI() {
        setTextValue(R.id.txt_strength, player.getTotal());
        setTextValue(R.id.txt_level, player.getLevel());
        setTextValue(R.id.txt_gear, player.getGear());
        setTextValue(R.id.txt_modifier, player.getBonus());
    }

    /**
     * Handle all button inputs in this screen.
     */
    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.btn_level_up:
                    btnLevelIncrement(); break;
                case R.id.btn_level_down:
                    btnLevelDecrement(); break;
                case R.id.btn_gear_up:
                    btnGearIncrement(); break;
                case R.id.btn_gear_down:
                    btnGearDecrement(); break;
                case R.id.btn_mod_up:
                    btnModIncrement(); break;
                case R.id.btn_mod_down:
                    btnModDecrement(); break;
                case R.id.btn_battle:
                    btnGoToBattle(); break;
            }

            updateUI();
        }
    };

    private void btnGoToBattle() {
        Intent intent = new Intent(this, CalculatorActivity.class);
        int pos = getIntent().getExtras().getInt("position");
        intent.putExtra("position", pos);
        startActivity(intent);
        overridePendingTransition(R.anim.push_right_in, R.anim.push_left_out);
    }

    private void btnLevelIncrement() {
        SoundPlayer.playSound(R.raw.levelup1);
        player.incrementLevel();
    }

    private void btnLevelDecrement() {
        SoundPlayer.playSound(R.raw.leveldown1);
        player.decrementLevel();
    }

    private void btnGearIncrement() {
        SoundPlayer.playSound(R.raw.tick);
        player.incrementGear();
    }

    private void btnGearDecrement() {
        SoundPlayer.playSound(R.raw.tick);
        player.decrementGear();
    }

    private void btnModIncrement() {
        SoundPlayer.playSound(R.raw.tick);
        player.incrementBonus();
    }

    private void btnModDecrement() {
        SoundPlayer.playSound(R.raw.tick);
        player.decrementBonus();
    }
}
