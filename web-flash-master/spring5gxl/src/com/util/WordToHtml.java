package com.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
 
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
 
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.converter.PicturesManager;
import org.apache.poi.hwpf.converter.WordToHtmlConverter;
import org.apache.poi.hwpf.usermodel.PictureType;

import org.apache.poi.xwpf.converter.xhtml.XHTMLConverter;
import org.apache.poi.xwpf.converter.xhtml.XHTMLOptions;

import org.junit.Test;
import org.w3c.dom.Document;

/**
 * word 转换成html
 */
public class WordToHtml {

	/**
	 * 2007版本word转换成html
	 * docx转html
	 * @throws IOException
	 */
	
	

	/**
	 * /** 2003版本word转换成html doc转html文件
	 * 
	 * @throws IOException
	 * @throws TransformerException
	 * @throws ParserConfigurationException
	 */
	@Test
	public void Word2003ToHtml() throws IOException, TransformerException, ParserConfigurationException {
		String filepath = "E:/";
		final String imagepath = "E:/test/image/";
		String fileName = "test.doc";
		String htmlName = "test.html";
		final String file = filepath + fileName;
		InputStream input = new FileInputStream(new File(file));
		HWPFDocument wordDocument = new HWPFDocument(input);
		WordToHtmlConverter wordToHtmlConverter = new WordToHtmlConverter(
				DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument());
		// 设置图片存放的位置
		wordToHtmlConverter.setPicturesManager(new PicturesManager() {
			public String savePicture(byte[] content, PictureType pictureType, String suggestedName, float widthInches,
					float heightInches) {
				File imgPath = new File(imagepath);
				if (!imgPath.exists()) {// 图片目录不存在则创建
					imgPath.mkdirs();
				}
				File file = new File(imagepath + suggestedName);
				try {
					OutputStream os = new FileOutputStream(file);
					os.write(content);
					os.close();
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				return imagepath + suggestedName;
			}
		});
		// 解析word文档
		wordToHtmlConverter.processDocument(wordDocument);
		Document htmlDocument = wordToHtmlConverter.getDocument();

		File htmlFile = new File(filepath + htmlName);
		OutputStream outStream = new FileOutputStream(htmlFile);

		// 也可以使用字符数组流获取解析的内容
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		// OutputStream outStream = new BufferedOutputStream(baos);

		DOMSource domSource = new DOMSource(htmlDocument);
		StreamResult streamResult = new StreamResult(outStream);

		TransformerFactory factory = TransformerFactory.newInstance();
		Transformer serializer = factory.newTransformer();
		serializer.setOutputProperty(OutputKeys.ENCODING, "utf-8");
		serializer.setOutputProperty(OutputKeys.INDENT, "yes");
		serializer.setOutputProperty(OutputKeys.METHOD, "html");

		serializer.transform(domSource, streamResult);

		// 也可以使用字符数组流获取解析的内容 //
		String content = baos.toString();
//        System.out.println(content);
//        baos.close();
		outStream.close();
	}
	
	
	   
	  /**
	   * 将word2003转换为html文件 2017-2-27 
	   * @param wordPath word文件路径
	   * @param wordName word文件名称无后缀
	   * @param suffix  word文件后缀
	   * @throws IOException
	   * @throws TransformerException
	   * @throws ParserConfigurationException
	   */
	  public String Word2003ToHtml(String wordPath,String wordName,String suffix) throws IOException, TransformerException, ParserConfigurationException {
	    String htmlPath = wordPath + File.separator + wordName + "_show" + File.separator;
	    String htmlName = wordName + ".html";
	    final String imagePath = htmlPath + "image" + File.separator;
	     
	    //判断html文件是否存在
	    File htmlFile = new File(htmlPath + htmlName);
	    if(htmlFile.exists()){ 
	      return htmlFile.getAbsolutePath();
	    }
	     
	    //原word文档
	    final String file = wordPath + File.separator + wordName + suffix;
	    InputStream input = new FileInputStream(new File(file));
	     
	    HWPFDocument wordDocument = new HWPFDocument(input);
	    WordToHtmlConverter wordToHtmlConverter = new WordToHtmlConverter(DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument());
	    //设置图片存放的位置
	    wordToHtmlConverter.setPicturesManager(new PicturesManager() {
	      public String savePicture(byte[] content, PictureType pictureType, String suggestedName, float widthInches, float heightInches) {
	        File imgPath = new File(imagePath);
	        if(!imgPath.exists()){//图片目录不存在则创建
	          imgPath.mkdirs();
	        }
	        File file = new File(imagePath + suggestedName);
	        try {
	          OutputStream os = new FileOutputStream(file);
	          os.write(content);
	          os.close();
	        } catch (FileNotFoundException e) {
	          e.printStackTrace();
	        } catch (IOException e) {
	          e.printStackTrace();
	        }
	        //图片在html文件上的路径 相对路径
	        return "image/" + suggestedName;
	      }
	    });
	     
	    //解析word文档
	    wordToHtmlConverter.processDocument(wordDocument);
	    Document htmlDocument = wordToHtmlConverter.getDocument();
	     
	    //生成html文件上级文件夹
	    File folder = new File(htmlPath);
	    if(!folder.exists()){ 
	      folder.mkdirs(); 
	    }
	     
	    //生成html文件地址
	    OutputStream outStream = new FileOutputStream(htmlFile);
	 
	    DOMSource domSource = new DOMSource(htmlDocument);
	    StreamResult streamResult = new StreamResult(outStream);
	 
	    TransformerFactory factory = TransformerFactory.newInstance();
	    Transformer serializer = factory.newTransformer();
	    serializer.setOutputProperty(OutputKeys.ENCODING, "utf-8");
	    serializer.setOutputProperty(OutputKeys.INDENT, "yes");
	    serializer.setOutputProperty(OutputKeys.METHOD, "html");
	     
	    serializer.transform(domSource, streamResult);
	 
	    outStream.close();
	     
	    return htmlFile.getAbsolutePath();
	  }
	   

}