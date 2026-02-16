/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gamemaker;

import gamemaker.Exceptions.ImageNotSelectedException;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.io.IOException;

/**
 *
 * @author ug77alw
 */
public interface MapsContainer{
    public void setCursorCrosshair();
    public void setCursorNormal();
    public void repaint();
    public void setCursor(Cursor c);
    public Cursor getCursor();
    public String getContainterType();
    public ImageWithDetails getImageChooser() throws ImageNotSelectedException, IOException;
    //public JScrollPane getJScrollPane();
    public int getWidth();
    public int getHeight();

    public void setMaximumSize(Dimension maximumSize);       
    public void setPreferredSize(Dimension maximumSize);
     public void setSize(Dimension maximumSize);
     public Dimension getSize();
     public void setVisible(boolean b);
    //public void refreshMapViewed();
   
    public String toString();
}
