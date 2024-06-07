package com.yyh.backendjava.mapper;

import com.yyh.backendjava.entity.Kanban;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author yyh
 * @since 2024-04-27
 */
public interface KanbanMapper extends BaseMapper<Kanban> {

    @Insert("insert into kanban (user_id,msg,status) values (#{userId},#{msg},#{status})")
    void addMsg(String userId,String msg,String status );

    @Select("select * from kanban where user_id = #{userId} and deleted = 0")
    List<Kanban> getListByUserId(String userId);

    @Update("update kanban set status = #{status} where msg_id = #{msgId}")
    void changeStatus(int msgId,String status);

    @Delete("update kanban set deleted = 1 where status = 'finished'")
    void deleteFinished();

    @Select("select * from kanban where deleted = 0")
    List<Kanban> getListNotDeleted();
}