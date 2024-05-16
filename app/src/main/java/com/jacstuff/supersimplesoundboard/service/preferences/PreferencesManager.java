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
       log("Entered getSteps() savedStepStr: " + savedStepsStr);
       int numberOfSteps = 16 ;//getPrefs().getInt(Prop.NUMBER_OF_STEPS.toString(), 16);
        List<List<Boolean>> temp = PrefUtils.getStepsFor(savedStepsStr, numberOfSteps);
        log("numberOfSteps: " + temp.size());
        log("number of sounds per step: " + temp.get(0).size());
        printStepRows(PrefUtils.getStepsFor(savedStepsStr, numberOfSteps));
        return PrefUtils.getStepsFor(savedStepsStr, numberOfSteps);
    }


    private void log(String msg){
        System.out.println("^^^ PreferencesManager: " + msg);
    }


    public void printStepRows(List<List<Boolean>> stepRows){
        for(List<Boolean> stepRow : stepRows){
            StringBuilder str = new StringBuilder();
            for(boolean cell : stepRow){
                str.append(cell);
                str.append(" ");
            }
            log("--> " + str);
        }
    }


    public void saveSteps(List<List<Boolean>> steps){
        log("Entered saveSteps() steps size: " + steps.size());
        getPrefs().edit()
                .putString(PreferencesManager.Prop.CURRENT_SONG_PART.toString(), PrefUtils.convertToStr(steps))
                .apply();
    }


    private SharedPreferences getPrefs(){
        return context.getSharedPreferences("soundboard", Context.MODE_PRIVATE);
    }
}
