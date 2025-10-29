package com.dingyabin.captcha.config;

import com.google.code.kaptcha.Constants;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.impl.DefaultNoise;
import com.google.code.kaptcha.impl.WaterRipple;
import com.google.code.kaptcha.util.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;


@Configuration
public class CaptchaConfig {


    @Bean
    public DefaultKaptcha defaultKaptcha() {
        DefaultKaptcha defaultKaptcha = new DefaultKaptcha();
        Properties properties = new Properties();
        // 验证码字符来源
        properties.setProperty(Constants.KAPTCHA_TEXTPRODUCER_CHAR_STRING, "23456789ABCDEFGHIJKMNPQRSTUVWXYZ");
        // 验证码长度
        properties.setProperty(Constants.KAPTCHA_TEXTPRODUCER_CHAR_LENGTH, "4");
        //字符间距
        properties.setProperty(Constants.KAPTCHA_TEXTPRODUCER_CHAR_SPACE, "16");
        // 图片宽度
        properties.setProperty(Constants.KAPTCHA_IMAGE_WIDTH, "280");
        // 图片高度
        properties.setProperty(Constants.KAPTCHA_IMAGE_HEIGHT, "90");
        // 字体大小
        properties.setProperty(Constants.KAPTCHA_TEXTPRODUCER_FONT_SIZE, "60");
        // 字体颜色（可选）
        properties.setProperty(Constants.KAPTCHA_TEXTPRODUCER_FONT_COLOR, "green");
        // 干扰线颜色
        properties.setProperty(Constants.KAPTCHA_NOISE_COLOR, "blue");
        properties.setProperty(Constants.KAPTCHA_NOISE_IMPL, DefaultNoise.class.getName());
        properties.setProperty(Constants.KAPTCHA_OBSCURIFICATOR_IMPL, WaterRipple.class.getName());
        // 背景色（RGB）
        properties.setProperty(Constants.KAPTCHA_BACKGROUND_CLR_FROM, "lightGray");
        properties.setProperty(Constants.KAPTCHA_BACKGROUND_CLR_TO, "white");
        // 边框
        properties.setProperty(Constants.KAPTCHA_BORDER, "no");
        properties.setProperty(Constants.KAPTCHA_BORDER_COLOR, "105,179,90");
        defaultKaptcha.setConfig(new Config(properties));
        return defaultKaptcha;
    }

}
