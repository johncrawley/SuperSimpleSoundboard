package com.jacstuff.supersimplesoundboard;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

public class MainActivity extends AppCompatActivity implements MediaPlayer.OnCompletionListener {

    private Map<Integer, MediaPlayer> mediaPlayerMap;
    private List<Integer> playingIds;
    private final int POLYPHONY = 4;
    private Map<Integer, Integer> soundMap;
    private LinearLayout buttonLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        buttonLayout = findViewById(R.id.buttonLayout);
        mediaPlayerMap = new HashMap<>(POLYPHONY);
        soundMap = new HashMap<>(100);

        playingIds = new CopyOnWriteArrayList<>();

        setupButton(R.id.f1_button, R.raw.hylophone_f, "F");
        setupButton(R.id.g1_button, R.raw.hylophone_g, "G");
        setupButton(R.id.a1_button, R.raw.hylophone_a1, "A");
    }


    private void setupButton(int viewId, int soundResId, String text){
        soundMap.put(viewId, soundResId);
        Button button = new Button(this);
        button.setText(text);
        button.setId(viewId);
        buttonLayout.addView(button);
        button.setOnClickListener(playListener);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        mediaPlayerMap.values().forEach(mp -> { if(mp!= null) mp.release();});
    }

/*
    private void assignTrack(){
        //mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.hylophone_f);
    }*/


    private final View.OnClickListener playListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            int id = v.getId();
            Integer resId = soundMap.get(id);
            if(resId == null){
                resId = R.raw.test_note;
            }
            playSound(id, resId);
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
        if(mediaPlayerMap.size() >= POLYPHONY){
            int oldestId = playingIds.remove(0);
            MediaPlayer mp = mediaPlayerMap.remove(oldestId);
            if(mp != null) {
                mp.stop();
                mp.release();
            }
        }
        playingIds.add(id);
        MediaPlayer mp = createAndPlay(resId);
        mediaPlayerMap.put(id, mp);

    }


    private MediaPlayer createAndPlay(int resId){
        MediaPlayer mediaPlayer = MediaPlayer.create(this, resId);
        try{
            mediaPlayer.start();
        }catch (Exception e){
            e.printStackTrace();
        }
        return mediaPlayer;
    }


    private void removeOldPlayingId(int oldId){
        playingIds.removeIf(id -> id == oldId);
    }


    @Override
    public void onCompletion(MediaPlayer mp) {
        //mediaPlayer.release();
        try {
            mp.stop();
            mp.prepare();
            mp.seekTo(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}