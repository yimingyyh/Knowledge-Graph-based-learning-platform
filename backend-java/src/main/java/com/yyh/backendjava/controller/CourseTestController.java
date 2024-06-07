package com.yyh.backendjava.controller;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yyh.backendjava.entity.*;
import com.yyh.backendjava.mapper.CourseInformationMapper;
import com.yyh.backendjava.mapper.CourseTestMapper;
import com.yyh.backendjava.mapper.DoTestMapper;
import com.yyh.backendjava.mapper.UserMapper;
import com.yyh.backendjava.util.ResultUtil;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author yyh
 * @since 2024-04-25
 */
@Controller
@RestController
@RequestMapping("/courseTest")
public class CourseTestController {
    @Resource
    private DoTestMapper doTestMapper;

    @Resource
    private UserMapper userMapper;
    @Resource
    private CourseTestMapper courseTestMapper;

    @Resource
    private CourseInformationMapper courseInformationMapper;

    @CrossOrigin
    @PostMapping("/findByCourseName")
    public Object findByCourseName(@RequestParam("courseName") String courseName){
        CourseInformation courseInformation = courseInformationMapper.selectByName(courseName);
        PageHelper.startPage(1, 10);
        List<List<String>> list = new ArrayList<>();
        List<String> temp =new ArrayList<>();
        temp.add(courseInformation.getCourseName());
        temp.add(courseInformation.getCourseIntroduction());
        temp.add(courseInformation.getCoursePptpath());
        temp.add(courseInformation.getCoursePracticepath());
        temp.add(courseInformation.getCourseVideopath());
        list.add(temp);
        PageInfo page = new PageInfo(list);
        return page;
    }
    @CrossOrigin
    @GetMapping("/getAllTests")
    public List<CourseTest> getAllTests(){
        return courseTestMapper.selectList(null);
    }
    //根据课程名删除所有测试
    @CrossOrigin
    @PostMapping("/deleteTestsByName")
    public void deleteTestsByName(@RequestParam("courseName") String courseName){
        courseTestMapper.deleteTestsByName(courseName);
    }

    @CrossOrigin
    @PostMapping("/getStuRes")
    public Object getStuRes(@RequestParam ("courseName")String courseName){

        List<CourseTest> courseTests = courseTestMapper.getTestListByName(courseName);
        // {  {userId,totalScore}  }
        List<List<String>> resList = new ArrayList<>();
        List<User> stuList = userMapper.getStudents();
        System.out.println("stuList:+++");
        System.out.println(stuList);
        System.out.println("courseTests:+++");
        System.out.println(courseTests);
        if(courseTests.size()>0 && stuList.size()>0){
            for(User stu : stuList){
                try{
                    System.out.println("stu::######");
                    System.out.println(stu.toString());
                    List<String> temp = new ArrayList<>();
                    String userId = stu.getUserId();
                    String userName = stu.getUserName();
                    float total = 0.0F;
                    for(CourseTest courseTest : courseTests){
                        System.out.println("courseTest#######");
                        System.out.println(courseTest);
                        int testId = courseTest.getTestId();
                        DoTest doTest = doTestMapper.findAnsById(testId,userId);
                        System.out.println("dotest########");
                        System.out.println(doTest);
                        if(doTest!=null)
                            if(doTest.getScore()!=null)
                                total += doTest.getScore();
                    }
                    temp.add(userId);
                    temp.add(userName);
                    temp.add(String.valueOf(total));
                    System.out.println("temp########"+temp);
                    resList.add(temp);
                    System.out.println("reslist########");
                    System.out.println(resList);
                }catch (Exception e){
                    System.out.println(e);
                }

            }
            return ResultUtil.isSuccess(resList);
        }
        else{
            return ResultUtil.isSuccess("暂无数据",555);
        }

    }
    //根据测试题目id假删除题目
    @CrossOrigin
    @PostMapping("/deleteById")
    public void deleteById(@RequestParam ("testId") int testId){
        courseTestMapper.deleteById(testId);
    }

    //根据courseName展示测试题目
    @CrossOrigin
    @GetMapping("/showTestByName/{courseName}/{current}/{size}")
    public Object findTestByName(@PathVariable ("courseName") String courseName,@PathVariable("current") Integer current, @PathVariable("size") Integer size){
        PageHelper.startPage(current, size);
        List<CourseTest> list = courseTestMapper.getTestListByName(courseName);
        List<List<String>> resList = new ArrayList<>();
        for(CourseTest courseTest : list){
            List<String> tmp = new ArrayList<>();
            tmp.add(String.valueOf(courseTest.getTestId()));
            tmp.add(String.valueOf(courseTest.getTestType()));
            tmp.add(courseTest.getTestName());
            if(courseTest.getTestType() == 1 || courseTest.getTestType() == 2){
                tmp.add(courseTest.getA());
                tmp.add(courseTest.getB());
                tmp.add(courseTest.getC());
                tmp.add(courseTest.getD());
                tmp.add(courseTest.getSelectAns());
            }
            resList.add(tmp);
        }
        PageInfo page = new PageInfo(resList);
        return ResultUtil.isSuccess(page);
    }
    //添加试题
    @CrossOrigin
    @PostMapping("/addTest")
    public Object addTest(@RequestBody CourseTest courseTest){
        System.out.println("#############");
        System.out.println(courseTest.toString());
        int type = courseTest.getTestType();
        int flag = courseTestMapper.findCountTestName(courseTest.getTestName(),courseTest.getCourseName());
        if(flag == 0){
            if(type==1 || type==2){
                //单选 或者 多选
                CourseTest test = new CourseTest();
                test.setCourseName(courseTest.getCourseName());
                test.setTestType(type);
                test.setTestName(courseTest.getTestName());
                test.setA(courseTest.getA());
                test.setB(courseTest.getB());
                test.setC(courseTest.getC());
                test.setD(courseTest.getD());
                test.setSelectAns(courseTest.getSelectAns());
                return ResultUtil.isSuccess(courseTestMapper.insert(test));
            }else{
                //问答
                CourseTest test1 = new CourseTest();
                test1.setCourseName(courseTest.getCourseName());
                test1.setTestType(courseTest.getTestType());
                test1.setTestName(courseTest.getTestName());
                return ResultUtil.isSuccess(courseTestMapper.insert(test1));
            }
        }else
            return  ResultUtil.isSuccess("555");
    }
    //获取所有课程列表
    @CrossOrigin
    @GetMapping("/getCourseList/{current}/{size}")
    public PageInfo<CourseInformation> findAllCourse(@PathVariable("current") Integer current, @PathVariable("size") Integer size){
        //获取第current页，size条内容，默认查询总数count
        PageHelper.startPage(current, size);
        List<List<String>> list = new ArrayList<>();
        List<CourseInformation> courseList = courseInformationMapper.selectList(null);
//        int len = courseList.toArray().length;
        for(CourseInformation courseInformation : courseList){
            List<String> temp =new ArrayList<>();
            temp.add(courseInformation.getCourseName());
            temp.add(courseInformation.getCourseIntroduction());
            temp.add(courseInformation.getCoursePptpath());
            temp.add(courseInformation.getCoursePracticepath());
            temp.add(courseInformation.getCourseVideopath());
            list.add(temp);
        }
//        List<String> tem1 =new ArrayList<>();
//        tem1.add(String.valueOf(len));
//        list.add(tem1);
//用PageInfo对结果进行包装
        PageInfo page = new PageInfo(list);

        return page;
    }

}
