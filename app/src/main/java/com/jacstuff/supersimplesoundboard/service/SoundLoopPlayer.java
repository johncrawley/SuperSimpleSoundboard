package com.jacstuff.supersimplesoundboard.service;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class SoundLoopPlayer {

    private ScheduledExecutorService executorService;
    private SoundLooper soundLooper;

    public SoundLoopPlayer(SoundLooper soundLooper){
        executorService = Executors.newScheduledThreadPool(1);

    }

    public void play(){

    }


    public void stop(){

    }
}
