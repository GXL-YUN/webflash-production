package cn.enilu.flash.api.utils.word.util;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.XmlObject;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTP;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTText;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class WordTextBoxEditor {

    public static void main(String[] args) throws IOException {
        // 1. 加载Word文档（使用try-with-resources确保关闭）
        try (XWPFDocument doc = new XWPFDocument(new FileInputStream("input.docx"));
             FileOutputStream out = new FileOutputStream("output.docx")) {

            // 2. 安全处理所有段落
            doc.getParagraphs().forEach(WordTextBoxEditor::safeProcessParagraph);

            // 3. 保存文档
            doc.write(out);
        }
    }

    private static void safeProcessParagraph(XWPFParagraph paragraph) {
        // 创建临时副本处理
        CTP tempP = (CTP) paragraph.getCTP().copy();
        XmlCursor cursor = tempP.newCursor();
        try {
            // 查找所有绘图对象
            cursor.selectPath(
                    "declare namespace w='http://schemas.openxmlformats.org/wordprocessingml/2006/main' " +
                            ".//w:drawing"
            );

            while (cursor.toNextSelection()) {
                safeProcessDrawing(cursor.getObject());
            }

            // 将修改后的内容写回原段落
            paragraph.getCTP().set(tempP);
        } finally {
            cursor.dispose();
        }
    }

    private static void safeProcessDrawing(XmlObject drawing) {
        XmlObject drawingCopy = drawing.copy();
        XmlCursor cursor = drawingCopy.newCursor();
        try {
            // 查找文本框内容
            cursor.selectPath(
                    "declare namespace w='http://schemas.openxmlformats.org/wordprocessingml/2006/main' " +
                            ".//w:txbxContent"
            );

            while (cursor.toNextSelection()) {
                safeReplaceText(cursor.getObject());
            }

            // 将修改后的绘图对象写回原对象
            drawing.set(drawingCopy);
        } finally {
            cursor.dispose();
        }
    }

    private static void safeReplaceText(XmlObject textBoxContent) {
        XmlObject contentCopy = textBoxContent.copy();
        XmlCursor cursor = contentCopy.newCursor();
        try {
            // 直接查找所有文本节点
            cursor.selectPath(".//w:t[contains(text(),'[name')]");

            while (cursor.toNextSelection()) {
                XmlObject textObj = cursor.getObject();
                if (textObj instanceof CTText) {
                    CTText ctText = (CTText) textObj;
                    String text = ctText.getStringValue();
                    ctText.setStringValue(text.replace("{{name}}", "张三"));
                }
            }

            // 将修改后的内容写回原对象
            textBoxContent.set(contentCopy);
        } finally {
            cursor.dispose();
        }
    }
}