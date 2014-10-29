package com.example.tristan.munchkincounter.Activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.example.tristan.munchkincounter.Data;
import com.example.tristan.munchkincounter.ListAdapter;
import com.example.tristan.munchkincounter.Player;
import com.example.tristan.munchkincounter.R;
import com.example.tristan.munchkincounter.SoundPlayer;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;


public class SummaryActivity extends BaseActivity {

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

        findViewById(R.id.btn_level).setOnClickListener(onClickListener);
        findViewById(R.id.btn_total_sort).setOnClickListener(onClickListener);

        // load soundPlayer
        SoundPlayer.getInstance();
        SoundPlayer.initSounds(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.actions_summary, menu);
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
            case R.id.reset_players:
                dialogDeleteGame();
                return true;
            case R.id.delete_players:
                dialogResetGame();
                return true;
            case R.id.settings:
                Intent i = new Intent(this, UserSettingActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.push_right_out, R.anim.push_left_in);
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

    /**
     * Clean up memory and close app.
     */
    @Override
    public void onBackPressed() {
        SoundPlayer.cleanUp();
        Data.adapter.saveData();
        finish();
    }

    // Shows dialog to input new player name
    private void showInputDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add new player");

        // Create EditText for entry
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);
        input.setTextSize(44);
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

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // dismiss
            }
        });

        // show dialog with keyboard input
        AlertDialog dialog = builder.create();
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
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

    private void dialogResetGame() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("New Game?");
        builder.setMessage("Reset all players for a new game of Munchkin.");

        // delete all players from the game
        builder.setPositiveButton("New Game", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Data.adapter.resetAllPlayers();
            }
        });

        // dismiss
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void dialogDeleteGame() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete Game?");
        builder.setMessage("Delete all players from the scoreboard.");

        // delete all players from the game
        builder.setPositiveButton("Delete All", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Data.adapter.deleteAllPlayers();
            }
        });

        // dismiss
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int selected = getResources().getColor(R.color.select_color);
            int notSelected = getResources().getColor(R.color.background_color);

            switch (view.getId()) {
                case R.id.btn_level:
                    findViewById(R.id.btn_level).setBackgroundColor(selected);
                    findViewById(R.id.btn_total_sort).setBackgroundColor(notSelected);
                    Data.sortByLevel = true;
                    break;
                case R.id.btn_total_sort:
                    findViewById(R.id.btn_level).setBackgroundColor(notSelected);
                    findViewById(R.id.btn_total_sort).setBackgroundColor(selected);
                    Data.sortByLevel = false;
                    break;
            }

            Data.adapter.sortList();
            Data.adapter.notifyDataSetChanged();
        }
    };
}
