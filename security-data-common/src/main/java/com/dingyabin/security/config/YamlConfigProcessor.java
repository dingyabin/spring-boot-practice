package com.dingyabin.security.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.boot.env.YamlPropertySourceLoader;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import java.io.IOException;
import java.util.List;

/**
 * 需要在spring.factories里配置
 * org.springframework.boot.env.EnvironmentPostProcessor =\
 * com.dingyabin.security.config.YamlConfigProcessor
 * @author 丁亚宾
 * Date: 2024/8/1.
 * Time:23:16
 */
public class YamlConfigProcessor implements EnvironmentPostProcessor {


    private final YamlPropertySourceLoader loader = new YamlPropertySourceLoader();

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        MutablePropertySources propertySources = environment.getPropertySources();
        this.loadProperty(propertySources);
    }

    private void loadProperty(MutablePropertySources propertySources) {
        Resource resource = new ClassPathResource("data-config.yml");
        try {
            List<PropertySource<?>> load = loader.load(resource.getFilename(), resource);
            load.forEach(propertySources::addLast);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}
