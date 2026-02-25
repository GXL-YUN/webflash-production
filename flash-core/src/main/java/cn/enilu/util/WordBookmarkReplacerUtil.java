package cn.enilu.util;

import org.apache.poi.xwpf.usermodel.*;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBookmark;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTP;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTR;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTText;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.*;


/**
 * word书签映射
 */
public class WordBookmarkReplacerUtil {

    /**
     * 稳定可靠的书签替换方法
     */
    public static boolean replaceBookmarks(String filePath, Map<String, String> replacements) {
        try (FileInputStream fis = new FileInputStream(filePath);
             XWPFDocument document = new XWPFDocument(fis)) {

            boolean replaced = false;

            // 先查找所有书签
            List<BookmarkInfo> bookmarkInfos = findAllBookmarks(document);

            // 按段落索引分组
            Map<Integer, List<BookmarkInfo>> bookmarksByParagraph = new HashMap<>();
            for (BookmarkInfo info : bookmarkInfos) {
                bookmarksByParagraph
                        .computeIfAbsent(info.paragraphIndex, k -> new ArrayList<>())
                        .add(info);
            }

            // 对每个段落进行替换
            for (Map.Entry<Integer, List<BookmarkInfo>> entry : bookmarksByParagraph.entrySet()) {
                int paraIndex = entry.getKey();
                List<BookmarkInfo> bookmarks = entry.getValue();

                // 按书签位置从后往前排序，避免索引变化
                bookmarks.sort((a, b) -> Integer.compare(b.position, a.position));

                for (BookmarkInfo bookmark : bookmarks) {
                    if (replacements.containsKey(bookmark.name)) {
                        replaceBookmarkInParagraph(document, paraIndex, bookmark,
                                replacements.get(bookmark.name));
                        replaced = true;
                    }
                }
            }

            if (replaced) {
                try (FileOutputStream fos = new FileOutputStream(filePath)) {
                    document.write(fos);
                }
                return true;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 查找文档中所有书签
     */
    private static List<BookmarkInfo> findAllBookmarks(XWPFDocument document) {
        List<BookmarkInfo> bookmarks = new ArrayList<>();

        int paraIndex = 0;
        for (XWPFParagraph paragraph : document.getParagraphs()) {
            List<CTBookmark> ctBookmarks = paragraph.getCTP().getBookmarkStartList();
            for (int i = 0; i < ctBookmarks.size(); i++) {
                CTBookmark ctBookmark = ctBookmarks.get(i);
                String name = ctBookmark.getName();
                if (name != null && !name.trim().isEmpty()) {
                    bookmarks.add(new BookmarkInfo(name, paraIndex, i));
                }
            }
            paraIndex++;
        }

        return bookmarks;
    }

    /**
     * 在指定段落中替换书签
     */
    private static void replaceBookmarkInParagraph(XWPFDocument document, int paraIndex,
                                                   BookmarkInfo bookmark, String newText) {
        XWPFParagraph paragraph = document.getParagraphs().get(paraIndex);

        // 获取段落的所有CTBookmark
        List<CTBookmark> ctBookmarks = paragraph.getCTP().getBookmarkStartList();

        if (bookmark.position < ctBookmarks.size()) {
            CTBookmark ctBookmark = ctBookmarks.get(bookmark.position);

            // 在书签位置之后插入文本
            XWPFRun newRun = paragraph.createRun();
            newRun.setText(newText);

            // 可选：移除原书签
            // paragraph.getCTP().removeBookmarkStart(bookmark.position);
        }
    }

    /**
     * 书签信息类
     */
    static class BookmarkInfo {
        String name;
        int paragraphIndex;
        int position;  // 在段落中书签列表中的位置

        BookmarkInfo(String name, int paragraphIndex, int position) {
            this.name = name;
            this.paragraphIndex = paragraphIndex;
            this.position = position;
        }
    }

    /**
     * 主方法
     */
    public static void main(String[] args) {
        String filePath = "C:\\Users\\think\\Desktop\\催款通知书.docx";

        Map<String, String> replacements = new HashMap<>();
        replacements.put("name", "张三");
        replacements.put("massage", "gxl");
        replacements.put("date", "2026年1月5日");

        boolean success = replaceBookmarks(filePath, replacements);
        if (success) {
            System.out.println("书签替换成功！");
        } else {
            System.out.println("书签替换失败或未找到匹配的书签。");
        }
    }
}