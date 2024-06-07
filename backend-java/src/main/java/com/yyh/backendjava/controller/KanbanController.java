package com.yyh.backendjava.controller;


import com.yyh.backendjava.entity.Kanban;
import com.yyh.backendjava.mapper.KanbanMapper;
import com.yyh.backendjava.service.IKanbanService;
import com.yyh.backendjava.util.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author yyh
 * @since 2024-04-27
 */
@Controller
@RestController
@RequestMapping("/kanban")
public class KanbanController {
    @Resource
    private KanbanMapper kanbanMapper;

    @Resource
    private IKanbanService kanbanService;

    //删除已完成的
    @CrossOrigin
    @PostMapping("/deleteFinished")
    public void deleteFinished(){
        kanbanMapper.deleteFinished();
    }

    //移动待办改变状态
    @CrossOrigin
    @PostMapping("/changeStatus")
    public void changeStatus(@RequestParam("msgId")int msgId,@RequestParam("status")String status){
        kanbanMapper.changeStatus(msgId,status);
    }

    @CrossOrigin
    @PostMapping("/addMsg")
    public void addKanbanMsg(@RequestParam ("userId") String userId,@RequestParam("msg")String msg,@RequestParam("status") String status){
        kanbanMapper.addMsg(userId,msg,status);
    }

    @CrossOrigin
    @GetMapping("/getList")
    public Object getList(){
        return  ResultUtil.isSuccess(kanbanMapper.getListNotDeleted());
    }

    @CrossOrigin
    @PostMapping("/getListByUser")
    public Object getListByUser(@RequestParam("userId")String userId){
        List<Kanban> list = new ArrayList<>();
        list = kanbanMapper.getListByUserId(userId);
        return ResultUtil.isSuccess(list);
    }
}
