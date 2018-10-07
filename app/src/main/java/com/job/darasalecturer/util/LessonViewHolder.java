package com.job.darasalecturer.util;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.button.MaterialButton;
import android.support.design.chip.Chip;
import android.support.design.chip.ChipGroup;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.job.darasalecturer.R;
import com.job.darasalecturer.appexecutor.DefaultExecutorSupplier;
import com.job.darasalecturer.model.LecTeachTime;
import com.job.darasalecturer.model.QRParser;
import com.job.darasalecturer.ui.ScannerActivity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.job.darasalecturer.ui.ScannerActivity.LECTEACHIDEXTRA;
import static com.job.darasalecturer.ui.ScannerActivity.QRPARSEREXTRA;
import static com.job.darasalecturer.ui.ScannerActivity.VENUEEXTRA;
import static com.job.darasalecturer.util.Constants.CURRENT_SEM_PREF_NAME;
import static com.job.darasalecturer.util.Constants.CURRENT_YEAR_PREF_NAME;
import static com.job.darasalecturer.util.Constants.LECTEACHCOL;
import static com.job.darasalecturer.util.Constants.LECTEACHCOURSESUBCOL;

/**
 * Created by Job on Monday : 8/6/2018.
 */
public class LessonViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.ls_qr_img)
    ImageView lsQrImg;
    @BindView(R.id.ls_chipgroup)
    ChipGroup lsChipgroup;
    @BindView(R.id.ls_unitcode)
    TextView lsUnitcode;
    @BindView(R.id.ls_unitname)
    TextView lsUnitname;
    @BindView(R.id.ls_time)
    TextView lsTime;
    @BindView(R.id.ls_btn)
    MaterialButton lsBtn;
    @BindView(R.id.ls_loc_img)
    ImageView lsLocImg;
    @BindView(R.id.ls_card)
    ConstraintLayout lsCard;
    @BindView(R.id.ls_venue)
    TextView lsVenue;

    private Context mContext;
    private FirebaseFirestore mFirestore;
    private FirebaseAuth mAuth;
    private LecTeachTime lecTeachTime;

    private static final String TAG = "LessonVH";

    public LessonViewHolder(@NonNull View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);

        //LayoutInflater.from(mContext).inflate(R.layout.single_lesson, null);

    }

    public void init(Context mContext, FirebaseFirestore mFirestore,FirebaseAuth mAuth,LecTeachTime lecTeachTime) {
        this.mContext = mContext;
        this.mFirestore = mFirestore;
        this.mAuth = mAuth;
        this.lecTeachTime = lecTeachTime;
    }

    @OnClick(R.id.ls_qr_img)
    public void onLsQrImgClicked() {
    }

    @OnClick(R.id.ls_btn)
    public void onLsBtnClicked() {

        QRParser qrParser = new QRParser();
        qrParser.setDate(Calendar.getInstance().getTime());
        qrParser.setClasstime(lecTeachTime.getTime());
        qrParser.setCourses(lecTeachTime.getCourses());
        qrParser.setLecteachtimeid(lecTeachTime.getLecteachtimeid());
        qrParser.setUnitcode(lecTeachTime.getUnitcode());
        qrParser.setUnitname(lecTeachTime.getUnitname());
        getSemYearPref(qrParser);

        sendToQr(qrParser);
    }

    private void getSemYearPref(QRParser qrParser) {
        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(mContext);
        // Check prefs and pass to qrparser
        qrParser.setSemester(sharedPreferences.getString(CURRENT_SEM_PREF_NAME,"0"));
        qrParser.setYear(sharedPreferences.getString(CURRENT_YEAR_PREF_NAME,"2000"));

    }

    @OnClick(R.id.ls_loc_img)
    public void onLsLocImgClicked() {
    }

    @OnClick(R.id.ls_card)
    public void onLsCardClicked() {
    }

    public void setUpUi(final LecTeachTime lecTeachTime) {

        //smoother experience...
        DefaultExecutorSupplier.getInstance().forMainThreadTasks()
                .execute(new Runnable() {
                    @Override
                    public void run() {

                        lsUnitcode.setText(lecTeachTime.getUnitcode());
                        lsUnitname.setText(lecTeachTime.getUnitname());
                        lsVenue.setText(lecTeachTime.getVenue());
                        lessonTime(lecTeachTime.getTime());
                        locationViewer(lecTeachTime);
                        getCourses(lecTeachTime);
                    }
                });
    }

    private void locationViewer(LecTeachTime lecTeachTime) {
        if (lecTeachTime.getVenue().isEmpty()) {

            DrawableHelper
                    .withContext(mContext)
                    .withColor(R.color.greyish)
                    .withDrawable(R.drawable.ic_location_on)
                    .tint()
                    .applyTo(lsLocImg);
        } else {
            DrawableHelper
                    .withContext(mContext)
                    .withColor(R.color.darkbluish)
                    .withDrawable(R.drawable.ic_location_on)
                    .tint()
                    .applyTo(lsLocImg);
        }
    }

    private void getCourses(LecTeachTime lecTeachTime) {

        mFirestore.collection(LECTEACHCOL).document(lecTeachTime.getLecteachid()).collection(LECTEACHCOURSESUBCOL)
                .document("courses")
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {

                            Map<String, Object> mapdata = task.getResult().getData();

                            if (mapdata != null) {
                                lsChipgroup.removeAllViews();
                                for (Map.Entry<String, Object> entry : mapdata.entrySet()) {
                                    //System.out.println(entry.getKey() + "/" + entry.getValue());
                                    addCourses(entry.getValue().toString());
                                }
                            }
                        }
                    }
                });
    }

    private void addCourses(String course) {
        Chip chip = new Chip(mContext);
        chip.setChipText(course);
        //chip.setCloseIconEnabled(true);
        //chip.setCloseIconResource(R.drawable.your_icon);
        //chip.setChipIconResource(R.drawable.your_icon);
        //chip.setChipBackgroundColorResource(R.color.red);
        chip.setTextAppearanceResource(R.style.ChipTextStyle);
        chip.setChipStartPadding(4f);
        chip.setChipEndPadding(4f);

        lsChipgroup.addView(chip);
    }

    private void lessonTime(Date timestamp) {
        //Timestamp timestamp = model.getTimestamp();
        if (timestamp != null) {

            Date date = timestamp;
            Calendar c = Calendar.getInstance();
            c.setTime(date);

            DateFormat dateFormat2 = new SimpleDateFormat("hh.mm aa");
            lsTime.setText(dateFormat2.format(date));
        }
    }

    private void sendToQr(QRParser qrParser) {
        Intent qrintent = new Intent(mContext,ScannerActivity.class);
        qrintent.putExtra(QRPARSEREXTRA,qrParser);
        qrintent.putExtra(VENUEEXTRA, lecTeachTime.getVenue());
        qrintent.putExtra(LECTEACHIDEXTRA, lecTeachTime.getLecteachid());
        mContext.startActivity(qrintent);
    }
}
