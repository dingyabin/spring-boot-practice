package com.dingyabin.web.desensization;

import com.dingyabin.web.desensization.enums.DesensitizedTypeEnum;
import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.lang.annotation.*;

/**
 * @author 丁亚宾
 * Date: 2024/11/8.
 * Time:14:28
 */
@JacksonAnnotationsInside
@JsonSerialize(using = DesensitizationJsonSerializer.class)
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
@Documented
@Inherited
public @interface Desensitization {


    DesensitizedTypeEnum value();

}
