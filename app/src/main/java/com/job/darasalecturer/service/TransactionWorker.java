package com.job.darasalecturer.service;

import android.content.Context;
import android.support.annotation.NonNull;

import androidx.work.Worker;
import androidx.work.WorkerParameters;

/**
 * Created by Job on Friday : 10/19/2018.
 */
public class TransactionWorker extends Worker {

    public TransactionWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        return null;
    }
}
