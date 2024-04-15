package com.jacstuff.supersimplesoundboard;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.google.android.material.button.MaterialButton;
import com.jacstuff.supersimplesoundboard.service.SoundPlayer;


public class MainActivity extends AppCompatActivity {

    private LinearLayout buttonLayout;
    private LinearLayout.LayoutParams buttonParams;
    private SoundPlayer soundPlayer;


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        buttonLayout = findViewById(R.id.buttonLayout);
        assignButtonLayout();
        soundPlayer = new SoundPlayer(getApplicationContext());
        loadSounds();
    }


    private void log(String msg){
        System.out.println("^^^ MainActivity: "  + msg);
    }


    private void loadSounds(){
        SoundFactory soundFactory = new SoundFactory();
        SoundBank soundBank = soundFactory.getSoundBank("n_bass");

        int current = 0;
        for(Sound sound : soundBank.getSounds()){
            soundPlayer.loadSound(sound);
            setupButton(sound);
            if(current++ > 7){
                break;
            }
        }
    }


    @SuppressLint("ClickableViewAccessibility")
    private void setupButton(Sound sound){
        MaterialButton button = new MaterialButton(this);
        button.setPadding(2,2,2,2);
        button.setLayoutParams(buttonParams);
        button.setText(sound.getDisplayName());
        button.setId(View.generateViewId());
        button.setTag(sound.getSoundPoolId());
        buttonLayout.addView(button);
        button.setOnClickListener(v -> soundPlayer.playSound(sound));
    }


    private void assignButtonLayout(){
        buttonParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                100);
        buttonParams.setMargins(-20,-10,-20,-10);
    }


}