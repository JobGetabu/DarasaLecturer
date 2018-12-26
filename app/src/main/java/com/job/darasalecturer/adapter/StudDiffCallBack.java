package com.job.darasalecturer.adapter;

import com.job.darasalecturer.model.StudentMessage;
import com.leodroidcoder.genericadapter.BaseDiffCallback;

import java.util.List;

/**
 * Created by Job on Wednesday : 12/26/2018.
 */
public class StudDiffCallBack extends BaseDiffCallback<StudentMessage> {

    public StudDiffCallBack(List<StudentMessage> oldItems, List<StudentMessage> newItems) {
        super(oldItems, newItems);
    }

    /**
     * Here you should figure out whether items are same.
     * For instance, In typical situation it should return <code>true</code> in case item ids (unique identifiers) are equal,
     * even though some parameters might have changed.
     * On the contrary it should return <code>false</code> for items with different ids.
     *
     * @param oldItemPosition position of old item
     * @param newItemPosition position of new item
     * @return `true` if items are the same, even though some parameters may vary.
     */
    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return getOldItems().get(oldItemPosition).getStudentid() == getNewItems().get(newItemPosition).getStudentid();
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        StudentMessage oldUser = getOldItems().get(oldItemPosition);
        StudentMessage newUser = getNewItems().get(newItemPosition);
        return oldUser.getStudentid().equals(newUser.getStudentid())
                && oldUser.getRegNo().equals(newUser.getRegNo());
    }
}
