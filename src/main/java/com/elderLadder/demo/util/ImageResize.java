package com.elderLadder.demo.util;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

public class ImageResize{

    public BufferedImage resizeImage(String url, int width, int height) throws IOException {
        URL imageUrl = new URL(url);
        BufferedImage originalImage = ImageIO.read(imageUrl);
        BufferedImage resizedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = resizedImage.createGraphics();
        g.drawImage(originalImage, 0, 0, width, height, null);
        g.dispose();
        return resizedImage;
    }
}
