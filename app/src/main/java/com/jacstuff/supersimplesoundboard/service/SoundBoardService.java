package com.jacstuff.supersimplesoundboard.service;


public interface SoundBoardService {

    void play();
    void stopAndReset();
    void playSoundAtIndex(int index);
    void toggleStep(int stepIndex, int soundIndex);
    void setBpm(int bpm);
    void onPause();
    void onResume();

}
