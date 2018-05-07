/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domain;

import java.io.IOException;
import java.io.Serializable;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 *
 * @author hansel
 */
public class ImageObject extends PartsImage{
    
    public ImageObject(byte[] image, int x, int y, int size) {
        super(image, x, y, size);
    } // constructor

    
    
     @Override
    public void printImageOnMosaic(GraphicsContext gc)throws IOException{
        ImageView imageView = new ImageView(SwingFXUtils.toFXImage(super.bytesToImage(), null));
        SnapshotParameters snapshot = new SnapshotParameters();
        //pinta la imagen en el mosaico
        gc.drawImage(imageView.snapshot(snapshot, null), (posix * pixelSize) + (1 + posix) * 10, (posiy * pixelSize) + (1 + posiy) * 10, pixelSize, pixelSize);
    } // draw

    public boolean pressMouse(int xMouse, int yMouse) {
        if ((xMouse >= (posix * pixelSize) + (1 + posix) * 10 && xMouse <= (posix * pixelSize) + (1 + posix) * 10 + this.pixelSize)
                && (yMouse >= (posiy * pixelSize) + (1 + posiy) * 10 && yMouse <= (posiy * pixelSize) + (1 + posiy) * 10 + this.pixelSize)) {
            return true;
        }
        return false;
    } 


}
