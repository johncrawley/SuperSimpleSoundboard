package com.jacstuff.supersimplesoundboard.service.recorder;


import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaRecorder;

import java.io.IOException;

public class AudioRecorder {

    private static String fileName = null;
    private MediaRecorder recorder = null;
    private MediaPlayer player = null;
    private boolean isRecording;
    private boolean isPlaying;


    public AudioRecorder(Context context){
        setupFilePathName(context);
    }

    public void toggleRecord(){
        if (!isRecording) {
            startRecording();
        } else {
            stopRecording();
        }
    }


    public void togglePlay(){
        if(isRecording){
            stopRecording();
        }
        playRecordedSound();
    }


    private void setupFilePathName(Context context){
        fileName = context.getFilesDir() + "/test.wav";
    }


    private void playRecordedSound() {
        if (!isPlaying) {
            startPlaying();
        } else {
            stopPlaying();
        }
    }


    private void startPlaying() {
        player = new MediaPlayer();
        try {
            player.setDataSource(fileName);
            player.prepare();
            player.start();
            isPlaying = true;
            player.setOnCompletionListener(mediaPlayer -> isPlaying = false);
        } catch (IOException e) {
            isPlaying = false;
            log("start playing prepare() failed");
        }
    }


    private void stopPlaying() {
        player.release();
        player = null;
    }


    private void startRecording() {
        isRecording = true;
        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        recorder.setOutputFile(fileName);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            recorder.prepare();
            recorder.start();
        } catch (IOException e) {
            log("startRecording() prepare() failed");
            isRecording = false;
        }
    }


    private void log(String msg){
        System.out.println("^^^ MainActivity: " + msg);
    }


    private void stopRecording() {
        recorder.stop();
        recorder.release();
        recorder = null;
        isRecording = false;
    }


}