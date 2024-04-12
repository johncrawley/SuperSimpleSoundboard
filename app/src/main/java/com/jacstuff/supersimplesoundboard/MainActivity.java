package com.jacstuff.supersimplesoundboard;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.res.AssetFileDescriptor;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.google.android.material.button.MaterialButton;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private LinearLayout buttonLayout;
    private SoundPool soundPool;
    private LinearLayout.LayoutParams buttonParams;


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        buttonLayout = findViewById(R.id.buttonLayout);
        assignButtonLayout();
        setupSoundPool();
        loadSounds();
    }


    private void log(String msg){
        System.out.println("^^^ MainActivity: "  + msg);
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


    private void loadSounds(){
        SoundFactory soundFactory = new SoundFactory();
        SoundBank soundBank = soundFactory.getSoundBank("n_bass");

        for(Sound sound : soundBank.getSounds()){
            loadSound(sound);
        }
    }


    private void loadSound(Sound sound){
        try (AssetFileDescriptor afd = getAssets().openFd(sound.getPath()) ){
            int soundId = soundPool.load(afd, 1);
            sound.setSoundPoolId(soundId);
            setupButton(sound);
        }catch (IOException e){
            e.printStackTrace();
        }
    }


    @SuppressLint("ClickableViewAccessibility")
    private void setupButton(Sound sound){
        MaterialButton button = new MaterialButton(this);
        button.setPadding(2,2,2,2);
        button.setLayoutParams(buttonParams);
        button.setText(sound.getDisplayName());
        button.setId(View.generateViewId());
        button.setTag(sound.getSoundPoolId());
        buttonLayout.addView(button);
        button.setOnClickListener(v -> soundPool.play(sound.getSoundPoolId(), 100,100, 1, 0,1));

    }



    private void assignButtonLayout(){
        buttonParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                100);
        buttonParams.setMargins(-20,-10,-20,-10);
    }



}