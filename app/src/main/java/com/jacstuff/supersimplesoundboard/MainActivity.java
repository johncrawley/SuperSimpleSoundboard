package com.jacstuff.supersimplesoundboard;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;


import static android.Manifest.permission.RECORD_AUDIO;
import com.jacstuff.supersimplesoundboard.service.SoundBoardServiceImpl;
import com.jacstuff.supersimplesoundboard.service.SoundBoardService;
import com.jacstuff.supersimplesoundboard.service.recorder.AudioRecorder;
import com.jacstuff.supersimplesoundboard.view.MainView;
import com.jacstuff.supersimplesoundboard.view.RecordPlaybackView;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;


public class MainActivity extends AppCompatActivity implements MainView, RecordPlaybackView {

    private LinearLayout stepLayout;
    private TextView currentBpmText;

    private SeekBar bpmSeekBar;
    private View currentlySelectedProgressView;
    private ViewGroup progressLayout;
    private final int unselectedProgressColor = Color.DKGRAY;
    private SoundBoardServiceImpl service;
    private final AtomicBoolean isServiceConnected = new AtomicBoolean();
    private AudioRecorder audioRecorder;
    private Button recordButton, stopRecordingButton, recordingPlaybackButton, stopPlaybackButton;


    private final ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            SoundBoardServiceImpl.LocalBinder binder = (SoundBoardServiceImpl.LocalBinder) service;
            MainActivity.this.service = binder.getService();
            MainActivity.this.service.setView(MainActivity.this);
            isServiceConnected.set(true);
        }

        @Override public void onServiceDisconnected(ComponentName arg0) {
            isServiceConnected.set(false);
        }
    };


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestPermissions();
        setContentView(R.layout.activity_main);
        audioRecorder = new AudioRecorder(this, getApplicationContext());
        setupRecordingButtons();
        assignButtonLayout();
        startService();
        setupSoundButtons();
        setupPlaybackButtons();
        setupStepGrid();
        setupBpmSeekbar();
    }


    private void startService(){
        Intent mediaPlayerServiceIntent = new Intent(this, SoundBoardServiceImpl.class);
        getApplicationContext().startService(mediaPlayerServiceIntent);
        getApplicationContext().bindService(mediaPlayerServiceIntent, serviceConnection, 0);
    }

    @Override
    public void onPause(){
        super.onPause();
        if(service != null){
            service.onPause();
        }
    }


    @Override
    public void onResume(){
        super.onResume();
        if(service != null){
            service.onResume();
        }
    }


    private void setupPlaybackButtons(){
        setupServiceButton(R.id.playButton, SoundBoardService::play);
        setupServiceButton(R.id.stopButton, SoundBoardService::stopAndReset);
        setupServiceButton(R.id.clearButton, SoundBoardService::clear);
    }


    private void setupServiceButton(int buttonId, Consumer<SoundBoardService> consumer){
        setupButton(buttonId, ()-> getService().ifPresent(consumer));
    }


    private Optional<SoundBoardServiceImpl> getService(){
        return Optional.ofNullable(service);
    }


    private void setupButton(int id, Runnable runnable){
        ImageButton button = findViewById(id);
        button.setOnClickListener(v -> runnable.run());
    }


    public void setStep(int stepIndex, List<Boolean> enabledList) {
        if(stepIndex < stepLayout.getChildCount()){
            for(int soundIndex = 0; soundIndex < enabledList.size(); soundIndex++){
                updateRowAtIndexForSoundAt(stepIndex, soundIndex, enabledList);
            }
        }
    }


    public void setStepRow(int rowIndex, List<Boolean> steps) {
        if(rowIndex < stepLayout.getChildCount()){
            ViewGroup row = (ViewGroup) stepLayout.getChildAt(rowIndex);
            int smallestMax = Math.min(steps.size(), row.getChildCount());
            for(int stepIndex = 0; stepIndex < smallestMax; stepIndex++){
                updateStepView(row.getChildAt(stepIndex), steps.get(stepIndex));
            }
        }
    }


    private void updateRowAtIndexForSoundAt(int stepIndex, int soundIndex, List<Boolean> enabledList){
        ViewGroup row = (ViewGroup) stepLayout.getChildAt(stepIndex);
        if(stepIndex >= row.getChildCount()){
            return;
        }
        updateStepView(row.getChildAt(stepIndex), enabledList.get(soundIndex));
    }


    @Override
    public void setBpmProgress(int progress){
        setCurrentBpmText(progress);
        bpmSeekBar.setProgress(progress);
    }


    private void setupBpmSeekbar(){
        bpmSeekBar = findViewById(R.id.bpmSeekbar);
        currentBpmText = findViewById(R.id.currentBpmText);
        bpmSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                setCurrentBpmText(i);
            }
            @Override public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int bpm = seekBar.getProgress();
                getService().ifPresent(sbs -> sbs.setBpm(bpm));
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
        service.toggleStep(stepIndex, rowId);
        updateStepView(v, !v.isSelected());
    }


    private void updateStepView(View v, boolean isSelected){
        v.setSelected(isSelected);
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


    public void hideStepProgress(){
        resetCurrentProgressView();
    }


    public void setCurrentProgress(int index){
        resetCurrentProgressView();
        var view = progressLayout.getChildAt(index);
        view.setBackgroundColor(Color.YELLOW);
        currentlySelectedProgressView = view;

    }

    @Override
    public void setNumberOfSteps(int numberOfSteps) {

    }


    private void resetCurrentProgressView(){
        if(currentlySelectedProgressView != null){
            currentlySelectedProgressView.setBackgroundColor(unselectedProgressColor);
        }
    }


    @SuppressLint("ClickableViewAccessibility")
    private void setupButton(int id, int index){
        Button button = findViewById(id);
        button.setPadding(-5,2,-5,2);
        button.setOnClickListener(v -> service.playSoundAtIndex(index));
    }


    private void assignButtonLayout(){
        LinearLayout.LayoutParams buttonParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                100);
        buttonParams.setMargins(4,4,4,4);
    }


    private final ActivityResultLauncher<String> recordAudioPermissionLauncher = registerForActivityResult(
            new ActivityResultContracts.RequestPermission(),
            result -> {
                if (result) {
                    // enable recording dialog
                }
            }
    );


    private void setupRecordingButtons(){
        recordButton = findViewById(R.id.recordSoundButton);
        recordButton.setOnClickListener(v -> audioRecorder.startRecording());

        stopRecordingButton = findViewById(R.id.stopRecordingSoundButton);
        stopRecordingButton.setOnClickListener(v -> audioRecorder.stopRecording());

        recordingPlaybackButton = findViewById(R.id.playRecordingButton);
        recordingPlaybackButton.setOnClickListener(v -> audioRecorder.startPlaying());


        stopPlaybackButton = findViewById(R.id.stopPlaybackButton);
        stopPlaybackButton.setOnClickListener(v -> audioRecorder.stopPlaying());
    }


    private void requestPermissions(){
        askPermissionFor(RECORD_AUDIO, recordAudioPermissionLauncher);
    }


    private void askPermissionFor(String permission, ActivityResultLauncher<String> resultLauncher){
        if(checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED){
            resultLauncher.launch(permission);
        }
    }


    @Override
    public void notifyRecordingStarted() {
        stopPlaybackButton.setVisibility(View.GONE);
        recordingPlaybackButton.setVisibility(View.GONE);
        recordButton.setVisibility(View.GONE);
        stopRecordingButton.setVisibility(View.VISIBLE);
    }


    @Override
    public void notifyRecordingStopped() {
        stopPlaybackButton.setVisibility(View.GONE);
        recordingPlaybackButton.setVisibility(View.VISIBLE);
        recordButton.setVisibility(View.VISIBLE);
        stopRecordingButton.setVisibility(View.GONE);
        log("entered notifyRecordingStopped()");
    }


    private void log(String msg){
        System.out.println("^^^ MainActivity : " + msg);
    }


    @Override
    public void notifyPlaybackStarted() {
        stopPlaybackButton.setVisibility(View.VISIBLE);
        recordingPlaybackButton.setVisibility(View.GONE);
        recordButton.setVisibility(View.GONE);
        stopRecordingButton.setVisibility(View.GONE);
    }


    @Override
    public void notifyPlaybackStopped() {
        stopPlaybackButton.setVisibility(View.GONE);
        recordingPlaybackButton.setVisibility(View.VISIBLE);
        recordButton.setVisibility(View.VISIBLE);
        stopRecordingButton.setVisibility(View.GONE);
    }
}