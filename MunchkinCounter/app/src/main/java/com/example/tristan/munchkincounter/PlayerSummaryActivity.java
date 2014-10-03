package com.example.tristan.munchkincounter;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;


public class PlayerSummaryActivity extends ActionBarActivity {

    private ListView playerList;
    private PlayerListAdapter listAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_summary);

        ArrayList<Player> players = new ArrayList<Player>();
        players.add(new Player("Tristan"));
        players.add(new Player("Chino"));
        players.add(new Player("Tristan"));
        players.add(new Player("Lachlan"));

        listAdapter = new PlayerListAdapter(this, players);
        playerList = (ListView)findViewById(R.id.playerList);
        playerList.setAdapter(listAdapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.player_summary, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
