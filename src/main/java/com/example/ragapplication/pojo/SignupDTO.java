package com.example.ragapplication.pojo;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * ClassName: SignupVo
 * Package: com.example.ragapplication.pojo
 * Description:
 *
 * @Author 刘翼
 * @Create 2025/2/17 23:10
 * @Vertion 1.0
 */
@Slf4j
@Data
public class SignupDTO {
    private String email;
    private String password;
    private String firstName;
    private String lastName;
}
