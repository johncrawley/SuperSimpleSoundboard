package com.jacstuff.supersimplesoundboard;


import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class SoundFactory {

    private final Map<String, SoundBank> soundBanks;

    public SoundFactory(){
        soundBanks = new HashMap<>(32);
        createSoundBanks();
    }


    private void createSoundBanks(){
        addSoundBank("volcanic", 8);
    }


    public SoundBank getSoundBank(String name){
        return soundBanks.get(name);
    }


    private void addSoundBank(String name, int octaves){
        soundBanks.put(name, createSoundBank(name, octaves));
    }


    public SoundBank createSoundBank(String soundBankName, int numberOfOctaves){
        SoundBank soundBank = new SoundBank("volcanic");
        for(int octave = 0; octave< numberOfOctaves; octave++){
            for(Note note : Note.values()){
                String path = createPath(soundBankName, octave, note);
                soundBank.add(new Sound(note, path, octave));
            }
        }
        soundBank.reverseSoundsOrder();
        return soundBank;
    }


    private String createPath(String soundBankName, int octave, Note note){
        return soundBankName + File.separator
                + octave +  File.separator
                + note.getFilePrefix() + ".mp3";
    }
}
