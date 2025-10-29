package com.dingyabin.captcha.service.impl;

import cn.hutool.core.util.RandomUtil;
import com.dingyabin.captcha.pojo.KaptchaWord;
import com.dingyabin.captcha.pojo.PointLocation;
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
import java.util.List;
import java.util.*;
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
        List<KaptchaWord> kaptchaWords = randomKaptchaWords(3);
        //按照顺序写入底图
        for (KaptchaWord kaptchaWord : kaptchaWords) {
            PointLocation pointLocation = kaptchaWord.getPointLocation();
            graphics.drawString(kaptchaWord.getWord(), pointLocation.getX(), pointLocation.getY());
        }
        //随机删除一个
        kaptchaWords.remove(RandomUtil.randomInt(0, kaptchaWords.size()));
        //剩下的打乱顺序
        Collections.shuffle(kaptchaWords);
        //写入redis做记录
        for (KaptchaWord kaptchaWord : kaptchaWords) {
            System.out.println(kaptchaWord);
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


    private List<KaptchaWord> randomKaptchaWords(int count) {
        List<String> wordsList = Arrays.stream(WORDS).collect(Collectors.toCollection(LinkedList::new));
        //打乱顺序
        Collections.shuffle(wordsList);
        List<String> randomWords = wordsList.subList(0, count);
        List<KaptchaWord> randomKaptcha = new LinkedList<>();
        for (int i = 0; i < randomWords.size(); i++) {
            randomKaptcha.add(new KaptchaWord(randomWords.get(i), randomPoint(i)));
        }
        return randomKaptcha;
    }


    private PointLocation randomPoint(int index) {
        return new PointLocation(index == 0 ? RandomUtil.randomInt(10, 40) : index * 80, RandomUtil.randomInt(30, 90));
    }

}
