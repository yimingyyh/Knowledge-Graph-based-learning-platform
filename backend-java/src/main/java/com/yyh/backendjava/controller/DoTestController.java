package com.yyh.backendjava.controller;

import com.yyh.backendjava.entity.CourseInformation;
import com.yyh.backendjava.entity.CourseTest;
import com.yyh.backendjava.entity.DoTest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yyh.backendjava.mapper.CourseTestMapper;
import com.yyh.backendjava.mapper.DoTestMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import com.yyh.backendjava.util.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import java.util.List;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author yyh
 * @since 2024-04-30
 */
@Controller
@RestController
@RequestMapping("/doTest")
public class DoTestController {

    @Resource
    private DoTestMapper doTestMapper;

    @Resource
    private CourseTestMapper courseTestMapper;
    @Resource
    private final ObjectMapper objectMapper;

    public DoTestController(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @CrossOrigin
    @PostMapping("/getScoreByName")
    public Object getScoreByName(@RequestParam("courseName") String courseName,@RequestParam("userId") String userId){
        List<CourseTest> list = courseTestMapper.getTestListByName(courseName);
        float score = 0.0f;
        if(list.size()>0){
            for(CourseTest test : list){
                DoTest doTest = doTestMapper.findAnsById(test.getTestId(), userId);
                if(doTest!=null)
                    score+= doTest.getScore();
            }
        }
        return ResultUtil.isSuccess(score);
    }

    @CrossOrigin
    @GetMapping("/getTests")
    public List<DoTest> getTests(){
        return doTestMapper.selectList(null);
    }
    @CrossOrigin
    @PostMapping("/submitTest")
    public ResponseEntity<?> submitTest (@RequestBody String rawData){
//        [{"testId":"44","selectAns":"A","userId":"123456","blockAns":"","score":null,"is_finished":1},{"testId":"47","selectAns":"A","userId":"123456","blockAns":"","score":null,"is_finished":1},
        System.out.println("Received data: " + rawData);
        try {
            List<DoTest> tests = objectMapper.readValue(rawData, new TypeReference<List<DoTest>>() {});
            System.out.println("Converted tests: " + tests);
            //处理数据
            for(DoTest dotest : tests){
                if(doTestMapper.findAnsById(dotest.getTestId(), dotest.getUserId())!=null){
                    //如果已经存在了 就覆盖更新
                    DoTest doTest11 = doTestMapper.findAnsById(dotest.getTestId(), dotest.getUserId());
                    CourseTest courseTest = courseTestMapper.findAnsById(dotest.getTestId());
                    if(courseTest.getTestType() == 1||courseTest.getTestType() == 2){
                        //单选题或者多选题
                        if(dotest.getSelectAns().length()!=0){
                            //单选或多选且作答了
                            doTest11.setIsFinished(1);
                            doTest11.setSelectAns(dotest.getSelectAns());
                            doTest11.setBlockAns("");
                            if(dotest.getSelectAns().equals(courseTest.getSelectAns()))
                                doTest11.setScore(20.0F);//假设加二十分
                            else
                                doTest11.setScore(0F);
                        }
                    }else{
                        //问答题
                        if(dotest.getBlockAns().length()>0){
                            doTest11.setIsFinished(1);
                            doTest11.setBlockAns(dotest.getBlockAns());
                            doTest11.setSelectAns("");
                            doTest11.setScore(10.0F);
                        }
                    }
                    doTestMapper.updateById(doTest11);
                }else{
                    //不存在则添加
                    DoTest doTest1 = new DoTest();
                    doTest1.setTestId(dotest.getTestId());
                    doTest1.setUserId(dotest.getUserId());
                    CourseTest courseTest = courseTestMapper.findAnsById(dotest.getTestId());
                    if(courseTest.getTestType() == 1||courseTest.getTestType() == 2){
                        //单选题或者多选题
                        if(dotest.getSelectAns().length()!=0){
                            //单选或多选且作答了
                            doTest1.setIsFinished(1);
                            doTest1.setSelectAns(dotest.getSelectAns());
                            doTest1.setBlockAns("");
                            if(dotest.getSelectAns().equals(courseTest.getSelectAns()))
                                doTest1.setScore(20.0F);//假设加十分
                            else
                                doTest1.setScore(0F);
                        }
                    }else{
                        //问答题
                        if(dotest.getBlockAns().length()>0){
                            doTest1.setIsFinished(1);
                            doTest1.setBlockAns(dotest.getBlockAns());
                            doTest1.setSelectAns("");
                            doTest1.setScore(10.0F);
                        }
                    }
                    doTestMapper.insert(doTest1);
                }

            }
            return ResponseEntity.ok("Tests processed successfully!");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to parse tests: " + e.getMessage());
        }
    }

}
