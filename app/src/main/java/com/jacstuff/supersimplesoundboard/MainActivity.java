package com.jacstuff.supersimplesoundboard;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.jacstuff.supersimplesoundboard.service.SoundHolder;
import com.jacstuff.supersimplesoundboard.service.SoundPlayer;
import com.jacstuff.supersimplesoundboard.service.steps.SoundSteps;
import com.jacstuff.supersimplesoundboard.service.steps.StepPlayer;
import com.jacstuff.supersimplesoundboard.view.StepGridView;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements StepGridView {

    private SoundPlayer soundPlayer;
    private SoundBank soundBank;
    private List<SoundHolder> soundHolders;
    private final SoundSteps soundSteps = new SoundSteps(16);
    private LinearLayout stepLayout;
    private StepPlayer stepPlayer;
    private TextView currentBpmText;

    private View currentlySelectedProgressView;
    private ViewGroup progressLayout;
    private final int unselectedProgressColor = Color.DKGRAY;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        assignButtonLayout();
        soundPlayer = new SoundPlayer(getApplicationContext());
        loadSounds();
        stepPlayer = new StepPlayer(soundSteps, soundPlayer, 70);
        stepPlayer.setView(MainActivity.this);
        setupSoundHolders();
        setupSoundButtons();
        setupRecordingButtons();
        setupStepGrid();
        setupBpmSeekbar();

    }


    private void loadSounds(){
        SoundFactory soundFactory = new SoundFactory();
        soundBank = soundFactory.getSoundBank("Drums 1");
        List<Sound> sounds = soundBank.getSounds();
        for(int i = 0; i < Math.min(8, sounds.size()); i++){
            Sound sound = sounds.get(i);
            sound.setButtonNumber(i);
            soundPlayer.loadSound(sound);
        }
    }


    private void setupRecordingButtons(){
        setupButton(R.id.playButton, ()-> stepPlayer.play());
        setupButton(R.id.stopButton, ()-> stepPlayer.stopAndReset());
    }


    private void setupButton(int id, Runnable runnable){
        ImageButton button = findViewById(id);
        button.setOnClickListener(v -> runnable.run());
    }


    private void setupBpmSeekbar(){
        SeekBar bpmSeekBar = findViewById(R.id.bpmSeekbar);
        currentBpmText = findViewById(R.id.currentBpmText);
        bpmSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                setCurrentBpmText(i);
            }
            @Override public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int bpm = seekBar.getProgress();
                stepPlayer.setBpm(bpm);
                setCurrentBpmText(bpm);
            }
        });
    }


    private void setCurrentBpmText(int bpm){
        String bpmStr = bpm + "bpm";
        currentBpmText.setText(bpmStr);
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
        setupProgressRow();
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


    private void setupProgressRow(){
        progressLayout = new LinearLayout(getApplicationContext());
        int numberOfSteps = 16;
        for(int i = 0; i < numberOfSteps; i++){
            addProgressStepTo(progressLayout);
        }
        stepLayout.addView(progressLayout);
    }


    private void addProgressStepTo(ViewGroup row){
        var params = new LinearLayout.LayoutParams(0, 5, 1.0f);
        params.setMargins(5,5,5,5);
        View view = new View(getApplicationContext());
        view.setLayoutParams(params);
        view.setBackgroundColor(unselectedProgressColor);
        view.setPadding(5,5,5,5);
        row.addView(view);
    }


    public void hideProgress(){
        resetCurrentProgressView();
    }


    public void setCurrentProgress(int index){
        resetCurrentProgressView();
        var view = progressLayout.getChildAt(index);
        view.setBackgroundColor(Color.YELLOW);
        currentlySelectedProgressView = view;

    }


    private void resetCurrentProgressView(){
        if(currentlySelectedProgressView != null){
            currentlySelectedProgressView.setBackgroundColor(unselectedProgressColor);
        }
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
                }
            }
        });
    }


    private void assignButtonLayout(){
        LinearLayout.LayoutParams buttonParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                100);
        buttonParams.setMargins(4,4,4,4);
    }


}