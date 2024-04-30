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

    private List<Set<SoundHolder>> steps;
    private final List<SoundHolder> soundHolders;
    private List<List<Boolean>> enabledSteps = new ArrayList<>();
    private int numberOfSteps;
    private MainView view;

    public SongPart(int numberOfSteps){
        this.numberOfSteps = numberOfSteps;
        soundHolders = new ArrayList<>();
        initSteps();
        initEnabledSteps();
    }


    private void initSteps(){
        steps = new ArrayList<>();
        for(int i=0; i < numberOfSteps; i++){
            steps.add(new HashSet<>());
        }
    }

    private void initEnabledSteps(){
        enabledSteps = new ArrayList<>();
        for(int i = 0; i < numberOfSteps; i++){
            enabledSteps.add(createStepState());
        }
    }

    private List<Boolean> createStepState(){
        List<Boolean> enabledList = new ArrayList<>();
        for(int i=0; i < soundHolders.size(); i ++){
            enabledList.add(false);
        }
        return enabledList;
    }


    public void toggleSelected(int stepIndex, int soundIndex){
        if(stepIndex > steps.size()){
            return;
        }
        toggleIndex(stepIndex, soundIndex);
        toggleSelected(steps.get(stepIndex), soundHolders.get(soundIndex));
    }


    private void toggleIndex(int stepIndex, int soundIndex){
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


    public List<Sound> getSoundsForStep(int index){
        return steps.get(index).stream().map(SoundHolder::sound).collect(Collectors.toList());
    }


    public void setView(MainView view){
        this.view = view;
    }


}
