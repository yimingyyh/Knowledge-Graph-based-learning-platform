package com.yyh.backendjava;

import com.yyh.backendjava.dao.CourseRepository;
import com.yyh.backendjava.entity.Course;
import com.yyh.backendjava.entity.CourseInformation;
import com.yyh.backendjava.entity.CourseTest;
import com.yyh.backendjava.mapper.CourseInformationMapper;
import com.yyh.backendjava.mapper.CourseTestMapper;
import com.yyh.backendjava.mapper.DoTestMapper;
import com.yyh.backendjava.service.CourseService;
import com.yyh.backendjava.service.ICourseInformationService;
import net.sf.jsqlparser.expression.StringValue;
import org.apache.commons.codec.digest.DigestUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.neo4j.core.Neo4jClient;
import org.springframework.data.neo4j.core.Neo4jTemplate;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;

@SpringBootTest
class BackendJavaApplicationTests {

    @Autowired
    CourseRepository courseRepository;
    @Resource
    private Neo4jClient neo4jClient;
    @Resource
    private Neo4jTemplate neo4jTemplate;
    @Autowired
    private CourseService courseService;
    @Resource
    private CourseInformationMapper courseInformationMapper;

    @Resource
    private DoTestMapper doTestMapper;
    @Resource
    private ICourseInformationService courseInformationService;
    @Test
    void contextLoads() {
    }

    @Test
    void addSalt(){
        String pwd1 = "123456";
        String salt = DigestUtils.md5Hex(pwd1);
        String pwd2 = "abc";
        String new_pwd1 = DigestUtils.md5Hex(pwd1+salt);
        String new_pwd2 = DigestUtils.md5Hex(pwd2+salt);
        System.out.println("salt:"+salt);
        System.out.println("new pwd1:"+new_pwd1);
        System.out.println("new pwd2:"+new_pwd2);

    }

    @Test
    void testNeo4j(){
        Course course = courseRepository.findCourseWithBranchesByName("离散数学");
        System.out.println(course.toString());
    }
    @Test
    void testCourse(){
//        CourseInformation courseInformation = new CourseInformation();
//        courseInformation.setCourseName("13");
//        courseInformation.setCoursePracticepath("1");
//        courseInformation.setCourseVideopath("1");
//        courseInformation.setCourseIntroduction("1");
//        courseInformation.setCoursePptpath("1");
        courseInformationMapper.updateCourseInformation("图论",null,"123","123",null);
//        courseInformationMapper.insert(courseInformation);
//        List<CourseInformation> list = courseInformationMapper.selectList(null);-
//        CourseInformation courseInformation = courseInformationMapper.selectByName("离散数学");
//        System.out.println(courseInformation.toString());
    }
    @Resource
    private CourseTestMapper courseTestMapper;
    @Test
    void testCourseTest(){
//        CourseTest  courseTest = new CourseTest();
//        courseTest.setTestName("1");
//        courseTest.setTestType(1);
//        courseTest.setCourseName("离散数学");
//        courseTestMapper.insert(courseTest);
//        System.out.println(courseTestMapper.getTestListByName("离散数学"));
//        System.out.println(doTestMapper.findAnsById(57, String.valueOf(123456)));
        System.out.println(courseTestMapper.findCountTestName("q是p1,p2,p3...pn的逻辑结论，则下列说法错误的是：","111念"));
    }
}
