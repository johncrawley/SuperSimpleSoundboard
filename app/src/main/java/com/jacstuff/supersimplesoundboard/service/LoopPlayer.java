package com.jacstuff.supersimplesoundboard.service;

import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class LoopPlayer {

    private final ScheduledExecutorService executorService;
    private final LoopRecorder loopRecorder;
    private final SoundPlayer soundPlayer;
    private ScheduledFuture<?> future;
    int currentTime = 0;
    private final AtomicBoolean isPlaying = new AtomicBoolean(false);

    public LoopPlayer(LoopRecorder loopRecorder, SoundPlayer soundPlayer){
        executorService = Executors.newScheduledThreadPool(1);
        this.loopRecorder = loopRecorder;
        this.soundPlayer = soundPlayer;
    }


    public void play(){
        currentTime = 0;
        isPlaying.set(true);
        future = executorService.scheduleAtFixedRate(this::playNextSound, 0, loopRecorder.getTimeDivisor(), TimeUnit.MILLISECONDS);
    }


    public void stop(){
        if(isPlaying.get()){
            future.cancel(false);
            isPlaying.set(false);
        }
    }


    private void playNextSound(){
        Set<Integer> buttonNumbers = loopRecorder.getSoundsForTime(currentTime);
        buttonNumbers.forEach(soundPlayer::playSoundAtButton);
        updateCurrentTime();
    }


    private void updateCurrentTime(){
        currentTime++;
        if(currentTime > loopRecorder.getDuration()){
            currentTime = 0;
        }
    }
}
