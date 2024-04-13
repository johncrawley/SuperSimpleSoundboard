package com.jacstuff.supersimplesoundboard;

import static org.junit.Assert.assertEquals;

import com.jacstuff.supersimplesoundboard.service.SoundLooper;

import org.junit.Before;
import org.junit.Test;

public class SoundLooperTest {

    private SoundLooper soundLooper;

    @Before
    public void init(){
        soundLooper = new SoundLooper();
    }

    @Test
    public void canRecordALoop(){
        soundLooper.startRecording();
        soundLooper.recordSound(1);
        soundLooper.recordSound(2);
        soundLooper.recordSound(1);
        soundLooper.stopRecording();
        assertEquals(3, soundLooper.getNumberOfSoundsRecorded());
    }


}
