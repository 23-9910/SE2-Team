package com.example.cinema.data.user;

import com.example.cinema.po.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.*;

/**
 * @author huwen
 * @date 2019/3/23
 */
@Mapper
public interface AccountMapper {

    /**
     * 创建一个新的账号(用户,state=2)
     * @param username
     * @param password
     * @return
     */
    int createNewAccount(@Param("username") String username, @Param("password") String password);

    /**
     * 根据用户名查找账号
     * @param username
     * @return
     */
    User getAccountByName(@Param("username") String username);

    /**
     * Modified by sun on 2019/06/03
     * 获得所有除了经理外的用户信息
     */
    List<User> getAllManagers();

    /**
     * 通过userId获得用户信息
     * @param userId
     * @return
     */
    User getManagerById(int userId);

    /**
     * 更新一个用户信息
     * @param user
     * @return
     */
    int updateOneManager(User user);

    /**
     * 删除一个用户信息
     * @param userId
     * @return
     */
    int deleteOneManager(int userId);

    /**
     * 加入一个用户信息
     * @param user
     * @return
     */
    int insertOneManager(User user);
}
