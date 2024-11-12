/*
 * Copyright (c) 2024. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.example.ragapplication.service.impl;

import com.example.ragapplication.mapper.UserMapper;
import com.example.ragapplication.pojo.Userdb;
import com.example.ragapplication.service.SignInService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

@Service
public class SignInServiceImpl implements SignInService {

    @Autowired
    UserMapper userMapper;

    @Override
    public Userdb signIn(Userdb user) {
        String md5Password = DigestUtils.md5DigestAsHex(user.getPassword().getBytes());
        user.setPassword(md5Password);
        Userdb userdb = new Userdb();
        userdb = userMapper.findUserByEmailAndPassword(user);
        if (userdb != null) {
            return userdb;
        }
        else return null;
    }
}
