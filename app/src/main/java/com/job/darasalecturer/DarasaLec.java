package com.job.darasalecturer;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;
import androidx.appcompat.app.AppCompatDelegate;

import com.crashlytics.android.Crashlytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;

import io.fabric.sdk.android.Fabric;

/**
 * Created by Job on Tuesday : 7/24/2018.
 */
public class DarasaLec extends MultiDexApplication {

    //this works < 19
    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    FirebaseAuth mAuth;
    FirebaseFirestore mFirestore;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    public void onCreate() {
        super.onCreate();
        //firebase init

        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();

        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .setTimestampsInSnapshotsEnabled(true)
                .build();
        mFirestore.setFirestoreSettings(settings);

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                if (firebaseUser != null) {
                    // do stuff
                    // Check if user is signed in (non-null) and update UI accordingly.

                }
            }
        };

        mAuth.addAuthStateListener(mAuthListener);

        //crashlytics
        if (!BuildConfig.DEBUG) {
            Fabric.with(this, new Crashlytics());
        }
    }

    @Override
    protected void attachBaseContext(Context context) {
        super.attachBaseContext(context);
        MultiDex.install(this);
    }

    public FirebaseFirestore getmFirestore() {
        return mFirestore;
    }
}
