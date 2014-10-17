package com.example.tristan.munchkincounter.Activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.tristan.munchkincounter.Data;
import com.example.tristan.munchkincounter.FontCache;
import com.example.tristan.munchkincounter.Player;
import com.example.tristan.munchkincounter.R;
import com.example.tristan.munchkincounter.SoundPlayer;


public class DetailActivity extends BaseActivity {
    private Player player;
    private int position;

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
                Data.adapter.killPlayer(position);
                updateUI();
                return true;
            case R.id.reset_player:
                player.reset();
                updateUI();
                return true;
            case R.id.remove_player:
                dialogDelete();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        updateUI();
    }

    /**
     * Set the player data and TextView text fonts.
     */
    private void setup() {
        // get the player from list
        Bundle extras = getIntent().getExtras();
        player = Data.adapter.getItem(extras.getInt("position"));
        position = extras.getInt("position");
        getSupportActionBar().setTitle(player.getName());

        // set strength textfont
        Typeface tf = FontCache.get("fonts/quasimodo.ttf", this.getBaseContext());
        TextView strength = (TextView)findViewById(R.id.txt_strength);
        strength.setTypeface(tf);
    }

    /**
     * Display a dialog to warn the user if they want
     * to remove the player from the game.
     */
    private void dialogDelete() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Are you sure?");
        builder.setMessage("Action: Remove player " + player.getName() + ".");

        // dismiss
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });

        // remove the player from the game
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Data.adapter.deletePlayer(position);
                transitionBack();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
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
        startActivityForResult(intent, 2);
        overridePendingTransition(R.anim.push_right_in, R.anim.push_left_out);
    }

    private void btnLevelIncrement() {
        player.incrementLevel();

        if (player.getLevel() % 10 == 0) {
            SoundPlayer.playMedia(R.raw.winning);
        } else {
            SoundPlayer.playLevelUp();
        }
    }

    private void btnLevelDecrement() {
        SoundPlayer.playLevelDown();
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
