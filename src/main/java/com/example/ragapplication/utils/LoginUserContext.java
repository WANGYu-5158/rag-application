package com.example.ragapplication.utils;


import com.example.ragapplication.pojo.SignInResp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoginUserContext {
    private static final Logger LOG = LoggerFactory.getLogger(LoginUserContext.class);

    private static ThreadLocal<SignInResp> user = new ThreadLocal<>();

    public static SignInResp getMember() {
        return user.get();
    }

    public static void setUser(SignInResp signInResp) {
        LoginUserContext.user.set(signInResp);
    }

    public static Long getId() {
        try {
            return user.get().getId();
        } catch (Exception e) {
            LOG.error("获取登录信息异常", e);
            throw e;
        }
    }

}
