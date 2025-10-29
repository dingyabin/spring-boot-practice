package com.dingyabin.captcha.pojo;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class KaptchaWord {

    private String word;

    private PointLocation pointLocation;

}
