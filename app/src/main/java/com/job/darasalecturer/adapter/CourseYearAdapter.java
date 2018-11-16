package com.job.darasalecturer.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;

import com.job.darasalecturer.R;
import com.job.darasalecturer.model.CourseYear;
import com.job.darasalecturer.viewmodel.AddClassViewModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Job on Friday : 11/16/2018.
 */
public class CourseYearAdapter extends RecyclerView.Adapter<CourseYearAdapter.CourseYearVH> {

    private List<CourseYear> courseYearList;
    private AddClassViewModel addClassViewModel;
    private Context context;

    public CourseYearAdapter(List<CourseYear> courseYearList, AddClassViewModel addClassViewModel, SpinnerItemListener spinnerItemListener) {
        this.courseYearList = courseYearList;
        this.addClassViewModel = addClassViewModel;
        this.spinnerItemListener = spinnerItemListener;
    }

    @NonNull
    @Override
    public CourseYearVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_yearcourse, parent, false);
        return new CourseYearVH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CourseYearVH holder, int position) {

        holder.setUpUI(courseYearList.get(position));
    }

    @Override
    public int getItemCount() {
        return courseYearList.size();
    }

    public class CourseYearVH extends RecyclerView.ViewHolder {

        @BindView(R.id.textInputLayout)
        TextInputLayout textInputLayout;
        @BindView(R.id.spinner)
        Spinner spinner;

        public CourseYearVH(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);

            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    String selectedItem = parent.getItemAtPosition(position).toString();
                    spinnerItemListener.onSelect(selectedItem);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }

        private void setUpUI(CourseYear courseYear){
            textInputLayout.getEditText().setText(courseYear.getCourse());

            Double xx = courseYear.getYearofstudy();
            Integer xxx = xx.intValue();
            //set position offset -1
            spinner.setSelection(xxx-1,true);
        }
    }

    private SpinnerItemListener spinnerItemListener;

    public interface SpinnerItemListener {
        void onSelect(String selected);
    }
}
