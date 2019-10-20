package com.job.darasalecturer.viewmodel;

import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;

import com.job.darasalecturer.model.StudentDetails;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Job on Tuesday : 8/14/2018.
 */
public class AddStudentViewModel extends ViewModel {

    private  List<StudentDetails> studentDetailsList = new ArrayList<>();
    private MediatorLiveData<List<StudentDetails>> studListMediatorLiveData = new MediatorLiveData<>();

    public MediatorLiveData<List<StudentDetails>> getStudListMediatorLiveData() {
        return studListMediatorLiveData;
    }

    public void setStudListMediatorLiveData(List<StudentDetails> studListMediatorLiveData) {
        this.studListMediatorLiveData.setValue(studListMediatorLiveData);
    }

    public List<StudentDetails> getStudentDetailsList() {
        return studentDetailsList;
    }

    public void setStudentDetailsList(List<StudentDetails> studentDetailsList) {
        this.studentDetailsList = studentDetailsList;
    }
}
