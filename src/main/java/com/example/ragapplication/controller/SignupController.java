/*
 * Copyright (c) 2024. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.example.ragapplication.controller;

import com.example.ragapplication.pojo.Userdb;
import com.example.ragapplication.pojo.UserdbVo;
import com.example.ragapplication.service.SignUpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth/jwt")
//@CrossOrigin(origins = "http://localhost:8081")
public class SignupController {
    @Autowired
    private SignUpService signUpService;

    @GetMapping("/sign-up")
    public Integer signUp(@RequestBody UserdbVo userdbVo) {
        Userdb userdb = new Userdb();
        userdb.setUserId(null);
        userdb.setPassword(userdbVo.getPassword());
        userdb.setEmail(userdbVo.getEmail());
        userdb.setFirstName(userdbVo.getFirstName());
        userdb.setLastName(userdbVo.getLastName());
        return signUpService.signUp(userdb);
    }
}
