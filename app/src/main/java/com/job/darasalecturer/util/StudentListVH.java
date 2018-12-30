package com.job.darasalecturer.util;

import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.hbb20.GThumb;
import com.job.darasalecturer.R;
import com.job.darasalecturer.model.StudentDetails;
import com.job.darasalecturer.model.StudentScanClass;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Job on Tuesday : 8/14/2018.
 */
public class StudentListVH extends RecyclerView.ViewHolder {

    private static final String TAG = "StudentVH";
    @BindView(R.id.attn_gthumb)
    GThumb attnGthumb;
    @BindView(R.id.attn_reg_no)
    TextView attnRegNo;
    @BindView(R.id.attn_stud_name)
    TextView attnStudName;
    @BindView(R.id.attn_main)
    ConstraintLayout attnMain;

    private StudentDetails model;

    public StudentListVH(@NonNull View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);

        //LayoutInflater.from(mContext).inflate(R.layout.single_attendance, null);

    }

    public void setUpUi(StudentDetails model) {
        attnStudName.setText(model.getFirstname() + " " + model.getLastname());
        attnRegNo.setText(model.getRegnumber());
        attnGthumb.loadThumbForName(model.getPhotourl(), model.getFirstname(), model.getLastname());

    }

    public void setUpUi(StudentScanClass model) {
        attnStudName.setText(model.getStudname());
        attnRegNo.setText(model.getRegno());
        attnGthumb.loadThumbForName("",model.getStudname());

    }
}
