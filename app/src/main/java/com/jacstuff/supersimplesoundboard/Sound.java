package com.jacstuff.supersimplesoundboard;

public class Sound {

    private final String name;
    private int soundPoolId;
    private final String path;
    private final int octave;


    public Sound(Note note, String path, int octave ){
        this.name = note.getDisplayName();
        this.path = path;
        this.octave = octave;
    }


    public void setSoundPoolId(int soundPoolId){
        this.soundPoolId = soundPoolId;
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
