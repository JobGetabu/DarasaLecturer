package com.job.darasalecturer.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.job.darasalecturer.R;
import com.job.darasalecturer.adapter.AddClassPagerAdapter;
import com.job.darasalecturer.adapter.NoSwipePager;
import com.shuhart.stepview.StepView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddClassActivity extends AppCompatActivity {

    @BindView(R.id.add_class_toolbar)
    Toolbar addClassToolbar;
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

        setSupportActionBar(addClassToolbar);

        pagerAdapter = new AddClassPagerAdapter(getSupportFragmentManager());
        pagerAdapter.addFragments(new StepInitFragment());
        pagerAdapter.addFragments(new StepUnitFragment());
        pagerAdapter.addFragments(new StepVenueFragment());
        pagerAdapter.addFragments(new StepXinfoFragment());

        addClassNoswipepager.setAdapter(pagerAdapter);
        addClassNoswipepager.setPagingEnabled(true);
        addClassNoswipepager.setOffscreenPageLimit(3);
    }

    @OnClick(R.id.add_class_step_view)
    public void onViewStepViewClicked() {
    }
}
