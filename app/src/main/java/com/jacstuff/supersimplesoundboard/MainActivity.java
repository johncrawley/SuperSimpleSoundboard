package com.jacstuff.supersimplesoundboard;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.google.android.material.button.MaterialButton;
import com.jacstuff.supersimplesoundboard.service.LoopPlayer;
import com.jacstuff.supersimplesoundboard.service.LoopRecorder;
import com.jacstuff.supersimplesoundboard.service.SoundPlayer;

import java.util.List;


public class MainActivity extends AppCompatActivity {

    private LinearLayout buttonLayout;
    private LinearLayout.LayoutParams buttonParams;
    private SoundPlayer soundPlayer;
    private SoundBank soundBank;
    private LoopRecorder loopRecorder;
    private LoopPlayer loopPlayer;


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        buttonLayout = findViewById(R.id.buttonLayout);
        assignButtonLayout();
        soundPlayer = new SoundPlayer(getApplicationContext());
        loopRecorder = new LoopRecorder();
        loopPlayer = new LoopPlayer(loopRecorder, soundPlayer);
        loadSounds();
        setupMusicButtons();
        setupRecordingButtons();
    }

    private void loadSounds(){
        SoundFactory soundFactory = new SoundFactory();
        soundBank = soundFactory.getSoundBank("n_bass");
        List<Sound> sounds = soundBank.getSounds();
        for(int i=0; i< Math.min(8, sounds.size()); i++){
            Sound sound = sounds.get(i);
            sound.setButtonNumber(i);
            soundPlayer.loadSound(sound);
        }
      //  soundBank.getSounds().forEach(soundPlayer::loadSound);
    }


    private void setupMusicButtons(){
        int buttonNumber = 0;
        for(Sound sound : soundBank.getSounds()){
            sound.setButtonNumber(buttonNumber);
            setupButton(sound);
            if(buttonNumber++ > 7){
                break;
            }
        }
    }


    private void setupRecordingButtons(){
        setupButton(R.id.recordButton, ()-> loopRecorder.startRecording());
        setupButton(R.id.stopButton, ()-> {
            loopRecorder.stopRecording();
            loopPlayer.stop();
        });

        setupButton(R.id.playButton, ()-> loopPlayer.play());
        setupButton(R.id.clearButton, ()-> loopRecorder.clear());
    }


    private void setupButton(int id, Runnable runnable){
        View button = findViewById(id);
        button.setOnClickListener(v -> runnable.run());
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setupButton(Sound sound){
        MaterialButton button = new MaterialButton(this);
        button.setPadding(0,2,0,2);
        button.setLayoutParams(buttonParams);
        button.setText(sound.getDisplayName());
        button.setId(View.generateViewId());
        button.setTag(sound.getSoundPoolId());
        buttonLayout.addView(button);
        button.setOnClickListener(v -> {
            soundPlayer.playSound(sound);
            loopRecorder.recordSound(sound.getButtonNumber());
        });
    }


    private void assignButtonLayout(){
        buttonParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                100);
        buttonParams.setMargins(4,4,4,4);
    }


    private void log(String msg){
        System.out.println("^^^ MainActivity: "  + msg);
    }



}