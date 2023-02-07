package com.jacstuff.supersimplesoundboard;

public enum Note {

    F("F"),
    F_SHARP("F#"),
    G("G"),
    G_SHARP("G#"),
    A("A"),
    A_SHARP("A#"),
    B("B"),
    C("C "),
    C_SHARP("C#"),
    D("D"),
    D_SHARP("D#"),
    E("E");

    private final String displayName, filePrefix;


    Note(String displayName){
        this.displayName = displayName;
        String sharp = displayName.length() > 1 ? "s" : "";
        this.filePrefix = displayName.toLowerCase().charAt(0) + sharp;
    }


    public String getDisplayName(){
        return displayName;
    }


    public String getFilePrefix(){
        return filePrefix;
    }
}
