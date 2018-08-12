package com.job.darasalecturer.ui;

import android.Manifest;
import android.app.ActivityManager;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.job.darasalecturer.R;
import com.job.darasalecturer.datasource.QRParser;
import com.job.darasalecturer.util.DoSnack;
import com.job.darasalecturer.viewmodel.ScannerViewModel;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import com.victor.loading.newton.NewtonCradleLoading;
import com.victor.loading.rotate.RotateLoading;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.nlopez.smartlocation.OnLocationUpdatedListener;
import io.nlopez.smartlocation.OnReverseGeocodingListener;
import io.nlopez.smartlocation.SmartLocation;
import io.nlopez.smartlocation.location.providers.LocationGooglePlayServicesProvider;
import me.zhanghai.android.materialratingbar.MaterialRatingBar;

import static android.widget.Toast.LENGTH_LONG;
import static com.job.darasalecturer.util.Constants.LECAUTHCOL;
import static com.job.darasalecturer.util.Constants.LECTEACHCOL;
import static com.job.darasalecturer.util.Constants.LECTEACHCOURSESUBCOL;

public class ScannerActivity extends AppCompatActivity implements OnLocationUpdatedListener {

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
    private Location location;
    private int locationcount = 1;
    private ShowPasscodeFragment showPasscodeFragment;

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
        //initPinning();

        doSnack = new DoSnack(this, ScannerActivity.this);

        initTimer();

        // Keep the screen always on
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);


        // Check if the location services are enabled
        checkLocationOn();
        SmartLocation.with(this).location().state().locationServicesEnabled();
        // Location permission not granted
        if (ContextCompat.checkSelfPermission(ScannerActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(ScannerActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_ID);
            return;
        }
        startLocation();

        showLoader(true);
        rotateloading.start();

        //init our fragment
        showPasscodeFragment = new ShowPasscodeFragment();

        //set up password
        showPasscode();
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
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                    unpin();
                                }
                                Toast.makeText(ScannerActivity.this, "Screen Unpinned", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFail() {

                        }
                    });

                } else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        pin();
                        Toast.makeText(this, "Screen pinned", Toast.LENGTH_SHORT).show();
                    }
                }

                updatePinningStatus();
                break;
            case R.id.smenu_record:
                showPasscodeFragment.show(getSupportFragmentManager(), ShowPasscodeFragment.TAG);
                showPasscodeFragment.setOnSuccessFail(new ShowPasscodeFragment.OnSuccessFail() {
                    @Override
                    public void onSuccess() {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            if (pinned) {
                                unpin();
                            }
                        }
                        //TODO: end class officially
                        //notification will be better.
                        Toast.makeText(ScannerActivity.this, "Class attendance recorded", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onFail() {

                    }
                });

                break;

            case R.id.smenu_confirm:

                showPasscodeFragment.show(getSupportFragmentManager(), ShowPasscodeFragment.TAG);
                showPasscodeFragment.setOnSuccessFail(new ShowPasscodeFragment.OnSuccessFail() {
                    @Override
                    public void onSuccess() {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            if (pinned) {
                                unpin();
                            }
                        }
                        //TODO: end class officially
                        //notification will be better.
                        Toast.makeText(ScannerActivity.this, "5 Student attendance recorded", Toast.LENGTH_LONG).show();
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

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void pin() {
        startLockTask();
        pinned = true;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void unpin() {
        if (am.isInLockTaskMode()) {
            stopLockTask();
            pinned = false;
        } else {
            Toast.makeText(this, "Application already unpinned !", LENGTH_LONG).show();
            pinned = true;
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

    private void showPasscode() {
        mFirestore.collection(LECAUTHCOL).document(mAuth.getUid()).get()
                .addOnSuccessListener(this, new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                        String passcode = documentSnapshot.getString("localpasscode");


                        //showPasscodeFragment.show(getSupportFragmentManager(), ShowPasscodeFragment.TAG);
                        model.setPasscodeLiveData(passcode);

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                doSnack.showSnackbarDissaper("Set Password to continue", "Set", new View.OnClickListener() {
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

        provider = new LocationGooglePlayServicesProvider();
        provider.setCheckLocationSettings(true);

        SmartLocation smartLocation = new SmartLocation.Builder(this).logging(true).build();

        smartLocation.location(provider).start(this);
    }

    private void checkLocationOn() {

        final LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setCancelable(false);
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
            builder.create().show();
            return;
        }

    }

    @Override
    public void onLocationUpdated(Location location) {
        showLocation(location);

        //true first time
        if (locationcount == 1) {
            generateQR(location);
            locationcount++;
        }

        //true second or more when location changes
        if (locationcount > 1 && this.location != location) {

            generateQR(location);
            locationcount++;
        }

        this.location = location;
    }

    private void generateQR(Location location) {

        qrParser.setLatitude(String.valueOf(location.getLatitude()));
        qrParser.setLongitude(String.valueOf(location.getLongitude()));

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
            startLocation();
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
    protected void onResume() {

        startLocation();
        super.onResume();
    }

    @Override
    protected void onPause() {
        stopLocation();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        stopLocation();
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
}
