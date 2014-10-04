package com.example.tristan.munchkincounter;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

import java.util.HashMap;

/**
 * Created by Tristan on 4/10/2014.
 */
public class SoundPoolPlayer {
    private SoundPool player;
    private HashMap sounds = new HashMap();

    public SoundPoolPlayer(Context context) {
        player = new SoundPool(4, AudioManager.STREAM_MUSIC, 0);

        // load sounds
        sounds.put(R.raw.gear1, player.load(context, R.raw.gear1, 1));
        sounds.put(R.raw.levelUp1, player.load(context, R.raw.levelUp1, 1));
    }

    // plays a sound in the hashmap.
    // usage: SoundPoolPlayer.playSound(R.raw.<sound_name>);
    public void playSound(int resource) {
        int soundId = (Integer)sounds.get(resource);
        player.play(soundId, 1f, 1f, 0, 0, 1f);
    }

    // clean up
    public void release() {
        player.release();
        player = null;
    }
}
