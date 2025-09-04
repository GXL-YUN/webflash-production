package com.alanlee.service.impl.ehs.util;
import org.apache.poi.xwpf.usermodel.*;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.*;

import java.io.*;
import java.util.*;
import java.util.regex.*;
/**
 * 对于模版文件操作
 */
public class WordTemplateReplacer {

    /**
     * 替换Word模板中的占位符
     * @param templatePath 模板文件路径
     * @param outputPath 输出文件路径
     * @param replacements 替换键值对
     */
    public static void replaceTemplate(String templatePath, String outputPath,
                                       Map<String, String> replacements) throws IOException {
        try (FileInputStream fis = new FileInputStream(templatePath);
             XWPFDocument doc = new XWPFDocument(fis)) {

            // 替换段落中的文本
            for (XWPFParagraph paragraph : doc.getParagraphs()) {
                replaceInParagraph(paragraph, replacements);
            }

            // 替换表格中的文本
            for (XWPFTable table : doc.getTables()) {
                for (XWPFTableRow row : table.getRows()) {
                    for (XWPFTableCell cell : row.getTableCells()) {
                        for (XWPFParagraph paragraph : cell.getParagraphs()) {
                            replaceInParagraph(paragraph, replacements);
                        }
                    }
                }
            }

            // 保存替换后的文档
            try (FileOutputStream fos = new FileOutputStream(outputPath)) {
                doc.write(fos);
            }
        }
    }

    private static void replaceInParagraph(XWPFParagraph paragraph, Map<String, String> replacements) {
        String text = paragraph.getText();
        if (text == null || text.isEmpty()) {
            return;
        }

        // 检查段落中是否包含任何占位符
        boolean containsPlaceholder = replacements.keySet().stream()
                .anyMatch(text::contains);

        if (!containsPlaceholder) {
            return;
        }

        // 获取段落的所有文本运行
        List<XWPFRun> runs = paragraph.getRuns();
        if (runs == null || runs.isEmpty()) {
            return;
        }

        // 合并所有运行的文本
        StringBuilder sb = new StringBuilder();
        for (XWPFRun run : runs) {
            String runText = run.getText(0);
            if (runText != null) {
                sb.append(runText);
            }
        }
        String paragraphText = sb.toString();

        // 执行替换
        for (Map.Entry<String, String> entry : replacements.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            if (paragraphText.contains(key)) {
                paragraphText = paragraphText.replace(key, value);
            }
        }

        // 清除原有运行
        for (int i = runs.size() - 1; i >= 0; i--) {
            paragraph.removeRun(i);
        }

        // 添加新的运行
        XWPFRun newRun = paragraph.createRun();
        newRun.setText(paragraphText);

        // 保留原始格式
        if (!runs.isEmpty()) {
            XWPFRun firstRun = runs.get(0);
            newRun.setFontFamily(firstRun.getFontFamily());
            newRun.setFontSize(firstRun.getFontSize());
            newRun.setBold(firstRun.isBold());
            newRun.setItalic(firstRun.isItalic());
            newRun.setColor(firstRun.getColor());
            // 可以添加更多格式属性...
        }
    }

    public static void main(String[] args) {
        // 示例用法
        try {
            // 准备替换数据
            Map<String, String> replacements = new HashMap<>();
            replacements.put("${name}", "张三");
            replacements.put("${date}", "2023-11-15");
          //  replacements.put("${company}", "示例公司");

            // 执行替换
            replaceTemplate("C:\\Users\\think\\Documents\\WeChat Files\\wxid_wc5abg3o0ikt22\\FileStorage\\File\\2025-07\\00华创需规合订版 v7.0\\00华创需规合订版 v7.0\\01安全员模块附件（9个）\\附件8：优秀安全员证书样式.docx", "D:\\项目文件.output.docx", replacements);

            System.out.println("Word模板替换完成！");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}