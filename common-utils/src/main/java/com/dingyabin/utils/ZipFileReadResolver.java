package com.dingyabin.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipFile;
import org.apache.commons.lang3.tuple.Pair;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.Enumeration;
import java.util.List;
import java.util.function.BiConsumer;


/**
 * @author 丁亚宾
 * Date: 2020/11/24.
 * Time:2:58
 */
@Slf4j
public class ZipFileReadResolver {


    /**
     * 读取原始文件
     *
     * @param path 文件
     * @return 读取出的原始文件信息
     */
    public static Pair<ZipFile, List<ZipArchiveEntry>> parseZip(String path) {
        List<ZipArchiveEntry> zipEntryList = null;
        ZipFile zf = null;
        try {
            zipEntryList = new ArrayList<>();
            ZipFile.Builder builder = new ZipFile.Builder();
            zf = builder.setSeekableByteChannel(
                            Files.newByteChannel(Paths.get(path), EnumSet.of(StandardOpenOption.READ))
                    )
                    .setIgnoreLocalFileHeader(false)
                    .setUseUnicodeExtraFields(true)
                    .setCharset(StandardCharsets.UTF_8)
                    .get();
            Enumeration<ZipArchiveEntry> entries = zf.getEntries();
            while (entries.hasMoreElements()) {
                ZipArchiveEntry zipEntry = entries.nextElement();
                //MAC系统自带的隐藏文件，不需要
                if (zipEntry.getName().startsWith("__MACOSX")) {
                    continue;
                }
                zipEntryList.add(zipEntry);
            }
        } catch (Exception e) {
            log.error("readZip error,path={}", path, e);
        }
        return Pair.of(zf, zipEntryList);
    }


    public static void parseZip(String path, BiConsumer<ZipFile, ZipFileEntry> consumer) {
        Pair<ZipFile, List<ZipArchiveEntry>> zipFileListPair = parseZip(path);
        ZipFile zipFile = zipFileListPair.getLeft();
        List<ZipArchiveEntry> entries = zipFileListPair.getRight();
        if (zipFile == null || entries == null) {
            return;
        }
        for (ZipArchiveEntry zipArchiveEntry : entries) {
            ZipFileEntry zipFileEntry = new ZipFileEntry(zipArchiveEntry.getName(), zipArchiveEntry);
            consumer.accept(zipFile, zipFileEntry);
        }
    }



    @Getter
    @Setter
    @AllArgsConstructor
    public static class ZipFileEntry {
        private String name;

        private ZipArchiveEntry zipArchiveEntry;

    }


}
