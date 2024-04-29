package com.jacstuff.supersimplesoundboard.view;

public interface MainView {

    void hideStepProgress();
    void setCurrentProgress(int index);
    void setNumberOfSteps(int numberOfSteps);
    void setStep(int index, boolean... enabled);
    void setBpmProgress(int progress);
}
