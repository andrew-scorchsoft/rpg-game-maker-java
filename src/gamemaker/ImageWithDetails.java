/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gamemaker;

import java.awt.Graphics;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Transparency;
import java.awt.image.BufferedImage;

/**
 *
 * @author Andy
 */
public class ImageWithDetails {
    private BufferedImage image;
    private String fileName;

    public ImageWithDetails(BufferedImage bfimage){

     
        /**
         * gets the graphics context of the display.
         * This is crucial as if there is a missmatch between the image graphics
         * context and the display graphics context then the image will have to be
         * reprocessed on every repaint! which is very slow.
         *
         * I identified that png8 was repainting lots faster than png24. This was
         * the reason.
        **/

        GraphicsDevice device = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice(); 
        image = device.getDefaultConfiguration().createCompatibleImage(bfimage.getWidth(),bfimage.getHeight(), Transparency.BITMASK);
        //draw the buffered image into the new bufferedimage (that has the
        // correct context
        Graphics g = image.getGraphics();
        g.drawImage(bfimage, 0, 0, null);
        g.dispose();


    }
    public void setImage(BufferedImage image){
        this.image = image;
    }
    public void setFileName(String fileName){
        this.fileName = fileName;
    }
    public String getFileName(){
        return this.fileName;
    }
    public BufferedImage getImage(){
        return image;
    }

}
