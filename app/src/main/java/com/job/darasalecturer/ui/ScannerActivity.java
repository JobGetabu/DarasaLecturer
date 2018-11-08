package com.job.darasalecturer.ui;

import android.app.ActivityManager;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.Address;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Source;
import com.google.firebase.firestore.Transaction;
import com.google.gson.Gson;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.job.darasalecturer.R;
import com.job.darasalecturer.appexecutor.DefaultExecutorSupplier;
import com.job.darasalecturer.model.QRParser;
import com.job.darasalecturer.service.TransactionWorker;
import com.job.darasalecturer.util.AppStatus;
import com.job.darasalecturer.util.DoSnack;
import com.job.darasalecturer.util.DrawableHelper;
import com.job.darasalecturer.viewmodel.ScannerViewModel;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import com.victor.loading.newton.NewtonCradleLoading;
import com.victor.loading.rotate.RotateLoading;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;
import io.nlopez.smartlocation.OnLocationUpdatedListener;
import io.nlopez.smartlocation.OnReverseGeocodingListener;
import io.nlopez.smartlocation.SmartLocation;
import io.nlopez.smartlocation.location.LocationProvider;
import io.nlopez.smartlocation.location.config.LocationParams;
import io.nlopez.smartlocation.location.providers.LocationGooglePlayServicesProvider;
import io.nlopez.smartlocation.location.providers.LocationManagerProvider;
import io.nlopez.smartlocation.location.providers.MultiFallbackProvider;
import me.zhanghai.android.materialratingbar.MaterialRatingBar;

import static android.widget.Toast.LENGTH_LONG;
import static com.job.darasalecturer.service.TransactionWorker.KEY_QR_LECTTID_ARG;
import static com.job.darasalecturer.service.TransactionWorker.KEY_QR_UNITCODE_ARG;
import static com.job.darasalecturer.service.TransactionWorker.KEY_QR_UNITNAME_ARG;
import static com.job.darasalecturer.ui.AddAttendanceActivity.ADDATTENDANCE_EXTRA;
import static com.job.darasalecturer.util.Constants.DATE_SCAN_FORMAT;
import static com.job.darasalecturer.util.Constants.DONECLASSES;
import static com.job.darasalecturer.util.Constants.LECAUTHCOL;
import static com.job.darasalecturer.util.Constants.LECTEACHCOL;
import static com.job.darasalecturer.util.Constants.LECTEACHCOURSESUBCOL;
import static com.job.darasalecturer.util.Constants.STUDENTDETAILSCOL;
import static com.job.darasalecturer.util.Constants.STUDENTSCANCLASSCOL;

public class ScannerActivity extends AppCompatActivity {

    private static final int PIN_NUMBER_REQUEST_CODE = 200;
    private static final String TAG = "Scanner";
    public static final String QRPARSEREXTRA = "QRPARSEREXTRA";
    public static final String VENUEEXTRA = "VENUEEXTRA";
    public static final String LECTEACHIDEXTRA = "LECTEACHIDEXTRA";

    private static final int LOCATION_PERMISSION_ID = 1001;

    @BindView(R.id.scan_toolbar)
    Toolbar scanToolbar;
    @BindView(R.id.scan_loading)
    NewtonCradleLoading scanLoading;
    @BindView(R.id.scan_timer_text)
    TextView scanTimerText;
    @BindView(R.id.scan_percentage_text)
    TextView scanPercentageText;
    @BindView(R.id.scan_satisfaction_progressBar)
    ProgressBar scanSatisfactionProgressBar;
    @BindView(R.id.rotateloading)
    RotateLoading rotateloading;
    @BindView(R.id.scan_loading_view)
    FrameLayout scanLoadingView;
    @BindView(R.id.scan_qr_imageView)
    ImageView scanQrImageView;
    @BindView(R.id.scan_unit_name)
    TextView scanUnitName;
    @BindView(R.id.scan_unit_details)
    TextView scanUnitDetails;
    @BindView(R.id.item_col_rating)
    MaterialRatingBar itemColRating;
    @BindView(R.id.scan_location_bool)
    TextView scanLocationBool;
    @BindView(R.id.scan_num_students)
    TextView scanNumStudents;
    @BindView(R.id.scan_loc_img)
    ImageView scanLocImg;
    @BindView(R.id.scan_venue)
    TextView scanVenue;
    @BindView(R.id.scan_loading_image)
    ImageView scanLoadingImage;


    private ScannerViewModel model;
    private ActivityManager am;
    private MenuItem pinMenu;
    private boolean pinned;
    private DoSnack doSnack;
    public static String userpasscode = null;
    private QRParser qrParser;
    private Gson gson;

    private FirebaseFirestore mFirestore;
    private FirebaseAuth mAuth;

    private LocationGooglePlayServicesProvider provider;
    private SweetAlertDialog pDialogLoc;
    private Location mLocation;
    private int locationcount = 1;
    private ShowPasscodeFragment showPasscodeFragment;

    private int noOfStudents;
    private int noOfScans;
    private int index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);
        ButterKnife.bind(this);

        setSupportActionBar(scanToolbar);
        getSupportActionBar().setTitle("");


        //firebase
        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();

        //init model
        model = ViewModelProviders.of(this).get(ScannerViewModel.class);

        //get qrparser
        qrParser = getIntent().getParcelableExtra(QRPARSEREXTRA);
        gson = new Gson();
        setUpUi(qrParser);

        am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);

        //temporary
        initPinning();

        doSnack = new DoSnack(this, ScannerActivity.this);

        initTimer();

        // Keep the screen always on
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        /*
        // Check if the location services are enabled
        checkLocationOn();
        SmartLocation.with(this).location().state().locationServicesEnabled();
        // Location permission not granted
        if (ContextCompat.checkSelfPermission(ScannerActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(ScannerActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_ID);
            return;
        }
        startLocation();
        */

        showLoader(true);
        rotateloading.start();

        //init our fragment
        showPasscodeFragment = new ShowPasscodeFragment();

        //set up password
        setInitPasscode();

        generateQR();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.smenu, menu);
        pinMenu = menu.findItem(R.id.smenu_pin);
        updatePinningStatus();
        return super.onCreateOptionsMenu(menu);
    }

    public void updatePinningStatus() {
        if (pinned) {
            pinMenu.setIcon(R.drawable.ic_unpin);
        } else {
            pinMenu.setIcon(R.drawable.ic_pin);
        }
    }

    private void initPinning() {
        Toast.makeText(this, "Screen pinned", Toast.LENGTH_SHORT).show();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            pin();
            pinned = true;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        int id = item.getItemId();
        switch (id) {
            case R.id.smenu_pin:

                if (pinned) {
                    showPasscodeFragment.show(getSupportFragmentManager(), ShowPasscodeFragment.TAG);
                    showPasscodeFragment.setOnSuccessFail(new ShowPasscodeFragment.OnSuccessFail() {
                        @Override
                        public void onSuccess() {
                            if (pinned) {
                                unpin();

                                Toast.makeText(ScannerActivity.this, "Screen Unpinned", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFail() {

                        }
                    });

                } else {
                    pin();
                }

                updatePinningStatus();
                break;
            case R.id.smenu_save:
                //passcode only works sdk > 19
                showPasscodeFragment.show(getSupportFragmentManager(), ShowPasscodeFragment.TAG);
                showPasscodeFragment.setOnSuccessFail(new ShowPasscodeFragment.OnSuccessFail() {
                    @Override
                    public void onSuccess() {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            if (pinned) {
                                unpin();
                            }
                        }
                        //notification will be better.
                        saveAndEndClass();
                    }

                    @Override
                    public void onFail() {

                    }
                });

                break;

            case R.id.smenu_addattendees:

                showPasscodeFragment.show(getSupportFragmentManager(), ShowPasscodeFragment.TAG);
                showPasscodeFragment.setOnSuccessFail(new ShowPasscodeFragment.OnSuccessFail() {
                    @Override
                    public void onSuccess() {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            if (pinned) {
                                unpin();
                            }
                        }

                        toAddAttendanceActivity();
                    }

                    @Override
                    public void onFail() {

                    }
                });
                break;
        }

        return true;
    }

    private void initTimer() {
        scanLoading.start();
        scanLoading.setLoadingColor(R.color.colorPrimary);


        model.getTimeLiveData().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                if (s != null) {
                    scanTimerText.setText(s);

                    if (s.equals("done!")) {
                        scanLoading.stop();
                        showLoader(true);

                        doSnack.showSnackbar("Time is up", "Restart", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                showPasscodeFragment.show(getSupportFragmentManager(), ShowPasscodeFragment.TAG);
                                showPasscodeFragment.setOnSuccessFail(new ShowPasscodeFragment.OnSuccessFail() {
                                    @Override
                                    public void onSuccess() {
                                        model.reStartTimer();
                                        scanLoading.start();
                                        showLoader(false);
                                    }

                                    @Override
                                    public void onFail() {
                                        model.endTimer();
                                        showLoader(true);
                                    }
                                });

                            }
                        });
                    }
                }
            }
        });
    }


    private void pin() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //TODO: enable for final testing
            startLockTask();
            pinned = true;
            Toast.makeText(this, "Screen pinned", Toast.LENGTH_SHORT).show();
        }
    }


    private void unpin() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (am.isInLockTaskMode()) {
                stopLockTask();
                pinned = false;
            } else {
                Toast.makeText(this, "Application already unpinned !", LENGTH_LONG).show();
                pinned = true;
            }
        }
    }

    @Override
    public void onBackPressed() {

        if (pinned) {
            doSnack.showSnackbarDissaper("Screen is pinned", "Unlock", new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    showPasscodeFragment.show(getSupportFragmentManager(), ShowPasscodeFragment.TAG);
                    showPasscodeFragment.setOnSuccessFail(new ShowPasscodeFragment.OnSuccessFail() {
                        @Override
                        public void onSuccess() {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                unpin();
                            }

                            updatePinningStatus();
                            ScannerActivity.super.onBackPressed();
                        }

                        @Override
                        public void onFail() {
                        }
                    });

                }
            });
        } else {
            super.onBackPressed();
        }
    }

    private void setInitPasscode() {
        mFirestore.collection(LECAUTHCOL).document(mAuth.getUid()).get(Source.DEFAULT)
                .addOnSuccessListener(this, new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        String passcode = documentSnapshot.getString("localpasscode");
                        model.setPasscodeLiveData(passcode);
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                doSnack.showSnackbar("Set Password to continue", "Set", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(ScannerActivity.this, PasscodeActivity.class);
                        startActivity(intent);
                    }
                });
            }
        });
    }

    private void startLocation() {

        pDialogLoc = new SweetAlertDialog(ScannerActivity.this, SweetAlertDialog.PROGRESS_TYPE);
        pDialogLoc.getProgressHelper().setBarColor(Color.parseColor("#FF5521"));
        pDialogLoc.setTitleText("Accessing Location" + "\n Just a moment...");
        pDialogLoc.setCancelable(true);
        pDialogLoc.show();
        /*pDialogLoc.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                if (mLocation != null) {

                } else {
                    Toast.makeText(ScannerActivity.this, "Location not acquired", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });*/

        //register location change broadcast
        registerReceiver(mGpsSwitchStateReceiver, new IntentFilter(LocationManager.PROVIDERS_CHANGED_ACTION));

        LocationManagerProvider locationManagerProvider = new LocationManagerProvider();

        LocationProvider fallbackProvider = new MultiFallbackProvider.Builder()
                .withProvider(locationManagerProvider).withGooglePlayServicesProvider().build();

        SmartLocation.with(this)
                .location()
                .config(LocationParams.NAVIGATION)
                .oneFix()
                .start(new OnLocationUpdatedListener() {
                    @Override
                    public void onLocationUpdated(Location location) {

                        Log.d(TAG, "onLocationUpdated: " + location);
                        showLocation(location);

                        mLocation = location;
                        //generateQR(location);

                        pDialogLoc.dismiss();

                        if (mLocation != null) {
                            if (pDialogLoc.isShowing()) {
                                pDialogLoc.dismiss();
                            }
                        }
                    }
                });

    }

    private void checkLocationOn() {

        final LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(R.string.location);  // GPS not found
        builder.setMessage(R.string.permission_rationale_location); // Want to enable?
        builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                ScannerActivity.this.startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            }
        });
        builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });
        AlertDialog dd = builder.create();

        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            dd.show();
            setUpLocationUi(false);

        } else {
            dd.dismiss();
            setUpLocationUi(true);

        }

    }

    /**
     * Following broadcast receiver is to listen the Location button toggle state in Android.
     */
    private BroadcastReceiver mGpsSwitchStateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            if (intent.getAction().matches("android.location.PROVIDERS_CHANGED")) {
                // Make an action or refresh an already managed state.
                checkLocationOn();
            }
        }
    };


    private void generateQR() {

        String qrtext = qrParser.classToGson(gson, qrParser);
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        try {
            BitMatrix bitMatrix = multiFormatWriter.encode(qrtext, BarcodeFormat.QR_CODE, dptoInt(200), dptoInt(200));
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
            scanQrImageView.setImageBitmap(bitmap);
            showLoader(false);
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }

    private int dptoInt(int dimen) {
        float density = getResources()
                .getDisplayMetrics()
                .density;
        return Math.round((float) dimen * density);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == LOCATION_PERMISSION_ID && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            //startLocation();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (provider != null) {
            provider.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void showLocation(Location location) {
        if (location != null) {
            final String text = String.format("Latitude %.6f, Longitude %.6f",
                    location.getLatitude(),
                    location.getLongitude());

            // We are going to get the address for the current position
            SmartLocation.with(this).geocoding().reverse(location, new OnReverseGeocodingListener() {
                @Override
                public void onAddressResolved(Location original, List<Address> results) {
                    if (results.size() > 0) {
                        Address result = results.get(0);
                        StringBuilder builder = new StringBuilder(text);
                        builder.append("\n[Reverse Geocoding] ");
                        List<String> addressElements = new ArrayList<>();
                        for (int i = 0; i <= result.getMaxAddressLineIndex(); i++) {
                            addressElements.add(result.getAddressLine(i));
                        }
                        builder.append(TextUtils.join(", ", addressElements));

                        doSnack.showShortSnackbar(TextUtils.join(", ", addressElements));
                    }
                }
            });
        } else {

            Log.d(TAG, "showLocation: Null location");
        }
    }

    private void stopLocation() {
        SmartLocation.with(this).location().stop();
        SmartLocation.with(this).geocoding().stop();

    }

    @Override
    protected void onStart() {
        super.onStart();

        //register location change broadcast
        //registerReceiver(mGpsSwitchStateReceiver, new IntentFilter(LocationManager.PROVIDERS_CHANGED_ACTION));
    }

    @Override
    protected void onResume() {

        //register location change broadcast
        //registerReceiver(mGpsSwitchStateReceiver, new IntentFilter(LocationManager.PROVIDERS_CHANGED_ACTION));
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @OnClick(R.id.scan_loading_view)
    public void onScanViewClicked() {
        if (rotateloading.isStart())
            Toast.makeText(this, "please wait loading...", Toast.LENGTH_SHORT).show();
    }

    private void setUpUi(QRParser qrParser) {

        scanUnitName.setText(qrParser.getUnitname());
        scanVenue.setText(getIntent().getStringExtra(VENUEEXTRA));


        fillUpScanDetails(qrParser);

        fillUpRatingBars();
    }

    private void fillUpRatingBars() {
        //TODO: Show in realtime students who have scanned for the current class

        //get short date today
        Calendar c = Calendar.getInstance();
        DateFormat dateFormat2 = new SimpleDateFormat(DATE_SCAN_FORMAT);
        String today = dateFormat2.format(c.getTime());

        /*
         * rating = no of scans / no of students * 5
         * */
        mFirestore.collection(STUDENTSCANCLASSCOL)
                .whereEqualTo("lecteachtimeid", qrParser.getLecteachtimeid())
                .whereEqualTo("semester", qrParser.getSemester())
                .whereEqualTo("year", qrParser.getYear())
                .whereEqualTo("classtime", qrParser.getClasstime())
                .whereEqualTo("querydate", today)
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                noOfScans = queryDocumentSnapshots.size();
                getNumberOfStudents(noOfScans);

            }
        });
    }


    private void getNumberOfStudents(final int noOfScans) {

        DefaultExecutorSupplier.getInstance().forMainThreadTasks()
                .execute(new Runnable() {
                    @Override
                    public void run() {

                        final ArrayList<String> courses = qrParser.getCourses();


                        for (String c : courses) {

                            mFirestore.collection(STUDENTDETAILSCOL)
                                    .whereEqualTo("course", c)
                                    .whereEqualTo("currentsemester", qrParser.getSemester())
                                    .whereEqualTo("currentyear", qrParser.getYear())
                                    .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                @Override
                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                                    noOfStudents += queryDocumentSnapshots.size();
                                    index++;

                                    if (index >= courses.size()) {

                                        //now we have noOfStudents and noOfScans

                                        double rating = 0;
                                        int perc = 0;

                                        try {
                                             rating = noOfScans / noOfStudents * 5;
                                             perc = noOfScans / noOfStudents * 100;
                                        }catch (ArithmeticException e){ }
                                        finally {
                                            scanNumStudents.setText(String.valueOf(noOfStudents));
                                            itemColRating.setRating(Float.parseFloat(String.valueOf(rating)));

                                            if (perc < 30){
                                                scanSatisfactionProgressBar.setProgress(perc);
                                                scanSatisfactionProgressBar.setSecondaryProgress(0);

                                            }else if(perc > 30){
                                                scanSatisfactionProgressBar.setProgress(perc);
                                                scanSatisfactionProgressBar.setSecondaryProgress(perc - 30);
                                            }


                                            scanPercentageText.setText(String.valueOf(perc)+" %");
                                            Log.d(TAG, "scans done is "+noOfScans);
                                        }
                                    }
                                }
                            });
                        }

                    }
                });

    }

    private void fillUpScanDetails(QRParser qrParser) {

        final StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append(qrParser.getUnitcode());

        mFirestore.collection(LECTEACHCOL).document(getIntent().getStringExtra(LECTEACHIDEXTRA)).collection(LECTEACHCOURSESUBCOL)
                .document("courses")
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {

                            Map<String, Object> mapdata = task.getResult().getData();

                            if (mapdata != null) {

                                for (Map.Entry<String, Object> entry : mapdata.entrySet()) {

                                    stringBuilder.append(", " + entry.getValue().toString());
                                }
                            }

                            scanUnitDetails.setText(stringBuilder.toString());
                        }
                    }
                });

    }

    private void showLoader(Boolean show) {

        if (show) {
            scanLoadingView.setVisibility(View.VISIBLE);
            scanLoadingImage.setVisibility(View.VISIBLE);
            scanQrImageView.setVisibility(View.INVISIBLE);

        } else {

            scanLoadingView.setVisibility(View.GONE);
            scanLoadingImage.setVisibility(View.GONE);
            scanQrImageView.setVisibility(View.VISIBLE);
        }
    }

    private void setUpLocationUi(Boolean on_off) {
        if (on_off) {
            scanLocationBool.setText("Location : ON");

            DrawableHelper
                    .withContext(this)
                    .withColor(R.color.darkbluish)
                    .withDrawable(R.drawable.ic_location_on)
                    .tint()
                    .applyTo(scanLocImg);
        } else {
            scanLocationBool.setText("Location : OFF");
            DrawableHelper
                    .withContext(this)
                    .withColor(R.color.greyish)
                    .withDrawable(R.drawable.ic_location_on)
                    .tint()
                    .applyTo(scanLocImg);
        }
    }

    private void saveAndEndClass() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setTitle("Save class");  // GPS not found
        builder.setMessage("This will record number of students that attended this lesson" +
                "\n Note: Add attendee(s) is recording attendance of students without smartphones");
        builder.setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {

                dialogInterface.dismiss();

                final SweetAlertDialog pDialog;
                pDialog = new SweetAlertDialog(ScannerActivity.this, SweetAlertDialog.PROGRESS_TYPE);
                pDialog.setCancelable(true);
                pDialog.setCanceledOnTouchOutside(false);
                pDialog.setContentText("Saving class attendance");
                pDialog.show();


                if (!AppStatus.getInstance(getApplicationContext()).isOnline()) {

                    //check network availability
                    //perform a job schedule if offline

                    scheduleTransactionWork();


                    doSnack.showShortSnackbar(getString(R.string.youre_offline));

                    pDialog.changeAlertType(SweetAlertDialog.WARNING_TYPE);
                    pDialog.setCancelable(true);
                    pDialog.setTitleText(getString(R.string.youre_offline));
                    pDialog.setContentText("Don't worry, We'll save the \n Class once you're Online");
                    pDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            sDialog.dismissWithAnimation();
                            finish();
                        }
                    });

                } else {

                    Map<String, Object> doneClassMAp = new HashMap<>();
                    doneClassMAp.put("lecteachtimeid", qrParser.getLecteachtimeid());
                    doneClassMAp.put("unitname", qrParser.getUnitname());
                    doneClassMAp.put("unitcode", qrParser.getUnitcode());

                    mFirestore.collection(DONECLASSES).document(qrParser.getLecteachtimeid())
                            .update(doneClassMAp)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {

                                    updateClassTransaction(pDialog);
                                }
                            });
                }
            }
        });
        builder.setNeutralButton(R.string.add_attendee, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                toAddAttendanceActivity();

            }
        });
        builder.setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void scheduleTransactionWork() {

        // Create the Data object:
        Data myData = new Data.Builder()
                .putString(KEY_QR_LECTTID_ARG, qrParser.getLecteachtimeid())
                .putString(KEY_QR_UNITNAME_ARG, qrParser.getUnitname())
                .putString(KEY_QR_UNITCODE_ARG, qrParser.getUnitcode())
                .build();

        //set network required
        Constraints myConstraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build();

        // ...then create and enqueue a OneTimeWorkRequest that uses those arguments
        OneTimeWorkRequest transWork = new OneTimeWorkRequest.Builder(TransactionWorker.class)
                .setConstraints(myConstraints)
                .setInputData(myData)
                .build();

        WorkManager.getInstance()
                .enqueue(transWork);


    }

    private void updateClassTransaction(final SweetAlertDialog pDialog) {

        final DocumentReference cdDocRef = mFirestore.collection(DONECLASSES).document(qrParser.getLecteachtimeid());

        mFirestore.runTransaction(new Transaction.Function<Void>() {
            @Override
            public Void apply(Transaction transaction) throws FirebaseFirestoreException {
                DocumentSnapshot snapshot = transaction.get(cdDocRef);
                double newPopulation = snapshot.getDouble("number") + 1;
                transaction.update(cdDocRef, "number", newPopulation);

                // Success
                return null;
            }
        }).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG, "Transaction success!");

                pDialog.changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                pDialog.setCancelable(true);
                pDialog.setTitleText("Saved Successfully");
                pDialog.setContentText("You're now set");
                pDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.dismissWithAnimation();

                        //showAddNewClassPrompt();
                        sendToMain();

                    }
                });

            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Transaction failure.", e);

                        //do a reschedule of the transaction

                    }
                });
    }

    private void sendToMain() {

        Intent mainIntent = new Intent(this, MainActivity.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(mainIntent);
    }

    private void toAddAttendanceActivity() {
        Intent addAttendIntent = new Intent(ScannerActivity.this, AddAttendanceActivity.class);
        addAttendIntent.putExtra(ADDATTENDANCE_EXTRA, qrParser);
        startActivity(addAttendIntent);
    }
}
