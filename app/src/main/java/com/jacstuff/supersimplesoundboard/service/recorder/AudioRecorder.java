package com.jacstuff.supersimplesoundboard.service.recorder;


import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;

import androidx.core.app.ActivityCompat;

import java.io.FileOutputStream;
import java.io.IOException;

public class AudioRecorder {

    private static final int RECORDER_SAMPLE_RATE = 8000;
    private static final int RECORDER_CHANNELS = AudioFormat.CHANNEL_IN_MONO;
    private static final int RECORDER_AUDIO_ENCODING = AudioFormat.ENCODING_PCM_16BIT;
    private final int BufferElements2Rec = 1024; // want to play 2048 (2K) since 2 bytes we use only 1024
    private final int BytesPerElement = 2; // 2 bytes in 16bit format
    private AudioRecord recorder = null;
    private Thread recordingThread = null;
    private boolean isRecording = false;
    private final Context context;



    int bufferSize = AudioRecord.getMinBufferSize(RECORDER_SAMPLE_RATE,
            RECORDER_CHANNELS, RECORDER_AUDIO_ENCODING);

    public AudioRecorder(Context context){
        this.context = context;
    }


    public void startRecording() {

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        recorder = new AudioRecord(MediaRecorder.AudioSource.MIC,
                RECORDER_SAMPLE_RATE,
                RECORDER_CHANNELS,
                RECORDER_AUDIO_ENCODING,
                BufferElements2Rec * BytesPerElement);

        recorder.startRecording();
        isRecording = true;
        recordingThread = new Thread(new Runnable() {
            public void run() {
                writeAudioDataToFile();
            }
        }, "AudioRecorder Thread");
        recordingThread.start();
    }



    public void stopRecording() {
        if (null != recorder) {
            isRecording = false;
            recorder.stop();
            recorder.release();
            recorder = null;
            recordingThread = null;
        }
    }


    //convert short to byte
    private byte[] short2byte(short[] sData) {
        byte[] bytes = new byte[sData.length * 2];
        for(int i = 0; i < sData.length; i++) {
            bytes[i * 2] = (byte) (sData[i] & 0x00FF);
            bytes[(i * 2) + 1] = (byte) (sData[i] >> 8);
            sData[i] = 0;
        }
        return bytes;
    }


    private void log(String msg){
        System.out.println("AudioRecorder: " + msg);
    }


    private void writeAudioDataToFile() {
        // Write the output audio in byte

        String filePath = "/sdcard/voice8K16bitmono.pcm";
        short[] sData = new short[BufferElements2Rec];

        try (FileOutputStream os = new FileOutputStream(filePath)){
            while (isRecording) {
                recorder.read(sData, 0, BufferElements2Rec);
                byte[] bData = short2byte(sData);
                os.write(bData, 0, BufferElements2Rec * BytesPerElement);
            }

        } catch (IOException  e) {
            log(e.getMessage());
        }
    }


}