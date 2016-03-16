package a420.blaze.it.final_frontier.Assets;


import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.SoundPool;

import java.util.HashMap;

public class AssetStore {

    //hashmaps used to store the assets and allow the program to call for them by their name

    //asset store created for bitmaps
    private HashMap<String, Bitmap> mBitmaps;

    //asset store created for music
    private HashMap<String, Music> mMusic;

    //asset store created for sounds
    private HashMap<String, Sound> mSounds;
    private SoundPool mSoundPool;



    //constructor
    public AssetStore() {

        mBitmaps = new HashMap<String, Bitmap>();
        mMusic = new HashMap<String, Music>();
        mSounds = new HashMap<String, Sound>();
        mSoundPool = new SoundPool(Sound.MAX_CONCURRENT_SOUNDS,
                AudioManager.STREAM_MUSIC, 0);
    }

    //bitmap is added to the store along with its name
    //false is returned if it cannot be added e.g. if one with that name already exists
    public boolean add(String assetName, Bitmap asset) {
        if (mBitmaps.containsKey(assetName))
            return false;

        mBitmaps.put(assetName, asset);
        return true;
    }

    //the same process applies for music and sounds
    public boolean add(String assetName, Music asset) {
        if (mBitmaps.containsKey(assetName))
            return false;

        mMusic.put(assetName, asset);
        return true;
    }

    public boolean add(String assetName, Sound asset) {
        if (mSounds.containsKey(assetName))
            return false;

        mSounds.put(assetName, asset);
        return true;
    }

    //load and add

    //getter methods used to retrive the bitmap, sound and music files
    public Bitmap getBitmap(String assetName) {
        return mBitmaps.get(assetName);
    }

    public Music getMusic(String assetName) {
        return mMusic.get(assetName);
    }

    public Sound getSound(String assetName) {
        return mSounds.get(assetName);
    }








//end of class
}
