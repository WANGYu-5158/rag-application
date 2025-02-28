package com.example.ragapplication.pojo;

import lombok.Data;

/**
 * ClassName: SignInResp
 * Package: com.example.ragapplication.pojo
 * Description:
 *
 * @Author 刘翼
 * @Create 2025/2/10 19:13
 * @Vertion 1.0
 */
@Data
public class SignInResp {
    private Long id;
    private String email;
    private String accessToken;
    private Integer status;
}
