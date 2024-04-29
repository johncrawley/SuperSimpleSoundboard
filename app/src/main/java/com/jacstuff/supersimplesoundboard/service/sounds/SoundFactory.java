package com.jacstuff.supersimplesoundboard.service.sounds;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class SoundFactory {

    private final Map<String, SoundBank> soundBanks;

    public SoundFactory(){
        soundBanks = new HashMap<>(32);
        createSoundNoteBanks();
        createSoundBanks();
    }


    private void createSoundNoteBanks(){
        addSoundBank("volcanic", 8);
        addSoundBank("n_bass", 8);
    }

    private void createSoundBanks(){
        addSoundBank("Drums 1", "drum_1", "Kick, Snare, Floor Tom, Low Tom, High Tom, Closed Hat, Open Hat, Crash, Ride, Splash, Cowbell");
    }


    private void addSoundBank(String bankName, String path, String soundNamesStr){
        SoundBank soundBank = new SoundBank(bankName);
        for(String soundName : soundNamesStr.split(", ")){
            addTo(soundBank, path, soundName);
        }
        soundBanks.put(bankName, soundBank);
    }


    private void addTo(SoundBank soundBank, String dirName, String name){
        String filename = name.toLowerCase().replaceAll(" ", "_");
        String fullPath = createPath(dirName, filename, ".wav");
        soundBank.add(new Sound(name, fullPath));
    }


    private String createPath(String dirName, String fileName, String extension){
        return dirName
                + File.separator
                + fileName
                + extension;
    }


    public SoundBank getSoundBank(String name){
        return soundBanks.get(name);
    }


    private void addSoundBank(String name, int octaves){
        soundBanks.put(name, createSoundBank(name, octaves));
    }


    public SoundBank createSoundBank(String soundBankName, int numberOfOctaves){
        SoundBank soundBank = new SoundBank(soundBankName);
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
