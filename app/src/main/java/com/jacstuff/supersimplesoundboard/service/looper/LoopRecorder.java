package com.jacstuff.supersimplesoundboard.service.looper;

import com.jacstuff.supersimplesoundboard.view.LoopView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class LoopRecorder {

    private final List<SoundLayer> soundLayers;
    private SoundLayer currentLayer;
    private final Map<Long, Set<Integer>> recordedSounds;
    private long startTime;
    private long endTime;
    private long duration;
    private int numberOfSoundsRecorded;
    private boolean isRecording;
    private final int timeDivisor = 80;
    private LoopView loopView;
    private boolean isInitialLoop = true;
    private int layerIndex;


    public LoopRecorder(){
        soundLayers = new ArrayList<>();
        currentLayer = new SoundLayer(layerIndex);
        recordedSounds = new HashMap<>(100);
    }


    public void setLoopView(LoopView loopView){
        this.loopView = loopView;
    }


    private Optional<LoopView> getView(){
        return Optional.ofNullable(loopView);
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
        if(isRecording){
            return;
        }
        isRecording = true;
        startTime = layerIndex == 0 ? -1 : getCurrentTime();
        currentLayer = new SoundLayer(layerIndex);
        getView().ifPresent(LoopView::notifyLoopRecordingStarted);
    }


    private long getCurrentTime(){
        return System.currentTimeMillis() / timeDivisor;
    }


    public void recordSound(int soundId){
        if(isRecording){
            numberOfSoundsRecorded++;
            currentLayer.addSound(getCurrentTimeElapsed(), soundId);
            recordedSounds.computeIfAbsent(getCurrentTimeElapsed(), k -> new HashSet<>()).add(soundId);
        }
    }


    public long getCurrentTimeElapsed(){
        if(startTime == -1){
            startTime = getCurrentTime();
            return 1;
        }
        return 1 + getCurrentTime() - startTime;
    }


    public void stopRecording(){
        log("Entered stopRecording() numberOfSounds: " + numberOfSoundsRecorded);
        if(!isRecording){
            return;
        }
        isRecording = false;
        if(isInitialLoop){
            endTime = getCurrentTime();
            duration = endTime - startTime;
            notifyViewOfEndTime();
        }
        getView().ifPresent(LoopView::notifyLoopRecordingStopped);
        saveLayer();
        isInitialLoop = false;
    }


    private void saveLayer(){
        soundLayers.add(currentLayer);
        layerIndex++;
    }


    private void notifyViewOfEndTime(){
        if(loopView != null){
            if(isInitialLoop){
                loopView.notifyEndTime(duration);
            }
        }
    }


    public void clear(){
        numberOfSoundsRecorded = 0;
        recordedSounds.clear();
        soundLayers.clear();
        layerIndex = 0;
        isInitialLoop = true;
        if(loopView != null){
            loopView.notifyLoopRecordingCleared();
        }
    }


    public Set<RecordedSounds> getSoundsForTime(long time){
        return soundLayers.stream().map(sl -> sl.getRecordedSoundsAt(time)).collect(Collectors.toSet());
    }


    public int getNumberOfSoundsRecorded(){
        return numberOfSoundsRecorded;
    }
}
