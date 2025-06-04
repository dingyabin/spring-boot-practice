package com.dingyabin.web.request;

import org.apache.commons.io.IOUtils;
import org.springframework.util.StreamUtils;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class RepeatReadHttpServletRequest extends HttpServletRequestWrapper {

    private byte[] sourceData;
    private final String contentType;

    public RepeatReadHttpServletRequest(HttpServletRequest request) throws IOException {
        super(request);
        contentType = getHeader("Content-Type") == null ? "TEXT/HTML" : getHeader("Content-Type");
    }

    /**
     * 包装可重复读的request，且可以手动传入contentBody，替换原来的contentBody，可用于对原参数解密
     * @param request 原始的request
     * @param contentBody 替换的contentBody
     * @throws IOException 异常
     */
    public RepeatReadHttpServletRequest(HttpServletRequest request, String contentBody) throws IOException {
        this(request);
        if (contentBody != null) {
            sourceData = contentBody.getBytes(StandardCharsets.UTF_8);
        }
    }

    public String getJsonBody() throws IOException {
        String result = null;
        if (contentType.toUpperCase().contains("APPLICATION/JSON")) {
            result = IOUtils.toString(getReader());
        }
        return result;
    }


    @Override
    public ServletInputStream getInputStream() throws IOException {
        if (sourceData == null) {
            sourceData = StreamUtils.copyToByteArray(getRequest().getInputStream());
        }
        final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(sourceData);

        return new ServletInputStream() {
            @Override
            public int read()  {
                return byteArrayInputStream.read();
            }

            @Override
            public boolean isFinished() {
                return byteArrayInputStream.available() == 0;
            }

            @Override
            public boolean isReady() {
                return false;
            }

            @Override
            public void setReadListener(ReadListener readListener) {

            }
        };
    }

    @Override
    public BufferedReader getReader() throws IOException {
        return new BufferedReader(new InputStreamReader(this.getInputStream(), StandardCharsets.UTF_8));
    }

}
