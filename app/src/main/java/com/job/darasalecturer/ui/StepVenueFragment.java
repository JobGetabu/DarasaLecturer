package com.job.darasalecturer.ui;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.job.darasalecturer.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class StepVenueFragment extends Fragment {


    public StepVenueFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_step_venue, container, false);
    }

}