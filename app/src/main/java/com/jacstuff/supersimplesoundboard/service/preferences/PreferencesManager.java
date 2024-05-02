package com.jacstuff.supersimplesoundboard.service.preferences;

import android.content.Context;
import android.content.SharedPreferences;
import java.util.List;

public class PreferencesManager {

    private Context context;
    private enum Prop { CURRENT_SONG_PART, NUMBER_OF_STEPS }

    public PreferencesManager(Context context){
        this.context = context;
    }


    public List<List<Boolean>> getSteps(){
       String savedStepsStr =  getPrefs().getString(Prop.CURRENT_SONG_PART.toString(), "");
       int numberOfSteps = getPrefs().getInt(Prop.NUMBER_OF_STEPS.toString(), 16);
        return PrefUtils.getStepsFor(savedStepsStr, numberOfSteps);
    }



    public void saveSteps(List<List<Boolean>> steps){
        getPrefs().edit()
                .putString(PreferencesManager.Prop.CURRENT_SONG_PART.toString(), PrefUtils.convertToStr(steps))
                .apply();
    }


    private SharedPreferences getPrefs(){
        return context.getSharedPreferences("soundboard", Context.MODE_PRIVATE);
    }
}
