package com.jacstuff.supersimplesoundboard.service;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class SoundLooper {

    private final Map<Long, Set<Integer>> recordedSounds;
    private long startTime;
    private long endTime;
    private long duration;
    private int numberOfSoundsRecorded;


    public SoundLooper(){
        recordedSounds = new HashMap<>(100);
    }


    public long getDuration(){
        return duration;
    }


    public void startRecording(){
        startTime = System.currentTimeMillis();
    }


    public void recordSound(int soundId){
        numberOfSoundsRecorded++;
        recordedSounds.computeIfAbsent(getCurrentTimeElapsed(), k -> new HashSet<>()).add(soundId);
    }


    public long getCurrentTimeElapsed(){
        return System.currentTimeMillis() - startTime;
    }


    public void stopRecording(){
        endTime = System.currentTimeMillis();
        duration = endTime - startTime;
    }


    public Set<Integer> getSoundsForTime(long time){
        return recordedSounds.getOrDefault(time, Collections.emptySet());
    }


    public int getNumberOfSoundsRecorded(){
        return numberOfSoundsRecorded;
    }
}
