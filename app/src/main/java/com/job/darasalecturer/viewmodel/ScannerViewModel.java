package com.job.darasalecturer.viewmodel;

import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.ViewModel;
import android.os.CountDownTimer;

/**
 * Created by Job on Tuesday : 8/7/2018.
 */
public class ScannerViewModel extends ViewModel {

    private MediatorLiveData<String> timeLiveData = new MediatorLiveData<>();

    public ScannerViewModel() {
        timer();
    }

    public MediatorLiveData<String> getTimeLiveData() {
        return timeLiveData;
    }

    public void setTimeLiveData(String timeLiveData) {
        this.timeLiveData.setValue(timeLiveData);
    }

    private void timer(){
        new CountDownTimer(120000, 1000) {

            public void onTick(long millisUntilFinished) {
                String timer = (toMinutes(millisUntilFinished) +" : " + toSec(millisUntilFinished));
                timeLiveData.setValue(timer);
            }

            public void onFinish() {
                String timer = "done!";
                timeLiveData.setValue(timer);
                //scanLoading.stop();
            }
        }.start();
    }

    private String toMinutes(long millisUntilFinished){
        long min =  (millisUntilFinished) / (1000 * 60);
        return String.valueOf(min);
    }

    private String toSec(long millisUntilFinished){
        long remainedSecs = millisUntilFinished / 1000;
        return String.valueOf((remainedSecs % 60));
    }
}
