package com.job.darasalecturer.adapter;

import android.content.Context;
import android.support.v7.util.DiffUtil;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.job.darasalecturer.R;
import com.job.darasalecturer.model.StudentMessage;
import com.job.darasalecturer.util.ScanStudentVH;
import com.leodroidcoder.genericadapter.GenericRecyclerViewAdapter;
import com.leodroidcoder.genericadapter.OnRecyclerItemClickListener;

import java.util.List;

/**
 * Created by Job on Sunday : 11/25/2018.
 */
public class ScanStudentAdapter extends GenericRecyclerViewAdapter<StudentMessage, OnRecyclerItemClickListener, ScanStudentVH> {

    public ScanStudentAdapter(Context context, OnRecyclerItemClickListener listener) {
        super(context, listener);
    }

    @Override
    public ScanStudentVH onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.ad_list_single, parent, false);

        return new ScanStudentVH(view);
    }

    @Override
    public void updateItems(List<StudentMessage> newItems) {
        // initialise stud diff callback
        DiffUtil.Callback diffCallback = new StudDiffCallBack(getItems(), newItems);
        // pass to parent adapter to let it do all the remaining job
        super.updateItems(newItems,diffCallback);
    }
}
