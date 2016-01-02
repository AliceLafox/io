package net.lafox.io.utils;

import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * Created by <tsyma@lafox.net> on 31.12.15
 * Lafox.Net Software developers Team http://dev.lafox.net
 */

public class ImgProcessing {


    public static Thumbnails.Builder width(File src, int width) throws IOException {
        return  Thumbnails.of(src).width(width);
    }

    public static Thumbnails.Builder height(File src, int width) throws IOException {
        return  Thumbnails.of(src).height(width);
    }

    public static  Thumbnails.Builder crop(File src, int width, int height) throws IOException {
        BufferedImage bi =ImageIO.read(new FileInputStream(src));
        double wOrig = (double) bi.getWidth();
        double hOrig = (double) bi.getHeight();
        double wCrop, hCrop;
        double kOrig = wOrig / hOrig;
        double kQuery = (1.0*width) / (1.0*height);
        if (kOrig > kQuery) {
            hCrop = hOrig;
            wCrop = hCrop * kQuery;
        } else {
            wCrop = wOrig;
            hCrop = wCrop / kQuery;
        }
        return Thumbnails.of(bi).sourceRegion(Positions.CENTER, i(wCrop), i(hCrop)).forceSize(width, height);
    }

    public static Thumbnails.Builder expand(File file, int width, int height) throws IOException {
        BufferedImage src =ImageIO.read(new FileInputStream(file));

        double wOrig = (double) src.getWidth();
        double hOrig = (double) src.getHeight();
        double wCrop, hCrop;
        double kOrig = wOrig / hOrig;
        double kQuery = (1.0*width) / (1.0*height);
        if (kOrig > kQuery) {
            wCrop = wOrig;
            hCrop = wCrop / kQuery;
        } else {
            hCrop = hOrig;
            wCrop = hCrop * kQuery;
        }

        BufferedImage biNew = new BufferedImage(i(wCrop), i(hCrop), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = biNew.createGraphics();
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC));
        g.setBackground(new Color(255, 255, 255, 0));
        g.clearRect(0, 0, i(wCrop), i(hCrop));
        g.drawImage(src, i((wCrop - wOrig) / 2), i((hCrop - hOrig) / 2), null);

        return Thumbnails.of(src)
                .sourceRegion(Positions.CENTER, i(wCrop), i(hCrop))
                .forceSize(width, height);
    }


    public static int i(double x) {
        return (int) Math.round(x);
    }


}
