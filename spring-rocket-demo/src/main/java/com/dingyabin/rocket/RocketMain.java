package com.dingyabin.rocket;

import com.dingyabin.rocket.helper.RocketSenderHelper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RocketMain {

    public static void main(String[] args) {
        SpringApplication.run(RocketMain.class, args);

        RocketSenderHelper.send("TEST_TOPIC", "xxxxxxxx耐闹");
    }
}