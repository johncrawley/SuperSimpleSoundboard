package com.jacstuff.supersimplesoundboard.view;

import java.util.List;

public interface MainView {

    void hideStepProgress();
    void setCurrentProgress(int index);
    void setNumberOfSteps(int numberOfSteps);
    void setStep(int index, List<Boolean> enabledList);
    void setStepRow(int rowIndex, List<Boolean> enabledSteps);
    void setBpmProgress(int progress);
}
