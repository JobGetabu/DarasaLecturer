package com.job.darasalecturer.ui;


import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.WriteBatch;
import com.job.darasalecturer.R;
import com.job.darasalecturer.viewmodel.AddClassViewModel;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.job.darasalecturer.util.Constants.LECTEACHCOL;
import static com.job.darasalecturer.util.Constants.LECTEACHTIMECOL;

/**
 * A simple {@link Fragment} subclass.
 */
public class StepXinfoFragment extends Fragment {


    @BindView(R.id.step_x_semester)
    TextInputLayout stepXSemester;
    @BindView(R.id.step_x_year)
    TextInputLayout stepXYear;
    @BindView(R.id.step_x_school)
    TextInputLayout stepXSchool;
    @BindView(R.id.step_x_acadyear)
    TextInputLayout stepXAcadyear;
    @BindView(R.id.step_x_back)
    TextView stepXBack;
    @BindView(R.id.step_x_finish)
    TextView stepXFinish;
    Unbinder unbinder;

    private AddClassViewModel model;

    private FirebaseFirestore mFirestore;
    private FirebaseAuth mAuth;

    public StepXinfoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_step_xinfo, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //firebase
        mFirestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        model = ViewModelProviders.of(getActivity()).get(AddClassViewModel.class);
    }

    @OnClick(R.id.step_x_back)
    public void onStepXBackClicked() {

        model.setCurrentStep(2);

    }

    @OnClick(R.id.step_x_finish)
    public void onStepXFinishClicked() {
        //finish
        if (validate()) {

            String sem = stepXSemester.getEditText().getText().toString();
            String yr = stepXYear.getEditText().getText().toString();
            String academ = stepXAcadyear.getEditText().getText().toString();
            String school = stepXSchool.getEditText().getText().toString();

            model.getLecTeachMediatorLiveData().getValue().setSemester(sem);
            model.getLecTeachMediatorLiveData().getValue().setStudyyear(yr);
            model.getLecTeachMediatorLiveData().getValue().setAcademicyear(academ);
            model.getLecTeachMediatorLiveData().getValue().setSchool(school);

            String lecid = mAuth.getCurrentUser().getUid();
            String lecteachid = mFirestore.collection(LECTEACHCOL).document().getId();
            String lecteachtimeid = mFirestore.collection(LECTEACHTIMECOL).document().getId();

            model.getLecTeachTimeMediatorLiveData().getValue().setLecid(lecid);
            model.getLecTeachTimeMediatorLiveData().getValue().setLecteachid(lecteachid);
            model.getLecTeachTimeMediatorLiveData().getValue().setLecteachtimeid(lecteachtimeid);

            if (model.getCourseList().getValue() != null && model.getCourseList().getValue().size() > 1){
                model.getLecTeachMediatorLiveData().getValue().setCombiner(true);

                //push courses list

            }else {
                model.getLecTeachMediatorLiveData().getValue().setCombiner(false);
            }

            // Get a new write batch
            WriteBatch batch = mFirestore.batch();

            /* // Set the value of lecteach
            DocumentReference lecteachRef =  mFirestore.collection(LECTEACHCOL).document(lecteachid);
            batch.set(lecteachRef, model.getLecTeachMediatorLiveData().getValue());
            DocumentReference lecteachtimeRef =  mFirestore.collection(LECTEACHTIMECOL).document(lecteachtimeid);
            batch.set(lecteachtimeRef, model.getLecTeachTimeMediatorLiveData().getValue());
            */

            Toast.makeText(getContext(), model.getLecTeachMediatorLiveData().getValue().toString(), Toast.LENGTH_SHORT).show();
        }
    }

    private boolean validate() {
        boolean valid = true;

        String sem = stepXSemester.getEditText().getText().toString();
        String yr = stepXYear.getEditText().getText().toString();
        String academ = stepXAcadyear.getEditText().getText().toString();
        String school = stepXSchool.getEditText().getText().toString();

        if (sem.isEmpty()) {
            stepXSemester.setError("enter semester");
            valid = false;
        } else {
            stepXSemester.setError(null);
        }

        if (yr.isEmpty()) {
            stepXYear.setError("enter year");
            valid = false;
        } else {
            stepXYear.setError(null);
        }

        if (academ.isEmpty()) {
            stepXAcadyear.setError("enter academic year");
            valid = false;
        } else {
            stepXAcadyear.setError(null);
        }

        if (school.isEmpty()) {
            stepXSchool.setError("enter school");
            valid = false;
        } else {
            stepXSchool.setError(null);
        }

        return valid;
    }
}
