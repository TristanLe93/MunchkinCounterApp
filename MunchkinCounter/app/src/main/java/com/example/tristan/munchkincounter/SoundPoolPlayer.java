package com.example.tristan.munchkincounter;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

/**
 * Created by Tristan on 4/10/2014.
 */
public class SoundPoolPlayer {
    private SoundPool player;
    private HashMap sounds = new HashMap();
    //private HashMap gearup = new HashMap();
    private Random randNum = new Random();
    private int[] gearup = {R.raw.gearup1, R.raw.gearup2, R.raw.gearup3};


    public SoundPoolPlayer(Context context) {
        player = new SoundPool(4, AudioManager.STREAM_MUSIC, 0);

        // load sounds
        player.load(context, R.raw.gearup1, 1);
        player.load(context, R.raw.gearup2, 1);
        player.load(context, R.raw.gearup3, 1);

        sounds.put(R.raw.levelup1, player.load(context, R.raw.levelup1, 1));
        sounds.put(R.raw.levelup2, player.load(context, R.raw.levelup2, 1));
        sounds.put(R.raw.levelup3, player.load(context, R.raw.levelup3, 1));
        sounds.put(R.raw.levelup4, player.load(context, R.raw.levelup4, 1));
        sounds.put(R.raw.level10, player.load(context, R.raw.level10, 1));
        sounds.put(R.raw.leveldown1, player.load(context, R.raw.leveldown1, 1));
    }

    // plays a sound in the hashmap.
    // usage: SoundPoolPlayer.playSound(R.raw.<sound_name>);
    public void playSound(int resource) {
        int soundId = (Integer)sounds.get(resource);
        player.play(soundId, 1f, 1f, 0, 0, 1f);
    }

    public void playGearUpSound() {
        int soundId = randNum.nextInt(gearup.length);
        player.play(soundId, 1f, 1f, 0, 0, 1f);
    }

    // clean up
    public void release() {
        player.release();
        player = null;
    }
}
