package com.dingyabin.springsecuritydemo.config.security.filter;

import com.dingyabin.redis.helper.RedisHelper;
import com.dingyabin.springsecuritydemo.config.security.SecurityUserDetails;
import com.dingyabin.springsecuritydemo.model.response.SecurityUserCache;
import com.dingyabin.springsecuritydemo.model.response.TokenMsg;
import com.dingyabin.springsecuritydemo.util.JwtUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.dingyabin.springsecuritydemo.enums.RedisKeyEnum.LOGIN_USER;

/**
 * @author 丁亚宾
 * Date: 2024/7/31.
 * Time:15:42
 */
@Service
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Resource
    private RedisHelper redisHelper;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = request.getHeader("token");
        if (StringUtils.isNotBlank(token)) {
            SecurityContextHolder.getContext().setAuthentication(buildAuthentication(token));
        }
        filterChain.doFilter(request, response);
    }


    private Authentication buildAuthentication(String token) {
        Pair<Boolean, TokenMsg> tokenMsgResult = JwtUtils.parseToken(token, TokenMsg.class);
        if (!tokenMsgResult.getLeft()) {
            return null;
        }
        TokenMsg tokenMsg = tokenMsgResult.getRight();
        SecurityUserCache securityUserCache = redisHelper.getCacheObject(LOGIN_USER.toKey(tokenMsg.getUserId()), SecurityUserCache.class);
        if (securityUserCache == null) {
            return null;
        }
        SecurityUserDetails userDetails = new SecurityUserDetails(securityUserCache);
        return UsernamePasswordAuthenticationToken.authenticated(userDetails, null, userDetails.getAuthorities());
    }


}
