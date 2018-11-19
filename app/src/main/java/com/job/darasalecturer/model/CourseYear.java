package com.job.darasalecturer.model;

/**
 * Created by Job on Friday : 11/16/2018.
 */
public class CourseYear  {
    private String course;
    private int yearofstudy;

    public CourseYear() {
    }

    public CourseYear(String course, int yearofstudy) {
        this.course = course;
        this.yearofstudy = yearofstudy;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public int getYearofstudy() {
        return yearofstudy;
    }

    public void setYearofstudy(int yearofstudy) {
        this.yearofstudy = yearofstudy;
    }

    @Override
    public String toString() {
        return "CourseYear{" +
                "course='" + course + '\'' +
                ", yearofstudy=" + yearofstudy +
                '}';
    }

}


/* List<Map<Integer, CourseYrData>> courseYrData = data.getCourseYrData();
                        for (Map<Integer,CourseYrData> map : courseYrData){

                            for (Map.Entry<Integer, CourseYrData> entery : map.entrySet()) {

                                CourseYrData courseYrData1 = (CourseYrData) entery.getValue();
                                Log.d(TAG, "onComplete: " + entery.getValue().toString());
                                addCourses(courseYrData1.getCourseYear());
                            }
                        }*/
