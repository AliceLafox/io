package net.lafox.io.utils;
//
//import net.sf.jmimemagic.Magic;
//import net.sf.jmimemagic.MagicException;
//import net.sf.jmimemagic.MagicMatchNotFoundException;
//import net.sf.jmimemagic.MagicParseException;

import javax.activation.MimetypesFileTypeMap;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URLConnection;

/**
 * Created by tsyma@lafox.net on 07-feb-2014.
 */
public class ImgUtils {


    public static Dimension imgDimension(final String fileName) {
        Dimension dim = new Dimension(0, 0);
        try {
            BufferedImage img = ImageIO.read(new File(fileName));
            dim.setSize(img.getWidth(), img.getHeight());
        } catch (IOException e) {

        }
        return dim;
    }

    public static Dimension imgDimension(final byte[] imageInByte) {
        Dimension dim = new Dimension(0, 0);
        try {
            InputStream in = new ByteArrayInputStream(imageInByte);
            BufferedImage img = ImageIO.read(in);
            dim.setSize(img.getWidth(), img.getHeight());
        } catch (IOException e) {
        }
        return dim;
    }

    public static String getContentType(final String fileName, boolean quick) {
        MimetypesFileTypeMap mimeTypesMap = new MimetypesFileTypeMap();

        String mimeType = "";
        if (quick) {
            // only by file name
            mimeType = mimeTypesMap.getContentType(fileName);
        } else {
            // or by actual File instance
            File file = new File(fileName);
            mimeType = mimeTypesMap.getContentType(file);
        }
        return mimeType;
    }

//    public static String getMimeType(final String fileName) {  //by content
//        String mimeType = "";
//        try {
//            mimeType = Magic.getMagicMatch(new File(fileName), false).getMimeType();
//        } catch (MagicParseException e) {
//            e.printStackTrace();
//        } catch (MagicMatchNotFoundException e) {
//            e.printStackTrace();
//        } catch (MagicException e) {
//            e.printStackTrace();
//        }
//
//
//        return mimeType;
//    }

    public static String getContentType(final String filename) { //only ny filename
        String g = URLConnection.guessContentTypeFromName(filename);
        if (g == null) {
            g = MimetypesFileTypeMap.getDefaultFileTypeMap().getContentType(filename);
        }
        return g;
    }

    public static byte[] getFileBytes(final String fileName) {
        byte[] b = "".getBytes();
        try {
            RandomAccessFile f = new RandomAccessFile(fileName, "r");
            b = new byte[(int) f.length()];
            f.read(b);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return b;
    }


    public static BufferedImage createImageFromBytes(byte[] imageData) {
        ByteArrayInputStream bais = new ByteArrayInputStream(imageData);
        try {
            return ImageIO.read(bais);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static byte[] createBytesFromImage(final BufferedImage img) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ImageIO.write(img, "png", byteArrayOutputStream);
        byteArrayOutputStream.flush();
        return byteArrayOutputStream.toByteArray();

    }
}
