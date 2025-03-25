package com.dingyabin.localmsg.model.common;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class InvokeContext {

    private String className;

    private String methodName;

    private List<String> parameterTypes;

    private List<String> methodArgs;


}
