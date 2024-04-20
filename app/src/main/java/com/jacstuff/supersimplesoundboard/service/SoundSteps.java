package com.jacstuff.supersimplesoundboard.service;

public class SoundSteps {
    private boolean[] steps;

    public SoundSteps(){
        this.steps = new boolean[]{};
    }


    public boolean isSelected(int index){
        return steps[index];
    }


    public void toggleSelected(int index){
        steps[index] = !steps[index];
    }


}
