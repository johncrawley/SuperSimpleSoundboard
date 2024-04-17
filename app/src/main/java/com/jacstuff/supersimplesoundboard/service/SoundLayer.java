package com.jacstuff.supersimplesoundboard.service;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class SoundLayer {
    private final Map<Long, Set<Integer>> recordedSounds;
    private int index;

    public SoundLayer(int index){
        this.index = index;
        recordedSounds = new HashMap<>();
    }

    public void addSound(long time, int soundId){
        recordedSounds.computeIfAbsent(time, k -> new HashSet<>()).add(soundId);
    }


    public RecordedSounds getRecordedSoundsAt(long time){
       return new RecordedSounds(index, recordedSounds.getOrDefault(time, Collections.emptySet()));
    }
}
