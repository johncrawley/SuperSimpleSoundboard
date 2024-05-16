package com.jacstuff.supersimplesoundboard.service.preferences;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class PrefUtils {

    public static List<List<Boolean>> getStepsFor(String savedStr, int numberOfSteps){
        List<Boolean> isEnabledList = convertToBoolArray(savedStr);
        int numberOfSounds = isEnabledList.size() / numberOfSteps;
        log("getStepsFor() savedStr length: " + savedStr.length() + " numberOfSteps: " + numberOfSteps + " numberOfSounds: " + numberOfSounds);
        List<List<Boolean>> steps = new ArrayList<>();
        for(int i = 0, index = 0; i < numberOfSounds; i++, index += numberOfSteps){
            addRowTo(steps, isEnabledList, numberOfSteps, index);
        }
        return steps;
    }


    private static void log(String msg){
        System.out.println("^^^ PrefUtils: " + msg);
    }


    public static void addRowTo(List<List<Boolean>> steps, List<Boolean> sourceList, int numberOfSteps, int index){
        List<Boolean> stepsRow = new ArrayList<>();
        for(int j = 0; j < numberOfSteps; j++){
            stepsRow.add(sourceList.get(index));
            index++;;
        }
        steps.add(stepsRow);
    }


    public static List<Boolean> convertToBoolArray(String savedStr){
        if(savedStr == null || savedStr.isEmpty()){
            return Collections.emptyList();
        }
        String[] strArray = savedStr.split("");
        List<Boolean> list = new ArrayList<>();
        for (String s : strArray) {
            list.add(s.equals("1"));
        }
        return list;
    }


    public static String convertToStr(List<List<Boolean>> steps){
        StringBuilder savedSteps = new StringBuilder();
        for(List<Boolean> step : steps){
            for(Boolean isEnabled : step){
                savedSteps.append(isEnabled ? "1" : "0");
            }
        }
        return savedSteps.toString();
    }


}
