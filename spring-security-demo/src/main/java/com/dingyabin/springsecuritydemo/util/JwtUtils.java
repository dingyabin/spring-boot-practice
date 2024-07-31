package com.dingyabin.springsecuritydemo.util;

import com.alibaba.fastjson.JSONObject;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.commons.lang3.tuple.Pair;

import java.nio.charset.StandardCharsets;
import java.util.Date;

/**
 * @author 丁亚宾
 * Date: 2024/7/31.
 * Time:16:14
 */
public class JwtUtils {
    
    // TOKEN的有效期1小时（S）
    public static final int TOKEN_TIME_OUT = 2;

    // 加密KEY
    private static final String TOKEN_SECRET = "HwRpTt5qubhJnIy2oOufdHwRpTt5qubhJnIy2oO";


    // 生成Token
    public static String getToken(Object subject) {
        return Jwts.builder()
                .signWith(Keys.hmacShaKeyFor(TOKEN_SECRET.getBytes(StandardCharsets.UTF_8)))
                .expiration(DateUtils.addHours(new Date(), TOKEN_TIME_OUT))
                .subject(JSONObject.toJSONString(subject))
                .compact();
    }


    // 生成Token
    public static String getToken(String subject) {
        return Jwts.builder()
                .signWith(Keys.hmacShaKeyFor(TOKEN_SECRET.getBytes(StandardCharsets.UTF_8)))
                .expiration(DateUtils.addHours(new Date(), TOKEN_TIME_OUT))
                .subject(subject)
                .compact();
    }


    /**
     * 获取Token中的claims信息
     */
    public static Pair<Boolean, Claims> parseToken(String token) {
        try {
            Claims payload = Jwts.parser()
                    .verifyWith(Keys.hmacShaKeyFor(TOKEN_SECRET.getBytes(StandardCharsets.UTF_8)))
                    .build()
                    .parseSignedClaims(token).getPayload();
            return Pair.of(Boolean.TRUE, payload);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Pair.of(Boolean.FALSE, null);
    }


    /**
     * 获取Token中的claims信息
     */
    public static <T> Pair<Boolean, T> parseToken(String token, Class<T> clz) {
        try {
            Claims payload = Jwts.parser()
                    .verifyWith(Keys.hmacShaKeyFor(TOKEN_SECRET.getBytes(StandardCharsets.UTF_8)))
                    .build()
                    .parseSignedClaims(token).getPayload();
            return Pair.of(Boolean.TRUE, JSONObject.parseObject(payload.getSubject(), clz));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Pair.of(Boolean.FALSE, null);
    }


//
//    public static void main(String[] args) {
//        String abcdefg = JwtUtils.getToken("abcdefsdsagbdfhrnfsdgreysdvwrehbfdbdtrnjgfhymntuymg");
//        System.out.println(abcdefg);
//
//        Pair<Boolean, Claims> booleanClaimsPair = JwtUtils.parseToken("");
//        System.out.println(booleanClaimsPair);
//    }

}
