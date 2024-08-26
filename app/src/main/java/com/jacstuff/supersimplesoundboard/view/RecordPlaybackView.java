package com.jacstuff.supersimplesoundboard.view;

public interface RecordPlaybackView {

    void notifyRecordingStarted();
    void notifyRecordingStopped();
    void notifyPlaybackStarted();
    void notifyPlaybackStopped();
}
