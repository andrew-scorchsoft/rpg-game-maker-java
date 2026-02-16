/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gamemaker;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import javax.swing.JComponent;

/**
 *
 * @author ug77alw
 */
public class PaintPanel extends JComponent{
    Image image = createVolatileImage(100, 100);

    GameMaps m;

    private  int paintWidth = 800;
    private  int paintHeight = 600;
  
    public PaintPanel(GameMaps m){
        this.setOpaque(true);
        this.m = m;

   
        //this.image = m.getCurrentMap().getBackgroundPanel().createVolatileImage(m.getCurrentMap().getWidth(), m.getCurrentMap().getHeight());
        //this.setSize(m.getCurrentMap().getBackgroundPanel().getSize());
        //this.setPreferredSize(m.getCurrentMap().getBackgroundPanel().getSize());
        this.setBackground(Color.black);
   

        this.setVisible(true);
        this.repaint();
        this.setAutoscrolls(true);

    }

    public void setPaintSize(int width,int height ){
        this.paintWidth= width;
        this.paintHeight = height;
        this.setSize(width, height);
    }
    public int getPaintWidth(){
        return paintWidth;
    }
    public int getPaintHeight(){
        return paintHeight;
    }

    public void updateImage(){

        this.updateUI();
        this.setSize(m.getCurrentMap().getBackgroundPanel().getSize());
        
      
        //this.setPreferredSize(m.getCurrentMap().getBackgroundPanel().getSize());
       // if(m.getMapsContainer().getContainterType().equals("playframe")){
        //    m.getMapsContainer().setPreferredSize(m.getCurrentMap().getBackgroundPanel().getSize());
            // m.getMapsContainer().setMaximumSize(m.getCurrentMap().getBackgroundPanel().getSize());
        //}
        image = createVolatileImage((int)m.getCurrentMap().getBackgroundPanel().getSize().getWidth(),(int)m.getCurrentMap().getBackgroundPanel().getSize().getSize().getHeight());
        //image = createVolatileImage((int)this.getSize().getWidth(),(int)this.getSize().getHeight());
      Dimension d = new Dimension(m.getMapsContainer().getWidth(),m.getMapsContainer().getHeight());
       //m.getMapsContainer().setSize(new Dimension (1024,777));
       m.getMapsContainer().setVisible(true);
     
    }



    public int getTopY(){
        paintHeight = (int)Math.ceil(this.getVisibleRect().getHeight());
        paintWidth = (int)Math.ceil(this.getVisibleRect().getWidth());
        int coy = (int)Math.ceil(m.getSprite().getSpritePositionCoord().getY()-(paintHeight/2.0));
        
        if(m.getCurrentMap().getBackgroundPanel().getHeight() < this.getVisibleRect().getHeight()){
                    int diff = (int)Math.ceil(this.getVisibleRect().getHeight() - m.getCurrentMap().getBackgroundPanel().getHeight());

                    //if(yedge == false){
                    //coy = coy - (diff/2);
                    coy = -(diff/2);
                    //}
                }else{

                    if((coy+paintHeight) >= image.getHeight(this)){
                        coy = (image.getHeight(this) - paintHeight);

                    }else if(coy <= 0){
                        coy = 0;

                    }
                }
        return coy;
    }
    public int getTopX(){
        paintHeight = (int)Math.ceil(this.getVisibleRect().getHeight());
        paintWidth = (int)Math.ceil(this.getVisibleRect().getWidth());
        //gets the area

        int cox = (int)Math.ceil(m.getSprite().getSpritePositionCoord().getX()-(paintWidth/2.0));
        

        if(m.getCurrentMap().getBackgroundPanel().getWidth() < this.getVisibleRect().getWidth()){
                    int diff = (int)Math.ceil(this.getVisibleRect().getWidth() - m.getCurrentMap().getBackgroundPanel().getWidth());
                    //if(xedge == false){
                        //cox = cox - (diff/2);
                       cox = -(diff/2);
                    //}
        }else{
            if((cox+paintWidth) >= image.getWidth(this)){
            cox = (image.getWidth(this) - paintWidth);

            }else if(cox <= 0){
                cox = 0;

            }

        }
        return cox;
    }

     @Override
    public void paintComponent(Graphics g){
        Graphics2D g2= (Graphics2D)g;
        if(m != null ){

            //only paints the image on the visible area
            g2.setClip(this.getVisibleRect());


            if(image == null){

               // m.getCurrentMap().paint(g2);
                m.getCurrentMap().getBackgroundPanel().paintComponent(g2);
                m.getCurrentMap().getSpritePanel().paintComponent(g2);
                m.getCurrentMap().getForegroundPanel().paintComponentTwo(g2);
                m.getCurrentMap().getDialoguePanel().paintComponents(g2);
                
                //m.getCurrentMap().getBackgroundPanel().paintComponent(g2);
                //m.getCurrentMap().getSpritePanel().paintComponent(g2);
                updateImage();
              
            }else{

               
                m.getCurrentMap().getBackgroundPanel().paintComponent(image.getGraphics());
                m.getCurrentMap().getSpritePanel().paintComponent(image.getGraphics());
                m.getCurrentMap().getForegroundPanel().paintComponentTwo(image.getGraphics());
                m.getCurrentMap().getDialoguePanel().paintComponent(image.getGraphics());
              

                //where to draw the image
                int coy = getTopY();
                int cox  = getTopX();
                
                //draws the image in the position relative to the sprite
                //so that is scrolls as it walks
                g2.setColor(Color.BLACK);
                g2.fillRect((int)this.getVisibleRect().getMinX(),
                        (int)this.getVisibleRect().getMinY(),
                        (int)this.getVisibleRect().getWidth(),
                        (int)this.getVisibleRect().getHeight());


                g2.drawImage(image, -cox, -coy, this);


                
              
           
              


            }
           
            

          
        }else{
             g2.setColor(Color.red);
            g2.fill3DRect(0, 0, 100, 100, true);
        }
       
    }


}
