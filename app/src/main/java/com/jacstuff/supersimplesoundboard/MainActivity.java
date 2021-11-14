package com.jacstuff.supersimplesoundboard;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.media.audiofx.EnvironmentalReverb;
import android.media.audiofx.PresetReverb;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, MediaPlayer.OnCompletionListener {

    /*

        list view
        open soundsboards dir each time activity starts, get names of files
            - turn names of files into string list items
            - click list item go to new activity
             - grid - each item corresponds to file in chosen dir
             - click on file, play sound
     */


    private Button playButton, reverbButton;
    private MediaPlayer mediaPlayer;
    private PresetReverb presetReverb;
    private  android.media.audiofx.EnvironmentalReverb environmentalReverb;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //createSoundboardsDirIfDoesntExist();
        //listExternalStorage();
        //1filesDir();

        playButton = findViewById(R.id.test_sound_button);
        reverbButton = findViewById(R.id.reverbButton);
        playButton.setText("Play Sound");
        assignTrack();
        playButton.setOnClickListener(playListener);
        int audioSessionId = mediaPlayer.getAudioSessionId();
        presetReverb = new PresetReverb(0, mediaPlayer.getAudioSessionId());
        environmentalReverb = new EnvironmentalReverb(0, mediaPlayer.getAudioSessionId());
        environmentalReverb.setDecayTime(200);
        environmentalReverb.setReverbLevel((short) 8);
        presetReverb.setPreset(PresetReverb.PRESET_LARGEHALL);
        presetReverb.setEnabled(false);
        reverbButton.setOnClickListener(this);
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mediaPlayer != null) {
            mediaPlayer.release();
        }
    }


    @Override
    public void onClick(View v) {
        int viewId = v.getId();
        if(viewId == R.id.reverbButton){
            presetReverb.setEnabled(!presetReverb.getEnabled());
            environmentalReverb.setEnabled(!environmentalReverb.getEnabled());
        }
    }

    private void assignTrack(){
        mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.test_note);
    }

    private final View.OnClickListener playListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            if(mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
                mediaPlayer.reset();
                System.out.println("audio session id :" + mediaPlayer.getAudioSessionId());
            }
            else {
                mediaPlayer.start();
            }
        }
    };

    @Override
    public void onCompletion(MediaPlayer mp) {
        //mediaPlayer.release();
        mediaPlayer.reset();
    }


    private void createSoundboardsDirIfDoesntExist(){
        String soundBoardsDirName = "Soundboards2/";
        File externalStorageDirectory = Environment.getExternalStorageDirectory();
        toast("dir path: " + externalStorageDirectory.getAbsolutePath());
        File[] filesArray = externalStorageDirectory.listFiles();
        if(filesArray == null){
            return;
        }
        List<File> files = Arrays.asList(filesArray);
        for(File f : files){
            System.out.println("external storage directory files: " + f.getName());
        }
        String parentDir = Environment.DIRECTORY_DCIM;
        File parentDirFile = new File(externalStorageDirectory, parentDir);
        File soundBoardsDir = new File(parentDirFile, soundBoardsDirName);

        if(soundBoardsDir.exists()){
            System.out.println("soundboard dir already exists");
            Toast.makeText(getApplicationContext(),"soundboard Directory exists!",Toast.LENGTH_LONG).show();
            return;
        }

        boolean created = soundBoardsDir.mkdir();
        System.out.println("soundboardsDir absolute path: "  + soundBoardsDir.getAbsolutePath());
        System.out.println("******* Create soundboardDir result: " + created);
        Toast.makeText(getApplicationContext(),"Directory exists: " + soundBoardsDir.getAbsolutePath() + " : " + soundBoardsDir.exists(),
                Toast.LENGTH_LONG).show();
        Toast.makeText(getApplicationContext(),"Parent Directory exists: " + parentDirFile.getAbsolutePath() + " : " + parentDirFile.exists(),
                Toast.LENGTH_LONG).show();


        File filesDir = getExternalFilesDir(null);
        File myDir = new File(filesDir, "soundBoards");
        boolean created2 = myDir.mkdirs();
        toast("files dir: " + myDir.getAbsolutePath() + " created: " + created2);


       // createDir();
    }

    private void toast(String msg){

        Toast.makeText(getApplicationContext(), msg,
                Toast.LENGTH_LONG).show();
    }

    private void setupList(){
        //ListView list = findViewById(R.id.list1);
        //listAdapterHelper = new ListAdapterHelper(context, list, this);
        refreshListFromDb();
    }


    public void refreshListFromDb(){
    //    List<SimpleListItem> items = db.getAnswerItems(answerPoolId);
        //listAdapterHelper.setupList(items, android.R.layout.simple_list_item_1, noResultsFoundView);
    }

     private void createDir(){
        setContentView(R.layout.activity_main);
        File file = new File(Environment.getExternalStorageDirectory()+"/Sample Directory");
        boolean success = true;
          if(!file.exists()) {
            Toast.makeText(getApplicationContext(),"Directory does not exist, create it",
                    Toast.LENGTH_LONG).show();
        }
          if(success) {
            Toast.makeText(getApplication(),"Directory created",
                    Toast.LENGTH_LONG).show();
        }
          else {
            Toast.makeText(this,"Failed to create Directory",
                    Toast.LENGTH_LONG).show();
        }
    }


        private void listExternalStorage() {
            String state = Environment.getExternalStorageState();

            if (Environment.MEDIA_MOUNTED.equals(state) || Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
                listFiles(Environment.getExternalStorageDirectory());
                Toast.makeText(this, "Successfully listed all the files!", Toast.LENGTH_SHORT)
                        .show();
            }
        }

        /**
         * Recursively list files from a given directory.
         */
        private void listFiles(File dir) {

            System.out.println("Entered listFiles()");
            File[] files = dir.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file != null) {
                        System.out.println("file :" + file.getAbsolutePath());
                    }
                }
            }
            System.out.println("Exiting listFiles()");
        }


        private void listDataDirFiles(){
            File dataDir = Environment.getRootDirectory();
            File[] files = dataDir.listFiles();
            if(files == null){
                System.out.println("File are null!");
                return;
            }
            for(File f: files){
                System.out.println("**** " + f.getAbsolutePath());
            }

            System.out.println("Environment music :"  + Environment.DIRECTORY_MUSIC);

        }

        private void getMusicDir(){
            String music = Environment.DIRECTORY_MUSIC;
            File file = new File(music);
            listFiles(file);

        }

        private void filesDir(){
            File[] files = getExternalMediaDirs();
            File[] fs2  = getExternalFilesDirs(Environment.DIRECTORY_MUSIC);
            for(File f: fs2){
                System.out.println("********* " + f.getAbsolutePath());
                listFiles(f);
            }
        }

}