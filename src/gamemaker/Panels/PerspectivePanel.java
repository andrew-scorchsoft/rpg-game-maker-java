/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gamemaker.Panels;

import gamemaker.*;
import gamemaker.Exceptions.GameXmlIncorrectException;
import gamemaker.Exceptions.SpriteNotSetException;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import org.jdom.DataConversionException;
import org.jdom.Element;

/**
 *
 * @author ug77alw
 */
public class PerspectivePanel extends GamePanel implements XMLable {
    //private Image theSprite;

    private Image frontSpriteImage;
    private Image backSpriteImage;

    private Image middleSprite;


    private Sprite sprite;


    /**
     * back
     * middle
     * front
     */
    private double tempPercentage;
   // private double frontSpritePercentage;
    //private double backSpritePercentage;


    private boolean panelOn = false;
    private Perspective perspective;
    private SpritePanel spritePanel;


    public PerspectivePanel(SpritePanel spritePanel) throws IOException{
        perspective = new Perspective();
        //theSprite = ImageIO.read(new File("sprite/0001/N/0001.png"));
        sprite = spritePanel.getSprite();

        panelOn = false;
        this.setOpaque(false);
        perspective = new Perspective();
        this.spritePanel = spritePanel;
        
        setSprites();

    }
    public Perspective getPerspective(){
        return perspective;
    }

    /**
     *
     * @param state true is on, false is off
     */
    public void setIsPanelOn(boolean state){
        this.panelOn = state;
    }



   

    public void changePerspective(Point from, Point to){



        double widthChange = from.getX() - to.getX();
     

        double percentageChange = Math.abs(widthChange/perspective.getStandardSpriteWidth());

        if(from.getY() < perspective.getHorizon()){
            //edits top sprite
            perspective.setBackPercentage(1 * percentageChange);
              
            
        }else{
            //edits bottom sprite
            perspective.setFrontPercentage( 1 * percentageChange);
  
        }
        spritePanel.setPerspective(perspective);
        setSprites();
        

    }


    public void setSprites(){

       
        try{

                backSpriteImage = sprite.getSpriteImage(new Point(0+(int)this.perspective.getHorizon(),0), this.perspective);
                int midpoint = (int)(this.perspective.getHorizon()+((this.getHeight()-this.perspective.getHorizon())/2));
                middleSprite = sprite.getSpriteImage(new Point(this.getHeight(),midpoint), this.perspective);
                frontSpriteImage = sprite.getSpriteImage(new Point(this.getHeight(),this.getHeight()), this.perspective);
        
        }catch(SpriteNotSetException snse){
            System.err.println(snse);
        }

    }

    public void setHorizon(Coordinate c){
        perspective.setHorizon(c.getY());
    }

     public void setHorizon(Point p){
        perspective.setHorizon((int)p.getY());
    }




     public void fromXML(Element rootElement)throws GameXmlIncorrectException{

         
        double front = XMLSimplify.getXMLDouble(rootElement,"front");
        double back = XMLSimplify.getXMLDouble(rootElement,"back");
        double horizon = XMLSimplify.getXMLDouble(rootElement,"horizon");

        this.getPerspective().setFrontPercentage(front);
        this.getPerspective().setBackPercentage(back);
        this.getPerspective().setHorizon((int)Math.round(horizon));

        spritePanel.setPerspective(perspective);



     }
     
     public String toXML(){
        return this.toXML("","");
    }

    public String toXML(String openTag, String closeTag){
        String xml = "";

        xml += openTag+"";

        xml += this.perspective.toXML();
        xml += closeTag+"";
        return xml;
    }
    public void paintComponent(Graphics g){
        Graphics2D g2 = (Graphics2D) g;
        if(this.panelOn == true){
            this.getHeight();
            setSprites();
            //rectangle fills top half of pane
            Rectangle2D top = new Rectangle2D.Double(0,0,this.getWidth(),perspective.getHorizon());
            //rectangle fills bottom half of pane
            Rectangle2D bottom = new Rectangle2D.Double(0,0+perspective.getHorizon(),this.getWidth(),this.getHeight()-perspective.getHorizon());

            //pale blue, with opacity
            g2.setColor(new Color(183,201,255,100));
            g2.fill(top);

            //pale green, with opacity
            g2.setColor(new Color(201,255,140,100));
            g2.fill(bottom);

            //draws the horizon line
            g2.setColor(Color.red);
            g2.drawLine(0, (int)perspective.getHorizon()-1, this.getWidth(), (int)(perspective.getHorizon()-1));
            g2.setFont(new Font("Arial", Font.PLAIN, 20));
            g2.drawString("Horizon", 40, (int)perspective.getHorizon()-3);

            int drawX = this.getWidth()/2 - backSpriteImage.getWidth(this)/2;
            //int drawY = 150 - backSpriteImage.getHeight(this);
            //int drawY = 0;
            int drawY = 0 - backSpriteImage.getHeight(this);
            g2.drawImage(backSpriteImage, drawX, drawY+(int)this.perspective.getHorizon(), this);

            drawX = this.getWidth()/2 - middleSprite.getWidth(this)/2;
            int midpoint = (int)(this.perspective.getHorizon()+((this.getHeight()-this.perspective.getHorizon())/2));
            drawY = midpoint - (int)(middleSprite.getHeight(this));
            g2.drawImage(middleSprite, drawX, drawY, this);

            drawX = this.getWidth()/2 - frontSpriteImage.getWidth(this)/2;
            drawY = this.getHeight() - frontSpriteImage.getHeight(this);
            g2.drawImage(frontSpriteImage, drawX, drawY, this);
           

            

           


            





        }
        
    }





}
