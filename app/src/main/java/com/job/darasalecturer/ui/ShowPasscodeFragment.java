package com.job.darasalecturer.ui;


import android.app.Fragment;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hanks.passcodeview.PasscodeView;
import com.job.darasalecturer.R;
import com.job.darasalecturer.viewmodel.ScannerViewModel;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class ShowPasscodeFragment extends DialogFragment {

    public static final String TAG = "ShowPasscodeFragment";

    private OnSuccessFail onSuccessFail;

    @BindView(R.id.passcodeView)
    PasscodeView passcodeView;

    private ScannerViewModel model;

    public void setOnSuccessFail(OnSuccessFail onSuccessFail) {
        this.onSuccessFail = onSuccessFail;
    }

    public ShowPasscodeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Translucent_NoTitleBar);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.activity_passcode, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //init model
        model = ViewModelProviders.of(getActivity()).get(ScannerViewModel.class);

        passcodeView
                .setPasscodeLength(4)
                .setLocalPasscode(model.getPasscodeLiveData().getValue())
                .setFirstInputTip(getResources().getString(R.string.enter_your_pin))
                .setPasscodeType(PasscodeView.PasscodeViewType.TYPE_CHECK_PASSCODE)
                .setListener(new PasscodeView.PasscodeViewListener() {
                    @Override
                    public void onFail() {


                        onSuccessFail.onFail();
                        dismiss();
                    }

                    @Override
                    public void onSuccess(String number) {

                        onSuccessFail.onSuccess();
                        dismiss();
                    }
                });

    }

    //Declare an Interface
    public interface OnSuccessFail {
        void onSuccess();

        void onFail();
    }
}
