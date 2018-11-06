package com.job.darasalecturer.service;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.job.darasalecturer.model.StudentDetails;
import com.job.darasalecturer.util.NotificationUtil;

import java.lang.reflect.Type;
import java.util.List;

import androidx.work.Worker;
import androidx.work.WorkerParameters;

/**
 * Created by Job on Tuesday : 11/6/2018.
 */
public class AddAttendanceWorker extends Worker {

    private static final String TAG = "AttendWorker";

    private FirebaseFirestore mFirestore;
    private Gson gson;

    // Define the parameter keys:

    public static final String KEY_STUD_LIST_ARG = "X";



    public AddAttendanceWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);

        mFirestore = FirebaseFirestore.getInstance();
        gson = new Gson();
    }

    @NonNull
    @Override
    public Result doWork() {

        if (mFirestore == null) {
            Log.d(TAG, "doWork:  mFirestore is null");
            mFirestore = FirebaseFirestore.getInstance();
        }

        String s = getInputData().getString(KEY_STUD_LIST_ARG);

        Type listOfStudObject = new TypeToken<List<StudentDetails>>(){}.getType();
        List<StudentDetails> studentDetailsList = gson.fromJson(s, listOfStudObject);

        if (studentDetailsList != null){

            doTheTransaction(new MyResultCallback() {
                @Override
                public Result onResultCallback(Result result) {
                    Log.d(TAG, "onResultCallback: => Worker.Result " + result.name());

                    return result;
                }
            }, studentDetailsList);
        }else {
            Log.d(TAG, "doWork: null list passed");
        }


        return Result.FAILURE;
    }

    private void doTheTransaction(final MyResultCallback resultCallback, final List<StudentDetails> studentDetailsList ) {


        for (StudentDetails s:studentDetailsList){

            String title = s.getFirstname();
            String message = s.getRegnumber() + "Confirmed";
            new NotificationUtil().showStandardHeadsUpNotification(getApplicationContext(), title, message);
        }

        resultCallback.onResultCallback(Result.SUCCESS);

        if (studentDetailsList.isEmpty()){
            resultCallback.onResultCallback(Result.FAILURE);
            Log.d(TAG, "doTheTransaction: empty list");
        }
    }
}
