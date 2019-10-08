package MyResizer;

import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.builders.BufferedImageBuilder;
import net.coobird.thumbnailator.resizers.configurations.ScalingMode;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class ImageResizer extends Thread {
    private File file;
    private int newWidth;
    private String dstFolder;

    public ImageResizer(File file, int newWidth, String dstFolder) {
        this.file = file;
        this.newWidth = newWidth;
        this.dstFolder = dstFolder;
    }

    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName() + " started");
        try {
            BufferedImage image = ImageIO.read(file);
            if (image == null) {
                return;
            }

            int newHeight = (int) Math.round(
                    image.getHeight() / (image.getWidth() / (double) newWidth)
            );

            BufferedImage newImage;

            if (image.getWidth() / newWidth > 2) {
                newImage = resizeImg(image, 2 * newWidth, 2 * newHeight, ScalingMode.BILINEAR);
                newImage = resizeImg(newImage, newWidth, newHeight, ScalingMode.BICUBIC);
            } else {
                newImage = resizeImg(image, newWidth, newHeight, ScalingMode.PROGRESSIVE_BILINEAR);
            }

            File newFile = new File(dstFolder + "/(" + Thread.currentThread().getName() + ")_" + file.getName());
            ImageIO.write(newImage, "jpg", newFile);

//            System.out.println("duration(" + this.getName() + "): " + (System.currentTimeMillis() - start));

        } catch (Exception e) {
            throw new RuntimeException();
        }

    }

    private BufferedImage resizeImg(BufferedImage image, int width, int height, ScalingMode mode) {
        try {
            return Thumbnails.of(image)
                    .size(width, height)
                    .scalingMode(mode)
                    .asBufferedImage();
        } catch (Exception e) {
            System.out.println("Ошибка при изменении размера изображения");
        }

        return image;
    }
}
