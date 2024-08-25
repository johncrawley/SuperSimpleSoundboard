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
    private List<List<Boolean>> stepRow = new ArrayList<>();
    private int numberOfSteps;
    private MainView view;

    public SongPart(int numberOfSteps, List<SoundHolder> soundHolders){
        this.numberOfSteps = numberOfSteps;
        setSoundHolders(soundHolders);
        initSoundsPerStepList();
        initEnabledSteps();
    }


    public List<List<Boolean>> getSteps(){
        return new ArrayList<>(stepRow);
    }


    public void loadSteps(List<List<Boolean>> stepColumns){
        if(stepColumns == null || stepColumns.isEmpty()){
            return;
        }
        stepRow = stepColumns;
        updateSoundsPerStepList(stepColumns);
        log("loadSteps() enabled steps size: " + stepRow.size());
    }


    public void toggleSelected(int stepIndex, int soundIndex){
        if(stepIndex >= numberOfSteps || soundIndex >= soundsPerStep.size()){
            return;
        }
        toggleIndex(stepIndex, soundIndex);
        toggleSelected(soundsPerStep.get(stepIndex), soundHolders.get(soundIndex));
    }


    public List<Sound> getSoundsForStep(int index){
        return soundsPerStep.get(index).stream().map(SoundHolder::sound).collect(Collectors.toList());
    }


    public void clearAllSteps(){
        for(int stepIndex = 0; stepIndex < soundsPerStep.size(); stepIndex++){
            for(int soundIndex = 0; soundIndex < soundHolders.size(); soundIndex++){
                setSelected(stepIndex, soundIndex, false);
            }
        }
        clearSteps();
        updateView();
    }


    private void clearSteps(){
        for(int i = 0; i < stepRow.size(); i++){
            List<Boolean> steps = stepRow.get(i);
            int numberOfSteps = steps.size();
            steps.clear();
            for(int j = 0; j < numberOfSteps; j++){
                steps.add(false);
            }
        }
    }


    public void setView(MainView view){
        this.view = view;
    }


    public void updateView(){
        if(view == null){
            log("updateView() view is null, returning");
            return;
        }
        log("entered updateView() enabledStepRows size: " + stepRow.size());
        for(int i = 0; i < stepRow.size(); i++){

            view.setStepRow(i, stepRow.get(i));
        }
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
        for(int i = 0; i < numberOfSteps; i++){
            soundsPerStep.add(new HashSet<>());
        }
    }


    private void initEnabledSteps(){
        stepRow = new ArrayList<>();
        for(int i = 0; i < numberOfSteps; i++){
            stepRow.add(createStepState());
        }
        log("initEnabledSteps() size: " + stepRow.size());
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



    private void toggleIndex(int stepIndex, int soundIndex){
        log("toggleIndex() stepIndex: " + stepIndex + " soundIndex: " + soundIndex + " enabledSteps.size() : " + stepRow.size());
        if(soundIndex >= stepRow.size()){
            log("toggleIndex step index exceeded");
            return;
        }
        List<Boolean> row = stepRow.get(soundIndex);
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




}
