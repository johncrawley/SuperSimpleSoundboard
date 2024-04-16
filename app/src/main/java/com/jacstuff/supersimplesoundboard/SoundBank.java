package com.jacstuff.supersimplesoundboard;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SoundBank {

    private final String name;
    private final List<Sound> sounds;

    public SoundBank(String name){
        this.name = name;
        sounds = new ArrayList<>(100);
    }


    public void add(Sound sound){
        sounds.add(sound);
    }


    public String getName(){
        return name;
    }


    public void reverseSoundsOrder(){
        Collections.reverse(sounds);
    }


    public List<Sound> getSounds(){
        return sounds;
    }
}
