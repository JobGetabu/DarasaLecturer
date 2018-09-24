package com.job.darasalecturer.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.job.darasalecturer.R;
import com.job.darasalecturer.adapter.AddClassPagerAdapter;
import com.job.darasalecturer.adapter.NoSwipePager;
import com.shuhart.stepview.StepView;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddClassActivity extends AppCompatActivity {

    @BindView(R.id.add_class_step_view)
    StepView addClassStepView;
    @BindView(R.id.add_class_noswipepager)
    NoSwipePager addClassNoswipepager;

    private AddClassPagerAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_class);
        ButterKnife.bind(this);

        pagerAdapter = new AddClassPagerAdapter(getSupportFragmentManager());
        pagerAdapter.addFragments(new StepInitFragment());
        pagerAdapter.addFragments(new StepUnitFragment());
        pagerAdapter.addFragments(new StepVenueFragment());
        pagerAdapter.addFragments(new StepXinfoFragment());

        addClassNoswipepager.setAdapter(pagerAdapter);
        addClassNoswipepager.setPagingEnabled(false);
        addClassNoswipepager.setOffscreenPageLimit(3);


        addClassStepView.getState()
                .steps(Arrays.asList(getResources().getStringArray(R.array.addclass_steps)))
                .commit();
    }

    @OnClick(R.id.add_class_step_view)
    public void onViewStepViewClicked() {
    }
}
