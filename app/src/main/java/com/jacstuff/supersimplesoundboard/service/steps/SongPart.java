package com.jacstuff.supersimplesoundboard.service.steps;

import com.jacstuff.supersimplesoundboard.service.sounds.Sound;
import com.jacstuff.supersimplesoundboard.service.SoundHolder;
import com.jacstuff.supersimplesoundboard.view.MainView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class SongPart {

    private List<Set<SoundHolder>> soundsPerStep;
    private List<SoundHolder> soundHolders;
    private List<List<Boolean>> enabledSteps = new ArrayList<>();
    private int numberOfSteps;
    private MainView view;

    public SongPart(int numberOfSteps, List<SoundHolder> soundHolders){
        this.numberOfSteps = numberOfSteps;
        setSoundHolders(soundHolders);
        initSoundsPerStepList();
        initEnabledSteps();
    }


    public List<List<Boolean>> getSteps(){
        return new ArrayList<>(enabledSteps);
    }


    public void loadSteps(List<List<Boolean>> steps){
        if(steps == null || steps.isEmpty()){
            return;
        }
        enabledSteps = steps;
        log("loadSteps() enabled steps size: " + enabledSteps.size());
    }


    private void updateSoundsPerStepList(){
        for(int i = 0; i< numberOfSteps; i++){
            for(int j = 0; j < Math.min(soundHolders.size(),8); j ++){

            }
        }

    }


    private void initSoundsPerStepList(){
        soundsPerStep = new ArrayList<>();
        for(int i=0; i < numberOfSteps; i++){
            soundsPerStep.add(new HashSet<>());
        }
    }


    private void initEnabledSteps(){
        enabledSteps = new ArrayList<>();
        for(int i = 0; i < numberOfSteps; i++){
            enabledSteps.add(createStepState());
        }
        log("initEnabledSteps() size: " + enabledSteps.size());
    }


    public void setSoundHolders(List<SoundHolder> soundHolders){
        this.soundHolders = new ArrayList<>(soundHolders);
    }


    private List<Boolean> createStepState(){
        List<Boolean> enabledList = new ArrayList<>();
        for(int i=0; i < soundHolders.size(); i ++){
            enabledList.add(false);
        }
        return enabledList;
    }


    public void toggleSelected(int stepIndex, int soundIndex){
        if(stepIndex >= numberOfSteps || soundIndex >= soundsPerStep.size()){
            return;
        }
        toggleIndex(stepIndex, soundIndex);
        toggleSelected(soundsPerStep.get(stepIndex), soundHolders.get(soundIndex));
    }


    private void toggleIndex(int stepIndex, int soundIndex){
        log("toggleIndex() stepIndex: " + stepIndex + " soundIndex: " + soundIndex + " enabledSteps.size() : " + enabledSteps.size());
        if(stepIndex >= enabledSteps.size()){
            log("toggleIndex step index exceeded");
            return;
        }
        List<Boolean> enabledSounds = enabledSteps.get(stepIndex);
        if(enabledSounds != null && soundIndex < enabledSounds.size()){
            boolean oldSoundIndex = enabledSounds.get(soundIndex);
            enabledSounds.set(soundIndex, !oldSoundIndex);
        }
    }


    private void toggleSelected(Set<SoundHolder> step, SoundHolder soundHolder){
        if(step.contains(soundHolder)){
            step.remove(soundHolder);
        }else{
            step.add(soundHolder);
        }
    }

    private void log(String msg){
        System.out.println("^^^ SongPart: " + msg);
    }


    public List<Sound> getSoundsForStep(int index){
        return soundsPerStep.get(index).stream().map(SoundHolder::sound).collect(Collectors.toList());
    }


    public void setView(MainView view){
        this.view = view;
    }

    public void updateView(){
        for(int i = 0; i < enabledSteps.size(); i++){
            view.setStep(i, enabledSteps.get(i));
        }
    }


}
