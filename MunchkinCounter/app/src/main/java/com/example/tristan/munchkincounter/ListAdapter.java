package com.example.tristan.munchkincounter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * ListAdapter class is used to manage a custom cell in a listview.
 */
public class ListAdapter extends BaseAdapter {

    private ArrayList<Player> listData;
    private LayoutInflater layoutInflater;

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
    }

    public void deletePlayer(int position) {
        listData.remove(position);
        this.notifyDataSetChanged();
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
