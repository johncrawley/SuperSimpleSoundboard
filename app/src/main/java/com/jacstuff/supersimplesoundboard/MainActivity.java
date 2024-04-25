package com.jacstuff.supersimplesoundboard;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;

import com.google.android.material.button.MaterialButton;
import com.jacstuff.supersimplesoundboard.service.LoopPlayer;
import com.jacstuff.supersimplesoundboard.service.LoopRecorder;
import com.jacstuff.supersimplesoundboard.service.SoundHolder;
import com.jacstuff.supersimplesoundboard.service.SoundPlayer;
import com.jacstuff.supersimplesoundboard.service.SoundSteps;
import com.jacstuff.supersimplesoundboard.view.LoopView;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements LoopView {

    private LinearLayout buttonLayout;
    private LinearLayout.LayoutParams buttonParams;
    private SoundPlayer soundPlayer;
    private SoundBank soundBank;
    private LoopRecorder loopRecorder;
    private LoopPlayer loopPlayer;
    private List<SoundHolder> soundHolders;
    private SeekBar loopProgressSeekBar;
    private ImageButton recordButton, playButton, clearButton;
    private final SoundSteps soundSteps = new SoundSteps(16);
    private LinearLayout stepLayout;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        buttonLayout = findViewById(R.id.buttonLayout);
        loopProgressSeekBar = findViewById(R.id.loopProgressSeekbar);
        assignButtonLayout();
        soundPlayer = new SoundPlayer(getApplicationContext());
        loopRecorder = new LoopRecorder();
        loopRecorder.setLoopView(this);
        loopPlayer = new LoopPlayer(loopRecorder, soundPlayer);
        loopPlayer.setLoopView(this);
        loadSounds();
        // setupMusicButtons();
        setupSoundHolders();
        setupSoundButtons();
        setupRecordingButtons();
        setupMutedButtons();
        setupTempoSeekBar();
        setupStepGrid();

    }


    private void loadSounds(){
        SoundFactory soundFactory = new SoundFactory();
        soundBank = soundFactory.getSoundBank("Drums 1");
        List<Sound> sounds = soundBank.getSounds();
        for(int i=0; i< Math.min(8, sounds.size()); i++){
            Sound sound = sounds.get(i);
            sound.setButtonNumber(i);
            soundPlayer.loadSound(sound);
        }
    }


    private void setupTempoSeekBar(){
        SeekBar tempoSeekBar = findViewById(R.id.loopTempoSeekbar);
        tempoSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {}

            @Override public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                loopPlayer.setLoopMultiplier(seekBar.getProgress());
            }
        });
    }


    @Override
    public void notifyEndTime(long endTime){
        log("Entered notifyEndTime() endTime: " + endTime);
        loopProgressSeekBar.setMax((int)endTime);
    }


    @Override
    public void notifyLoopRecordingStopped(){
        setEnabled(true, recordButton, playButton, clearButton);
    }


    @Override
    public void notifyLoopRecordingStarted(){
        setEnabled(false, recordButton, playButton, clearButton);
    }


    @Override
    public void notifyLoopPlaying(){
        setEnabled(false, recordButton, playButton, clearButton);
    }


    @Override
    public void notifyLoopRecordingCleared(){
        loopProgressSeekBar.setProgress(0);
        loopProgressSeekBar.setMax(1);
        setEnabled(false, playButton, clearButton);
    }


    @Override
    public void notifyLoopProgress(int progress){
        runOnUiThread(()-> loopProgressSeekBar.setProgress(progress));
    }


    @Override
    public void notifyLoopStopped(){
        runOnUiThread(()->loopProgressSeekBar.setProgress(0));
        setEnabled(true, playButton, recordButton, clearButton);
    }


    public void setupMutedButtons(){
        setupButton(R.id.muteLayer0Button, ()-> loopPlayer.toggleMuted(0));
        setupButton(R.id.muteLayer1Button, ()-> loopPlayer.toggleMuted(1));
        setupButton(R.id.muteLayer2Button, ()-> loopPlayer.toggleMuted(2));
        setupButton(R.id.muteLayer3Button, ()-> loopPlayer.toggleMuted(3));
        setupButton(R.id.muteLayer4Button, ()-> loopPlayer.toggleMuted(4));
    }


    private void setEnabled(boolean isEnabled, ImageButton... buttons){
        log("Entered setVisibility()");
        runOnUiThread(()->{
            for(ImageButton button : buttons){
                button.setEnabled(isEnabled);
            }
        });
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
        recordButton = setupButton(R.id.recordButton, ()-> loopRecorder.startRecording());
        setupButton(R.id.stopButton, ()-> {
            loopRecorder.stopRecording();
            loopPlayer.stop();
        });
       playButton =  setupButton(R.id.playButton, ()-> loopPlayer.play());
       clearButton = setupButton(R.id.clearButton, ()-> loopRecorder.clear());
    }


    private ImageButton setupButton(int id, Runnable runnable){
        ImageButton button = findViewById(id);
        button.setOnClickListener(v -> runnable.run());
        return button;
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


    private void setupSoundButtons(){
        int[] ids = new int[]{R.id.soundButton1,
                R.id.soundButton2,
                R.id.soundButton3,
                R.id.soundButton4,
                R.id.soundButton5,
                R.id.soundButton6,
                R.id.soundButton7,
                R.id.soundButton8};
        for(int i=0; i< ids.length; i++){
            setupButton(ids[i], i);
        }
    }


    private void setupStepGrid(){
        stepLayout = findViewById(R.id.stepLayout);
        int numberOfSounds = 8;
        for(int i = 0; i < numberOfSounds; i++){
            setupStepGridRow(i);
        }
    }


    private void setupStepGridRow(int rowId){
        LinearLayout stepRow = new LinearLayout(getApplicationContext());
        int numberOfSteps = 16;
        for(int i = 0; i < numberOfSteps; i++){
            createStepFor(stepRow, rowId, i);
        }
        stepLayout.addView(stepRow);
    }


    private void createStepFor(ViewGroup row, int rowId, int stepIndex){
        var params = new LinearLayout.LayoutParams(
                0,
                30,
                1.0f);
        params.setMargins(5,5,5,5);
        View view = new View(getApplicationContext());
        view.setLayoutParams(params);
        view.setBackgroundColor(Color.DKGRAY);
        view.setPadding(5,5,5,5);
        view.setOnClickListener(v -> onStepClick(v, rowId, stepIndex));
        row.addView(view);
    }


    private void onStepClick(View v, int rowId, int stepIndex){
        soundSteps.toggleSelected(stepIndex, soundHolders.get(rowId));
        v.setSelected(!v.isSelected());
        v.setBackgroundColor(v.isSelected() ? Color.CYAN : Color.DKGRAY);
    }


    private void setupSoundHolders(){
        soundHolders = new ArrayList<>();
        for(Sound sound : soundBank.getSounds()){
            soundHolders.add(new SoundHolder(sound));
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setupButton(int id, int index){
        Button button = findViewById(id);
        SoundHolder soundHolder = soundHolders.size() <= index ? null : soundHolders.get(index);
        button.setPadding(-5,2,-5,2);
        button.setOnClickListener(v -> {
            if(soundHolder != null){
                Sound sound = soundHolder.sound();
                if(sound != null){
                    soundPlayer.playSound(sound);
                    loopRecorder.recordSound(sound.getButtonNumber());
                }
            }
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