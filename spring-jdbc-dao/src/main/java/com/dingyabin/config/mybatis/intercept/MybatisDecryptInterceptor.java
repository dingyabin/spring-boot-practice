package com.dingyabin.config.mybatis.intercept;

import com.dingyabin.config.mybatis.MybatisMetaHelper;
import com.dingyabin.config.mybatis.MybatisPlusEncryptProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.resultset.ResultSetHandler;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;

import javax.annotation.Resource;
import java.sql.Statement;

/**
 * 出参解密拦截器
 *
 * @author 老马
 * @version 4.6.0
 */
@Slf4j
@Intercepts({@Signature(
        type = ResultSetHandler.class,
        method = "handleResultSets",
        args = {Statement.class})
})
public class MybatisDecryptInterceptor implements Interceptor {

    @Resource
    private MybatisPlusEncryptProperties mybatisPlusEncryptProperties;


    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        // 获取执行mysql执行结果
        Object result = invocation.proceed();
        if (result != null) {
            MybatisMetaHelper.tryDealObject(result, object -> MybatisMetaHelper.decryptObject(object, mybatisPlusEncryptProperties.getEncryptKey()));
        }
        return result;
    }

}
