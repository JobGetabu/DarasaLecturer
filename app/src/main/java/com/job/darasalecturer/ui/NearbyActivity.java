package com.job.darasalecturer.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.nearby.messages.Message;
import com.google.android.gms.nearby.messages.MessageListener;
import com.job.darasalecturer.R;

public class NearbyActivity extends AppCompatActivity {

    private static final String TAG = "NearbyActivity";
    private MessageListener mMessageListener;
    private Message mMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nearby);


    }
}
