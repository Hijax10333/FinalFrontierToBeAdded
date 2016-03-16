package a420.blaze.it.final_frontier.Assets;

import android.media.SoundPool;

public class Sound {

    //maximum number of concurrent sounds set to 5
    public static final int MAX_CONCURRENT_SOUNDS = 5;

    //each sound effect given an id
    private int mSoundId;

    //the soundpool playing the sound effect
    private SoundPool mSoundPool;

    private float mVolume;

    // /////////////////////////////////////////////////////////////////////////
    // Constructors
    // /////////////////////////////////////////////////////////////////////////

    //new sound effect is created with the parameters of its sound pool and its id
    //default playback volume is assigned

    public Sound(SoundPool soundPool, int soundId) {

        mSoundId = soundId;
        mSoundPool = soundPool;
        mVolume = 1.0f;
    }


    //method to play the sound effect
    public void play() {
        mSoundPool.play(mSoundId, mVolume, mVolume, 0, 0, 1);
    }


    //the default playback volume is set
    public void setVolume(float volume) {
        mVolume = volume;
    }

    //sound effect is disposed when it is finished
    public void dispose() {
        mSoundPool.unload(mSoundId);
    }
}