package com.yyh.backendjava.mapper;

import com.yyh.backendjava.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author yyh
 * @since 2024-04-28
 */
public interface UserMapper extends BaseMapper<User> {
    User selectById(String userId);

    void updateMes(String userId,String userName,String userPassword,int userType,String userEmail,String userPhone,String userSalt);

    @Select("select * from user where user_type = 2 and deleted = 0")
    List<User> getStudents();

    void changeEmail(String userEmail,String userId);
}
