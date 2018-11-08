package com.job.darasalecturer.util;

import com.abdeveloper.library.MultiSelectModel;

/**
 * Created by Job on Thursday : 11/8/2018.
 */
public class LecClassMultiSelectModel extends MultiSelectModel {

    public LecClassMultiSelectModel(Integer id, String name) {
        super(id, name);
    }

    private Integer id;
    private String sid;
    private String name;
    private Boolean isSelected;

    public LecClassMultiSelectModel(Integer id, String name, String sid) {
        super(id, name);
        this.sid = sid;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    Boolean getSelected() {
        return this.isSelected;
    }

    void setSelected(Boolean selected) {
        this.isSelected = selected;
    }
}
