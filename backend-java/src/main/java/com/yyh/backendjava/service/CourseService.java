package com.yyh.backendjava.service;

import com.yyh.backendjava.entity.Course;

import java.util.List;

public interface CourseService {
    //根据id查找course
    Course findById(Long id);
    //修改状态颜色（是否完成）

    //传入两个课程名 将name1指向name2
    Object addCourseRelation(String name1,String name2);

    //
}
