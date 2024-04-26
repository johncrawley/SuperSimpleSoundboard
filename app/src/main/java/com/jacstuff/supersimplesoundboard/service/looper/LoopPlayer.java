package com.jacstuff.supersimplesoundboard.service.looper;

import com.jacstuff.supersimplesoundboard.service.RecordedSounds;
import com.jacstuff.supersimplesoundboard.service.SoundPlayer;
import com.jacstuff.supersimplesoundboard.view.LoopView;

import java.util.HashSet;
import java.util.Optional;
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
    private LoopView loopView;
    private final Set<Integer> mutedLayers = new HashSet<>();
    private float loopMultiplier = 1f;
    private float nextLoopMultiplier = 1f;


    public LoopPlayer(LoopRecorder loopRecorder, SoundPlayer soundPlayer){
        executorService = Executors.newScheduledThreadPool(1);
        this.loopRecorder = loopRecorder;
        this.soundPlayer = soundPlayer;
    }


    public void setLoopView(LoopView loopView){
        this.loopView = loopView;
    }


    public void setLoopMultiplier(int value){
        nextLoopMultiplier = (Math.max(1, value))/ 10f;
    }


    public void play(){
        currentTime = 0;
        isPlaying.set(true);
        getView().ifPresent(LoopView::notifyLoopPlaying);
        future = executorService.scheduleAtFixedRate(this::playNextSound, 0, (long)(loopRecorder.getTimeDivisor() / loopMultiplier), TimeUnit.MILLISECONDS);
    }


    public void stop(){
        if(!isPlaying.get()) {
            return;
        }
        future.cancel(false);
        isPlaying.set(false);
        getView().ifPresent(LoopView::notifyLoopStopped);
        updateLoopMultiplier();
    }


    private Optional<LoopView> getView(){
        return Optional.ofNullable(loopView);
    }


    private void updateLoopViewProgress(){
        getView().ifPresent(v -> v.notifyLoopProgress(currentTime));
    }


    public void toggleMuted(int layerIndex){
        if(mutedLayers.contains(layerIndex)){
            unMuteLayer(layerIndex);
            return;
        }
        muteLayer(layerIndex);
    }


    private void muteLayer(int layerIndex){
        mutedLayers.add(layerIndex);
    }


    private void unMuteLayer(int layerIndex){
        mutedLayers.remove(layerIndex);
    }


    private void playNextSound(){
        Set<RecordedSounds> recordedSounds = loopRecorder.getSoundsForTime(currentTime);
        recordedSounds.stream()
                .filter(this::isNotMuted)
                .forEach(rs -> rs.soundIds().
                        forEach(soundPlayer::playSoundAtButton));
        updateCurrentTime();
        updateLoopViewProgress();
    }


    private boolean isNotMuted(RecordedSounds recordedSounds){
        return !mutedLayers.contains(recordedSounds.index());
    }


    private void updateCurrentTime(){
        currentTime++;
        if(currentTime > (loopRecorder.getDuration())){
            currentTime = 0;
            restartPlayIfLoopMultiplierChanged();
        }
    }


    private void restartPlayIfLoopMultiplierChanged(){
        if(loopMultiplier != nextLoopMultiplier){
            stop();
            play();
        }
    }


    private void updateLoopMultiplier(){
        loopMultiplier = nextLoopMultiplier;
    }

}
