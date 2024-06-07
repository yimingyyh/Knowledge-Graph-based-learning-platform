package com.yyh.backendjava.mapper;

import com.yyh.backendjava.entity.Course;
import com.yyh.backendjava.entity.CourseInformation;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author yyh
 * @since 2024-04-22
 */
public interface CourseInformationMapper extends BaseMapper<CourseInformation> {
    @Select("select * from course_information where course_name = #{courseName}")
    CourseInformation selectByName(String courseName);

//    @Update("update course_information set " +
//            "course_introduction = #{courseIntroduction}," +
//            "course_pptpath = #{coursePptpath}," +
//            "course_practicepath = #{coursePracticepath}," +
//            "course_videopath = #{courseVideopath}," +
//            "where course_name = #{courseName}")
    void updateCourseInformation(@RequestParam("courseName") String courseName,
                                  @RequestParam("courseIntroduction") String courseIntroduction,
                                  @RequestParam("coursePptpath") String coursePptpath,
                                  @RequestParam("coursePracticepath") String coursePracticepath,
                                  @RequestParam("courseVideopath") String courseVideopath);
}
