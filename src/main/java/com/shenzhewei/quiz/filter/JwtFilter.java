package com.shenzhewei.quiz.filter;

import com.alibaba.fastjson.JSONObject;
import com.shenzhewei.quiz.pojo.Result;
import com.shenzhewei.quiz.utils.JwtUtil;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.util.StringUtils;

import java.io.IOException;

/**
 * JWT 拦截器
 */
@WebFilter(urlPatterns = "/*")
public class JwtFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("JwtFilter init");
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        // 1、获取请求url
        String url = request.getRequestURL().toString();

        // 2、判断url中是否包含login或register，如果包含，则说明是登录或注册操作，放行
        if (url.contains("login") || url.contains("register")) {
            chain.doFilter(request, response);
            return;
        }

        // Handle CORS preflight
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            chain.doFilter(request, response);
            return;
        }

        // 3、获取请求头中的令牌(token)
        String token = request.getHeader("token");

        // 4、判断令牌是否存在，如果不存在，返回未登录信息
        if (!StringUtils.hasLength(token)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            Result<String> result = Result.error("NOT_LOGIN");
            // 手动将对象转为json，并传回前端
            String noLogin = JSONObject.toJSONString(result);
            response.setContentType("application/json;charset=UTF-8");
            res.getWriter().write(noLogin);
            return;
        }

        // 5、解析token，如果解析失败，返回未登录信息
        try {
            JwtUtil.parseToken(token);
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            Result<String> result = Result.error("NOT_LOGIN");
            // 手动将对象转为json，并传回前端
            String noLogin = JSONObject.toJSONString(result);
            response.setContentType("application/json;charset=UTF-8");
            res.getWriter().write(noLogin);
            return;
        }

        // 6、放行
        chain.doFilter(req, res);
    }

    @Override
    public void destroy() {
        System.out.println("JwtFilter destroy");
    }
}
