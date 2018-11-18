package com.job.darasalecturer.ui.newlesson;


import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.job.darasalecturer.R;
import com.job.darasalecturer.adapter.CourseYearAdapter;
import com.job.darasalecturer.model.CourseYear;
import com.job.darasalecturer.viewmodel.AddClassViewModel;
import com.leodroidcoder.genericadapter.OnRecyclerItemClickListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class StepYStudyFragment extends Fragment implements OnRecyclerItemClickListener {

    public static final String TAG = "StepY";

    @BindView(R.id.step_ys_back)
    TextView stepYsBack;
    @BindView(R.id.step_ys_next)
    TextView stepYsNext;
    @BindView(R.id.step_ys_selectedcourses)
    TextView stepYsSelectedcourses;
    @BindView(R.id.step_ys_course_list)
    RecyclerView stepYsCourseList;

    Unbinder unbinder;

    private AddClassViewModel model;

    private Boolean _areLecturesLoaded = false;
    private CourseYearAdapter mAdapter;

    public StepYStudyFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_step_ystudy, container, false);
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

        model = ViewModelProviders.of(getActivity()).get(AddClassViewModel.class);

        initRecycler();

        // instantiate the adapter and set it onto a RecyclerView
        mAdapter = new CourseYearAdapter(getActivity(), this);
        stepYsCourseList.setAdapter(mAdapter);

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        //Code executes EVERY TIME user views the fragment


        if (isVisibleToUser && !_areLecturesLoaded) {
            _areLecturesLoaded = true;
            // Code executes ONLY THE FIRST TIME fragment is viewed.

            // populate adapter with data when it is ready
            List<CourseYear> courseYears = model.getCourseYearList().getValue();
            mAdapter.setItems(courseYears);
        }
    }

    @OnClick(R.id.step_ys_back)
    public void onStepYsBackClicked() {
        model.setCurrentStep(2);
    }

    @OnClick(R.id.step_ys_next)
    public void onStepYsNextClicked() {
        model.setCurrentStep(4);
    }

    private void initRecycler() {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        stepYsCourseList.setLayoutManager(layoutManager);
        stepYsCourseList.setHasFixedSize(true);
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        stepYsCourseList.addItemDecoration(itemDecoration);
        stepYsCourseList.setItemAnimator(new DefaultItemAnimator());
    }


    /**
     * This is a callback of the recycler listener.
     * {@link OnRecyclerItemClickListener}.
     * Is being triggered when an item has been clicked.
     *
     * @param position clicked position
     */
    @Override
    public void onItemClick(int position) {

    }
}
