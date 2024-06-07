package com.yyh.backendjava.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author yyh
 * @since 2024-04-22
 */
@TableName("course_information")
public class CourseInformation implements Serializable {

    private static final long serialVersionUID = 1L;

    private String courseName;

    private String courseIntroduction;

    private String coursePptpath;

    private String coursePracticepath;

    private String courseVideopath;

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }
    public String getCourseIntroduction() {
        return courseIntroduction;
    }

    public void setCourseIntroduction(String courseIntroduction) {
        this.courseIntroduction = courseIntroduction;
    }
    public String getCoursePptpath() {
        return coursePptpath;
    }

    public void setCoursePptpath(String coursePptpath) {
        this.coursePptpath = coursePptpath;
    }
    public String getCoursePracticepath() {
        return coursePracticepath;
    }

    public void setCoursePracticepath(String coursePracticepath) {
        this.coursePracticepath = coursePracticepath;
    }
    public String getCourseVideopath() {
        return courseVideopath;
    }

    public void setCourseVideopath(String courseVideopath) {
        this.courseVideopath = courseVideopath;
    }

    @Override
    public String toString() {
        return "CourseInformation{" +
            "courseName=" + courseName +
            ", courseIntroduction=" + courseIntroduction +
            ", coursePptpath=" + coursePptpath +
            ", coursePracticepath=" + coursePracticepath +
            ", courseVideopath=" + courseVideopath +
        "}";
    }
}
