package com.yyh.backendjava.serviceImpl;

import com.yyh.backendjava.entity.User;
import com.yyh.backendjava.mapper.UserMapper;
import com.yyh.backendjava.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author yyh
 * @since 2024-04-28
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

}
