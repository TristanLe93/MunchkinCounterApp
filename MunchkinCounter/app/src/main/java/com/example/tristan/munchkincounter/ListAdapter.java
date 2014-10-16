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

    public ListAdapter(Context context, ArrayList<Player> theData) {
        listData = theData;
        layoutInflater = LayoutInflater.from(context);
        this.notifyDataSetChanged();
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
            holder.levelText = (TextView)convertView.findViewById(R.id.levelText);
            holder.totalText = (TextView)convertView.findViewById(R.id.totalText);
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
        holder.levelView.setTypeface(tf);
        holder.totalView.setTypeface(tf);
        holder.levelText.setTypeface(tf);
        holder.totalText.setTypeface(tf);

        return convertView;
    }



    // store textviews in static class for easier access
    static class ViewHolder {
        TextView playerNameView;
        TextView levelView;
        TextView totalView;
        TextView levelText;
        TextView totalText;
    }
}
