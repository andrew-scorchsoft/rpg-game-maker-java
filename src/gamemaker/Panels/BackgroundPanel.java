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
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import javax.swing.JPanel;
import gamemaker.Exceptions.GameXmlIncorrectException;
import java.awt.image.renderable.ParameterBlock;
import java.io.File;

import org.jdom.Element;

/**
 *
 * @author ug77alw
 */
public class BackgroundPanel extends JPanel {
    private GameMap gameMap;
    private ImageWithDetails bgImage;

    private int imageHeight = 600;
    private int imageWidth = 800;

    public BackgroundPanel(GameMap gameMap){
        super();
        //this.setOpaque(false);
        this.gameMap = gameMap;
        this.setOpaque(true);
        
        
     



    }
    public void saveImage(String location)throws IOException{
        


        FileHandler.imageToFile(bgImage.getImage(), location+"background-"+bgImage.hashCode()+".png");
    }

    public Image getBackgroundImage(){
        return bgImage.getImage();
    }
    public Image getBackgroundImageThumb(){
        if(bgImage != null){
        int newHeight = 60;
        double ratio = bgImage.getImage().getHeight(gameMap) / newHeight;
        int newWidth = (int)(bgImage.getImage().getWidth(gameMap)/ratio);

        
        return bgImage.getImage().getScaledInstance(newWidth, newHeight, 1);
        }else{
            return  null;

        }
    }

    public int getHeight(){
        return imageHeight;
    }

    public int getWidth(){
        return imageWidth;
    }



 
    @Override
    public void paintComponent(Graphics g){
 
        Graphics2D g2 = (Graphics2D)g;


       

        if (bgImage != null){

            //only paints the image on the visible area
            //g2.setClip(this.getVisibleRect());
            g2.drawImage(bgImage.getImage(), 0,0,imageWidth,imageHeight,this);
          
            
           

            

        }else{
            g2.setBackground(Color.yellow);
         
        }

        //g2.setColor(Color.black);
        //g2.setStroke(new BasicStroke(1,BasicStroke.CAP_ROUND,1));
        //g2.draw(new Rectangle2D.Double(0,0,imageWidth-1,imageHeight-1));


    }
    


    public void fromXML(Element rootElement)throws GameXmlIncorrectException{
        String ps = File.separator;
        
        //allows for the reading of old files where the exact url of the file was stored
        //in the xml. This removes anything before the first /
        String[] parts = rootElement.getAttributeValue("img").split("/");
        String img = "gamesave"+ps+parts[parts.length-1];
        
        try{
           this.setImage(img);

        }catch(IOException ioe){
            throw new GameXmlIncorrectException("Unable to add background. The Image '"+img+"' could not be found.");
        }

    }

    public String toXML(){
        return this.toXML("","");
    }

    public String toXML(String openTag, String closeTag){
        String xml = "";

        xml += "<background img=\"background-"+bgImage.hashCode()+".png\"/>";
        xml += "\n";
        return xml;
    }

    public void setImage(String location)throws IOException{

       
        setImage(FileHandler.imageFromFile(FileHandler.getFilesLocation()+location));
        //setImage(FileHandler.imageFromURL(gameMap.getClass().getResource("files/"+location)));
       //setImage(FileHandler.imageFromFile(location));
    }
    public void setImage(ImageWithDetails newImage) throws IOException{


        /**
            //Grab the alpha band
            ParameterBlock pb = new ParameterBlock();
            pb.addSource(newImage.getImage());
            pb.add(new int[] {0,1,2});
            PlanarImage image = JAI.create("bandSelect", pb);
            newImage.setImage(image.getAsBufferedImage());
**/
            bgImage = newImage;
           

            

            imageWidth = newImage.getImage().getWidth(gameMap);
            imageHeight = newImage.getImage().getHeight(gameMap);
            //updateSizeOfAll()
            


            gameMap.setMapDimensions(imageWidth,imageHeight);
            gameMap.updateSizeOfAll();

            this.repaint();
            gameMap.repaint();
      



    }

}
