package utilitis;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class RotateImage {

    public static BufferedImage rotate(BufferedImage bufferedImage, double degree) {
        return RotateImage.rotate(bufferedImage, degree, true);
    }

    public static BufferedImage rotate(BufferedImage bufferedImage, double degree, boolean maxSize) {
        return RotateImage.rotate(new ImageIcon(bufferedImage), degree, maxSize);
    }

    public static BufferedImage rotate(ImageIcon image, double degrees, boolean maxSize) {
        // Calculate the new size of the image based on the angle of rotaion
        double radians = Math.toRadians(degrees);
        double sin = Math.abs(Math.sin(radians));
        double cos = Math.abs(Math.cos(radians));
        int newWidth, newHeight;
        if (maxSize) {
            newWidth = image.getIconWidth() + image.getIconHeight();
            newHeight = newWidth;
        } else {
            newWidth = (int) Math.round(image.getIconWidth() * cos + image.getIconHeight() * sin);
            newHeight = (int) Math.round(image.getIconWidth() * sin + image.getIconHeight() * cos);
        }
        // Create a new image
        BufferedImage rotate = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = rotate.createGraphics();
        // Calculate the "anchor" point around which the image will be rotated
        int x = (newWidth - image.getIconWidth()) / 2;
        int y = (newHeight - image.getIconHeight()) / 2;
        // Transform the origin point around the anchor point
        AffineTransform at = new AffineTransform();
        at.rotate(radians, x + (image.getIconWidth() / 2d), y + (image.getIconHeight() / 2d));
        at.translate(x, y);
        g2d.setTransform(at);
        // Paint the original image
        g2d.drawImage(image.getImage(), 0, 0, null);
        g2d.dispose();
        return rotate;
    }

}