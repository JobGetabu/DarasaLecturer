package com.job.darasalecturer.viewmodel;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.job.darasalecturer.model.LecTeach;

/**
 * Created by Job on Thursday : 11/8/2018.
 */
public class UnitsViewModel extends ViewModel {

    private MutableLiveData<Boolean> isChecked;
    private MutableLiveData<LecTeach> lecTeachMutableLiveData;

    public UnitsViewModel() {
        isChecked = new MutableLiveData<>();
        lecTeachMutableLiveData = new MutableLiveData<>();
    }

    public MutableLiveData<Boolean> getIsChecked() {
        return isChecked;
    }

    public void setIsChecked(Boolean isChecked) {
        this.isChecked.setValue(isChecked);
    }

    public MutableLiveData<LecTeach> getLecTeachMutableLiveData() {
        return lecTeachMutableLiveData;
    }

    public void setLecTeachMutableLiveData(LecTeach lecTeach) {
        this.lecTeachMutableLiveData.setValue(lecTeach);
    }
}
