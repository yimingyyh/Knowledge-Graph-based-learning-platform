package com.yyh.backendjava.serviceImpl;

import com.yyh.backendjava.dao.CourseRepository;
import com.yyh.backendjava.entity.Course;
import com.yyh.backendjava.service.CourseService;
import com.yyh.backendjava.util.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.core.Neo4jTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Currency;
import java.util.List;
import java.util.Optional;

@Service
public class CourseServiceImpl implements CourseService {
    @Autowired
    private CourseRepository courseRepository;

    @Resource
    private Neo4jTemplate neo4jTemplate;
    @Override
    public Course findById(Long id) {
        Optional<Course> course = courseRepository.findById(id);
        if(course.isPresent())
            return  course.get();
        return new Course();
    }

    @Override
    public Object addCourseRelation(String name1, String name2) {
        return null;
    }


}
