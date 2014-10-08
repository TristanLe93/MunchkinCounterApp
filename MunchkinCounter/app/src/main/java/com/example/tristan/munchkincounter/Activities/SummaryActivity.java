package com.example.tristan.munchkincounter.Activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.tristan.munchkincounter.Activities.DetailActivity;
import com.example.tristan.munchkincounter.Data;
import com.example.tristan.munchkincounter.ListAdapter;
import com.example.tristan.munchkincounter.Player;
import com.example.tristan.munchkincounter.R;
import com.example.tristan.munchkincounter.SoundPlayer;

import java.util.Random;


public class SummaryActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);

        // set the array adapter to listview
        Data.adapter = new ListAdapter(this);
        ListView playerList = (ListView)findViewById(R.id.playerList);
        playerList.setAdapter(Data.adapter);

        // set a listener to transition to detail activity when pressed
        playerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View v, int position, long l) {
                startDetailActivity(position);
            }
        });

        // set context menu for listView
        registerForContextMenu(playerList);

        // load persistent data if available
        Data.adapter.readData();

        // load soundPlayer
        SoundPlayer.getInstance();
        SoundPlayer.initSounds(this);

        // keep screen on
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.summary_actions, menu);
        return true;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.context_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks
        switch (item.getItemId()) {
            case R.id.add_player:
                showInputDialog();
                return true;
            case R.id.roll_dice:
                showDiceRoll();
                return true;
            case R.id.reset_players:
                Data.adapter.resetAllPlayers();
                return true;
            case R.id.delete_players:
                Data.adapter.deleteAllPlayers();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        switch (item.getItemId()) {
            case R.id.delete:
                Data.adapter.deletePlayer(info.position);
                return true;
            case R.id.reset_player:
                Data.adapter.resetPlayer(info.position);
            default:
                return super.onContextItemSelected(item);
        }
    }
    // Shows dialog to input new player name
    private void showInputDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Add new player");

        // Create EditText for entry
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);
        builder.setView(input);

        // Make an "Add" button to create a new player
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String name = input.getText().toString();
                if (name.length() > 0) {
                    addNewPlayer(name);
                } else {
                    showInputDialog();
                }
            }
        });

        // show dialog with keyboard input
        AlertDialog dialog = builder.create();
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        dialog.show();
    }

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

    // add a new player to the listview
    private void addNewPlayer(String name) {
        Player p = new Player(name);
        Data.adapter.addNewPlayer(p);
    }

    private void startDetailActivity(int position) {
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra("position", position);

        startActivity(intent);
        overridePendingTransition(R.anim.push_right_in, R.anim.push_left_out);
    }
}
