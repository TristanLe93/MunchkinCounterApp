package com.example.tristan.munchkincounter;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * ListAdapter class is used to manage a custom cell in a listview.
 */
public class ListAdapter extends BaseAdapter {
    private ArrayList<Player> listData;
    private LayoutInflater layoutInflater;
    private final String key = "playerData";

    public ArrayList<Player> getListData() {
        return listData;
    }

    public ListAdapter(Context context) {
        listData = new ArrayList<Player>();
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return listData.size();
    }

    @Override
    public Player getItem(int position) {
        return listData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void addNewPlayer(Player p) {
        listData.add(p);
        this.notifyDataSetChanged();
        saveData();
    }

    public void deletePlayer(int position) {
        listData.remove(position);
        this.notifyDataSetChanged();
        saveData();
    }

    public void resetPlayer(int position) {
        Player p = listData.get(position);
        p.reset();
        this.notifyDataSetChanged();
        saveData();
    }

    public void killPlayer(int position) {
        Player p = listData.get(position);
        p.setGearZero();

        // get user setting for death penalty
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(
                layoutInflater.getContext());
        String value = pref.getString("pref_deathPenalty", "0");
        int i = Integer.parseInt(value);

        /**
         * -1: Reduce player level by ONE
         * -2: Reduce player level by TWO
         * 1: Make player level 1
         */
        switch (i) {
           case -1: p.decrementLevel(); break;
           case -2: p.decrementLevel(); p.decrementLevel(); break;
           case 1: p.setLevelOne(); break;
        }
    }

    /**
     * Resets all players to level 1 with zero gear bonus.
     */
    public void resetAllPlayers() {
        for (int i = 0; i < listData.size(); i++) {
            resetPlayer(i);
        }
    }

    public void deleteAllPlayers() {
        while (listData.size() > 0) {
            deletePlayer(0);
        }
    }

    // save player data to persistent store
    public void saveData() {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(layoutInflater.getContext());
        SharedPreferences.Editor editor = sharedPrefs.edit();
        Gson gson = new Gson();

        String json = gson.toJson(listData);

        editor.putString(key, json);
        editor.commit();
    }

    // read player data from persistent store
    public void readData() {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(layoutInflater.getContext());
        Gson gson = new Gson();
        String json = sharedPrefs.getString(key, null);
        Type type = new TypeToken<ArrayList<Player>>() {}.getType();
        ArrayList<Player> list = gson.fromJson(json, type);

        if (list != null) {
            listData = list;
            this.notifyDataSetChanged();
        }
    }

    public void sortList() {
        if (Data.sortByLevel) {
            sortByLevel();
        } else {
            sortByTotal();
        }
    }

    private void sortByLevel() {
        Collections.sort(listData, new Comparator<Player>() {
            @Override
            public int compare(Player player1, Player player2) {
                return player2.getLevel() - player1.getLevel();
            }
        });
    }

    private void sortByTotal() {
        Collections.sort(listData, new Comparator<Player>() {
            @Override
            public int compare(Player player1, Player player2) {
                return player2.getTotal() - player1.getTotal();
            }
        });
    }

    /**
     * Create the list view cell
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        // set the view holder with textview elements
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.list_cell, null);
            holder = new ViewHolder();
            holder.playerNameView = (TextView)convertView.findViewById(R.id.playerNameText);
            holder.levelView = (TextView)convertView.findViewById(R.id.levelNumberText);
            holder.totalView = (TextView)convertView.findViewById(R.id.totalNumberText);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder)convertView.getTag();
        }

        // set player information
        Player player = listData.get(position);
        holder.playerNameView.setText(player.getName());
        holder.levelView.setText(Integer.toString(player.getLevel()));
        holder.totalView.setText(Integer.toString(player.getTotal()));

        // set text font
        Typeface tf = FontCache.get("fonts/quasimodo.ttf", layoutInflater.getContext());
        holder.playerNameView.setTypeface(tf);
        holder.totalView.setTypeface(tf);

        return convertView;
    }



    // store textviews in static class for easier access
    static class ViewHolder {
        TextView playerNameView;
        TextView levelView;
        TextView totalView;
    }
}
