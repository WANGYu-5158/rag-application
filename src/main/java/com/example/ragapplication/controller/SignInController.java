/*
 * Copyright (c) 2024. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.example.ragapplication.controller;

import com.example.ragapplication.pojo.SignInVo;
import com.example.ragapplication.pojo.Userdb;
import com.example.ragapplication.pojo.UserdbVo;
import com.example.ragapplication.service.SignInService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.security.PublicKey;

@RestController
@RequestMapping("/auth/jwt")
public class SignInController {
    @Autowired
    SignInService signInService;

    @GetMapping("/sign-in")
    public Userdb signInController(@RequestBody SignInVo signInVo){
        Userdb userdb = new Userdb();
        userdb.setUserId(null);
        userdb.setEmail(signInVo.getEmail());
        userdb.setPassword(signInVo.getPassword());
        userdb.setFirstName(null);
        userdb.setLastName(null);
        return signInService.signIn(userdb);
    }

}
