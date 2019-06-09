package com.example.cinema.bl.user;

import com.example.cinema.vo.UserForm;
import com.example.cinema.vo.ResponseVO;
import com.example.cinema.vo.UserVO;
import com.example.cinema.po.User;
/**
 * @author huwen
 * @date 2019/3/23
 */
public interface AccountService {

    /**
     * 注册账号
     * @return
     */
    ResponseVO registerAccount(UserForm userForm);

    /**
     * 用户登录，登录成功会将用户信息保存再session中
     * @return
     */
    UserVO login(UserForm userForm);

    /**
     * Modified by sun on 2019/06/03
     */

    /**
     * 查看所有管理员信息
     */
    ResponseVO searchAllManager(int userId);

    /**
     * 查看某个管理员信息
     */
    ResponseVO searchOneManager(int userId);

    /**
     * 修改某个管理员信息
     */
    ResponseVO updateOneManager(User user);

    /**
     * 删除某个管理员信息
     */
    ResponseVO deleteOneManager(int userId);

    /**
     * 添加某个管理员信息
     */
    ResponseVO addOneManager(User user);

}
