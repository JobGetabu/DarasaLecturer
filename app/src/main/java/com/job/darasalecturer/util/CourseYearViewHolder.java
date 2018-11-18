package com.job.darasalecturer.util;

import android.support.design.card.MaterialCardView;
import android.support.design.widget.TextInputLayout;
import android.view.View;
import android.widget.TextView;

import com.job.darasalecturer.R;
import com.job.darasalecturer.model.CourseYear;
import com.leodroidcoder.genericadapter.BaseViewHolder;
import com.leodroidcoder.genericadapter.OnRecyclerItemClickListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Job on Sunday : 11/18/2018.
 */
public class CourseYearViewHolder extends BaseViewHolder<CourseYear, OnRecyclerItemClickListener> {

    @BindView(R.id.sinc_yc_course)
    TextInputLayout textInputCourse;
    @BindView(R.id.sinc_yc_txt)
    TextView yrText;
    @BindView(R.id.card_sinc_yc_id)
    MaterialCardView cardView;

    OnRecyclerItemClickListener listener;

    public CourseYearViewHolder(View itemView, final OnRecyclerItemClickListener listener) {
        super(itemView, listener);
        ButterKnife.bind(this,itemView);
        this.listener = listener;
    }

    @OnClick({R.id.card_sinc_yc_id,R.id.sinc_yc_txt,R.id.sinc_yc_course})
    public void onCardViewClicked(){
        listener.onItemClick(getAdapterPosition());
    }

    @Override
    public void onBind(CourseYear item) {

        textInputCourse.getEditText().setText(item.getCourse());
        yrText.setText("Year of Study: "+ item.getYearofstudyInt());

    }
}
