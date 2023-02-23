package com.util;


import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;
import com.sun.istack.internal.logging.Logger;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.ImageOutputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.util.Iterator;
/**
 * 此工具类包含了读取图片流、加密解密base64字符、图片压缩以及截取图片大小的图片处理工具，此工具类并未限制图片类型，此处可还需要优化。
 * @author Lenovo
 *
 */
public class ImageUtils {

    public static String PREFIX_HTTP = "http:";

    public static String PREFIX_HTTPS = "https:";

    public static int BUFFER_SIZE = 1024;

    private static final Logger logger = Logger.getLogger(ImageUtils.class);
    /**
     * 根据图片路径读取该图片的IO流
     *
     * @param imageUrl 图片地址
     * @return 图片IO流
     */
    public static InputStream readImage(String imageUrl) {

        if (isEmpty(imageUrl)) {

            return null;
        }
        try {
            if (imageUrl.startsWith(PREFIX_HTTP) || imageUrl.startsWith(PREFIX_HTTPS)) {

                //读取网络图片
                URL urls = new URL(imageUrl);

                return urls.openConnection().getInputStream();

            } else {
                //读取本地图片
                return new FileInputStream(new File(imageUrl));
            }
        } catch (IOException var) {
            throw new IllegalArgumentException("read image inputStream error :", var);
        }

    }


    /**
     * 保存图片到本地地址
     *
     * @param fileUrl  图片地址
     * @param writeUrl 保存图片地址
     * @return true:成功 false:失败
     */
    public static boolean writeImage(String fileUrl, String writeUrl) {

        //读取IO流
        InputStream input = readImage(fileUrl);

        if (input == null || isEmpty(writeUrl)) {
            return false;
        }

        return transformWrite(input, getOutputStream(writeUrl));
    }

    /**
     * 保存图片到本地地址
     *
     * @param bytes    数组
     * @param writeUrl 保存图片地址
     * @return
     */
    public static boolean writeImage(byte[] bytes, String writeUrl) {

        InputStream inputStream = imageByteArray(bytes);

        if (inputStream == null || isEmpty(writeUrl)) {
            return false;
        }

        return transformWrite(inputStream, getOutputStream(writeUrl));

    }

    /**
     * 根据图片路径转换成字节数组
     *
     * @param fileUrl 图片路径
     * @return
     */
    public static byte[] bytesImage(String fileUrl) {

        InputStream input = readImage(fileUrl);

        if (input == null) {

            return null;
        }
        ByteArrayOutputStream out = null;
        try {
            out = new ByteArrayOutputStream();

            byte[] buf = new byte[BUFFER_SIZE];

            int len = 0;

            while ((len = input.read(buf)) != -1) {
                out.write(buf, 0, len);
            }

            return out.toByteArray();

        } catch (IOException e) {
            throw new IllegalArgumentException("convert image to byte array error:", e);
        } finally {
            close(input, out);
        }

    }

    /**
     * 将IO流转换成byte数组
     *
     * @param input
     * @return
     */
    public static byte[] bytesImage(InputStream input) {

        if (input == null) {

            return null;
        }

        try {
            byte[] data = new byte[input.available()];
            input.read(data);
            return data;
        } catch (IOException e) {
            throw new IllegalArgumentException("convert image to byte array error:", e);
        } finally {
            close(input, null);
        }

    }


    /**
     * 将byte array 转成IO流
     *
     * @param bytes
     * @return
     */
    public static InputStream imageByteArray(byte[] bytes) {

        if (bytes == null || bytes.length == 0) {

            return null;
        }

        return new ByteArrayInputStream(bytes);
    }


    /**
     * 将input保存到out中
     *
     * @param input
     * @param out
     * @return
     */
    public static boolean transformWrite(InputStream input, OutputStream out) {

        try {
            //缓冲区
            byte[] buf = new byte[BUFFER_SIZE];

            int len = 0;

            while ((len = input.read(buf)) != -1) {

                out.write(buf, 0, len);
            }
            //刷到硬盘中
            out.flush();

            return true;
        } catch (IOException e) {

            throw new IllegalArgumentException("write image error:", e);

        } finally {
            close(input, out);
        }
    }

    /**
     * 根据路径生成输出流
     *
     * @param path
     * @return
     */
    public static OutputStream getOutputStream(String path) {

        if (isEmpty(path)) {
            throw new IllegalArgumentException("file path cannot null error");
        }

        try {
            return new FileOutputStream(path);
        } catch (FileNotFoundException e) {
            throw new IllegalArgumentException("file not found error:", e);
        }
    }

    /**
     * 关闭IO流
     *
     * @param input
     * @param out
     */
    private static void close(InputStream input, OutputStream out) {

        if (out != null) {
            try {
                out.close();
            } catch (IOException e) {
                throw new IllegalArgumentException("close OutputStream ERROR:", e);
            }
        }
        if (input != null) {
            try {
                input.close();
            } catch (IOException e) {
                throw new IllegalArgumentException("close InputStream ERROR:", e);
            }
        }
    }

    /**
     * 关闭io流
     *
     * @param var1
     * @param var2
     */
    private static void close(OutputStream var1, ImageOutputStream var2) {

        if (var1 != null) {
            try {
                var1.close();
            } catch (IOException e) {
                throw new IllegalArgumentException("close OutputStream ERROR:", e);
            }
        }
        if (var2 != null) {
            try {
                var2.close();
            } catch (IOException e) {
                throw new IllegalArgumentException("close OutputStream ERROR:", e);
            }
        }
    }

    /**
     * 将IO转换成base64的字符串
     *
     * @param inputStream
     * @return
     */
    public static String imageBase64(InputStream inputStream) {

        if (inputStream == null) {

            return null;
        }

        //将数据转换成byte数组
        byte[] bytes = bytesImage(inputStream);
        // 加密
        BASE64Encoder encoder = new BASE64Encoder();

        return encoder.encode(bytes);
    }

    /**
     * 将图片地址转换成base64的字符串
     *
     * @param fileUrl
     * @return
     */
    public static String imageBase64(String fileUrl) {

        InputStream inputStream = readImage(fileUrl);

        if (inputStream == null) {

            return null;
        }

        return imageBase64(inputStream);
    }

    /**
     * 将base64图片字符串转换成数组
     *
     * @param base64Image
     * @return
     */
    public static byte[] base64ByteArray(String base64Image) {

        if (isEmpty(base64Image)) {
            return null;
        }
        BASE64Decoder decoder = new BASE64Decoder();

        try {

            return decoder.decodeBuffer(base64Image);
        } catch (IOException e) {
            throw new IllegalArgumentException("convert base64 Image to byte array error:", e);
        }

    }

    /**
     * 将base64图片字符串转换成IO流
     *
     * @param base64Image
     * @return
     */
    public static InputStream base64Stream(String base64Image) {

        byte[] bytes = base64ByteArray(base64Image);

        return new ByteArrayInputStream(bytes);
    }


    /**
     * 判断字符串是否为空
     *
     * @param var
     * @return
     */
    public static boolean isEmpty(String var) {

        return var == null || var.trim().length() == 0;
    }


    /**
     * 压缩图片处理
     *
     * @param imageUrl 图片地址
     * @param width    图片宽
     * @param height   图片高
     * @param rate     压缩比例
     * @return
     */
    public static InputStream compressImage(String imageUrl, Integer width, Integer height, Float rate) {

        InputStream inputStream = readImage(imageUrl);

        if (inputStream == null) {
            return null;
        }
        ByteArrayOutputStream bs = null;

        ImageOutputStream imOut = null;

        try {
            //处理图片生成新的BufferedImage
            BufferedImage tag = handleImage(inputStream, width, height, rate);

            bs = new ByteArrayOutputStream();

            imOut = ImageIO.createImageOutputStream(bs);

            ImageIO.write(tag, "png", imOut);

            return new ByteArrayInputStream(bs.toByteArray());

        } catch (IOException e) {
            throw new IllegalArgumentException("compress image error:", e);
        } finally {
            close(bs, imOut);
        }
    }

    /**
     * 压缩图片处理
     *
     * @param imageUrl 图片地址
     * @param saveUrl  保存图片地址
     * @param width    压缩的图片宽
     * @param height   压缩的图片高
     * @param rate     压缩比例
     * @return
     */
    public static boolean compressImage(String imageUrl, String saveUrl, Integer width, Integer height, Float rate) {

        if (isEmpty(imageUrl) || isEmpty(saveUrl)) {

            return false;
        }

        //读取io流
        InputStream inputStream = readImage(imageUrl);

        OutputStream out = null;

        try {
            //处理图片生成新的BufferedImage
            BufferedImage tag = handleImage(inputStream, width, height, rate);

            if (tag == null) {
                return false;
            }

            //创建文件输出流
            out = new FileOutputStream(saveUrl);
            //将图片按JPEG压缩，保存到out中
            JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
            encoder.encode(tag);

        } catch (IOException e) {
            throw new IllegalArgumentException("compress image error:", e);
        } finally {
            close(inputStream, out);
        }


        return true;
    }

    /**
     * 处理图片生成新的BufferedImage
     *
     * @param inputStream
     * @param width
     * @param height
     * @param rate
     * @return
     * @throws IOException
     */
    public static BufferedImage handleImage(InputStream inputStream, Integer width, Integer height, Float rate) throws IOException {

        //转换流成图片流
        BufferedImage bufferedImage = ImageIO.read(inputStream);

        //原图片高
        int srcHeight = bufferedImage.getHeight();
        //原图片宽
        int srcWidth = bufferedImage.getWidth();

        System.out.println("原图片的高：" + srcHeight + ";宽：" + srcWidth);

        if (rate != null && rate > 0) {

            width = (int) (srcWidth * rate);
            height = (int) (srcHeight * rate);
        }
        if (width == null || height == null) {
            return null;
        }

        System.out.println("压缩文件的高：" + height + ";宽：" + width);

        //绘制图像:返回一个新的按照with,height缩放呈现的Image
        Image scaledInstance = bufferedImage.getScaledInstance(width, height, Image.SCALE_SMOOTH);

        // 构造一个新的BufferedImage
        bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        bufferedImage.getGraphics().drawImage(scaledInstance, 0, 0, null);

        return bufferedImage;
    }


    /**
     * 处理图片生成新的BufferedImage
     *
     * @param inputStream
     * @param width
     * @param height
     * @param x
     * @param y
     * @param suffix
     * @return
     * @throws IOException
     */
    public static BufferedImage handleImage(InputStream inputStream, int width, int height, int x, int y, String suffix) throws IOException {

        Iterator<ImageReader> it = ImageIO.getImageReadersByFormatName(suffix);

        ImageReader reader = it.next();

        ImageInputStream imageInputStream = ImageIO.createImageInputStream(inputStream);

        reader.setInput(imageInputStream, true);

        ImageReadParam param = reader.getDefaultReadParam();

        Rectangle rect = new Rectangle(x, y, width, height);

        param.setSourceRegion(rect);

        return reader.read(0, param);
    }


    /**
     * 截取指定位置的图片并保存到指定位置
     *
     * @param imageUrl
     * @param saveUrl
     * @param width
     * @param height
     * @param x
     * @param y
     * @return
     */
    public static boolean cutImage(String imageUrl, String saveUrl, int width, int height, int x, int y) {

        if (isEmpty(imageUrl) || isEmpty(saveUrl)) {
            return false;
        }
        try {
            //图片后缀名
            String suffix = imageUrl.substring(imageUrl.lastIndexOf(".") + 1);

            //读取io流
            InputStream inputStream = readImage(imageUrl);

            //处理图片
            BufferedImage bi = handleImage(inputStream, width, height, x, y, suffix);

            //写入IO
            ImageIO.write(bi, suffix, new File(saveUrl));

            return true;

        } catch (IOException e) {
            throw new IllegalArgumentException("cut image error:", e);
        }

    }

/**
 * 将BufferedImage转换为InputStream
 * @param image
 * @return
 */
public static InputStream bufferedImageToInputStream(BufferedImage image){
    ByteArrayOutputStream os = new ByteArrayOutputStream();
    try {
        ImageIO.write(image, "png", os);
        InputStream input = new ByteArrayInputStream(os.toByteArray());
        return input;
    } catch (IOException e) {
       // logger.error("提示:",e);
    }
    return null;
}


}