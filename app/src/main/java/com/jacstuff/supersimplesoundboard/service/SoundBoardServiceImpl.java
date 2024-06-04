package com.jacstuff.supersimplesoundboard.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import com.jacstuff.supersimplesoundboard.MainActivity;
import com.jacstuff.supersimplesoundboard.service.preferences.PreferencesManager;
import com.jacstuff.supersimplesoundboard.service.sounds.Sound;
import com.jacstuff.supersimplesoundboard.service.sounds.SoundBank;
import com.jacstuff.supersimplesoundboard.service.sounds.SoundFactory;
import com.jacstuff.supersimplesoundboard.service.steps.SongPart;
import com.jacstuff.supersimplesoundboard.service.steps.StepPlayer;

import java.util.ArrayList;
import java.util.List;

public class SoundBoardServiceImpl extends Service implements SoundBoardService {


    private MainActivity mainActivity;
    private final IBinder binder = new LocalBinder();

    private SoundPlayer soundPlayer;
    private SoundBank soundBank;
    private List<SoundHolder> soundHolders;
    private StepPlayer stepPlayer;
    private SongPart songPart;
    private PreferencesManager preferencesManager;
    private boolean isFirstAttach;

    public SoundBoardServiceImpl() {
    }


    @Override
    public void onCreate() {
        super.onCreate();
        isFirstAttach = true;
        preferencesManager = new PreferencesManager(getApplicationContext());
        soundPlayer = new SoundPlayer(getApplicationContext());
        loadSounds();
        setupSoundHolders();
        songPart = new SongPart(16, soundHolders);
        stepPlayer = new StepPlayer(songPart, soundPlayer, 70);
        loadSavedSteps();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        log("Entered onDestroy()");
        preferencesManager.saveSteps(songPart.getSteps());
    }


    private void log(String msg){
        System.out.println("^^^ SoundBoardServiceImpl: " + msg);
    }


    public void setView(MainActivity mainActivity){
        this.mainActivity = mainActivity;
        stepPlayer.setView(mainActivity);
        songPart.setView(mainActivity);
        if(isFirstAttach) {
            loadSavedSteps();
        }
        songPart.updateView();
    }


    @Override
    public void onPause(){
        log("Entered onPause()");
        preferencesManager.saveSteps(songPart.getSteps());
    }


    @Override
    public void onResume(){
        loadSavedSteps();
        songPart.updateView();
    }


    private void loadSavedSteps(){
        songPart.loadSteps(preferencesManager.getSteps());
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return Service.START_NOT_STICKY; // service is not restarted when terminated
    }


    public class LocalBinder extends Binder {
        public SoundBoardServiceImpl getService() {
            return SoundBoardServiceImpl.this;
        }
    }


    @Override
    public IBinder onBind(Intent intent) {
        return binder;
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


    private void setupSoundHolders(){
        soundHolders = new ArrayList<>();
        for(Sound sound : soundBank.getSounds()){
            soundHolders.add(new SoundHolder(sound));
        }
    }


    @Override
    public void toggleStep(int stepIndex, int soundIndex){
        songPart.toggleSelected(stepIndex, soundIndex);
    }


    @Override
    public void play() {
        stepPlayer.play();
    }


    @Override
    public void stopAndReset(){
        stepPlayer.stopAndReset();
    }


    @Override
    public void setBpm(int bpm){
        stepPlayer.setBpm(bpm);
    }


    @Override
    public void playSoundAtIndex(int index){
        SoundHolder soundHolder = soundHolders.size() <= index ? null : soundHolders.get(index);
        if(soundHolder != null){
            Sound sound = soundHolder.sound();
            if(sound != null){
                soundPlayer.playSound(sound);
            }
        }
    }
}