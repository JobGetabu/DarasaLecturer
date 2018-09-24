package com.job.darasalecturer.viewmodel;

import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.ViewModel;

/**
 * Created by Job on Monday : 9/24/2018.
 */
public class AddClassViewModel extends ViewModel {

    private MediatorLiveData<Integer> currentStep;

    public AddClassViewModel() {
        currentStep = new MediatorLiveData<>();
    }

    public MediatorLiveData<Integer> getCurrentStep() {
        return currentStep;
    }

    public void setCurrentStep(Integer currentStep) {
        this.currentStep.setValue(currentStep);
    }
}
