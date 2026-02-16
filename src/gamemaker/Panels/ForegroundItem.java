/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gamemaker.Panels;


import gamemaker.*;
import java.awt.BasicStroke;
import java.awt.Color;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.geom.Rectangle2D;
import java.io.IOException;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

/**
 *
 * @author ug77alw
 */
public class ForegroundItem extends JLabel{

    private ImageWithDetails itemImage;
    private Image displayedImage;
    private GameMap gameMap;
    private ImageIcon thisIcon;
    private Coordinate startPlace;
    private Coordinate lastMove;


    /**
     * Resizer reference:
     *  0,1,
     *  2,3
     */
    private Rectangle2D[] resizers = new Rectangle2D[4];
    private int resizerWidth = 6;
    private int lastResizerClicked = 0;

    public ForegroundItem(GameMap gameMap){
        super();
        this.gameMap = gameMap;
        thisIcon = new ImageIcon();
    
    }


   
    /**
     * This method should be used for the playing of the game in conjunction
     * with double buffering. it paints the items as flat images onto the graphics
     * object g
     * @param g the graphics object to draw onto
     */
    public void paintComponentTwo(Graphics g){

        
        //ensures that the image is drawn onto the right place
        int x = startPlace.getX() - Math.round(displayedImage.getWidth(this)/2);
        int y = startPlace.getY() - Math.round(displayedImage.getHeight(this)/2);
        //draws the image
        g.drawImage(displayedImage, x, y, this);

    }


  
    public Coordinate getImageLocation(){
       return startPlace;
    }
    public String getImageFileName(){
        return itemImage.getFileName();
    }
    public int getImageHashCode(){
        return itemImage.getImage().hashCode();
    }
    /**
     * saves the image to file. Automatically handles the file name
     * @param location the location to save to. Without the file name!
     * @throws IOException if there is a problem saving the image
     */
    public void saveImage(String location) throws IOException{


        FileHandler.imageToFile(displayedImage, location + itemImage.getImage().hashCode()+".png");
    }
    public void setImage(Coordinate c, ImageWithDetails theImage) throws IOException{
       


            itemImage = theImage;



            int imageWidth = itemImage.getImage().getWidth(gameMap);
            int imageHeight = itemImage.getImage().getHeight(gameMap);
            int x1 = c.getX() - imageWidth/2;
            int y1 = c.getY() - imageHeight/2;
            this.setBounds(x1,y1,imageWidth,imageHeight);

            displayedImage = itemImage.getImage();
            thisIcon.setImage(displayedImage);
            this.setIcon(thisIcon);
            setResizers();

            startPlace = new Coordinate(c.getX(),c.getY());
           
        
    }


    public void startMoveItem(Coordinate from, Coordinate to){
        int width = from.getX() - to.getX();
        int height = from.getY() - to.getY();
        
        int x1 = (int)from.getX()-(int)(this.getBounds().getWidth()/2)-width;
        int y1 = (int)from.getY()-(int)(this.getBounds().getHeight()/2)-height;
        this.setBounds(x1, y1, (int)this.getBounds().getWidth(), (int)this.getBounds().getHeight());

        setResizers();
        //lastMove = new Coordinate(to.getX()-(int)this.getBounds().getWidth()/2,to.getY()-(int)this.getBounds().getHeight()/2);

       //System.out.println("from x:"+from.getX()+" from y"+from.getY()+"  to x:"+to.getX()+" to y"+to.getY());


    }

    private Rectangle2D initialBounds;

    public void resizeItem(int ResizerIndex, Coordinate from, Coordinate to){
        //only accepts a valid resizer index
        if(initialBounds == null){
            initialBounds = this.getBounds();
        }
        if(ResizerIndex > -1 && ResizerIndex <= 3){

            int widthChange = to.getX() - from.getX();
            int heightChange = to.getY() - from.getY();
            int totalWidth = (int)initialBounds.getWidth() + widthChange;
            int totalHeight = (int)initialBounds.getHeight() + heightChange;

            int minX = (int)initialBounds.getMinX();
            int minY = (int)initialBounds.getMinY();

            int width = widthChange;
            int height = heightChange;
           

            //0 and 1 are both on the same y axis
            if(ResizerIndex == 0 || ResizerIndex == 1){
                if((int)initialBounds.getMinY()+heightChange < initialBounds.getMaxY()){
                    minY = (int)to.getY();
                }else{
                    minY = (int)initialBounds.getMaxY();
                }
               
                height = (int)initialBounds.getHeight()-heightChange;

            }else{

                if(totalHeight < 0){
                    minY = (int)initialBounds.getMinY() - Math.abs(totalHeight);
                }
                 height = totalHeight;


            }

            //0 and 2 are both on the same x axis
            if(ResizerIndex == 0 || ResizerIndex == 2){
                if((int)initialBounds.getMinX()+widthChange < initialBounds.getMaxX()){
                    minX = (int)to.getX();
                }else{
                    minX = (int)initialBounds.getMaxX();
                }
                 width = (int)initialBounds.getWidth()-widthChange;
            }else{
                if(totalWidth < 0){
                    minX = (int)initialBounds.getMinX() - Math.abs(totalWidth);
                }
                width = totalWidth;
                
            }
                


            this.setBounds( minX,
                            minY,
                            Math.abs(width),
                            Math.abs(height));


            int newWidth = Math.abs(width);
            int newHeight= Math.abs(height);

            if(newWidth != 0 && newHeight != 0){
                displayedImage = itemImage.getImage().getScaledInstance(newWidth, newHeight, 1);
                thisIcon.setImage(displayedImage);
            }
            setResizers();

        }
    }

    public void finishResize(){
       initialBounds = null;
    }

    /**
     * this sets the resizer boxes to be in the corners of the bounds of the icon
     */
    private void setResizers(){
        //top left
        setResizer(0,(int)this.getBounds().getMinX(),(int)this.getBounds().getMinY());
        //top right
        setResizer(1,(int)this.getBounds().getMaxX(),(int)this.getBounds().getMinY());
        //bottom left
        setResizer(2,(int)this.getBounds().getMinX(),(int)this.getBounds().getMaxY());
        //bottom right
        setResizer(3,(int)this.getBounds().getMaxX(),(int)this.getBounds().getMaxY());
    }
    private void setResizer(int index, int x, int y){
        int x1 = x - resizerWidth/2;
        int y1 = y - resizerWidth/2;

        resizers[index] = new Rectangle2D.Double(x1,y1,resizerWidth,resizerWidth);
    }

    /**
     *
     * @param c
     * @return the id of the resizer under the coordinate.
     * 0 = top left
     * 1 = top right
     * 2 = bottom left
     * 3 = bottom right
     * -1 = no selection
     */
    public int checkResizers(Coordinate c){
        int found = -1;

        int i = 0;
        while(i <= 3 && found == -1){
            if(resizers[i].contains(new Point(c.getX(),c.getY()))){
                found = i;
                lastResizerClicked = i;
            }
            i++;
        }
        return found;
    }

    public void drawResizers(Graphics2D g2){

        g2.setColor(Color.white);

        g2.fill(resizers[0]);
        g2.fill(resizers[1]);
        g2.fill(resizers[2]);
        g2.fill(resizers[3]);

        g2.setStroke(new BasicStroke(1,BasicStroke.CAP_ROUND,1));
        g2.setColor(Color.black);

        g2.draw(resizers[0]);
        g2.draw(resizers[1]);
        g2.draw(resizers[2]);
        g2.draw(resizers[3]);



    }

    public boolean isPointWithin(Coordinate c){
       boolean itIs = false;
        if( this.getBounds().contains(new Point(c.getX(),c.getY())) == true ||
            checkResizers(c) > -1){
            
             itIs = true;
        }

        return itIs;
    }


}
