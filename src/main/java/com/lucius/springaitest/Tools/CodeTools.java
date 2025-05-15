package com.lucius.springaitest.Tools;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

@Component
public class CodeTools {
    @Tool(description = "创建用户要求创建的文件,并且把生成的代码写入文件")
    public static boolean writeFile(@ToolParam(description = "用户要求创建的文件的名称")String fileName, @ToolParam(description = "你生成的代码的内容")String content) {
        String directoryPath="C:\\Users\\86180\\Desktop\\新建文件夹 (2)";
        // 创建目录对象
        File directory = new File(directoryPath);

        // 如果目录不存在，则尝试创建它
        if (!directory.exists()) {
            if (!directory.mkdirs()) {
                System.out.println("无法创建目录: " + directoryPath);
                return false;
            }
        }

        // 创建文件对象
        File file = new File(directory, fileName);

        try (FileWriter fileWriter = new FileWriter(file);
             BufferedWriter writer = new BufferedWriter(fileWriter)) {

            // 写入内容
            writer.write(content);
            System.out.println("文件写入成功: " + file.getAbsolutePath());
            return true;

        } catch (IOException e) {
            System.err.println("写入文件时发生错误: ");
            return false;
        }
    }
}
