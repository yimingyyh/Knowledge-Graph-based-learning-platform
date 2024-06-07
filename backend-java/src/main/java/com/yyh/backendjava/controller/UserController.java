package com.yyh.backendjava.controller;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yyh.backendjava.entity.User;
import com.yyh.backendjava.mapper.UserMapper;
import com.yyh.backendjava.util.ResultUtil;
import org.apache.commons.codec.digest.DigestUtils;
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
 * @since 2024-04-28
 */
@Controller
@RestController
@RequestMapping("/user")
public class UserController {
    @Resource
    private UserMapper userMapper;

    @CrossOrigin
    @PostMapping("/getUserMes")
    public Object getStudMes(@RequestParam("userId")String userId){
        return ResultUtil.isSuccess(userMapper.selectById(userId));
    }

    @CrossOrigin
    @PostMapping("/updateUserMes")
    public void updateUserMes(@RequestParam("userId") String userId,
                              @RequestParam("userName")String userName,
                              @RequestParam("userPassword") String userPassword,
                              @RequestParam("userType")int userType,
                              @RequestParam("userEmail") String userEmail,
                              @RequestParam("userPhone")String userPhone){
        System.out.println("传回的userPwd"+userPassword);
        User user = userMapper.selectById(userId);
        System.out.println("原来的密码"+user.getUserPassword());
        System.out.println("原来的盐"+user.getUserSalt());
//        String pwd = user.getUserPassword();
        System.out.println("新盐"+DigestUtils.md5Hex(userPassword));
        System.out.println("新密码"+DigestUtils.md5Hex(userPassword+user.getUserSalt()));
        user.setUserSalt(DigestUtils.md5Hex(userPassword));//加盐
        user.setUserPassword(DigestUtils.md5Hex(userPassword+user.getUserSalt()));//原密码跟盐重新组成新密码
        userMapper.updateMes(userId,userName,user.getUserPassword(),userType,userEmail,userPhone,user.getUserSalt());
    }

    //更改邮箱
    @CrossOrigin
    @PostMapping("/changeEmail")
    public void changeEamil(@RequestParam("userEmail") String userEmail,@RequestParam("userId")String userId){
//        User user = userMapper.selectById(userId);
//        user.setUserEmail(userEmail);
        userMapper.changeEmail(userEmail,userId);
    }

    //查询所有用户信息
    @CrossOrigin
    @GetMapping("/getStudents/{current}/{size}")
    public Object getStudents(@PathVariable("current") Integer current, @PathVariable("size") Integer size){
        PageHelper.startPage(current, size);
        List<List<String>> list = new ArrayList<>();
        List<User> users = userMapper.getStudents();
        for( User user :users){
            List<String> temp =new ArrayList<>();
            temp.add(user.getUserId());
            temp.add(user.getUserName());
            temp.add(user.getUserEmail());
            temp.add(user.getUserPhone());
            list.add(temp);
        }
        PageInfo page = new PageInfo(list);
        return ResultUtil.isSuccess(page);
    }
    //查询所有未被删除的用户信息
    @CrossOrigin
    @GetMapping("/getAllUsersNotDeleted")
    public Object getAllUsers(){
        List<User> userLists = userMapper.selectList(null);
        return ResultUtil.isSuccess(userLists);
    }

    //添加用户--注册
    @CrossOrigin
    @PostMapping("/addUser")
    public Object addUser(@RequestBody User user){
        String pwd = user.getUserPassword();
        user.setUserSalt(DigestUtils.md5Hex(user.getUserPassword()));//加盐
        user.setUserPassword(DigestUtils.md5Hex(pwd+user.getUserSalt()));//原密码跟盐重新组成新密码
        int i = userMapper.insert(user);
        if(i == 1){
            return ResultUtil.isSuccess("添加成功",6);
        }else {
            return ResultUtil.isSuccess("添加失败",555);
        }
    }

    //登录
    @CrossOrigin
    @PostMapping("/login")
    public Object login(@RequestBody User user){

        System.out.println("传入的用户数据+");
        System.out.println(user.toString());
        //与数据库中的信息进行比较 用户输入的密码与数据库中的盐再次加密 然后与数据库中的密码进行比较
        User user1 = userMapper.selectById(user.getUserId());
        System.out.println("数据库中用户信息"+user1.toString());
//        System.out.println(user1.toString());
        String fakePwd = DigestUtils.md5Hex(user.getUserPassword()+user1.getUserSalt());
        System.out.println(fakePwd);
        System.out.println(user1.getUserPassword());
        if(user1.getUserPassword().equals(fakePwd)){
            return ResultUtil.isSuccess(user1);
        }else {
            return ResultUtil.isSuccess("密码错误！",null);
        }


    }
}

