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
    private List<List<Boolean>> enabledStepColumns = new ArrayList<>();
    private int numberOfSteps;
    private MainView view;

    public SongPart(int numberOfSteps, List<SoundHolder> soundHolders){
        this.numberOfSteps = numberOfSteps;
        setSoundHolders(soundHolders);
        initSoundsPerStepList();
        initEnabledSteps();
    }


    public List<List<Boolean>> getSteps(){
        return new ArrayList<>(enabledStepColumns);
    }


    public void loadSteps(List<List<Boolean>> stepColumns){
        if(stepColumns == null || stepColumns.isEmpty()){
            return;
        }
        enabledStepColumns = stepColumns;
        updateSoundsPerStepList(stepColumns);
        log("loadSteps() enabled steps size: " + enabledStepColumns.size());
    }


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
        enabledStepColumns = new ArrayList<>();
        for(int i = 0; i < numberOfSteps; i++){
            enabledStepColumns.add(createStepState());
        }
        log("initEnabledSteps() size: " + enabledStepColumns.size());
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
        log("toggleIndex() stepIndex: " + stepIndex + " soundIndex: " + soundIndex + " enabledSteps.size() : " + enabledStepColumns.size());
        if(soundIndex >= enabledStepColumns.size()){
            log("toggleIndex step index exceeded");
            return;
        }
        List<Boolean> row = enabledStepColumns.get(soundIndex);
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


    public void clearAllSteps(){
        initSoundsPerStepList();
        initEnabledSteps();
        updateView();
    }


    public void setView(MainView view){
        this.view = view;
    }


    public void updateView(){
        if(view == null){
            log("updateView() view is null, returning");
            return;
        }
        log("entered updateView() enabledStepRows size: " + enabledStepColumns.size());
        for(int i = 0; i < enabledStepColumns.size(); i++){
            view.setStepRow(i, enabledStepColumns.get(i));
        }
    }


}
