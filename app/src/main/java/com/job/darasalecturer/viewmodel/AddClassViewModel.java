package com.job.darasalecturer.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.ViewModel;

import com.job.darasalecturer.model.LecTeach;
import com.job.darasalecturer.model.LecTeachTime;

import java.util.List;

/**
 * Created by Job on Monday : 9/24/2018.
 */
public class AddClassViewModel extends ViewModel {

    private MediatorLiveData<Integer> currentStep;
    private MediatorLiveData<LecTeach> lecTeachMediatorLiveData = new MediatorLiveData<>();
    private MediatorLiveData<LecTeachTime> lecTeachTimeMediatorLiveData = new MediatorLiveData<>();
    private MediatorLiveData<List<String>> courseList = new MediatorLiveData<>();

    private LiveData<LecTeach> lecTeachLiveData;
    private LiveData<LecTeachTime> lecTeachTimeLiveData;

    public AddClassViewModel() {
        currentStep = new MediatorLiveData<>();

    }

    public MediatorLiveData<Integer> getCurrentStep() {
        return currentStep;
    }

    public void setCurrentStep(Integer currentStep) {
        this.currentStep.setValue(currentStep);
    }

    public MediatorLiveData<LecTeach> getLecTeachMediatorLiveData() {
        return lecTeachMediatorLiveData;
    }

    public void setLecTeachMediatorLiveData(LecTeach lecTeachMediatorLiveData) {
        this.lecTeachMediatorLiveData.setValue(lecTeachMediatorLiveData);
    }

    public MediatorLiveData<LecTeachTime> getLecTeachTimeMediatorLiveData() {
        return lecTeachTimeMediatorLiveData;
    }

    public void setLecTeachTimeMediatorLiveData(LecTeachTime lecTeachTimeMediatorLiveData) {
        this.lecTeachTimeMediatorLiveData.setValue(lecTeachTimeMediatorLiveData);
    }

    public MediatorLiveData<List<String>> getCourseList() {
        return courseList;
    }

    public void setCourseList(List<String> courseList) {
        this.courseList.setValue(courseList);
    }
}
