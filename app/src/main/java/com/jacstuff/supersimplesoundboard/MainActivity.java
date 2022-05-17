package com.jacstuff.supersimplesoundboard;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.media.audiofx.EnvironmentalReverb;
import android.media.audiofx.PresetReverb;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, MediaPlayer.OnCompletionListener {

    /*

        list view
        open soundsboards dir each time activity starts, get names of files
            - turn names of files into string list items
            - click list item go to new activity
             - grid - each item corresponds to file in chosen dir
             - click on file, play sound
     */


    private Button playButton, reverbButton, fButton, gButton, aButton;
    private MediaPlayer mediaPlayer;
    private Map<Integer, MediaPlayer> mediaPlayerMap;
    private List<Integer> playingIds;
    private PresetReverb presetReverb;
    private  android.media.audiofx.EnvironmentalReverb environmentalReverb;
    private final int POLOPHONY = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mediaPlayerMap = new HashMap<>(POLOPHONY);
        playingIds = new CopyOnWriteArrayList<>();


        playButton = findViewById(R.id.test_sound_button);
        fButton = findViewById(R.id.f1_button);
        gButton = findViewById(R.id.g1_button);
        aButton = findViewById(R.id.a1_button);
        reverbButton = findViewById(R.id.reverbButton);
        assignTrack();
        playButton.setOnClickListener(playListener);
        int audioSessionId = mediaPlayer.getAudioSessionId();
        presetReverb = new PresetReverb(0, mediaPlayer.getAudioSessionId());
        environmentalReverb = new EnvironmentalReverb(0, mediaPlayer.getAudioSessionId());
        environmentalReverb.setDecayTime(2000);
        environmentalReverb.setReverbLevel((short) 8);
        presetReverb.setPreset(PresetReverb.PRESET_LARGEHALL);
        presetReverb.setEnabled(false);
        reverbButton.setOnClickListener(this);
        mediaPlayer.setOnCompletionListener(this);
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mediaPlayer != null) {
            mediaPlayer.release();
        }
    }


    @Override
    public void onClick(View v) {
        int viewId = v.getId();
        if(viewId == R.id.reverbButton){
            presetReverb.setEnabled(!presetReverb.getEnabled());
            environmentalReverb.setEnabled(!environmentalReverb.getEnabled());
        }
    }


    private void assignTrack(){
        mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.hylophone_f);
    }


    private final View.OnClickListener playListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            int id = v.getId();
            int resId = R.raw.hylophone_a1;
            playSound(id, resId);
            resetAndStartIfAlreadyPlaying(mediaPlayer);
            mediaPlayer.start();
        }
    };


    private void resetAndStartIfAlreadyPlaying(MediaPlayer mediaPlayer){
        if(mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            try {
                mediaPlayer.prepare();
                mediaPlayer.seekTo(0);
                mediaPlayer.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
            // mediaPlayer.reset();
            System.out.println("audio session id :" + mediaPlayer.getAudioSessionId());
        }
        else{
            mediaPlayer.start();
        }

    }

    private void playSound(int id, int resId){
        if(mediaPlayerMap.containsKey(id)){
            MediaPlayer mediaPlayer = mediaPlayerMap.get(id);
            if(mediaPlayer != null){
                resetAndStartIfAlreadyPlaying(mediaPlayer);
            }
            removeOldPlayingId(id);
            playingIds.add(id);
            return;
        }
        if(mediaPlayerMap.size() >= POLOPHONY){
            int oldestId = playingIds.remove(0);
            mediaPlayerMap.remove(oldestId);
        }
        playingIds.add(id);
        mediaPlayerMap.put(id, createAndPlay(resId));

    }


    private MediaPlayer createAndPlay(int resId){
        MediaPlayer mediaPlayer = MediaPlayer.create(this, resId);
        try{
            mediaPlayer.prepare();
            mediaPlayer.start();
        }catch (IOException e){
            e.printStackTrace();
        }
        return mediaPlayer;
    }

    private void removeOldPlayingId(int oldId){
        Iterator <Integer> iterator = playingIds.iterator();
        while(iterator.hasNext()){
            int id = iterator.next();
            if(id == oldId){
                iterator.remove();
            }
        }
    }


    @Override
    public void onCompletion(MediaPlayer mp) {
        //mediaPlayer.release();
        try {
            mediaPlayer.stop();
            mediaPlayer.prepare();
            mediaPlayer.seekTo(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}