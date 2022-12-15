package com.cc.common.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.*;
import java.nio.charset.StandardCharsets;

public class JsonUtils {
    private static final Logger logger = LoggerFactory.getLogger(JsonUtils.class);

    public static String readJSONString(String filePath) {
        try(BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(filePath), StandardCharsets.UTF_8))) {
            StringBuilder content = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                content.append(line);
            }
            return content.toString();
        } catch (IOException e) {
            logger.error("读取json文件异常",e);
            return "";
        }
    }

    public static void writeJSONString(String filePath, String fileContent) {
        try(BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filePath), StandardCharsets.UTF_8))) {
            bufferedWriter.write(fileContent);
            bufferedWriter.flush();
        } catch (IOException e) {
            logger.error("写入json文件异常",e);
        }
    }

}
