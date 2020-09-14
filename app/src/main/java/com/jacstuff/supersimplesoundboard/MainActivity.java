package com.jacstuff.supersimplesoundboard;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    /*

        list view
        open soundsboards dir each time activity starts, get names of files
            - turn names of files into string list items
            - click list item go to new activity
             - grid - each item corresponds to file in chosen dir
             - click on file, play sound



     */


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}