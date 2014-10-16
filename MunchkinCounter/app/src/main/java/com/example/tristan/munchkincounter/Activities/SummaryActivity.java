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

import com.example.tristan.munchkincounter.ListAdapter;
import com.example.tristan.munchkincounter.Player;
import com.example.tristan.munchkincounter.PlayerData;
import com.example.tristan.munchkincounter.R;
import com.example.tristan.munchkincounter.SoundPlayer;


public class SummaryActivity extends BaseActivity {
    ListAdapter playerList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);

        // initialise player data
        PlayerData.getInstance();
        PlayerData.initialise(this);

        // initialise ListAdapter for listView
        playerList = new ListAdapter(this, PlayerData.get());

        // setup listView
        ListView playerList = (ListView)findViewById(R.id.playerList);
        playerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View v, int position, long l) {
                startDetailActivity(position);
            }
        });

        registerForContextMenu(playerList);
        findViewById(R.id.btn_level).setOnClickListener(onClickListener);
        findViewById(R.id.btn_total).setOnClickListener(onClickListener);

        // load soundPlayer
        SoundPlayer.getInstance();
        SoundPlayer.initSounds(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actions_summary, menu);
        return true;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
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
                PlayerData.resetAllPlayers();
                return true;
            case R.id.delete_players:
                PlayerData.removeAllPlayers();
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
                PlayerData.removePlayer(info.position);
                return true;
            case R.id.reset_player:
                PlayerData.resetPlayer(info.position);

            default:
                return super.onContextItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        playerList.notifyDataSetChanged();
    }

    /**
     * Clean up memory and close app.
     */
    @Override
    public void onBackPressed() {
        SoundPlayer.cleanUp();
        PlayerData.saveData();
        finish();
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

    // add a new player to the listview
    private void addNewPlayer(String name) {
        Player p = new Player(name);
        PlayerData.addNewPlayer(p);
    }

    private void startDetailActivity(int position) {
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra("position", position);
        startActivityForResult(intent, 1);
        overridePendingTransition(R.anim.push_right_in, R.anim.push_left_out);
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int selected = getResources().getColor(R.color.background_color);
            int notSelected = getResources().getColor(R.color.monster_color);

            switch (view.getId()) {
                case R.id.btn_level:
                    findViewById(R.id.btn_level).setBackgroundColor(selected);
                    findViewById(R.id.btn_total).setBackgroundColor(notSelected);
                    PlayerData.setSortByLevel_True();
                    break;
                case R.id.btn_total:
                    findViewById(R.id.btn_level).setBackgroundColor(notSelected);
                    findViewById(R.id.btn_total).setBackgroundColor(selected);
                    PlayerData.setSortByLevel_False();
                    break;
            }

            PlayerData.sortList();
            playerList.notifyDataSetChanged();
        }
    };
}
