package com.jacstuff.supersimplesoundboard;

public class Sound {

    private final String name;
    private int soundPoolId;
    private final String path;
    private final int octave;
    private int buttonNumber;

    public Sound(Note note, String path, int octave ){
        this.name = note.getDisplayName();
        this.path = path;
        this.octave = octave;
    }


    public Sound(String name, String path){
        this.name = name;
        this.path = path;
        this.octave = 0;
    }


    public void setSoundPoolId(int soundPoolId){
        this.soundPoolId = soundPoolId;
    }


    public void setButtonNumber(int buttonNumber){
        this.buttonNumber = buttonNumber;
    }


    public int getButtonNumber(){
        return buttonNumber;
    }

    public String getDisplayName(){
        return name;
    }


    public String getPath(){
        return path;
    }


    public int getOctave(){
        return octave;
    }


    public int getSoundPoolId(){
        return soundPoolId;
    }

}
