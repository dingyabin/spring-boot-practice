package com.dingyabin.captcha.service.impl;

import cn.hutool.core.util.RandomUtil;
import com.dingyabin.captcha.service.KaptchaGenerateService;
import com.dingyabin.captcha.service.ResourceManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ClickWordsKaptchaGenerateService implements KaptchaGenerateService {

    @Resource
    private ResourceManager resourceManager;

    private static final String[] WORDS = {"是", "的", "非", "额", "日", "人", "需", "要", "源", "码", "的", "可", "到"};


    @Override
    public void createKaptcha(HttpServletRequest httpServletRequest, HttpServletResponse response) {
        BufferedImage bufferedImage = resourceManager.randomBgImage();
        Graphics graphics = bufferedImage.getGraphics();
        configGraphics(graphics);
        List<String> words = randomWords(3);
        for (int i = 0; i < words.size(); i++) {
            graphics.drawString(words.get(i), i == 0 ? RandomUtil.randomInt(10, 50) : i * 80, RandomUtil.randomInt(40, 100));
        }

        initImageResponseHeader(response);
        try (ServletOutputStream outputStream = response.getOutputStream()) {
            ImageIO.write(bufferedImage, "jpg", outputStream);
        } catch (Exception e) {
            log.error("验证码生成出错...", e);
        }
    }


    @Override
    public String createBase64Kaptcha(HttpServletRequest httpServletRequest, HttpServletResponse response) {

        return null;
    }


    private void configGraphics(Graphics graphics) {
        //随机字体颜色 [0,255]
        graphics.setColor(new Color(RandomUtil.randomInt(0, 256), RandomUtil.randomInt(0, 256), RandomUtil.randomInt(0, 256)));
        //设置角度
        AffineTransform affineTransform = new AffineTransform();
        affineTransform.rotate(Math.toRadians(RandomUtil.randomInt(-45, 45)), 0, 0);
        // 设置字体
        Font clickWordFont = new Font("宋体", Font.BOLD, 30);
        Font rotatedFont = clickWordFont.deriveFont(affineTransform);
        graphics.setFont(rotatedFont);
    }


    private List<String> randomWords(int count) {
        List<String> wordsList = Arrays.stream(WORDS).collect(Collectors.toCollection(LinkedList::new));
        //打乱顺序
        Collections.shuffle(wordsList);
        return wordsList.subList(0, count);
    }
}
