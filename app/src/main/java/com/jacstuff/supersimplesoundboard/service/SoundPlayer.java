package com.jacstuff.supersimplesoundboard.service;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.AudioAttributes;
import android.media.SoundPool;

import com.jacstuff.supersimplesoundboard.Sound;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SoundPlayer {

    private SoundPool soundPool;
    private final Context context;
    private final Map<Integer, Sound> soundMap;


    public SoundPlayer(Context context){
        this.context = context;
        soundMap = new HashMap<>();
        setupSoundPool();
    }


    public void playSound(Sound sound){
        soundPool.play(sound.getSoundPoolId(), 100,100, 1, 0,0.8f);
    }


    public void loadSound(Sound sound){
        try (AssetFileDescriptor afd = context.getAssets().openFd(sound.getPath()) ){
            int soundId = soundPool.load(afd, 1);
            sound.setSoundPoolId(soundId);
            soundMap.put(sound.getButtonNumber(), sound);
        }catch (IOException e){
            e.printStackTrace();
        }
    }


    public void playSoundAtButton(int buttonNumber){
        Sound sound = soundMap.get(buttonNumber);
        if(sound != null){
            playSound(sound);
        }
    }


    private void setupSoundPool(){
        AudioAttributes attributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build();

        soundPool = new SoundPool.Builder()
                .setMaxStreams(6)
                .setAudioAttributes(attributes)
                .build();
    }


}
