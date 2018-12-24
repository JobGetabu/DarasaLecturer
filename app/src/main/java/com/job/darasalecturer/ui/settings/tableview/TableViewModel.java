
package com.job.darasalecturer.ui.settings.tableview;

import android.view.Gravity;

import com.job.darasalecturer.model.ClassListModel;
import com.job.darasalecturer.ui.settings.tableview.model.CellModel;
import com.job.darasalecturer.ui.settings.tableview.model.ColumnHeaderModel;
import com.job.darasalecturer.ui.settings.tableview.model.RowHeaderModel;

import java.util.ArrayList;
import java.util.List;

public class TableViewModel {
    // View Types
    public static final int GENDER_TYPE = 1;
    public static final int MONEY_TYPE = 2;

    private List<ColumnHeaderModel> mColumnHeaderModelList;
    private List<RowHeaderModel> mRowHeaderModelList;
    private List<List<CellModel>> mCellModelList;

    public int getCellItemViewType(int column) {

        switch (column) {
            case 5:
                // 5. column header is gender.
                return GENDER_TYPE;
            case 8:
                // 8. column header is Salary.
                return MONEY_TYPE;
            default:
                return 0;
        }
    }

     /*
       - Each of Column Header -

            "Name"
            "Registration No."


     */

    public int getColumnTextAlign(int column) {
        switch (column) {
            // Name
            case 0:
                return Gravity.LEFT;
            // Registration No.
            case 1:
                return Gravity.LEFT;
            // Email
            case 2:
                return Gravity.LEFT;

            default:
                return Gravity.CENTER;
        }

    }

    private List<ColumnHeaderModel> createColumnHeaderModelList() {
        List<ColumnHeaderModel> list = new ArrayList<>();

        // Create Column Headers
        list.add(new ColumnHeaderModel("Name"));
        list.add(new ColumnHeaderModel("Registration Number."));

        return list;
    }

    private List<List<CellModel>> createCellModelList(List<ClassListModel> studList) {
        List<List<CellModel>> lists = new ArrayList<>();

        // Creating cell model list from User list for Cell Items
        // In this example, User list is populated from web service

        for (int i = 0; i < studList.size(); i++) {
            ClassListModel user = studList.get(i);

            List<CellModel> list = new ArrayList<>();

            // The order should be same with column header list;
            list.add(new CellModel("1-" + i, user.getName()));          // "Name"
            list.add(new CellModel("2-" + i, user.getRegno()));        // "Regno"

            // Add
            lists.add(list);
        }

        return lists;
    }

    private List<RowHeaderModel> createRowHeaderList(int size) {
        List<RowHeaderModel> list = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            // In this example, Row headers just shows the index of the TableView List.
            list.add(new RowHeaderModel(String.valueOf(i + 1)));
        }
        return list;
    }


    public List<ColumnHeaderModel> getColumHeaderModeList() {
        return mColumnHeaderModelList;
    }

    public List<RowHeaderModel> getRowHeaderModelList() {
        return mRowHeaderModelList;
    }

    public List<List<CellModel>> getCellModelList() {
        return mCellModelList;
    }


    public void generateListForTableView(List<ClassListModel> users) {
        mColumnHeaderModelList = createColumnHeaderModelList();
        mCellModelList = createCellModelList(users);
        mRowHeaderModelList = createRowHeaderList(users.size());
    }

}