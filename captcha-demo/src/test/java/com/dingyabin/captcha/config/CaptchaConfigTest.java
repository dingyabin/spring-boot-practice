package com.dingyabin.captcha.config;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.FileOutputStream;

@SpringBootTest
class CaptchaConfigTest {

    @Resource
    private DefaultKaptcha defaultKaptcha;

    @Test
    public void testDefaultKaptcha() throws Exception {
        String text = defaultKaptcha.createText();
        BufferedImage image = defaultKaptcha.createImage(text);
        ImageIO.write(image, "jpg", new FileOutputStream("F:\\captcha.jpg"));
    }
}