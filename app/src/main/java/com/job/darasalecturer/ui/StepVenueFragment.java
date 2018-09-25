package com.job.darasalecturer.ui;


import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.button.MaterialButton;
import android.support.design.chip.Chip;
import android.support.design.chip.ChipGroup;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.abdeveloper.library.MultiSelectDialog;
import com.abdeveloper.library.MultiSelectModel;
import com.job.darasalecturer.R;
import com.job.darasalecturer.viewmodel.AddClassViewModel;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class StepVenueFragment extends Fragment {


    @BindView(R.id.step_venue_name)
    TextInputLayout stepVenueName;
    @BindView(R.id.step_venue_department)
    TextInputLayout stepVenueDepartment;
    @BindView(R.id.step_venue_chipgroup)
    ChipGroup stepVenueChipgroup;
    @BindView(R.id.step_venue_course_btn)
    MaterialButton stepVenueCourseBtn;
    @BindView(R.id.step_venue_back)
    TextView stepVenueBack;
    @BindView(R.id.step_venue_next)
    TextView stepVenueNext;
    Unbinder unbinder;

    private AddClassViewModel model;
    private static final String TAG = "stepvenue";

    public StepVenueFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_step_venue, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        model = ViewModelProviders.of(getActivity()).get(AddClassViewModel.class);

        stepVenueChipgroup.setVisibility(View.GONE);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.step_venue_course_btn)
    public void onStepVenueCourseBtnClicked() {

        //List of Countries with Name and Id
        ArrayList<MultiSelectModel> listOfCourses= new ArrayList<>();
        listOfCourses.add(new MultiSelectModel(1,"Bs of Commerce"));
        listOfCourses.add(new MultiSelectModel(2,"Bs of Purchasing Supplies and Management"));
        listOfCourses.add(new MultiSelectModel(3,"Bs of Business Administration and Management"));
        listOfCourses.add(new MultiSelectModel(4,"Bs of Business Administration and Management"));
        listOfCourses.add(new MultiSelectModel(5,"Bs. Food Science"));
        listOfCourses.add(new MultiSelectModel(6,"Bs of Science in Computer Science"));
        listOfCourses.add(new MultiSelectModel(7,"Bs of Business Administration and Management"));
        listOfCourses.add(new MultiSelectModel(8,"Bs of Science in Criminology and Security Management"));
        listOfCourses.add(new MultiSelectModel(9,"Bs of Science Electrical and Electronics Engineering"));
        listOfCourses.add(new MultiSelectModel(10,"Bs of Science Mechatronic Engineering"));

        //MultiSelectModel
        MultiSelectDialog multiSelectDialog = new MultiSelectDialog()
                .title(getResources().getString(R.string.select_course)) //setting title for dialog
                .titleSize(20)
                .positiveText("Done")
                .negativeText("Cancel")
                .setMinSelectionLimit(1) //you can set minimum checkbox selection limit (Optional)
                //.preSelectIDsList() //List of ids that you need to be selected
                .multiSelectList(listOfCourses) // the multi select model list with ids and name
                .onSubmit(new MultiSelectDialog.SubmitCallbackListener() {
                    @Override
                    public void onSelected(ArrayList<Integer> selectedIds, ArrayList<String> selectedNames, String dataString) {

                        stepVenueChipgroup.removeAllViews();
                        //will return list of selected IDS
                        for (int i = 0; i < selectedIds.size(); i++) {
                            //Toast.makeText(getContext(), "Selected Ids : " + selectedIds.get(i) + "\n" + "Selected Names : " + selectedNames.get(i) + "\n" + "DataString : " + dataString, Toast.LENGTH_SHORT).show();

                            addChipCourse(selectedNames.get(i));
                        }

                        stepVenueChipgroup.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onCancel() {
                        Log.d(TAG,"Dialog cancelled");
                    }


                });

        multiSelectDialog.show(getActivity().getSupportFragmentManager(), "multiSelectDialog");

    }

    @OnClick(R.id.step_venue_back)
    public void onStepVenueBackClicked() {
        model.setCurrentStep(1);
    }

    @OnClick(R.id.step_venue_next)
    public void onStepVenueNextClicked() {
        if (validate()) {

            model.setCurrentStep(3);
        }
    }

    public boolean validate() {
        boolean valid = true;

        String venue = stepVenueName.getEditText().getText().toString();
        String dept = stepVenueDepartment.getEditText().getText().toString();

        if (venue.isEmpty()) {
            stepVenueName.setError("enter venue");
            valid = false;
        } else {
            stepVenueName.setError(null);
        }

        if (dept.isEmpty()) {
            stepVenueDepartment.setError("enter department");
            valid = false;
        } else {
            stepVenueDepartment.setError(null);
        }


        return valid;
    }

    private void addChipCourse(String course) {
        Chip chip = new Chip(getContext());
        chip.setChipText(course);
        //chip.setCloseIconEnabled(true);
        //chip.setCloseIconResource(R.drawable.ic_clear);

        chip.setChipBackgroundColorResource(R.color.lightGrey);
        chip.setTextAppearanceResource(R.style.ChipTextStyle2);
        chip.setChipStartPadding(4f);
        chip.setChipEndPadding(4f);

        stepVenueChipgroup.addView(chip);

    }
}
