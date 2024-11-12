/*
 * Copyright (c) 2024. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.example.ragapplication.mapper;

import com.example.ragapplication.pojo.Userdb;
import com.example.ragapplication.pojo.UserdbVo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {
    Integer insertUser(Userdb user);
    Integer findUserByEmail(Userdb userdb);
    Userdb findUserByEmailAndPassword(Userdb userdb);

}
