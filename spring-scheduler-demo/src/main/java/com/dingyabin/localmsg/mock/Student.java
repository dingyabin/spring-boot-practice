package com.dingyabin.localmsg.mock;

import lombok.Data;

import java.util.Arrays;
import java.util.List;

@Data
public class Student {

    private Integer age;

    private List<String> hobby;

    private String name;

    public Student() {
        setAge(10);
        setName("Yab");
        setHobby(Arrays.asList("Run", "Drink"));
    }
}
