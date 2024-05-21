package cn.enilu.util;

import java.io.*;

import com.aspose.words.License;
import com.aspose.words.SaveFormat;
import com.lowagie.text.Document;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfStream;
import com.lowagie.text.pdf.PdfWriter;

import java.util.ArrayList;
import java.util.List;
import java.io.File;
import java.io.FileOutputStream;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.aspectj.weaver.ast.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


// Define the class
public class PdfUtil {

    public static boolean getLicense() {
        boolean result = false;
        try {
            InputStream is = Test.class.getClassLoader().getResourceAsStream("\\listent\\license.xml"); // license.xml应放在..\WebRoot\WEB-INF\classes路径下
            License aposeLic = new License();
            aposeLic.setLicense(is);
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static boolean doc2pdf(String inPath, String outPath) {
        if (!getLicense()) { // 验证License 若不验证则转化出的pdf文档会有水印产生
            return false;
        }
        FileOutputStream os = null;
        try {
            long old = System.currentTimeMillis();
            File file = new File(outPath); // 新建一个空白pdf文档
            os = new FileOutputStream(file);
            com.aspose.words.Document doc = new com.aspose.words.Document(inPath); // Address是将要被转化的word文档
            doc.save(os, SaveFormat.PDF);// 全面支持DOC, DOCX, OOXML, RTF HTML, OpenDocument, PDF,
            // EPUB, XPS, SWF 相互转换
            long now = System.currentTimeMillis();
            System.out.println("pdf转换成功，共耗时：" + ((now - old) / 1000.0) + "秒"); // 转化用时
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            if (os != null) {
                try {
                    os.flush();
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return true;
    }


    /**
     * 获取文件类型
     * 通过路径 进行
     * 判断文件是那种类型
     *
     * @param Path
     */
    public static String getType(String Path) {
        try {
            InputStream inputStream = new FileInputStream(Path);
            byte[] header = new byte[8];
            inputStream.read(header, 0, 8);
            String fileHex = bytesToHex(header);
            String fileType = getFileType(fileHex);
            return fileType;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static String bytesToHex(byte[] bytes) {
        StringBuilder hex = new StringBuilder();
        for (byte b : bytes) {
            hex.append(String.format("%02x", b));
        }
        return hex.toString();
    }

    private static String getFileType(String fileHex) {
        switch (fileHex) {
            case "255044462d312e": // PDF
                return "PDF";
            case "504b0304": // DOCX
                return "DOCX";
            case "504b030414000600": // XLSX, PPTX
                return "Office Open XML";
            case "d0cf11e0a1b11ae1": // DOC, XLS, PPT
                return "DOC";
            case "7b5c727466": // RTF
                return "Rich Text Format";
            case "3c3f786d6c": // XML
                return "XML";
            case "68746d6c3e": // HTML
                return "HTML";
            case "44656c69766572792d646174653a": // EML
                return "Email Message";
            default:
                return "Unrecognized file type";
        }
    }

    //doc转为pdf
    // Define the method for converting a document to PDF
    public static void convertToPDFFile(String docPath, String pdfPath) {
        try {
            // Create a new Document object
            Document document = new Document();
            // Create a new PdfWriter object
            PdfWriter.getInstance(document, new FileOutputStream(pdfPath));
            // Open the Document
            document.open();
            // Read the contents of the document
            FileInputStream fis = new FileInputStream(new File(docPath));
            int n = 0;
            byte[] buffer = new byte[1024];
            // Write the contents of the document to the PDF
            while ((n = fis.read(buffer)) != -1) {
                document.add(new Paragraph(new String(buffer, 0, n)));
            }
            // Close the input stream
            fis.close();
            // Close the Document
            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @param
     * @param docFile doc文件流
     */
    public static void convertToPDF(FileInputStream docFile) {
        try {

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static final Logger logger = LoggerFactory.getLogger(FileUtils.class);

    /**
     * copy文件
     */
    public static void rwFile(InputStream in) {
        FileWriter fw = null;
        BufferedReader br = null;
        try {
            fw = new FileWriter("A:\\text.txt", true);
            br = new BufferedReader(new InputStreamReader(in, "utf-8"));
            String line = null;
            while ((line = br.readLine()) != null) {
                logger.info("文件内容.line={}" + line);
                fw.write(line);
                fw.flush();
            }
            br.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fw != null) {
                try {
                    fw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 流转成PDF
     */
    public static void getPDF(InputStream in) {
        try {
            FileOutputStream fo = new FileOutputStream("D:\\fx.pdf");
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            for (int i; (i = in.read()) != -1; ) {
                baos.write(i);
            }
            baos.flush();
            Document doc = new Document();
            PdfStream pdfStream = new PdfStream(baos.toByteArray());
            PdfWriter pw = PdfWriter.getInstance(doc, fo);
            pdfStream.toPdf(pw, fo);
            logger.info("doc内容.doc={}", doc.newPage());
            try {
                pw.flush();
                baos.close();
                pw.close();
                fo.close();
                in.close();
            } catch (Exception e) {

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 流编码
     */
    public static byte[] encrypt(InputStream in) throws Exception {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024 * 1024];
        int r;
        while ((r = in.read(buffer)) > 0) {
            bos.write(buffer, 0, r);
            bos.flush();
        }
        byte[] outBytes = bos.toByteArray();
        in.close();
        bos.close();
        return new Base64().encode(outBytes);
    }

    /**
     * 流解码
     */
    public static byte[] decrypt(InputStream in) throws Exception {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int r;
        while ((r = in.read(buffer)) > 0) {
            bos.write(buffer, 0, r);
            bos.flush();
        }
        byte[] outBytes = bos.toByteArray();
        in.close();
        bos.close();
        return new Base64().encode(outBytes);
    }


    //doc 文档比对
    // Define the method for comparing two documents
    public static List<String> compareDocuments(String docPath1, String docPath2) {
        List<String> differences = new ArrayList<String>();
        try {
            // Read the contents of the first document
            BufferedReader reader1 = new BufferedReader(new FileReader(docPath1));
            String line1 = reader1.readLine();
            // Read the contents of the second document
            BufferedReader reader2 = new BufferedReader(new FileReader(docPath2));
            String line2 = reader2.readLine();
            // Compare the contents of the two documents line by line
            while (line1 != null || line2 != null) {
                if (line1 == null || !line1.equals(line2)) {
                    differences.add(line2);
                }
                if (line2 == null || !line2.equals(line1)) {
                    differences.add(line1);
                }
                line1 = reader1.readLine();
                line2 = reader2.readLine();
            }
            // Close the readers
            reader1.close();
            reader2.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return differences;
    }

    //集合对象去重
    // Define a method for removing duplicates from a List
    public static <T> List<T> removeDuplicates(List<T> list) {
        // Create a new ArrayList to hold the unique elements
        List<T> uniqueList = new ArrayList<T>();
        // Loop through the original list
        for (T element : list) {
            // If the uniqueList does not already contain the element, add it
            if (!uniqueList.contains(element)) {
                uniqueList.add(element);
            }
        }
        // Return the uniqueList
        return uniqueList;
    }
}
