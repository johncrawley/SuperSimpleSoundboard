package com.jacstuff.supersimplesoundboard.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import com.jacstuff.supersimplesoundboard.MainActivity;

public class SoundBoardService extends Service {


    private MainActivity mainActivity;
    private final IBinder binder = new LocalBinder();

    public SoundBoardService() {
    }


    @Override
    public void onCreate() {
        super.onCreate();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    public void setActivity(MainActivity mainActivity){
        this.mainActivity = mainActivity;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return Service.START_NOT_STICKY; // service is not restarted when terminated
    }


    public class LocalBinder extends Binder {
        public SoundBoardService getService() {
            return SoundBoardService.this;
        }
    }


    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

}