package com.jacstuff.supersimplesoundboard;

import static org.junit.Assert.assertEquals;

import com.jacstuff.supersimplesoundboard.service.LoopRecorder;

import org.junit.Before;
import org.junit.Test;

public class LoopRecorderTest {

    private LoopRecorder loopRecorder;

    @Before
    public void init(){
        loopRecorder = new LoopRecorder();
    }

    @Test
    public void canRecordALoop(){
        loopRecorder.startRecording();
        loopRecorder.recordSound(1);
        loopRecorder.recordSound(2);
        loopRecorder.recordSound(1);
        loopRecorder.stopRecording();
        assertNumberOfSounds(3);
        loopRecorder.recordSound(3);
        assertNumberOfSounds(3);
        loopRecorder.startRecording();
        loopRecorder.recordSound(1);
        loopRecorder.stopRecording();
        assertNumberOfSounds(4);
    }

    @Test
    public void canWipeALoop(){
        loopRecorder.startRecording();
        loopRecorder.recordSound(1);
        loopRecorder.recordSound(2);
        loopRecorder.stopRecording();
        assertNumberOfSounds(2);
        loopRecorder.clear();
        assertNumberOfSounds(0);
    }


    private void assertNumberOfSounds(int expectedNumberOfSounds){
        assertEquals(expectedNumberOfSounds, loopRecorder.getNumberOfSoundsRecorded());
    }
}
