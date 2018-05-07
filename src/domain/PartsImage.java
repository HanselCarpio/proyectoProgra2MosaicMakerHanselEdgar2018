/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domain;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.Serializable;
import javafx.scene.canvas.GraphicsContext;
import javax.imageio.ImageIO;

/**
 *
 * @author hansel
 */
public abstract class PartsImage implements Serializable{    
    protected int posix, posiy;
    protected int pixelSize;
    protected byte[] iBytes;

    public PartsImage(byte[] iBytes, int posix, int posiy, int pixelSize) {
        this.posix = posix;
        this.posiy = posiy;
        this.pixelSize = pixelSize;
        this.iBytes = iBytes;
    }

    public int getPosix() {
        return posix;
    }

    public void setPosix(int posix) {
        this.posix = posix;
    }

    public int getPosiy() {
        return posiy;
    }

    public void setPosiy(int posiy) {
        this.posiy = posiy;
    }

    public int getPixelSize() {
        return pixelSize;
    }

    public void setPixelSize(int pixelSize) {
        this.pixelSize = pixelSize;
    }

    public byte[] getiBytes() {
        return iBytes;
    }

    public void setiBytes(byte[] iBytes) {
        this.iBytes = iBytes;
    }
    
    public BufferedImage bytesToImage() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(this.iBytes);
        //convierte imagen a bytes
        BufferedImage bufferedImageToConvert = ImageIO.read(inputStream);
        return bufferedImageToConvert;
    } // bytesToImage
    
    public abstract void printImageOnMosaic(GraphicsContext gc) throws IOException;

    public abstract boolean pressMouse(int xMouse, int yMouse);
}
