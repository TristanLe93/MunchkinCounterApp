package com.example.tristan.munchkincounter;

import android.content.Context;
import android.content.SharedPreferences;
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

/**
 * ListAdapter class is used to manage a custom cell in a listview.
 */
public class ListAdapter extends BaseAdapter {

    private ArrayList<Player> listData;
    private LayoutInflater layoutInflater;
    private String key = "playerData";

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

        return convertView;
    }

    // store textviews in static class for easier access
    static class ViewHolder {
        TextView playerNameView;
        TextView levelView;
        TextView totalView;
    }
}
