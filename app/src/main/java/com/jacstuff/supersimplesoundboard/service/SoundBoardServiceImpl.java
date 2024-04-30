package com.jacstuff.supersimplesoundboard.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import com.jacstuff.supersimplesoundboard.MainActivity;
import com.jacstuff.supersimplesoundboard.service.sounds.Sound;
import com.jacstuff.supersimplesoundboard.service.sounds.SoundBank;
import com.jacstuff.supersimplesoundboard.service.sounds.SoundFactory;
import com.jacstuff.supersimplesoundboard.service.steps.SoundBoardService;
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
    private final SongPart songPart = new SongPart(16);


    public SoundBoardServiceImpl() {
    }


    @Override
    public void onCreate() {
        super.onCreate();
        soundPlayer = new SoundPlayer(getApplicationContext());
        loadSounds();
        stepPlayer = new StepPlayer(songPart, soundPlayer, 70);
        setupSoundHolders();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    public void setView(MainActivity mainActivity){
        this.mainActivity = mainActivity;
        stepPlayer.setView(mainActivity);
        songPart.setView(mainActivity);
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

    }

    @Override
    public void play() {

    }

    @Override
    public void stopAndReset(){

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