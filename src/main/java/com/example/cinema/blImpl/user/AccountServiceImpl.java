package com.example.cinema.blImpl.user;

import com.example.cinema.bl.user.AccountService;
import com.example.cinema.data.user.AccountMapper;
import com.example.cinema.po.User;
import com.example.cinema.vo.UserForm;
import com.example.cinema.vo.ResponseVO;
import com.example.cinema.vo.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;

/**
 * @author huwen
 * @date 2019/3/23
 */
@Service
public class AccountServiceImpl implements AccountService {
    private final static String ACCOUNT_EXIST = "账号已存在";
    @Autowired
    private AccountMapper accountMapper;

    @Override
    public ResponseVO registerAccount(UserForm userForm) {
        try {
            accountMapper.createNewAccount(userForm.getUsername(), userForm.getPassword());
        } catch (Exception e) {
            return ResponseVO.buildFailure(ACCOUNT_EXIST);
        }
        return ResponseVO.buildSuccess();
    }

    @Override
    public UserVO login(UserForm userForm) {
        User user = accountMapper.getAccountByName(userForm.getUsername());
        if (null == user || !user.getPassword().equals(userForm.getPassword())) {
            return null;
        }
        return new UserVO(user);
    }

    @Override
    public ResponseVO searchAllManager(int userId) {
        try {
            if(userId == 8){
                List<User> userList = accountMapper.getAllManagers();
                return ResponseVO.buildSuccess(userList);
            }else {
                return ResponseVO.buildFailure("您没有访问权限");
            }
        }catch (Exception e){
            e.printStackTrace();
            return ResponseVO.buildFailure("失败");
        }
    }

    @Override
    public ResponseVO searchOneManager(int userId) {
        try {
            User user = accountMapper.getManagerById(userId);
            return ResponseVO.buildSuccess(user);
        }catch (Exception e){
            e.printStackTrace();
            return ResponseVO.buildFailure("失败");
        }
    }

    @Override
    public ResponseVO updateOneManager(User user) {
        try {
            accountMapper.updateOneManager(user);
        }catch (Exception e){
            return ResponseVO.buildFailure(ACCOUNT_EXIST );
        }
        return ResponseVO.buildSuccess();
    }

    @Override
    public ResponseVO deleteOneManager(int userId) {
        try {
            accountMapper.deleteOneManager(userId);
            return ResponseVO.buildSuccess();
        }catch (Exception e){
            e.printStackTrace();
            return ResponseVO.buildFailure("失败");
        }
    }

    @Override
    public ResponseVO addOneManager(User user) {
        try {
            accountMapper.insertOneManager(user);
        }catch (Exception e){
            System.out.println("????");
            return ResponseVO.buildFailure(ACCOUNT_EXIST );
        }
        return ResponseVO.buildSuccess();
    }



}
