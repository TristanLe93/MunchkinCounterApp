package com.example.tristan.munchkincounter;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.preference.PreferenceManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class SoundPlayer {
    private static SoundPlayer instance;
    private static SoundPool soundPool;
    private static HashMap<Integer, Integer> soundPoolMap;
    private static Context context;

    private static final int MAX_CHANCE = 9;
    private static final int LEVEL_UP_CHANCE = 2;       // 2/10 chance to play unique sound
    private static final int LEVEL_DOWN_CHANCE = 2;     // 2/10 chance to play unique sound

    private static final int[] levelUpSounds = {R.raw.levelup2};
    private static final int[] levelDownSounds = {R.raw.leveldown2};

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
     * Initialises the storage for the sounds and loads the sounds.
     *
     * @param theContext The Application context
     */
    public static void initSounds(Context theContext) {
        context = theContext;
        soundPool = new SoundPool(4, AudioManager.STREAM_MUSIC, 0);
        soundPoolMap = new HashMap<Integer, Integer>();

        // load sounds
        soundPoolMap.put(R.raw.levelup1, soundPool.load(context, R.raw.levelup1, 1));
        soundPoolMap.put(R.raw.levelup2, soundPool.load(context, R.raw.levelup2, 1));
        soundPoolMap.put(R.raw.leveldown1, soundPool.load(context, R.raw.leveldown1, 1));
        soundPoolMap.put(R.raw.leveldown2, soundPool.load(context, R.raw.leveldown2, 1));
        soundPoolMap.put(R.raw.tick, soundPool.load(context, R.raw.tick, 1));
        soundPoolMap.put(R.raw.winning, soundPool.load(context, R.raw.winning, 1));
    }

    /**
     * Plays a sound.
     *
     * @param resource The resource value of the sound to be played.
     */
    public static void playSound(int resource) {
        if (!canPlaySound()) return;

        // get random song to play
        /*
        List<Integer> keys = new ArrayList<Integer>(soundPoolMap.keySet());
        Random r = new Random();
        int randNum = r.nextInt(keys.size());
        int res = keys.get(randNum);
        */
        soundPool.play(soundPoolMap.get(resource), 1f, 1f, 0, 0, 1f);
    }

    public static void playMedia(int resource) {
        MediaPlayer mediaPlayer = MediaPlayer.create(context, resource);
        mediaPlayer.start();
    }

    /**
     * Plays a level up sound.
     * The chance of playing a unique sound is 2/10 chance.
     */
    public static void playLevelUp() {
        Random r = new Random();
        int chance = r.nextInt(MAX_CHANCE);

        if (chance < LEVEL_UP_CHANCE) {
            int num = r.nextInt(levelUpSounds.length);
            int resource = levelUpSounds[num];
            playSound(resource);
        } else {
            playSound(R.raw.levelup1);
        }
    }

    /**
     * Plays a level down sound.
     * The chance of playing a unique sound is 2/10 chance.
     */
    public static void playLevelDown() {
        Random r = new Random();
        int chance = r.nextInt(MAX_CHANCE);

        if (chance < LEVEL_DOWN_CHANCE) {
            int num = r.nextInt(levelDownSounds.length);
            int resource = levelDownSounds[num];
            playSound(resource);
        } else {
            playSound(R.raw.leveldown1);
        }
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
