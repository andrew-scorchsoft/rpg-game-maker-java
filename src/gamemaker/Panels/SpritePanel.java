/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gamemaker.Panels;

import gamemaker.*;
import gamemaker.Exceptions.SpriteNotSetException;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Panel;
import java.awt.Point;
import java.io.IOException;

/**
 *
 * @author ug77alw
 */
public class SpritePanel extends GamePanel {


    private Sprite theSprite;
    //private Coordinate spritePosition;
    private boolean displaySprite = true;
    private Perspective perspective;
    private EventPanel eventPanel;
    private GameMap gameMap;

    public SpritePanel(Sprite s, GameMap gameMap) throws IOException{
        this.theSprite = s;
        this.gameMap = gameMap;
        theSprite.loadDefaultSprite();
        this.perspective = new Perspective();
        this.setOpaque(false);
        this.setDoubleBuffered(true);
    }

    public Sprite getSprite(){
        return theSprite;
    }
    public void setSprite(Sprite newSprite){
        theSprite = newSprite;
    }

    public void setEventPanel(EventPanel eventPanel){
        this.eventPanel = eventPanel;
    }

    /**
    public void triggerActionEvent() throws PanelNotPassedException{
        if(eventPanel == null){
            throw new PanelNotPassedException("EventPanel");
        }else{
            eventPanel.triggerActionEventAtPoint(theSprite.getSpritePosition());
        
        }
    }**/

    /**
     *
     * @param c This is the position of the sprites feet. It does not relate to the
     * corners of the sprites bounding box.
     */
    public void setSpritePosition(Point p){
        this.theSprite.setSpritePosition(p);
        //will need to check for event here
    }


    public Coordinate getSpritePositionCoord(){
        return new Coordinate(theSprite.getSpritePosition().getX(),theSprite.getSpritePosition().getY());
    }
    public Point getSpritePosition(){
        return this.theSprite.getSpritePosition();
    }

    public void setPerspective(Perspective p){
        this.perspective = p;
        this.repaint();
    }
    public Perspective getPerspective(){
        return perspective;
    }
    public void setDisplaySprite(boolean displaySprite){
        this.displaySprite = displaySprite;
    }

    @Override
    public void paintComponent(Graphics g){
        Panel p = new Panel();
        Graphics2D g2 = (Graphics2D)g;
        if(displaySprite == true && theSprite!=null && theSprite.isImageSet() ==true){

            //only display the sprite if this is the map that it is currently on
            if(theSprite.getCurrentGameMap().equals(this.gameMap)){
                try{
                    Image spriteToPaint = theSprite.getSpriteImage(this.theSprite.getSpritePosition(), perspective);
                     int drawX = (int)Math.round(theSprite.getSpritePosition().getX() - spriteToPaint.getWidth(p)/2);

                    //has the feet as slightly above the base of the image (10% up)

                    //int drawY = (int)(this.spritePosition.getY() - spriteToPaint.getHeight(p)/0.9);
                    int drawY = (int)Math.round(this.theSprite.getSpritePosition().getY() - spriteToPaint.getHeight(p));
                    g2.drawImage(spriteToPaint, drawX, drawY, this);

                }catch(SpriteNotSetException snse){
                    System.err.println(snse);
                }
            }
           

        }

    }




}
