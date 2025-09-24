package com.dingyabin.captcha.service;


import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dingyabin.captcha.mapper.ImageCaptchaMapper;
import com.dingyabin.captcha.model.ImageCaptcha;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;

/**
 * @author 丁亚宾
 * @description 针对表【image_captcha】的数据库操作Service
 * @createDate 2025-09-24 23:49:02
 */
@Service
public class ImageCaptchaService extends ServiceImpl<ImageCaptchaMapper, ImageCaptcha> implements InitializingBean {

    public static final int IMAGE_CAPTCHA_SIZE = 6;

    @Override
    public void afterPropertiesSet() throws Exception {
        List<String> imageCaptcha = getImageCaptcha(true);
        System.out.println(imageCaptcha);
    }


    /**
     * 从数据库随机获取一个验证码
     *
     * @param useConfuse 是否采用混淆验证码
     * @return 一个验证码
     */
    private ImageCaptcha getRandomCaptcha(Boolean useConfuse) {
        LambdaQueryWrapper<ImageCaptcha> lambdaQuery = Wrappers.lambdaQuery();
        if (Objects.nonNull(useConfuse)) {
            lambdaQuery.eq(useConfuse, ImageCaptcha::getConfuse, 1);
        }
        lambdaQuery.last(" ORDER BY RAND() LIMIT 1 ");
        return getOne(lambdaQuery, Boolean.FALSE);
    }


    /**
     * 从数据库随机获取一个不等于excludeId的验证码
     *
     * @param excludeId 不等于excludeId的验证码
     * @return 一个验证码
     */
    private ImageCaptcha getExcludeRandomCaptcha(Long excludeId) {
        LambdaQueryWrapper<ImageCaptcha> lambdaQuery = Wrappers.lambdaQuery();
        lambdaQuery.ne(ImageCaptcha::getId, excludeId);
        lambdaQuery.last(" ORDER BY RAND() LIMIT 1 ");
        return getOne(lambdaQuery, Boolean.FALSE);
    }


    /**
     * 获取验证码
     *
     * @param useConfuse
     * @return
     */
    public List<String> getImageCaptcha(Boolean useConfuse) {
        ImageCaptcha randomCaptcha = getRandomCaptcha(useConfuse);
        List<String> rightImageCaptcha = randomCaptcha.getImage();
        //打乱顺序，防止每次都一样
        Collections.shuffle(rightImageCaptcha);
        int errorImageCaptchaCount = 0;
        List<String> imageCaptchaResult = new ArrayList<>(IMAGE_CAPTCHA_SIZE);
        for (int i = 0; i < IMAGE_CAPTCHA_SIZE; i++) {
            if (RandomUtil.randomBoolean() && !rightImageCaptcha.isEmpty()) {
                //随机位置放置正确的图片
                imageCaptchaResult.add(rightImageCaptcha.remove(rightImageCaptcha.size() - 1));
                System.out.println("--"+ (i+1));
            } else {
                //否则放置错误的图片, 计一下数
                errorImageCaptchaCount++;
                imageCaptchaResult.add(StringUtils.EMPTY);
            }
        }
        //获取错误的验证码
        List<String> errorImageCaptcha = getWrongImageCaptcha(randomCaptcha, errorImageCaptchaCount);
        for (int i = 0; i < imageCaptchaResult.size(); i++) {
            if (StringUtils.isEmpty(imageCaptchaResult.get(i))) {
                imageCaptchaResult.set(i, errorImageCaptcha.remove(0));
            }
        }
        return imageCaptchaResult;
    }


    /**
     * 获取错误的验证码
     *
     * @param randomCaptcha          当前验证码
     * @param errorImageCaptchaCount 需要的错误的验证码个数
     * @return 错误的验证码
     */
    private List<String> getWrongImageCaptcha(ImageCaptcha randomCaptcha, int errorImageCaptchaCount) {
        List<String> errorImageCaptcha = new ArrayList<>();
        if (isNotEmpty(randomCaptcha.getConfuseImg())) {
            errorImageCaptcha.addAll(randomCaptcha.getConfuseImg());
            Collections.shuffle(errorImageCaptcha);
        }
        if (errorImageCaptchaCount > errorImageCaptcha.size()) {
            errorImageCaptcha.addAll(getExcludeRandomCaptcha(randomCaptcha.getId()).getImage());
        }
        return errorImageCaptcha;
    }

}
