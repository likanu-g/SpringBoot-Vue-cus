package com.cc.common.utils;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class JsonUtils {

    public static String readJSONString(String filePath) {
        try(BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(filePath), StandardCharsets.UTF_8))) {
            StringBuilder content = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                content.append(line);
            }
            return content.toString();
        } catch (IOException e) {
            return "";
        }
    }

    public static void writeJSONString(String filePath, String fileContent) {
        try(BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filePath), StandardCharsets.UTF_8))) {
            bufferedWriter.write(fileContent);
            bufferedWriter.flush();
        } catch (IOException e) {

        }
    }

}
