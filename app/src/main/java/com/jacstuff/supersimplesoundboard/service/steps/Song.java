package com.jacstuff.supersimplesoundboard.service.steps;

import com.jacstuff.supersimplesoundboard.service.SoundHolder;
import com.jacstuff.supersimplesoundboard.service.SoundPlayer;
import com.jacstuff.supersimplesoundboard.service.preferences.PreferencesManager;
import com.jacstuff.supersimplesoundboard.view.MainView;

import java.util.ArrayList;
import java.util.List;

public class Song {

    private List<SongPart> songParts;
    private int currentIndex;
    private SongPart currentSongPart;
    private MainView view;
    private StepPlayer stepPlayer;

    public Song(List<SoundHolder> soundHolders, int numberOfParts, StepPlayer stepPlayer){
        songParts = new ArrayList<>(numberOfParts);
        this.stepPlayer = stepPlayer;
        for(int i = 0; i < numberOfParts; i++){
            songParts.add(new SongPart(16, soundHolders));
        }
        assignCurrentSongPart();
    }


    private void setView(MainView view){
        this.view = view;
    }


    public void loadSavedSteps(PreferencesManager preferencesManager){

        for(int i = 0; i< songParts.size(); i++){
            songParts.get(i).loadSteps(preferencesManager.getSteps(i));
        }
    }


    public void saveSteps(PreferencesManager preferencesManager){

    }


    public void loadNextSongPart(){
        incrementCurrentIndex();
        assignCurrentSongPart();
    }


    private void assignCurrentSongPart(){
        currentSongPart = songParts.get(currentIndex);
        currentSongPart.setView(view);
        currentSongPart.updateView();
    }


    private void incrementCurrentIndex(){
        currentIndex = currentIndex > songParts.size() -1 ? 0 : currentIndex + 1;
    }


    private void decrementCurrentIndex(){
        currentIndex = currentIndex - 1 < 0 ? songParts.size() -1 : currentIndex -1;
    }




}
