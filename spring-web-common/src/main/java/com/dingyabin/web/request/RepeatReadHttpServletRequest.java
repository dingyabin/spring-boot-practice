package com.dingyabin.web.request;

import org.apache.commons.io.IOUtils;

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
            sourceData = IOUtils.toByteArray(getRequest().getInputStream());
        }
        final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(sourceData);

        return new ServletInputStream() {
            public int read() throws IOException {
                return byteArrayInputStream.read();
            }

            @Override
            public boolean isFinished() {
                return false;
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
