package com.jacstuff.supersimplesoundboard.service;

import static org.junit.Assert.assertEquals;

import com.jacstuff.supersimplesoundboard.service.preferences.PrefUtils;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class PrefUtilsTest {

    
    @Test
    public void testBoolConverter(){
        List<Boolean> list =  PrefUtils.convertToBoolArray("1001");
        assertList(list, true, false, false, true);

        list = PrefUtils.convertToBoolArray("");
        assertEquals(0, list.size());

        list =  PrefUtils.convertToBoolArray("00000000");
        assertList(list, false, false, false, false, false, false, false, false);
    }


    @Test
    public void canAddRowToStepsList(){
        List<List<Boolean>> stepsList = new ArrayList<>();
        List<Boolean> sourceArray = List.of(true, true, true, false, false, false);
        int numberOfSteps = 3;
        int index = 0;
        PrefUtils.addRowTo(stepsList, sourceArray, numberOfSteps, index);
        index += numberOfSteps;
        PrefUtils.addRowTo(stepsList, sourceArray, numberOfSteps, index);
        assertList(stepsList.get(0), true, true, true);
        assertList(stepsList.get(1), false, false, false);
    }


    @Test
    public void canConvertSavedStrToStepsList(){
        String savedStr = "1111000010101100";
        int numberOfSteps = 4;
        List<List<Boolean>> steps =  PrefUtils.getStepsFor(savedStr, numberOfSteps);
        assertEquals(4, steps.size());
    }


    @Test
    public void canConvertStepsToAStringAndBack(){
        List<List<Boolean>> steps = new ArrayList<>();
        steps.add(List.of(true,true,true));
        steps.add(List.of(false, true, true));
        steps.add(List.of(false, false, false));

        String savedStr = PrefUtils.convertToStr(steps);
        assertEquals("111011000", savedStr);
        List<List<Boolean>> retrievedSteps = PrefUtils.getStepsFor(savedStr, 3);
        for(int i = 0; i< steps.size(); i++) {
            assertListsAreEqual(steps.get(i), retrievedSteps.get(i));
        }
    }


    private void assertListsAreEqual(List<Boolean> list1, List<Boolean> list2){
        assertEquals(list1.size(), list2.size());
        for(int i = 0; i<list1.size(); i++){
            assertEquals(list1.get(i), list2.get(i));
        }
    }


    @SafeVarargs
    private <T> void assertList(List<T> list, T... values){
        assertEquals(list.size(), values.length);
        for(int i = 0; i < values.length; i++){
            assertEquals(list.get(i), values[i]);
        }
    }
}
