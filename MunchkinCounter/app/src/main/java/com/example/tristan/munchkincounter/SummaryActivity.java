package com.example.tristan.munchkincounter;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;


public class SummaryActivity extends ActionBarActivity {

    private ListView playerList;
    private ListAdapter listAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_summary);

        // set the array adapter to listview
        ArrayList<Player> players = new ArrayList<Player>();
        listAdapter = new ListAdapter(this, players);
        playerList = (ListView)findViewById(R.id.playerList);
        playerList.setAdapter(listAdapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.player_summary_actions, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks
        int id = item.getItemId();

        if (id == R.id.action_new) {
                showInputDialog();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // Shows dialog to input new player name
    private void showInputDialog() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Add new player");
        alert.setMessage("Enter name?");

        // Create EditText for entry
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);
        alert.setView(input);

        // Make an "Add" button to create a new player
        alert.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String name = input.getText().toString();
                if (name.length() > 0) {
                    addNewPlayer(name);
                } else {
                    showInputDialog();
                }
            }
        });

        // Make a "Cancel" button. Simply dismiss the alert
        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {}
        });

        alert.show();
    }

    // add a new player to the listview
    private void addNewPlayer(String name) {
        Player p = new Player(name);
        listAdapter.addNewPlayer(p);
    }
}
