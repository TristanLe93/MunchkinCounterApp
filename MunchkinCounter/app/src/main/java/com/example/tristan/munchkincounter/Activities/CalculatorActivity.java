package com.example.tristan.munchkincounter.Activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.tristan.munchkincounter.Data;
import com.example.tristan.munchkincounter.FontCache;
import com.example.tristan.munchkincounter.Player;
import com.example.tristan.munchkincounter.R;
import com.example.tristan.munchkincounter.SoundPlayer;

import java.util.ArrayList;

/**
 * Created by Tristan on 7/10/2014.
 */
public class CalculatorActivity extends BaseActivity {
    private Player player;
    private int playerModifier;
    private int monsterLevel;
    private int monsterModifier;

    private String helper;
    private int helperStrength;

    private int listPosition;

    /**
     * Initialise the battle screen
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setup();
        setButtonListeners();
        updateUI();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actions_calculator, menu);
        return true;
    }

    /**
     * Handles the items selection on the action bar.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_helper:
                showScoreboard();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Sets up the initial text values of the calculator.
     */
    private void setup() {
        Bundle extras = getIntent().getExtras();

        // get the player and assign the name to textView
        listPosition = extras.getInt("position");
        player = Data.adapter.getItem(listPosition);
        playerModifier = player.getBonus();
        helper = "";
        helperStrength = 0;

        monsterLevel = 0;
        monsterModifier = 0;

        // set the Strength TextView textfont
        Typeface tf = FontCache.get("fonts/quasimodo.ttf", this.getBaseContext());
        TextView pStr = (TextView)findViewById(R.id.txt_player_strength);
        TextView mStr = (TextView)findViewById(R.id.txt_monster_strength);
        TextView pName = (TextView)findViewById(R.id.txt_player_name);
        TextView mName = (TextView)findViewById(R.id.txt_monster_name);
        pStr.setTypeface(tf);
        mStr.setTypeface(tf);
        pName.setTypeface(tf);
        mName.setTypeface(tf);

        // Change monster color side.
        // There is a bug that changes the color so we going to override it for now....)
        findViewById(R.id.monster_panel).setBackgroundColor(getResources().getColor(R.color.monster_color));
        findViewById(R.id.monster_stats_panel).setBackgroundColor(getResources().getColor(R.color.monster_color));
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
     * Shows the current status of the scoreboard.
     */
    private void showScoreboard() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select combat helper...");

        // create player selection list
        ArrayList<String> playerData = new ArrayList<String>();
        playerData.add("None");
        for (int i = 0; i < Data.adapter.getCount(); i++) {
            Player p = Data.adapter.getItem(i);

            if (listPosition == i) {
                String doppelText = doppelgangerText(p);
                playerData.add(doppelText);
            } else {
                playerData.add(p.toString());
            }
        }

        // set dismiss button
        builder.setPositiveButton("Cancel",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        dialog.dismiss();
                    }
                });

        // setup the listView
        ListView lv = new ListView(this);
        ArrayAdapter adapter = new ArrayAdapter(
                this,
                android.R.layout.simple_list_item_1,
                playerData);
        lv.setAdapter(adapter);
        builder.setView(lv);

        // create and show
        final AlertDialog dialog = builder.create();
        dialog.show();

        // updates the UI to include a selected combat helper
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long itemId) {
                if (position == 0) {
                    // No helper was selected
                    helperStrength = 0;
                    helper = "";
                } else if (listPosition == position-1) {
                    // doppelganger was selected
                    helperStrength = player.getLevel() + player.getGear() + playerModifier;
                    helper = " + Doppelganger(" + helperStrength + ")";
                } else  {
                    // grab the helper's details
                    Player p = Data.adapter.getItem(position-1);
                    helperStrength = p.getTotal();
                    helper = " + " + p.getName() + "(" + helperStrength + ")";
                }

                updateUI();
                dialog.dismiss();
            }
        });
    }

    private String doppelgangerText(Player p) {
        StringBuilder sb = new StringBuilder();
        sb.append("Doppelganger");
        sb.append(" - Level: ");
        sb.append(p.getLevel());
        sb.append(" Total: ");
        sb.append(p.getLevel()+p.getGear()+playerModifier);

        return sb.toString();
    }

    /**
     * Updates the UI of current values
     */
    private void updateUI() {
        // assign player values
        assignText(R.id.txt_player_level, player.getLevel());
        assignText(R.id.txt_player_gear, player.getGear());
        assignText(R.id.txt_player_modifier, playerModifier);
        assignText(R.id.txt_player_name, player.getName() + helper);

        // assign monster values
        assignText(R.id.txt_monster_level, monsterLevel);
        assignText(R.id.txt_monster_modifier, monsterModifier);

        // assign total strength values
        int playerStrength = player.getLevel() + player.getGear() + playerModifier + helperStrength;
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
