package com.job.darasalecturer.util;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.hbb20.GThumb;
import com.job.darasalecturer.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.refactor.library.SmoothCheckBox;

/**
 * Created by Job on Tuesday : 8/14/2018.
 */
public class StudentViewHolder extends RecyclerView.ViewHolder {

    private static final String TAG = "StudentVH";
    @BindView(R.id.attn_gthumb)
    GThumb attnGthumb;
    @BindView(R.id.attn_reg_no)
    TextView attnRegNo;
    @BindView(R.id.attn_stud_name)
    TextView attnStudName;
    @BindView(R.id.attn_check_box)
    SmoothCheckBox attnCheckBox;
    @BindView(R.id.attn_main)
    ConstraintLayout attnMain;

    private Context mContext;
    private FirebaseFirestore mFirestore;

    public StudentViewHolder(@NonNull View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);

        //LayoutInflater.from(mContext).inflate(R.layout.single_attendance, null);
    }

    @OnClick({R.id.attn_gthumb, R.id.attn_reg_no, R.id.attn_stud_name, R.id.attn_check_box, R.id.attn_main})
    public void onViewClicked(View view) {

    }
}
