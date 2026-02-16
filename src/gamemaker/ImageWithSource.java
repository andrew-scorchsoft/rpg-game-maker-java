/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gamemaker;

import java.awt.Image;
import java.awt.image.ImageObserver;

/**
 *
 * @author ug77alw
 */
public class ImageWithSource{

    private Image theImage;
    public ImageWithSource(Image image,String sourceName){
        this.theImage = image;
    }
    public Image getImage(){
        return theImage;
    }
    public void setImage(Image image){
        this.theImage = image;
    }
  
}
