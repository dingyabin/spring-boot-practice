package com.dingyabin.cache.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author 丁亚宾
 * Date: 2024/8/22.
 * Time:23:47
 */
@Getter
@Setter
@ToString
public class Student {

    private String name;

    private int age;

    public Student(String name, int age) {
        this.name = name;
        this.age = age;
    }
}
