package com.yyh.backendjava.controller;

import com.yyh.backendjava.dao.CourseRepository;
import com.yyh.backendjava.entity.Course;
import com.yyh.backendjava.entity.CourseTest;
import com.yyh.backendjava.entity.DoTest;
import com.yyh.backendjava.mapper.CourseTestMapper;
import com.yyh.backendjava.mapper.DoTestMapper;
import com.yyh.backendjava.service.CourseService;
import com.yyh.backendjava.util.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.neo4j.core.Neo4jClient;
import org.springframework.data.neo4j.core.Neo4jTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.xml.transform.Result;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RestController//以json格式返回
@RequestMapping("/kg")
public class KgController {
    @Resource
    private Neo4jClient neo4jClient;
    @Resource
    private Neo4jTemplate neo4jTemplate;

    @Autowired
    CourseRepository courseRepository;

    @Autowired
    private CourseService courseService;

    @Resource
    private CourseTestMapper courseTestMapper;

    @Resource
    private DoTestMapper doTestMapper;
    //删除某个节点及所有关系
    @CrossOrigin
    @PostMapping("deleteNodeAndRelation")
    public void deleteNodeAndRelation(@RequestParam("courseName") String courseName){
        Optional<Course> course = courseRepository.findCourseByName(courseName);
        courseRepository.deleteById(course.get().getId());
    }
    @CrossOrigin
    @PostMapping("/getAllCourse")
    //查询所有节点
    public Object getAll(@RequestParam("userId") String userId){
        List<Course> courseList = new ArrayList<>();
        courseList = courseRepository.findAll();//所有课程信息
        for(Course course : courseList){
            courseRepository.updateStatusInitial(course.getId());
            //根据课程名字查询课程的其他信息
            //若courseTests为空说明还没添加测试
            List<CourseTest> courseTests = courseTestMapper.getTestListByName(course.getName());
            System.out.println(course.getName()+"测试有："+courseTests);
            //测试题目的数量
            int len = courseTests.size();
            int flagLen = 0;//完成测试的数量
            if(len > 0){
                for(CourseTest test : courseTests){
                    try{
                        //以防没有学生做 空对象可能报错
                        System.out.println("测试："+test);
                        DoTest doTest = doTestMapper.findAnsById(test.getTestId(),userId);
                        System.out.println("学生答案:"+doTest);
                        if(doTest.getIsFinished()==1)
                            flagLen++;
                    }catch (Exception e){
                        System.out.println(e);
                    }


                }
                if(flagLen == len)
                    courseRepository.updateStatusById(course.getId());
            }

        }
//        courseRepository.updateStatusById();
        return ResultUtil.isSuccess(courseList);
    }
    //只添加节点
    @CrossOrigin
    @PostMapping("/addSingleNode")
    public Object addSingleNode(@RequestBody Course course){
        String name = course.getName();
        Optional<Course> courseTest = courseRepository.findCourseByName(name);
        if(!courseTest.isPresent()){
            Course course1 = new Course();
            course1.setName(name);
            return ResultUtil.isSuccess(neo4jTemplate.save(course1));
        }else{
            return ResultUtil.isSuccess(new Course());
        }

//        neo4jTemplate.save(course1);

    }

    //把course放入Id为id的course的branches中，即id的箭头指向course
    @CrossOrigin
    @PostMapping("/addNodeAndBranches")
    public Object addNodeAndBranches(@RequestBody Course course,@RequestParam("branchId") Long branchId ){
        Course course1 = new Course();
        course1.setName(course.getName());
        Course course2 = courseService.findById(branchId);
        course2.getBranches().add(course1);
        //这样存course2会报错
//        Optional<Course> course2 = courseRepository.findById(branchId);
//        course2.get().getBranches().add(course1);
        return neo4jTemplate.save(course2);
    }

    //改变字段 isFinished 再配合前端改变节点颜色
    @CrossOrigin
    @PostMapping("/updateToFinished")
    public Object updateToFinished(@RequestParam("courseId") Long courseId){
        return ResultUtil.isSuccess(courseRepository.updateStatusById(courseId));
    }

    @CrossOrigin
    @PostMapping("/addCourseAndBranchesByName")
    public Object addCourseAndBranchesByName(@RequestParam("courseName1") String name1, @RequestParam("courseName2") String name2){
        courseRepository.createNodesAndRelationship(name1,name2);
        return ResultUtil.isSuccess("添加成功",66);
    }

//    @Transactional
//    @CrossOrigin
//    @PostMapping("/addCourseAndBranchesByName")
//    public Object addCourseAndBranchesByName(@RequestParam("courseName1") String name1, @RequestParam("courseName2") String name2) {
//        Optional<Course> optCourse1 = courseRepository.findCourseByName(name1);
//        Optional<Course> optCourse2 = courseRepository.findCourseByName(name2);
//
//        Course course1 = optCourse1.orElseGet(() -> {
//            Course newCourse = new Course();
//            newCourse.setName(name1);
//            courseRepository.save(newCourse);  // 确保新创建的课程保存到数据库中
//            return newCourse;
//        });
//
//        Course course2 = optCourse2.orElseGet(() -> {
//            Course newCourse = new Course();
//            newCourse.setName(name2);
//            courseRepository.save(newCourse);  // 确保新创建的课程保存到数据库中
//            return newCourse;
//        });
//
//        course1 = courseRepository.findCourseByName(name1).orElse(course1);  // 重新加载course1以确保其为最新状态
//
//        if (!course1.getBranches().contains(course2)) {
//            course1.getBranches().add(course2);
//            neo4jTemplate.save(course1);  // 更新数据库中的course1节点和关系
//        } else {
//            System.out.println("关系已存在，不重复添加");
//        }
//
//        return ResultUtil.isSuccess(course1);
//    }



}

