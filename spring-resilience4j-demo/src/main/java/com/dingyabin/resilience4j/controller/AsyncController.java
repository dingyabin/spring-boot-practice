package com.dingyabin.resilience4j.controller;


import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.write.metadata.WriteSheet;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RestController
public class AsyncController {

    private final ExecutorService EXECUTOR_SERVICE = Executors.newFixedThreadPool(2);


    @GetMapping(value = "/test")
    public ResponseEntity<ResponseBodyEmitter> ResponseBodyEmitter() {
        ResponseBodyEmitter bodyEmitter = new ResponseBodyEmitter(10000L);
        EXECUTOR_SERVICE.submit(() -> {
            for (int i = 0; i < 10; i++) {
                try {
                    bodyEmitter.send("你好，Hello World_" + i + "\r\n");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                _sleep(600);
            }
            bodyEmitter.complete();
        });
        return ResponseEntity.ok().contentType(MediaType.TEXT_EVENT_STREAM).body(bodyEmitter);
    }


    @GetMapping(value = "/test2")
    public ResponseEntity<StreamingResponseBody> StreamingResponseBody() {
        StreamingResponseBody responseBody = outputStream -> {
            try (FileInputStream fileInputStream = new FileInputStream("E:\\电影\\风声.HD1280高清国语中字版.mp4")) {
                StreamUtils.copy(fileInputStream, outputStream);
            }
        };
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=orders.xlsx");
        return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_OCTET_STREAM).body(responseBody);
    }


    @GetMapping(value = "/test3")
    public ResponseEntity<StreamingResponseBody> StreamingResponseBody2() {
        StreamingResponseBody responseBody = outputStream -> {
            ExcelWriter excelWriter = EasyExcel.write(outputStream).head(ExcelModel.class).build();
            WriteSheet writeSheet = EasyExcel.writerSheet().build();
            for (int i = 0; i < 100; i++) {
                excelWriter.write(dataList(), writeSheet);
                System.out.println("写入数据一次");
                _sleep(100);
            }
            excelWriter.close();
        };
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=orders.xlsx");
        return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_OCTET_STREAM).body(responseBody);
    }


    @SneakyThrows
    private void _sleep(int time) {
        Thread.sleep(time);
    }


    private List<ExcelModel> dataList(){
        List<ExcelModel> objects = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            objects.add(new ExcelModel("THIS IS " + i, new Date(), 1.0D));
        }
        return objects;
    }


    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ExcelModel {

        @ExcelProperty("字符串标题")
        private String str;

        @ExcelProperty("日期标题")
        private Date date;

        @ExcelProperty("数字标题")
        private Double doubleData;

    }

}
