package com.job.darasalecturer.util;

import android.content.Context;
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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.job.darasalecturer.R;
import com.job.darasalecturer.appexecutor.DefaultExecutorSupplier;
import com.job.darasalecturer.datasource.LecTeachTime;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

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

    private static final String TAG = "LessonVH";

    public LessonViewHolder(@NonNull View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);

        //LayoutInflater.from(mContext).inflate(R.layout.single_lesson, null);

    }

    public void init(Context mContext, FirebaseFirestore mFirestore) {
        this.mContext = mContext;
        this.mFirestore = mFirestore;
    }

    @OnClick(R.id.ls_qr_img)
    public void onLsQrImgClicked() {
    }

    @OnClick(R.id.ls_btn)
    public void onLsBtnClicked() {
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
                        locationViewer(lecTeachTime);
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

    private void getCourses(LecTeachTime lecTeachTime){

        mFirestore.collection(LECTEACHCOL).document(lecTeachTime.getLecteachid()).collection(LECTEACHCOURSESUBCOL)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> dbtask) {
                        if (dbtask.isSuccessful()){

                           //dbtask.getResult().toObjects()
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
}
