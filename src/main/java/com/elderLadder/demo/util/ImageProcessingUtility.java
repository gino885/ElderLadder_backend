package com.elderLadder.demo.util;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Map;
import java.util.Set;

public class ImageProcessingUtility {

    public static BufferedImage addToImage(BufferedImage originalImage,
                                           String title,
                                           String description,
                                           BufferedImage IT_seed,
                                           BufferedImage dallePicture)
    {

        Graphics2D g2d = originalImage.createGraphics();
        g2d.setColor(Color.BLACK);
        Font font = new Font("Source Han Sans", Font.PLAIN, 12);
        g2d.setFont(font);

        int dpi = 96;
        double Dcm = 0.42; // The margin in centimeters
        double Dcm2 = 5.3;

        int D_leftMargin = (int)(Dcm / 2.54 * dpi);
        int D_topMargin = (int)(Dcm2 / 2.54 * dpi);

        // Calculate the y coordinate to place the text below the white square
        FontMetrics fm = g2d.getFontMetrics();
        String[] lines = description.split("\n");

        for (int i = 0; i < lines.length; i++) {
            // Calculate the y coordinate for each line
            int y = D_topMargin + (fm.getHeight() * i);

            // Draw the string
            g2d.drawString(lines[i], D_leftMargin, y);
        }
        double Tcm = 0.35;
        double Tcm2 = 0.24;
        int T_leftMargin = (int)(Tcm / 2.54 * dpi);
        int T_topMargin = (int)(Tcm2 / 2.54 * dpi)+fm.getHeight();
        g2d.drawString(title, T_leftMargin, T_topMargin);

        double ITcm = 3.75;
        double ITcm2 = 0.2;
        int IT_leftMargin = (int)(ITcm / 2.54 * dpi);
        int IT_topMargin = (int)(ITcm2 / 2.54 * dpi);
        g2d.drawImage(IT_seed, IT_leftMargin, IT_topMargin, null);

        double DLcm = 1.2;
        int DL_topMargin = (int)(DLcm / 2.54 * dpi);
        g2d.drawImage(dallePicture, D_leftMargin, DL_topMargin, null);

        g2d.dispose();

        return originalImage;
    }

}