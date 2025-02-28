/*
 * Copyright (c) 2024. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.example.ragapplication.service.impl;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.ObjectUtil;
import com.example.ragapplication.mapper.UserMapper;
import com.example.ragapplication.pojo.SignupDTO;
import com.example.ragapplication.pojo.Userdb;
import com.example.ragapplication.pojo.UserdbVo;
import com.example.ragapplication.service.SignUpService;
import com.example.ragapplication.utils.SnowUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.swing.*;

@Service
public class SignupServiceImpl implements SignUpService {
    @Autowired
    UserMapper userMapper;

    @Override
    public Long signUp(SignupDTO signupDTO) {
        String email = signupDTO.getEmail();
        Userdb userdb = userMapper.findUserByEmail(email);

//        if(ObjectUtil.isNotEmpty(user)) {
//
//        }
        Userdb user = new Userdb();
        user.setId(SnowUtil.getSnowflakeNextId());
        user.setEmail(email);
        user.setPassword(signupDTO.getPassword());
        user.setLastName(signupDTO.getLastName());
        user.setFirstName(signupDTO.getFirstName());
        userMapper.insertUser(user);
        return user.getId();
    }

//    @Override
//    public Integer signUp(Userdb userdb) {
//        String md5password = DigestUtils.md5DigestAsHex(userdb.getPassword().getBytes());
//        userdb.setPassword(md5password);
//        Integer id = userMapper.findUserByEmail(userdb);
//        if (id != null) {
//            return 0;
//        }
//        else return userMapper.insertUser(userdb);
//        return 0;
//    }

}
