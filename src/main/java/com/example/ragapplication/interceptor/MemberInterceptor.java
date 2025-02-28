package com.example.ragapplication.interceptor;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.example.ragapplication.pojo.SignInResp;
import com.example.ragapplication.utils.JwtUtil;
import com.example.ragapplication.utils.LoginUserContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * 拦截器：Spring框架特有的，常用于登录校验，权限校验，请求日志打印
 */
@Component
@Slf4j
public class MemberInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader("token");

//        if (token == null || !isValidToken(token)) {
//            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//            return false
//        }

        if (StrUtil.isNotBlank(token)) {
            log.info("Login token：{}", token);
            JSONObject loginUser = JwtUtil.getJSONObject(token);
            log.info("current user：{}", loginUser);
            SignInResp user = JSONUtil.toBean(loginUser, SignInResp.class);
            LoginUserContext.setUser(user);
        }

        return true;
    }

    private boolean isValidToken(String token) {
        // 在这里实现 Token 校验逻辑
        // 例如：验证 Token 是否过期、是否有效等
        return true; // 返回校验结果
    }
}