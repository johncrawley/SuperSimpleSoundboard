package com.jacstuff.supersimplesoundboard.service;

import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class SoundLoopPlayer {

    private final ScheduledExecutorService executorService;
    private final SoundLooper soundLooper;
    private final SoundPlayer soundPlayer;
    private ScheduledFuture<?> future;
    int currentTime = 0;
    private final AtomicBoolean isPlaying = new AtomicBoolean(false);

    public SoundLoopPlayer(SoundLooper soundLooper, SoundPlayer soundPlayer){
        executorService = Executors.newScheduledThreadPool(1);
        this.soundLooper = soundLooper;
        this.soundPlayer = soundPlayer;
    }


    public void play(){
        currentTime = 0;
        isPlaying.set(true);
        future = executorService.scheduleAtFixedRate(this::playNextSound, 0, 1, TimeUnit.MILLISECONDS);
    }


    public void stop(){
        if(isPlaying.get()){
            future.cancel(false);
            isPlaying.set(false);
        }
    }


    private void playNextSound(){
        Set<Integer> buttonNumbers = soundLooper.getSoundsForTime(currentTime);
        buttonNumbers.forEach(soundPlayer::playSoundAtButton);
        updateCurrentTime();
    }


    private void updateCurrentTime(){
        currentTime++;
        if(currentTime > soundLooper.getDuration()){
            currentTime = 0;
        }
    }
}
