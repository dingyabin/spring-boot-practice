package com.dingyabin.captcha.service;


import cn.hutool.core.util.RandomUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.util.Arrays;

@Slf4j
@Component
public class ResourceManager implements InitializingBean {

    private static final String IMAGE_PATH = "classpath:image/**";

    private static Resource[] IMAGE_RESOURCE;


    @Override
    public void afterPropertiesSet() throws Exception {
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        Resource[] resArr = resolver.getResources(IMAGE_PATH);
        IMAGE_RESOURCE = Arrays.stream(resArr).filter(Resource::isReadable).toArray(Resource[]::new);
    }


    public BufferedImage randomBgImage() {
        int randomInt = RandomUtil.randomInt(0, IMAGE_RESOURCE.length);
        Resource resource = IMAGE_RESOURCE[randomInt];
        try (InputStream inputStream = resource.getInputStream()) {
            return ImageIO.read(inputStream);
        } catch (Exception e) {
            log.error("获取图片报错...", e);
        }
        throw new RuntimeException("无法获取底图...");
    }

}
