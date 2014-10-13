package com.example.tristan.munchkincounter.Activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.tristan.munchkincounter.Data;
import com.example.tristan.munchkincounter.FontCache;
import com.example.tristan.munchkincounter.Player;
import com.example.tristan.munchkincounter.R;
import com.example.tristan.munchkincounter.SoundPlayer;

import java.util.Random;

/**
 * Created by Tristan on 7/10/2014.
 */
public class CalculatorActivity extends ActionBarActivity {
    private Player player;
    private int playerModifier;
    private int monsterLevel;
    private int monsterModifier;

    /**
     * Initialise the battle screen
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        setup();
        setButtonListeners();
        updateUI();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.actions_calculator, menu);
        return true;
    }

    /**
     * Handles the items selection on the action bar.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Data.adapter.saveData();
                finish();
                overridePendingTransition(R.anim.push_right_out, R.anim.push_left_in);
                return true;
            case R.id.roll_dice:
                showDiceRoll();
                return true;
            case R.id.show_scoreboard:
                showScoreboard();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * When "up" key is pressed, save the data
     * and return to details activity.
     */
    @Override
    public void onBackPressed() {
        Data.adapter.saveData();
        finish();
        overridePendingTransition(R.anim.push_right_out, R.anim.push_left_in);
    }

    /**
     * Sets up the initial text values of the calculator.
     */
    private void setup() {
        Bundle extras = getIntent().getExtras();

        // get the player and assign the name to textView
        int pos = extras.getInt("position");
        player = Data.adapter.getItem(pos);
        assignText(R.id.txt_player_name, player.getName());
        playerModifier = player.getBonus();

        monsterLevel = 0;
        monsterModifier = 0;

        // set the Strength TextView textfont
        Typeface tf = FontCache.get("fonts/quasimodo.ttf", this.getBaseContext());
        TextView pStr = (TextView)findViewById(R.id.txt_player_strength);
        TextView mStr = (TextView)findViewById(R.id.txt_monster_strength);
        pStr.setTypeface(tf);
        mStr.setTypeface(tf);
    }

    /**
     * Sets all buttons in the screen to the local OnClickListener().
     */
    private void setButtonListeners() {
        // player buttons
        setOnClickListener(R.id.btn_player_level_up);
        setOnClickListener(R.id.btn_player_level_down);
        setOnClickListener(R.id.btn_player_gear_up);
        setOnClickListener(R.id.btn_player_gear_down);
        setOnClickListener(R.id.btn_player_mod_up);
        setOnClickListener(R.id.btn_player_mod_down);

        // monster buttons
        setOnClickListener(R.id.btn_monster_level_up);
        setOnClickListener(R.id.btn_monster_level_down);
        setOnClickListener(R.id.btn_monster_mod_up);
        setOnClickListener(R.id.btn_monster_mod_down);
    }

    /**
     * Helper method to set the local OnClickListener().
     * @param resId The resource id
     */
    private void setOnClickListener(int resId) {
        ImageView img = (ImageView)findViewById(resId);
        img.setOnClickListener(onClickListener);
    }

    /**
     * Sets a TextView a string value.
     * @param resId The resource id
     * @param text The text to be set
     */
    private void assignText(int resId, String text) {
        TextView textview = (TextView)findViewById(resId);
        textview.setText(text);
    }

    /**
     * Sets a TextView a int value.
     * @param resId The resource id
     * @param num The int to be set
     */
    private void assignText(int resId, int num) {
        TextView textview = (TextView)findViewById(resId);
        String text = Integer.toString(num);
        textview.setText(text);
    }

    /**
     * Show a random D6 dice roll.
     */
    private void showDiceRoll() {
        // roll the dice and grab its related image
        Random random = new Random();
        int randNum = random.nextInt(6) + 1;
        String imgName = "dice" + randNum;
        int imgId = getResources().getIdentifier(imgName, "drawable", getPackageName());

        // set the dice image
        ImageView image = new ImageView(this);
        image.setImageResource(imgId);

        // build the AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("You rolled a...");
        builder.setView(image);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // dismiss dialog
            }
        });

        // create and show
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    /**
     * Shows the current status of the scoreboard.
     */
    private void showScoreboard() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Scoreboard");

        ListView lv = new ListView(this);
        lv.setAdapter(Data.adapter);
        builder.setView(lv);

        // set dismiss button
        builder.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        dialog.dismiss();
                    }
                });

        // create and show
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    /**
     * Updates the UI of current values
     */
    private void updateUI() {
        // assign player values
        assignText(R.id.txt_player_level, player.getLevel());
        assignText(R.id.txt_player_gear, player.getGear());
        assignText(R.id.txt_player_modifier, playerModifier);

        // assign monster values
        assignText(R.id.txt_monster_level, monsterLevel);
        assignText(R.id.txt_monster_modifier, monsterModifier);

        // assign total strength values
        int playerStrength = player.getLevel() + player.getGear() + playerModifier;
        int monsterStrength = monsterLevel + monsterModifier;
        assignText(R.id.txt_player_strength, playerStrength);
        assignText(R.id.txt_monster_strength, monsterStrength);
    }

    /**
     * Handles all button inputs by the user.
     * Update the UI after input.
     */
    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                // player buttons
                case R.id.btn_player_level_up:
                    player.incrementLevel();
                    SoundPlayer.playSound(R.raw.levelup1);
                    break;
                case R.id.btn_player_level_down:
                    player.decrementLevel();
                    SoundPlayer.playSound(R.raw.leveldown1);
                    break;
                case R.id.btn_player_gear_up:
                    player.incrementGear();
                    SoundPlayer.playSound(R.raw.tick);
                    break;
                case R.id.btn_player_gear_down:
                    player.decrementGear();
                    SoundPlayer.playSound(R.raw.tick);
                    break;
                case R.id.btn_player_mod_up:
                    playerModifier += 1;
                    SoundPlayer.playSound(R.raw.tick);
                    break;
                case R.id.btn_player_mod_down:
                    playerModifier -= 1;
                    SoundPlayer.playSound(R.raw.tick);
                    break;
                // monster buttons
                case R.id.btn_monster_level_up:
                    monsterLevel += 1;
                    SoundPlayer.playSound(R.raw.tick);
                    break;
                case R.id.btn_monster_level_down:
                    monsterLevel -= 1;
                    SoundPlayer.playSound(R.raw.tick);
                    break;
                case R.id.btn_monster_mod_up:
                    monsterModifier += 1;
                    SoundPlayer.playSound(R.raw.tick);
                    break;
                case R.id.btn_monster_mod_down:
                    monsterModifier -= 1;
                    SoundPlayer.playSound(R.raw.tick);
                    break;
            }

            updateUI();
        }
    };
}
