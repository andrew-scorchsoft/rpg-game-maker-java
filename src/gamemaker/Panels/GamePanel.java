/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gamemaker.Panels;

import gamemaker.Coordinate;
import gamemaker.Exceptions.GameXmlIncorrectException;
import javax.swing.JPanel;
import org.jdom.DataConversionException;
import org.jdom.Element;

/**
 *
 * @author ug77alw
 */
public class GamePanel extends JPanel {
    private int lastX;
    private int lastY;

    public GamePanel(){
        super();
    }



     public void setLastMousePosition(int x, int y){
        this.lastX = x;
        this.lastY = y;
    }

    public Coordinate getLastMousePosition(){
        return new Coordinate(lastX,lastY);
    }





    
}
