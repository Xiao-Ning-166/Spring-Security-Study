package com.example.security;

import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.example.entity.SysUser;
import com.example.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsPasswordService;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * @author xiaoning
 * @date 2022/10/03
 */
public class MyUserDetailService implements UserDetailsService, UserDetailsPasswordService {

    @Autowired
    private UserMapper userMapper;

    /**
     * 从数据库中查询用户信息
     *
     * @param username the username identifying the user whose data is required.
     * @return a fully populated user record (never <code>null</code>)
     * @throws UsernameNotFoundException if the user could not be found or the user has no
     *                                   GrantedAuthority
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        SysUser user = userMapper.loadUserByUsername(username);

        if (ObjectUtils.isEmpty(user)) {
            throw new UsernameNotFoundException("用户名不存在！！");
        }

        return user;
    }

    /**
     * 自动升级加密密码。默认以Spring Security相对最安全的加密方式加密
     *
     * @param user        the user to modify the password for
     * @param newPassword the password to change to, encoded by the configured
     *                    {@code PasswordEncoder}
     * @return the updated UserDetails with the new password
     */
    @Override
    public UserDetails updatePassword(UserDetails user, String newPassword) {
        // 更新密码
        Integer changeCount = userMapper.updatePassword(user.getUsername(), newPassword);

        if (changeCount >= 1) {
            // 将新密码赋值给本地user对象
            ((SysUser) user).setPassword(newPassword);
        }

        return user;
    }

}
