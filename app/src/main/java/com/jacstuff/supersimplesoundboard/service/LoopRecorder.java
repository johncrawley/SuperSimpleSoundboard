package com.jacstuff.supersimplesoundboard.service;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class LoopRecorder {

    private final Map<Long, Set<Integer>> recordedSounds;
    private long startTime;
    private long endTime;
    private long duration;
    private int numberOfSoundsRecorded;
    private boolean isRecording;
    private final int timeDivisor = 20;

    public LoopRecorder(){
        recordedSounds = new HashMap<>(100);
    }


    public long getDuration(){
        return duration;
    }

    private void log(String msg){
        System.out.println("^^^ SoundLooper: " + msg);
    }


    public int getTimeDivisor(){
        return timeDivisor;
    }


    public void startRecording(){
        log("Entered startRecording()");
        if(isRecording){
            return;
        }
        isRecording = true;
        startTime = getCurrentTime();
    }

    private long getCurrentTime(){
        return System.currentTimeMillis() / timeDivisor;
    }

    public void recordSound(int soundId){
        if(isRecording){
            numberOfSoundsRecorded++;
            recordedSounds.computeIfAbsent(getCurrentTimeElapsed(), k -> new HashSet<>()).add(soundId);
        }
    }


    public long getCurrentTimeElapsed(){
        return getCurrentTime() - startTime;
    }


    public void stopRecording(){
        log("Entered stopRecording() numberOfSounds: " + numberOfSoundsRecorded);
        if(!isRecording){
            return;
        }
        isRecording = false;
        endTime = getCurrentTime();
        duration = endTime - startTime;
    }


    public void clear(){
        numberOfSoundsRecorded = 0;
        recordedSounds.clear();
    }


    public Set<Integer> getSoundsForTime(long time){
        return recordedSounds.getOrDefault(time, Collections.emptySet());
    }


    public int getNumberOfSoundsRecorded(){
        return numberOfSoundsRecorded;
    }
}
