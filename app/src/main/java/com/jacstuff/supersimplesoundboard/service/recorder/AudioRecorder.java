package com.jacstuff.supersimplesoundboard.service.recorder;


import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaRecorder;

import com.jacstuff.supersimplesoundboard.view.RecordPlaybackView;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class AudioRecorder {

    private static String fileName = null;
    private MediaRecorder recorder = null;
    private MediaPlayer player = null;
    private final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
    private Future<?> timerFuture;
    private final RecordPlaybackView recordPlaybackView;

    public AudioRecorder(RecordPlaybackView recordPlaybackView, Context context){
        this.recordPlaybackView = recordPlaybackView;
        setupFilePathName(context);
    }


    private void cancelTimer(){
        if(timerFuture != null && !timerFuture.isCancelled()){
            timerFuture.cancel(false);
        }
    }


    private void startTimer(){
       timerFuture = executorService.schedule(this::stopRecording, 4800, TimeUnit.MILLISECONDS);
    }


    private void setupFilePathName(Context context){
        fileName = context.getFilesDir() + "/test.wav";
    }



    public void startPlaying() {
        player = new MediaPlayer();
        try {
            player.setDataSource(fileName);
            player.prepare();
            player.start();
            recordPlaybackView.notifyPlaybackStarted();
            player.setOnCompletionListener(mediaPlayer -> recordPlaybackView.notifyPlaybackStopped());
        } catch (IOException e) {
            log("start playing prepare() failed");
        }
    }


    public void stopPlaying() {
        if(player != null){
            player.release();
            player = null;
            recordPlaybackView.notifyPlaybackStopped();
        }
    }


    public void startRecording() {
        startTimer();
        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        recorder.setOutputFile(fileName);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            recorder.prepare();
            recorder.start();
            recordPlaybackView.notifyRecordingStarted();
        } catch (IOException e) {
            log("startRecording() prepare() failed");
            cancelTimer();
        }
    }


    private void log(String msg){
        System.out.println("^^^ MainActivity: " + msg);
    }


    public void stopRecording() {
        log("Entered stopRecording()");
        cancelTimer();
        recorder.stop();
        recorder.release();
        recorder = null;
        recordPlaybackView.notifyRecordingStopped();
        log("exiting stopRecording()");
    }


}