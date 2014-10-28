package com.example.tristan.munchkincounter;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.SoundPool;
import android.preference.PreferenceManager;

import java.util.HashMap;

public class SoundPlayer {
    private static SoundPlayer instance;
    private static SoundPool soundPool;
    private static HashMap<Integer, Integer> soundPoolMap;
    private static Context context;

    private SoundPlayer() {
    }

    /**
     * Request the instance of SoundPlayer and creates it
     * if it does not exist.
     *
     * @return Returns the single instance of SoundPlayer
     */
    public static synchronized SoundPlayer getInstance() {
        if (instance == null) {
            instance = new SoundPlayer();
        }

        return instance;
    }

    /**
     * Initialises the storage for the sounds and loads
     * the sounds.
     *
     * @param theContext The Application context
     */
    public static void initSounds(Context theContext) {
        context = theContext;
        soundPool = new SoundPool(4, AudioManager.STREAM_MUSIC, 0);
        soundPoolMap = new HashMap<Integer, Integer>();

        // load sounds
        soundPoolMap.put(R.raw.tick, soundPool.load(context, R.raw.tick, 1));
    }

    /**
     * Plays a sound.
     *
     * @param resource The resource value of the sound to be played.
     */
    public static void playSound(int resource) {
        if (!canPlaySound()) return;
        soundPool.play(soundPoolMap.get(resource), 1f, 1f, 0, 0, 1f);
    }

    /**
     * Deallocates the resources and Instance of SoundPlayer.
     */
    public static void cleanUp() {
        soundPool.release();
        soundPool = null;
        soundPoolMap.clear();
        instance = null;
    }

    /**
     * Does the user's settings allow the app to play sound?
     * @return true or false
     */
    private static boolean canPlaySound() {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        boolean canPlay = pref.getBoolean("pref_enableSounds", true);

        return canPlay;
    }
}
