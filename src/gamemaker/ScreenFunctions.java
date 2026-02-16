/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gamemaker;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;

/**
 *
 * @author Andy
 */
public class ScreenFunctions {

    public static Point getScreenCentre(Component c){
        Dimension screenDimensions = Toolkit.getDefaultToolkit().getScreenSize();
        int screenCentreX = (int)Math.round((screenDimensions.getWidth()/2)-(c.getSize().getWidth()/2));
        int screenCentreY = (int)Math.round((screenDimensions.getHeight()/2)-(c.getSize().getHeight()/2));
        return new Point(screenCentreX,screenCentreY);
    }
}
