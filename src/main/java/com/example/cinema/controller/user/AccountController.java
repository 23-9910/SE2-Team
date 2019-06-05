package com.example.cinema.controller.user;

import com.example.cinema.blImpl.user.AccountServiceImpl;
import com.example.cinema.config.InterceptorConfiguration;
import com.example.cinema.vo.UserForm;
import com.example.cinema.vo.ResponseVO;
import com.example.cinema.vo.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import com.example.cinema.po.User;
/**
 * @author huwen
 * @date 2019/3/23
 */
@RestController()
public class AccountController {
    private final static String ACCOUNT_INFO_ERROR="用户名或密码错误";
    @Autowired
    private AccountServiceImpl accountService;
    @PostMapping("/login")
    public ResponseVO login(@RequestBody UserForm userForm, HttpSession session){
        UserVO user = accountService.login(userForm);
        if(user==null){
           return ResponseVO.buildFailure(ACCOUNT_INFO_ERROR);
        }
        //注册session
        session.setAttribute(InterceptorConfiguration.SESSION_KEY,userForm);
        return ResponseVO.buildSuccess(user);
    }
    @PostMapping("/register")
    public ResponseVO registerAccount(@RequestBody UserForm userForm){
        return accountService.registerAccount(userForm);
    }

    @PostMapping("/logout")
    public String logOut(HttpSession session){
        session.removeAttribute(InterceptorConfiguration.SESSION_KEY);
        return "index";
    }

    /**
     * Modified by sun on 2019/06/03
     */
    @PostMapping("/search/all/manager")
    public ResponseVO searchAllManager(Integer userId){
        return accountService.searchAllManager(userId);
    }

    @PostMapping("/search/one/manager")
    public ResponseVO searchOneManager(Integer userId){
        return accountService.searchOneManager(userId);
    }

    @PostMapping("/update/one/manager")
    public ResponseVO updateOneManager(User user){
        return accountService.updateOneManager(user);
    }

    @PostMapping("/delete/one/manager")
    public ResponseVO deleteOneManager(Integer userId){
        return accountService.deleteOneManager(userId);
    }

    @PostMapping("/add/one/manager")
    public ResponseVO addOneManager(User user){
        return accountService.addOneManager(user);
    }

}
