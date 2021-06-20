package com.guidian.teaching.security;

import com.guidian.teaching.entity.User;
import com.guidian.teaching.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.TreeSet;

/**
 * @author dhxstart
 * @Description
 * @date 2021/6/13 10:35
 */
@Service
public class UserDetailServiceImpl implements UserDetailsService {
    @Autowired
    UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.getByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("用户名或密码不正确！");
        }
        return new AccountUser(user.getUserId(), user.getUsername(), user.getPassword(), new TreeSet<>());
    }

    /**
     * @Description 获取用户权限信息（角色、菜单权限）
     * @author dhxstart
     * @date 2021/6/13 11:05
     * @param userId 用户编号
     * @return java.util.List<org.springframework.security.core.GrantedAuthority>
     */
	public List<GrantedAuthority> getUserAuthority(String userId){
		 //角色(ROLE_admin)、菜单操作权限 system:user:list
		return AuthorityUtils.commaSeparatedStringToAuthorityList(null);
	}
}
