package com.jacstuff.supersimplesoundboard.service.steps;

import com.jacstuff.supersimplesoundboard.Sound;
import com.jacstuff.supersimplesoundboard.service.SoundHolder;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class SoundSteps {
    private final List<Set<SoundHolder>> steps;

    public SoundSteps(int numberOfSteps){
        steps = new ArrayList<>();
        for(int i=0; i < numberOfSteps; i++){
            steps.add(new HashSet<>());
        }
    }


    public void toggleSelected(int index, SoundHolder soundHolder){
        var step = steps.get(index);
        if(step.contains(soundHolder)){
            step.remove(soundHolder);
        }else{
            step.add(soundHolder);
        }
    }


    public List<Sound> getSoundsForStep(int index){
        return steps.get(index).stream().map(SoundHolder::sound).collect(Collectors.toList());
    }


}
