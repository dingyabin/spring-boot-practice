package com.dingyabin.utils;

import com.aliyun.oss.ClientBuilderConfiguration;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.common.auth.DefaultCredentialProvider;
import com.aliyun.oss.common.comm.SignVersion;
import com.aliyun.oss.model.PutObjectRequest;
import com.aliyuncs.utils.StringUtils;
import org.apache.commons.io.FilenameUtils;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class AliYunOssUtil {

    private static final String KEY_ID = "";

    private static final String ACCESS_KEY = "";

    private static final String endpoint = "https://oss-cn-beijing.aliyuncs.com";

    private static final String region = "cn-beijing";

    private static final String buckets = "dyb-tests";

    private static final OSS OSS_CLIENT;

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");


    static {
        ClientBuilderConfiguration configuration = new ClientBuilderConfiguration();
        configuration.setSignatureVersion(SignVersion.V4);
        OSS_CLIENT = OSSClientBuilder.create()
                .endpoint(endpoint)
                .region(region)
                .clientConfiguration(configuration)
                .credentialsProvider(new DefaultCredentialProvider(KEY_ID, ACCESS_KEY))
                .build();

    }

    public static String putObject(String fileName, InputStream inputStream) {
        String key = uuidName();
        String extension = FilenameUtils.getExtension(fileName);
        if (!StringUtils.isEmpty(extension)) {
            key = key + "." + extension;
        }
        PutObjectRequest objectRequest = new PutObjectRequest(buckets, key, inputStream);
        OSS_CLIENT.putObject(objectRequest);
        return generateUrl(key, 1, TimeUnit.DAYS);
    }


    public static String putObject(File file) throws IOException {
        return putObject(file.getName(), Files.newInputStream(file.toPath()));
    }


    public static String generateUrl(String key, long duration, TimeUnit timeUnit) {
        URL url = OSS_CLIENT.generatePresignedUrl(buckets, key, new Date(System.currentTimeMillis() + timeUnit.toMillis(duration)));
        return url.toString();
    }


    private static String uuidName() {
        String date = DATE_TIME_FORMATTER.format(LocalDate.now());
        return date + "/" + UUID.randomUUID().toString().replaceAll("-", "");
    }

}
