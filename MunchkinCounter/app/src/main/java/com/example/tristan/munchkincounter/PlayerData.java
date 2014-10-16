package com.example.tristan.munchkincounter;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


public class PlayerData {
    private static ArrayList<Player> data;
    private static PlayerData instance;
    private static Context context;
    private static boolean sortByLevel;
    private static final String KEY = "playerData";

    private PlayerData() {
    }

    public static ArrayList<Player> get() {
        return data;
    }

    public static void setSortByLevel_True() {
        sortByLevel = true;
    }

    public static void setSortByLevel_False() {
        sortByLevel = false;
    }

    public static PlayerData getInstance() {
        if (instance == null) {
            instance = new PlayerData();
        }

        return instance;
    }

    public static void initialise(Context theContext) {
        context = theContext;
        sortByLevel = true;
        data = new ArrayList<Player>();
        readData();
    }

    public static void cleanUp() {
        instance = null;
        data.clear();
    }

    public static void addNewPlayer(Player p) {
        data.add(p);
    }

    public static Player getPlayer(int position) {
        return data.get(position);
    }

    public static void removePlayer(int position) {
        data.remove(position);
    }
    public static void removePlayer(Player p) {
        data.remove(p);
    }

    public static void resetPlayer(int position) {
        data.get(position).reset();
    }

    public static void killPlayer(Player p) {
        p.setGearZero();

        // get user setting for death penalty
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        String value = pref.getString("pref_deathPenalty", "0");
        int i = Integer.parseInt(value);

        switch (i) {
            case -1: p.decrementLevel(); break;
            case -2: p.decrementLevel(); p.decrementLevel(); break;
            case 1: p.setLevelOne(); break;
        }
    }

    public static void resetAllPlayers() {
        for (int i = 0; i < data.size(); i++) {
            resetPlayer(i);
        }
    }

    public static void removeAllPlayers() {
        while (data.size() > 0) {
            removePlayer(0);
        }
    }

    // save player data to persistent store
    public static void saveData() {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        Gson gson = new Gson();

        String json = gson.toJson(data);

        editor.putString(KEY, json);
        editor.commit();
    }

    // read player data from persistent store
    private static void readData() {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        Gson gson = new Gson();
        String json = sharedPrefs.getString(KEY, null);
        Type type = new TypeToken<ArrayList<Player>>() {}.getType();
        ArrayList<Player> list = gson.fromJson(json, type);

        if (list != null) {
            data = list;
        }
    }

    public static void sortList() {
        if (sortByLevel) {
            sortByLevel();
        } else {
            sortByTotal();
        }
    }

    private static void sortByLevel() {
        Collections.sort(data, new Comparator<Player>() {
            @Override
            public int compare(Player p1, Player p2) {
                return p2.getLevel() - p1.getLevel();
            }
        });
    }

    private static void sortByTotal() {
        Collections.sort(data, new Comparator<Player>() {
            @Override
            public int compare(Player p1, Player p2) {
                return p2.getTotal() - p1.getTotal();
            }
        });
    }
}
