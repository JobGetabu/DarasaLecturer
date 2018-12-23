package com.job.darasalecturer.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.button.MaterialButton;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.abdeveloper.library.MultiSelectDialog;
import com.abdeveloper.library.MultiSelectModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Source;
import com.google.firebase.firestore.WriteBatch;
import com.job.darasalecturer.R;
import com.job.darasalecturer.adapter.CourseYearAdapter;
import com.job.darasalecturer.model.CourseYear;
import com.job.darasalecturer.util.CourseYearViewHolder;
import com.leodroidcoder.genericadapter.OnRecyclerItemClickListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.pedant.SweetAlert.SweetAlertDialog;

import static com.job.darasalecturer.util.Constants.DKUTCOURSES;
import static com.job.darasalecturer.util.Constants.FIRST_NAME_PREF_NAME;
import static com.job.darasalecturer.util.Constants.LAST_NAME_PREF_NAME;
import static com.job.darasalecturer.util.Constants.NOTIFICATIONCOL;

/**
 * Created by Job on Monday : 12/17/2018.
 */
public class SendMessageBottomSheet extends BottomSheetDialogFragment
        implements CourseYearViewHolder.ImageClickListener, OnRecyclerItemClickListener {

    public static final String TAG = "messageSheet";

    @BindView(R.id.fap_ll1)
    LinearLayout fapLl1;
    @BindView(R.id.meso_selectcourses)
    MaterialButton mesoSelectcourses;
    @BindView(R.id.linearLayout)
    LinearLayout linearLayout;
    @BindView(R.id.message_edit)
    EditText messageEdit;

    Unbinder unbinder;
    @BindView(R.id.meso_course_list)
    RecyclerView mesoCourseList;
    @BindView(R.id.meso_ll_rv)
    LinearLayout mesoLlRv;

    private CourseYearAdapter mAdapter;
    private List<CourseYear> courseYearList;
    private SharedPreferences mSharedPreferences;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_send_message, null);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mSharedPreferences = getActivity().getSharedPreferences(getActivity().getApplicationContext().getPackageName(), Context.MODE_PRIVATE);
        initRecycler();

        // instantiate the adapter and set it onto a RecyclerView
        mAdapter = new CourseYearAdapter(getContext(), this, this);
        mesoCourseList.setAdapter(mAdapter);
        mesoLlRv.setVisibility(View.GONE);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.submit_button)
    void onSubmitButtonClick() {

        if (validate()) {

            sendNotification();
        }
    }

    private boolean validate() {
        boolean valid = true;

        String message = messageEdit.getText().toString();


        if (message.isEmpty()) {
            messageEdit.setError("enter some message");
            valid = false;
        } else {
            messageEdit.setError(null);
        }

        if (courseYearList == null) {
            Toast.makeText(getContext(), "Select a course", Toast.LENGTH_SHORT).show();
            valid = false;

        }

        return valid;
    }

    private void initRecycler() {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        mesoCourseList.setLayoutManager(layoutManager);
        mesoCourseList.setHasFixedSize(true);
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        mesoCourseList.addItemDecoration(itemDecoration);
        mesoCourseList.setItemAnimator(new DefaultItemAnimator());
    }

    @OnClick(R.id.meso_selectcourses)
    public void onSelectCourseBtnClicked() {

        final SweetAlertDialog pDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#FF5521"));
        pDialog.setTitleText("Loading courses...");
        pDialog.setCancelable(true);
        pDialog.show();

        FirebaseFirestore.getInstance().collection(DKUTCOURSES).document("dkut")
                .get(Source.DEFAULT)
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {

                            pDialog.dismiss();

                            Map<String, Object> mapdata = task.getResult().getData();

                            if (mapdata != null) {

                                //List of courses with Name and Id
                                ArrayList<MultiSelectModel> listOfCourses = new ArrayList<>();

                                int i = 1;
                                for (Map.Entry<String, Object> entry : mapdata.entrySet()) {
                                    //System.out.println(entry.getKey() + "/" + entry.getValue());

                                    listOfCourses.add(new MultiSelectModel(i, entry.getValue().toString()));
                                    i++;
                                }

                                promptCourseList(listOfCourses);
                            }
                        }
                    }
                });

    }

    private void promptCourseList(ArrayList<MultiSelectModel> listOfCourses) {
        //MultiSelectModel
        MultiSelectDialog multiSelectDialog = new MultiSelectDialog()
                .title(getResources().getString(R.string.select_course)) //setting title for dialog
                .titleSize(20)
                .positiveText("Done")
                .negativeText("Cancel")
                .setMaxSelectionLimit(2)
                .setMinSelectionLimit(1) //you can set minimum checkbox selection limit (Optional)
                //.preSelectIDsList() //List of ids that you need to be selected
                .multiSelectList(listOfCourses) // the multi select model list with ids and name
                .onSubmit(new MultiSelectDialog.SubmitCallbackListener() {
                    @Override
                    public void onSelected(ArrayList<Integer> selectedIds, ArrayList<String> selectedNames, String dataString) {

                        //init the courseYear list
                        courseYearList = (new ArrayList<CourseYear>());

                        for (String c : selectedNames) {
                            CourseYear courseYear = new CourseYear(c, 1);
                            Log.d(TAG, "courseYear: " + courseYear.toString());
                            courseYearList.add(courseYear);
                        }

                        //mesoChipgrp.setVisibility(View.VISIBLE);
                        mesoLlRv.setVisibility(View.VISIBLE);
                        mAdapter.setItems(courseYearList);
                    }

                    @Override
                    public void onCancel() {
                        Log.d(TAG, "Dialog cancelled");
                    }


                });
        multiSelectDialog.show(getActivity().getSupportFragmentManager(), "multiSelectDialog");
    }

    private void sendNotification() {

        final SweetAlertDialog pDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#FF5521"));
        pDialog.setTitleText("Sending notification...");
        pDialog.setCancelable(true);
        pDialog.show();

        // Get a new write batch
        WriteBatch batch = FirebaseFirestore.getInstance().batch();

        for (CourseYear cs : courseYearList){
            //create a TopicName
            String topicname = cs.getCourse().replace(" ","")+ cs.getYearofstudy();
            String title = mSharedPreferences.getString(FIRST_NAME_PREF_NAME,"") +" "
                    + mSharedPreferences.getString(LAST_NAME_PREF_NAME,"");

            Map<String,Object> map = new HashMap<>();
            map.put("topic",topicname);
            map.put("title",title);
            map.put("message",messageEdit.getText().toString());
            map.put("time",FieldValue.serverTimestamp());

            DocumentReference ref = FirebaseFirestore.getInstance().collection(NOTIFICATIONCOL).document();
            batch.set(ref,map);
        }

        batch.commit()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        pDialog.changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                        pDialog.setTitleText("Sent Successfully");
                        pDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.dismissWithAnimation();

                            }
                        });
                    }
                });

        dismiss();
    }

    @Override
    public void onImageClick(final int position, OnRecyclerItemClickListener listener, View itemView) {

        //Creating the instance of PopupMenu
        final PopupMenu popup = new PopupMenu(getActivity(), itemView);
        //Inflating the Popup using xml file
        popup.getMenuInflater()
                .inflate(R.menu.yrmenu, popup.getMenu());

        popup.setGravity(Gravity.BOTTOM);

        //registering popup with OnMenuItemClickListener
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {

                courseYearList.get(position).setYearofstudy(Integer.parseInt(item.getTitle().toString()));
                mAdapter.notifyDataSetChanged();
                return true;
            }
        });

        popup.show(); //showing popup menu
    }

    @Override
    public void onItemClick(int position) {
        /*no enough info passed use {@link onImageClick}*/
    }
}
