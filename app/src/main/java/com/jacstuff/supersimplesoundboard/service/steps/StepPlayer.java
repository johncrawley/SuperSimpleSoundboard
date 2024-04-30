package com.jacstuff.supersimplesoundboard.service.steps;

import com.jacstuff.supersimplesoundboard.service.sounds.Sound;
import com.jacstuff.supersimplesoundboard.service.SoundPlayer;
import com.jacstuff.supersimplesoundboard.view.MainView;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class StepPlayer {

    private final SongPart songPart;
    private final SoundPlayer soundPlayer;
    private final ScheduledExecutorService executorService;
    private ScheduledFuture<?> future;
    private int currentStepIndex;
    private int bpm;
    private final AtomicBoolean isPlaying = new AtomicBoolean();
    private MainView view;

    public StepPlayer(SongPart songPart, SoundPlayer soundPlayer, int initialBpm){
        this.songPart = songPart;
        this.soundPlayer = soundPlayer;
        executorService = Executors.newScheduledThreadPool(1);
        this.bpm = initialBpm;
    }


    public void play(){
        currentStepIndex = 0;
        future = executorService.scheduleAtFixedRate(this::playNextStep, 0, getIntervalFromBpm(), TimeUnit.MILLISECONDS);
        isPlaying.set(true);
    }


    public void setView(MainView view){
        this.view = view;
    }


    private int getIntervalFromBpm(){
        int soundsPerBeat = 4;
        return 60 * 1000/(soundsPerBeat * bpm);
    }


    public void setBpm(int bpm){
        this.bpm = bpm;
        if(isPlaying.get()){
            stop();
            play();
        }
    }


    public void stopAndReset(){
        stop();
        currentStepIndex = 0;
    }


    private void stop(){
        if(future != null && !future.isCancelled()){
            future.cancel(false);
            isPlaying.set(false);
            view.hideStepProgress();
        }
    }


    private void log(String msg){
        System.out.println("^^^ StepPlayer: " + msg);
    }


    private void playNextStep(){
        for(Sound sound : songPart.getSoundsForStep(currentStepIndex)){
            soundPlayer.playSound(sound);
        }
        view.setCurrentProgress(currentStepIndex);
        currentStepIndex++;
        int numberOfSteps = 16;
        if(currentStepIndex == numberOfSteps){
            currentStepIndex = 0;
        }
    }
}
