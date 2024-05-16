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
    private List<List<Boolean>> enabledStepRows = new ArrayList<>();
    private int numberOfSteps;
    private MainView view;

    public SongPart(int numberOfSteps, List<SoundHolder> soundHolders){
        this.numberOfSteps = numberOfSteps;
        setSoundHolders(soundHolders);
        initSoundsPerStepList();
        initEnabledSteps();
    }


    public List<List<Boolean>> getSteps(){
        return new ArrayList<>(enabledStepRows);
    }


    public void loadSteps(List<List<Boolean>> stepRows){
        if(stepRows == null || stepRows.isEmpty()){
            return;
        }
        enabledStepRows = stepRows;
        updateSoundsPerStepList(stepRows);
        log("loadSteps() enabled steps size: " + enabledStepRows.size());
    }

// private List<Set<SoundHolder>> soundsPerStep;

    private void updateSoundsPerStepList(List<List<Boolean>> stepRows){
        numberOfSteps = stepRows.get(0).size();
        initSoundsPerStepList();

        for(int stepIndex = 0; stepIndex< numberOfSteps; stepIndex++){
            for(int soundIndex= 0; soundIndex < Math.min(soundHolders.size(),8); soundIndex ++){
                boolean isEnabled = stepRows.get(soundIndex).get(stepIndex);
                setSelected(stepIndex, soundIndex, isEnabled);
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
        enabledStepRows = new ArrayList<>();
        for(int i = 0; i < numberOfSteps; i++){
            enabledStepRows.add(createStepState());
        }
        log("initEnabledSteps() size: " + enabledStepRows.size());
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
        log("toggleIndex() stepIndex: " + stepIndex + " soundIndex: " + soundIndex + " enabledSteps.size() : " + enabledStepRows.size());
        if(soundIndex >= enabledStepRows.size()){
            log("toggleIndex step index exceeded");
            return;
        }
        List<Boolean> row = enabledStepRows.get(soundIndex);
        if(row != null && stepIndex < row.size()){
            boolean oldState = row.get(stepIndex);
            row.set(stepIndex, !oldState);
        }
    }


    private void toggleSelected(Set<SoundHolder> step, SoundHolder soundHolder){
        if(step.contains(soundHolder)){
            step.remove(soundHolder);
        }else{
            step.add(soundHolder);
        }
    }


    private void setSelected(int stepIndex, int soundIndex, boolean isEnabled){
        if(stepIndex >= numberOfSteps || soundIndex >= soundsPerStep.size()){
            return;
        }
        toggleIndex(stepIndex, soundIndex);
        setSelectedSound(soundsPerStep.get(stepIndex), soundHolders.get(soundIndex), isEnabled);
    }


    private void setSelectedSound(Set<SoundHolder> step, SoundHolder soundHolder, boolean isSelected){
        if(isSelected){
            step.add(soundHolder);
        }
        else{
            step.remove(soundHolder);
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
        if(view == null){
            log("updateView() view is null, returning");
            return;
        }
        for(int i = 0; i < enabledStepRows.size(); i++){
            view.setStepRow(i, enabledStepRows.get(i));
        }
    }


}
