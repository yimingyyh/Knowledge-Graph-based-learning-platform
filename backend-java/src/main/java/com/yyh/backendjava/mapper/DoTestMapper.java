package com.yyh.backendjava.mapper;

import com.yyh.backendjava.entity.DoTest;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;
import java.util.List;
/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author yyh
 * @since 2024-04-30
 */
public interface DoTestMapper extends BaseMapper<DoTest> {
    //根据testId和userID返回学生测试结果
    DoTest findAnsById(int testId,String userId);

}
