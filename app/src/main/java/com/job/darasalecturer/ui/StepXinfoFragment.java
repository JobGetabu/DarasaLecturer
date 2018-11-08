package com.job.darasalecturer.ui;


import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.WriteBatch;
import com.job.darasalecturer.R;
import com.job.darasalecturer.model.DoneClasses;
import com.job.darasalecturer.viewmodel.AddClassViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.pedant.SweetAlert.SweetAlertDialog;

import static com.job.darasalecturer.util.Constants.DONECLASSES;
import static com.job.darasalecturer.util.Constants.LECTEACHCOL;
import static com.job.darasalecturer.util.Constants.LECTEACHCOURSESUBCOL;
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

        //TODO: Make a smart autofill for the fields previous values
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

            model.getLecTeachTimeMediatorLiveData().getValue().setSemester(sem);
            model.getLecTeachTimeMediatorLiveData().getValue().setStudyyear(yr);

            String lecid = mAuth.getCurrentUser().getUid();
            String lecteachid = mFirestore.collection(LECTEACHCOL).document().getId();
            String lecteachtimeid = mFirestore.collection(LECTEACHTIMECOL).document().getId();

            model.getLecTeachMediatorLiveData().getValue().setLecid(lecid);
            model.getLecTeachMediatorLiveData().getValue().setLecteachid(lecteachid);

            model.getLecTeachTimeMediatorLiveData().getValue().setLecid(lecid);
            model.getLecTeachTimeMediatorLiveData().getValue().setLecteachid(lecteachid);
            model.getLecTeachTimeMediatorLiveData().getValue().setLecteachtimeid(lecteachtimeid);

            ArrayList<String> courses = new ArrayList<>(model.getCourseList().getValue());
            model.getLecTeachTimeMediatorLiveData().getValue().setCourses(courses);

            if (model.getCourseList().getValue() != null && model.getCourseList().getValue().size() > 1){
                model.getLecTeachMediatorLiveData().getValue().setCombiner(true);

                //push courses list

            }else {
                model.getLecTeachMediatorLiveData().getValue().setCombiner(false);
            }

            //set up DoneClasses db
            DoneClasses doneClasses = new DoneClasses();

            // Get a new write batch
            WriteBatch batch = mFirestore.batch();

            // Set the value of lecteach
            DocumentReference lecteachRef =  mFirestore.collection(LECTEACHCOL).document(lecteachid);
            batch.set(lecteachRef, model.getLecTeachMediatorLiveData().getValue());
            DocumentReference lecteachtimeRef =  mFirestore.collection(LECTEACHTIMECOL).document(lecteachtimeid);
            batch.set(lecteachtimeRef, model.getLecTeachTimeMediatorLiveData().getValue());
            DocumentReference doneClassRef =  mFirestore.collection(DONECLASSES).document(lecteachtimeid);
            batch.set(doneClassRef, doneClasses);


            DocumentReference courseRef =  mFirestore.collection(LECTEACHCOL).document(lecteachid)
                    .collection(LECTEACHCOURSESUBCOL).document("courses");

            Map<String,String> cMap = new HashMap<>();
            int i = 1;
            for (String course : model.getCourseList().getValue()){
                cMap.put(String.valueOf(i),course);
                i++;
            }

            batch.set(courseRef, cMap);

            //load progress
            final SweetAlertDialog pDialog;
            pDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.PROGRESS_TYPE);
            pDialog.setCancelable(false);
            pDialog.setContentText("Saving class");
            pDialog.show();

            batch.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){

                        pDialog.changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                        pDialog.setCancelable(true);
                        pDialog.setTitleText("Saved Successfully");
                        pDialog.setContentText("You're now set");
                        pDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.dismissWithAnimation();


                                sendToMain();

                            }
                        });

                    }else {
                        pDialog.changeAlertType(SweetAlertDialog.ERROR_TYPE);
                        pDialog.setCancelable(false);
                        pDialog.setTitleText("Check Connection");
                        pDialog.setContentText("Your class was saved and will show up once you're online");
                        pDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.dismissWithAnimation();
                                startActivity(new Intent(getContext(),MainActivity.class));
                            }
                        });
                    }
                }
            });
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

    private void sendToMain() {

        Intent mainIntent = new Intent(getContext(), MainActivity.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(mainIntent);
    }
}
