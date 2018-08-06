package com.job.darasalecturer.util;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.button.MaterialButton;
import android.support.design.chip.ChipGroup;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.job.darasalecturer.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

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

    private Context mContext;
    private FirebaseFirestore mFirestore;

    private static final String TAG = "LessonVH";

    public LessonViewHolder(@NonNull View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);

        LayoutInflater.from(mContext).inflate(R.layout.single_lesson, null);

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
}
