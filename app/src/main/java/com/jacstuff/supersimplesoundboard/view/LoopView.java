package com.jacstuff.supersimplesoundboard.view;

public interface LoopView {

    void notifyLoopRecordingStopped();
    void notifyEndTime(long endTime);
    void notifyLoopRecordingStarted();
    void notifyLoopRecordingCleared();
    void notifyLoopPlaying();
    void notifyLoopStopped();
    void notifyLoopProgress(int progress);
}
