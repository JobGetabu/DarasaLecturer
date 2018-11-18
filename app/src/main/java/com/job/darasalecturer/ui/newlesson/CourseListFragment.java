package com.job.darasalecturer.ui.newlesson;


import android.app.Dialog;
import android.app.Fragment;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.button.MaterialButton;
import android.support.v7.app.AppCompatDialogFragment;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.job.darasalecturer.R;
import com.job.darasalecturer.adapter.CourseYearAdapter;
import com.job.darasalecturer.viewmodel.AddClassViewModel;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class CourseListFragment extends AppCompatDialogFragment {


    public static final String TAG = "courselist";

    @BindView(R.id.frg_course_list)
    RecyclerView frgCourseList;
    @BindView(R.id.frg_course_done)
    MaterialButton frgDone;
    Unbinder unbinder;

    private static AddClassViewModel model;
    private CourseYearAdapter adapter;

    public CourseListFragment() {
        // Required empty public constructor
    }

    public static CourseListFragment newInstance(AddClassViewModel model) {
        CourseListFragment.model = model;
        return new CourseListFragment();
    }



    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = new Dialog(this.getActivity());
        dialog.getWindow().requestFeature(1);
        dialog.getWindow().setFlags(32, 1024);
        dialog.setContentView(R.layout.fragment_course_list);
        dialog.getWindow().setLayout(-1, ConstraintLayout.LayoutParams.WRAP_CONTENT);
        // Inflate the layout for this fragment
        //View view = inflater.inflate(R.layout.fragment_class_list, container, false);
        unbinder = ButterKnife.bind(this, dialog);

        if (model == null){
            dismiss();
            Toast.makeText(getContext(), "dismissed", Toast.LENGTH_SHORT).show();
        }

        return dialog;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.frg_course_done)
    public void onFrgDoneClicked() {
        dismiss();
    }

}
