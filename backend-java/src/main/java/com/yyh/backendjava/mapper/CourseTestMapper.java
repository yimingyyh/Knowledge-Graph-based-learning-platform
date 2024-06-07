package com.yyh.backendjava.mapper;

import com.yyh.backendjava.entity.CourseTest;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author yyh
 * @since 2024-04-25
 */
public interface CourseTestMapper extends BaseMapper<CourseTest> {
    //根据课程名返回所有的测试题目
    @Select("select * from course_test where course_name = #{courseName} and deleted = 0" )
    List<CourseTest>  getTestListByName(String courseName);

    //根据测试题目id假删除题目

    @Update("update course_test set deleted = 1 where test_id = #{testId}")
    void deleteById(int testId);

    //根据课程名删除所有测试
    void deleteTestsByName(String courseName);

    //根据testId查询答案
    @Select("select * from course_test where test_id = #{testId}")
    CourseTest findAnsById(int testId);

    @Select("select count(*) from course_test where test_name = #{test_name} and course_name = #{course_name}")
    int findCountTestName(String test_name,String course_name);
}
