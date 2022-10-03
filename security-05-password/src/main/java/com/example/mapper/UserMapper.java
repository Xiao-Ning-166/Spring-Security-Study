package com.example.mapper;

import com.example.entity.SysUser;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author xiaoning
 * @date 2022/10/02
 */
@Mapper
public interface UserMapper {

    /**
     * 根据用户名查询用户
     *
     * @param username
     * @return
     */
    SysUser loadUserByUsername(String username);

    /**
     * 更新用户密码
     *
     * @param username
     * @param newPassword
     * @return
     */
    Integer updatePassword(String username, String newPassword);
}
