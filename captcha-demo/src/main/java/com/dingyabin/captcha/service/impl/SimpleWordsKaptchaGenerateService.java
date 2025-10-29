package com.dingyabin.captcha.service.impl;

import com.dingyabin.captcha.service.KaptchaGenerateService;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.Base64;

@Slf4j
@Service
public class SimpleWordsKaptchaGenerateService implements KaptchaGenerateService {


    @Resource
    private DefaultKaptcha defaultKaptcha;


    @Override
    public void createKaptcha(HttpServletRequest httpServletRequest, HttpServletResponse response) {
        String text = defaultKaptcha.createText();
        BufferedImage image = defaultKaptcha.createImage(text);
        initImageResponseHeader(response);
        try (ServletOutputStream outputStream = response.getOutputStream()) {
            ImageIO.write(image, "jpg", outputStream);
        } catch (Exception e) {
            log.error("验证码生成出错...", e);
        }
    }

    @Override
    public String createBase64Kaptcha(HttpServletRequest httpServletRequest, HttpServletResponse response) {
        String text = defaultKaptcha.createText();
        try (ByteArrayOutputStream os = new ByteArrayOutputStream()) {
            ImageIO.write(defaultKaptcha.createImage(text), "jpg", os);
            return Base64.getEncoder().encodeToString(os.toByteArray());
        } catch (Exception e) {
            log.error("验证码生成出错...", e);
        }
        return null;
    }
}
