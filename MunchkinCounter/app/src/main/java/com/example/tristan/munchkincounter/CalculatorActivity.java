package com.example.tristan.munchkincounter;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

/**
 * Created by Tristan on 7/10/2014.
 */
public class CalculatorActivity extends ActionBarActivity {
    private int player = 0;
    private int monster = 0;

    private TextView txtPlayer;
    private TextView txtMonster;
    private TextView txtResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        TextView txtName = (TextView)findViewById(R.id.txt_player_name);
        txtPlayer = (TextView)findViewById(R.id.txt_player_strength);
        txtMonster = (TextView)findViewById(R.id.txt_monster_strength);
        txtResult = (TextView)findViewById(R.id.txt_result);

        // get player strength and name from Bundle
        Bundle extras = getIntent().getExtras();
        player = extras.getInt("strength");
        String name = extras.getString("name");
        int initialStrength = extras.getInt("strength");
        txtName.setText(name + "(" + initialStrength + ")");
        recalc();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }

        return(super.onOptionsItemSelected(item));
    }

    /**
     * Recalculates the combat result defined by:
     * Player's Strength - Monster's Strength.
     */
    private void recalc() {
        int result = player - monster;
        if (result > 0) {
            txtResult.setText("+");
            txtResult.append(Integer.toString(result));
        } else {
            txtResult.setText(Integer.toString(result));
        }

        txtPlayer.setText(Integer.toString(player));
        txtMonster.setText(Integer.toString(monster));
    }

    /**
     * Player strength modifiers
     */
    public void btnMinusTen_player(View v) {
        player -= 10;
        recalc();
    }

    public void btnMinusFive_player(View v) {
        player -= 5;
        recalc();
    }

    public void btnMinusOne_player(View v) {
        player -= 1;
        recalc();
    }

    public void btnPlusOne_player(View v) {
        player += 1;
        recalc();
    }

    public void btnPlusFive_player(View v) {
        player += 5;
        recalc();
    }

    public void btnPlusTen_player(View v) {
        player += 10;
        recalc();
    }

    /**
     * Monster strength modifiers
     */
    public void btnMinusTen_monster(View v) {
        monster -= 10;
        recalc();
    }

    public void btnMinusFive_monster(View v) {
        monster -= 5;
        recalc();
    }

    public void btnMinusOne_monster(View v) {
        monster -= 1;
        recalc();
    }

    public void btnPlusOne_monster(View v) {
        monster += 1;
        recalc();
    }

    public void btnPlusFive_monster(View v) {
        monster += 5;
        recalc();
    }

    public void btnPlusTen_monster(View v) {
        monster += 10;
        recalc();
    }
}
