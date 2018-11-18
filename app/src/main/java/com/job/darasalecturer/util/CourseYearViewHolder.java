package com.job.darasalecturer.util;

import android.support.design.widget.TextInputLayout;
import android.view.View;
import android.widget.TextView;

import com.job.darasalecturer.R;
import com.job.darasalecturer.model.CourseYear;
import com.leodroidcoder.genericadapter.BaseViewHolder;
import com.leodroidcoder.genericadapter.OnRecyclerItemClickListener;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Job on Sunday : 11/18/2018.
 */
public class CourseYearViewHolder extends BaseViewHolder<CourseYear, OnRecyclerItemClickListener> {

    @BindView(R.id.sinc_yc_course)
    TextInputLayout textInputCourse;
    @BindView(R.id.sinc_yc_txt)
    TextView yrText;

    public CourseYearViewHolder(View itemView, OnRecyclerItemClickListener listener) {
        super(itemView, listener);
        ButterKnife.bind(this,itemView);
    }

    @Override
    public void onBind(CourseYear item) {

        textInputCourse.getEditText().setText(item.getCourse());
        yrText.setText("Year of Study: "+ item.getYearofstudyInt());

    }
}
