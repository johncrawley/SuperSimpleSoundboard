package com.jacstuff.supersimplesoundboard.service.steps;

import com.jacstuff.supersimplesoundboard.service.sounds.Sound;

import java.util.Optional;

public interface SoundBoardService {

    void play();
    void stopAndReset();
    void playSoundAtIndex(int index);
    void toggleStep(int stepIndex, int soundIndex);
    void setBpm(int bpm);

}
