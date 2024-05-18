package com.jacstuff.supersimplesoundboard.service.steps;

import com.jacstuff.supersimplesoundboard.service.preferences.PreferencesManager;

import java.util.List;

public class Song {

    private List<SongPart> songParts;
    private SongPart currentSongPart;

    public void loadSavedSteps(PreferencesManager preferencesManager){

        for(int i = 0; i< songParts.size(); i++){
            songParts.get(i).loadSteps(preferencesManager.getSteps(i));
        }
    }


    public void saveSteps(PreferencesManager preferencesManager){

    }



}
