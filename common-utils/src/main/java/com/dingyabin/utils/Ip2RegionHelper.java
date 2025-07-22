package com.dingyabin.utils;

import com.google.common.base.Splitter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.lionsoul.ip2region.xdb.Searcher;

import java.io.InputStream;
import java.util.List;
import java.util.Objects;

@Slf4j
public class Ip2RegionHelper {

    private static Searcher SEARCHER;

    private static final Splitter SPLITTER = Splitter.on("|");

    static {
        /*
         * 用整个 xdb 数据缓存创建的查询对象可以安全的用于并发，也就是你可以把这个 searcher 对象做成全局对象去跨线程访问
         */
        try (InputStream inputStream = Ip2RegionHelper.class.getResourceAsStream("/ip2region.xdb")) {
            if (Objects.nonNull(inputStream)) {
                SEARCHER = Searcher.newWithBuffer(IOUtils.toByteArray(inputStream));
            }
            if (Objects.nonNull(SEARCHER)) {
                Runtime.getRuntime().addShutdownHook(new Thread(Ip2RegionHelper::close));
            }
        } catch (Exception e) {
            log.error("加载ip2region.xdb出错...", e);
        }
    }


    /**
     * 关闭文件
     */
    public static void close() {
        if (Objects.nonNull(SEARCHER)) {
            try {
                SEARCHER.close();
            } catch (Exception e) {
                log.error("关闭ip2region出错...", e);
            }
        }
    }


    public static String ip2Region(String ip) {
        try {
            return SEARCHER.search(ip);
        } catch (Exception e) {
            log.error("ip2region.searcher报错...", e);
        }
        return null;
    }


    public static IpMeta ip2RegionMeta(String ip) {
        String region = ip2Region(ip);
        if (StringUtils.isEmpty(region)) {
            return null;
        }
        return IpMeta.instance(region);
    }


    @Data
    @AllArgsConstructor
    public static class IpMeta {

        private String country;

        private String region;

        private String province;

        private String city;

        private String isp;


        public static IpMeta instance(String ipRegion) {
            List<String> regionMeta = SPLITTER.splitToList(ipRegion);
            return new IpMeta(regionMeta.get(0), regionMeta.get(1), regionMeta.get(2), regionMeta.get(3), regionMeta.get(4));
        }
    }

}


