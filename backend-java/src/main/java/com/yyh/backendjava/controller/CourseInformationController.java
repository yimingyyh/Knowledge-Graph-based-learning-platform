package com.yyh.backendjava.controller;


import com.yyh.backendjava.entity.CourseInformation;
import com.yyh.backendjava.mapper.CourseInformationMapper;
import com.yyh.backendjava.util.ResultUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author yyh
 * @since 2024-04-22
 */
@Controller
@RestController//以json格式返回
@RequestMapping("/courseInformation")
public class CourseInformationController {
    @Resource
    private CourseInformationMapper courseInformationMapper;

//    @Value("http://localhost:9527/source/")
//    private String filePath;

    @CrossOrigin
    @GetMapping("/getAllCourseInformations")
    public Object getAllCourseInformations(){
        return ResultUtil.isSuccess(courseInformationMapper.selectList(null));
    }

    //添加
    @CrossOrigin
    @PostMapping("/addCourseInformations")
    public Object addCourseInformations(@RequestBody CourseInformation courseInformation){
        CourseInformation courseInformation1 = courseInformationMapper.selectByName(courseInformation.getCourseName());
        if(courseInformation1==null){
            return ResultUtil.isSuccess(courseInformationMapper.insert(courseInformation));
        }else {
            return ResultUtil.isSuccess("已存在",66);
        }

    }

    //根据课程名查询
    @CrossOrigin
    @PostMapping("/getCourseByName")
    public Object getCourseByName(@RequestParam("courseName") String courseName){
        CourseInformation courseInformation = new CourseInformation();
        //若对应的课程有信息了 返回 否则返回空对象
        try{
            courseInformation = courseInformationMapper.selectByName(courseName);
            return ResultUtil.isSuccess(courseInformation);
        }catch (Exception e){
            System.out.println(e);
            return ResultUtil.isSuccess(new CourseInformation());
        }
    }

    //修改ppt 视频等信息
    @CrossOrigin
    @PostMapping("/updateCourseInformation")
    public void updateCourseInformation(@RequestParam("courseName") String courseName,
                                          @RequestParam("courseIntroduction") String courseIntroduction,
                                          @RequestParam("coursePptpath") String coursePptpath,
                                          @RequestParam("coursePracticepath") String coursePracticepath,
                                          @RequestParam("courseVideopath") String courseVideopath){
        courseInformationMapper.updateCourseInformation(courseName,courseIntroduction,coursePptpath,coursePracticepath,courseVideopath);
//        return ResultUtil.isSuccess(1);
    }

    @CrossOrigin
    @PostMapping("/saveFile")
    public Map<String,String> saveFiles(@RequestParam("file") MultipartFile file,@RequestParam("courseName") String courseName,@RequestParam("type") int type){
        Map<String,String> resultMap = new HashMap<>();
        try{

            //获取文件后缀，因此此后端代码可接收一切文件，上传格式前端限定
//            String savePath = "E:\\2020-2024HDU\\graduation_project\\learningSources";
            String savePath = "E:\\2020-2024HDU\\graduation_project\\kg_vue-element-admin-master\\src\\assets\\learningSources";
            String fileExt = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1)
                    .toLowerCase();
            // 重构文件名称
            System.out.println("前端传递的保存路径："+savePath);
            String pikId = UUID.randomUUID().toString().replaceAll("-", "");
            String newVidoeName = pikId + "." + fileExt;
            System.out.println("重构文件名防止上传同名文件："+newVidoeName);
            //保存视频
            File fileSave = new File(savePath, newVidoeName);
            file.transferTo(fileSave);
            //构造Map将视频信息返回给前端
            //视频名称重构后的名称
            resultMap.put("newVidoeName",newVidoeName);
            //正确保存视频则设置返回码为200
            resultMap.put("resCode","200");
            //返回视频保存路径
            resultMap.put("VideoUrl",savePath + "/" + newVidoeName);//返回虚拟路径给前端
            CourseInformation courseInformation = courseInformationMapper.selectByName(courseName);
            if(type == 1){
                //存ppt
                courseInformationMapper.updateCourseInformation(
                        courseInformation.getCourseName(),
                        courseInformation.getCourseIntroduction(),
                        savePath + "/" + newVidoeName,
                        courseInformation.getCoursePracticepath(),
                        courseInformation.getCourseVideopath());
            }else if(type == 2){
                //存practice
                courseInformationMapper.updateCourseInformation(
                        courseInformation.getCourseName(),
                        courseInformation.getCourseIntroduction(),
                        courseInformation.getCoursePptpath(),
                        savePath + "/" + newVidoeName,
                        courseInformation.getCourseVideopath());
            }else if(type == 3){
                //存视频
                courseInformationMapper.updateCourseInformation(
                        courseInformation.getCourseName(),
                        courseInformation.getCourseIntroduction(),
                        courseInformation.getCoursePptpath(),
                        courseInformation.getCoursePptpath(),
                        savePath + "/" + newVidoeName);
            }

            return  resultMap;

        }catch (Exception e){
            e.printStackTrace();
            e.getMessage();
            //保存视频错误则设置返回码为400
            resultMap.put("resCode","400");
            return  resultMap ;

        }
    }

    @CrossOrigin
    @PostMapping("updateIntroduction")
    public void updateIntroduction(@RequestParam ("courseName")String courseName, @RequestParam("introduction") String introduction){
        CourseInformation courseInformation = courseInformationMapper.selectByName(courseName);
        courseInformation.setCourseIntroduction(introduction);
        courseInformationMapper.updateCourseInformation(
                courseInformation.getCourseName(),
                courseInformation.getCourseIntroduction(),
                courseInformation.getCoursePptpath(),
                courseInformation.getCoursePptpath(),
                courseInformation.getCourseVideopath());

    }
}
