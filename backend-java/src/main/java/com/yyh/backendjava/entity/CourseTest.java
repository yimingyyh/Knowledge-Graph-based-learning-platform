package com.yyh.backendjava.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author yyh
 * @since 2024-04-25
 */
@TableName("course_test")
public class CourseTest implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "test_id", type = IdType.AUTO)
    private Integer testId;

    private String courseName;

    /**
     * 1为单选题，2为多选题，3为问答题
     */
    private Integer testType;

    private String testName;

    /**
     * 0为未删除，1为删除
     */
    private Integer deleted;

    private String a;

    private String b;

    private String c;

    private String d;

    private String block;

    private String selectAns;

    public Integer getTestId() {
        return testId;
    }

    public void setTestId(Integer testId) {
        this.testId = testId;
    }
    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }
    public Integer getTestType() {
        return testType;
    }

    public void setTestType(Integer testType) {
        this.testType = testType;
    }
    public String getTestName() {
        return testName;
    }

    public void setTestName(String testName) {
        this.testName = testName;
    }
    public Integer getDeleted() {
        return deleted;
    }

    public void setDeleted(Integer deleted) {
        this.deleted = deleted;
    }
    public String getA() {
        return a;
    }

    public void setA(String a) {
        this.a = a;
    }
    public String getB() {
        return b;
    }

    public void setB(String b) {
        this.b = b;
    }
    public String getC() {
        return c;
    }

    public void setC(String c) {
        this.c = c;
    }
    public String getD() {
        return d;
    }

    public void setD(String d) {
        this.d = d;
    }
    public String getBlock() {
        return block;
    }

    public void setBlock(String block) {
        this.block = block;
    }
    public String getSelectAns() {
        return selectAns;
    }

    public void setSelectAns(String selectAns) {
        this.selectAns = selectAns;
    }

    @Override
    public String toString() {
        return "CourseTest{" +
            "testId=" + testId +
            ", courseName=" + courseName +
            ", testType=" + testType +
            ", testName=" + testName +
            ", deleted=" + deleted +
            ", a=" + a +
            ", b=" + b +
            ", c=" + c +
            ", d=" + d +
            ", block=" + block +
            ", selectAns=" + selectAns +
        "}";
    }
}
