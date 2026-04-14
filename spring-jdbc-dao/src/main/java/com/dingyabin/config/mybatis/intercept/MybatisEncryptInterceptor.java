package com.dingyabin.config.mybatis.intercept;

import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.ObjectUtil;
import com.dingyabin.config.mybatis.MybatisMetaHelper;
import com.dingyabin.config.mybatis.MybatisPlusEncryptProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;

import javax.annotation.Resource;
import java.sql.PreparedStatement;

/**
 * 入参加密拦截器
 *
 * @author 老马
 * @version 4.6.0
 */
@Slf4j
@Intercepts({@Signature(
        type = ParameterHandler.class,
        method = "setParameters",
        args = {PreparedStatement.class})
})
public class MybatisEncryptInterceptor implements Interceptor {

    @Resource
    private MybatisPlusEncryptProperties mybatisPlusEncryptProperties;


    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        Object target = invocation.getTarget();

        Object parameterObject = null;
        if (target instanceof ParameterHandler) {
            parameterObject = ((ParameterHandler) target).getParameterObject();
        }

        //先加密入参
        if (ObjectUtil.isNotNull(parameterObject) && !ClassUtil.isSimpleValueType(parameterObject.getClass())) {
            MybatisMetaHelper.tryDealObject(parameterObject, object -> MybatisMetaHelper.encryptObject(object, mybatisPlusEncryptProperties.getEncryptKey()));
        }

        //执行后续操作
        Object proceed = invocation.proceed();

        //再解密入参
        if (ObjectUtil.isNotNull(parameterObject) && !ClassUtil.isSimpleValueType(parameterObject.getClass())) {
            MybatisMetaHelper.tryDealObject(parameterObject, object -> MybatisMetaHelper.decryptObject(object, mybatisPlusEncryptProperties.getEncryptKey()));
        }

        return proceed;
    }

}
