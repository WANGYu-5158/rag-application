/*
 * Copyright (c) 2024. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.example.ragapplication.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.jwt.JWTUtil;
import com.example.ragapplication.mapper.UserMapper;
import com.example.ragapplication.pojo.SignInResp;
import com.example.ragapplication.pojo.SignInVo;
import com.example.ragapplication.pojo.Userdb;
import com.example.ragapplication.service.SignInService;
import com.example.ragapplication.utils.JwtUtil;
import com.example.ragapplication.utils.LoginUserContext;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.Map;

@Service
@Slf4j
public class SignInServiceImpl implements SignInService {

    @Autowired
    UserMapper userMapper;

//    @Override
//    public Userdb signIn(Userdb user) {
//        String md5Password = DigestUtils.md5DigestAsHex(user.getPassword().getBytes());
//        user.setPassword(md5Password);
//        Userdb userdb = new Userdb();
//        userdb = userMapper.findUserByEmailAndPassword(user);
//        if (userdb != null) {
//            return userdb;
//        }
//        else return null;
//    }

    @Override
    public SignInResp signIn(SignInVo signInVo) {
        String email = signInVo.getEmail();
        Userdb userdb = userMapper.findUserByEmail(email);
        SignInResp signInResp = new SignInResp();

        if(ObjectUtil.isNull(userdb)) {
            signInResp.setStatus(401);
            return signInResp;
        }
        if(!StrUtil.equals(userdb.getPassword(), signInVo.getPassword())) {
            signInResp.setStatus(401);
            return signInResp;
        }

        signInResp.setStatus(200);
        signInResp = BeanUtil.copyProperties(userdb, SignInResp.class);
        String token = JwtUtil.createToken(signInResp.getId(), signInResp.getEmail());
        signInResp.setAccessToken(token);
        return signInResp;
    }
}
