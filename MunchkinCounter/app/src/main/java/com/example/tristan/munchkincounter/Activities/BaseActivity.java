package com.example.tristan.munchkincounter.Activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.ImageView;

import com.example.tristan.munchkincounter.PlayerData;
import com.example.tristan.munchkincounter.R;

import java.util.Random;

/**
 * Created by Tristan on 14/10/2014.
 */
public class BaseActivity extends ActionBarActivity {
    /**
     * Initialise the activity.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
    }

    /**
     * Handles the items selection on the action bar.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                transitionBack();
                return true;
            case R.id.roll_dice:
                showDiceRoll();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * When "up" key is pressed, save the data
     * and return to previous activity.
     */
    @Override
    public void onBackPressed() {
        transitionBack();
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
     * Transition Back to the previous screen.
     */
    protected void transitionBack() {
        PlayerData.saveData();
        finish();
        overridePendingTransition(R.anim.push_right_out, R.anim.push_left_in);
    }
}
