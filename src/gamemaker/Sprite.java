/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gamemaker;

import gamemaker.events.VariableOperation;
import gamemaker.Exceptions.GameXmlIncorrectException;
import gamemaker.Exceptions.SpriteNotSetException;
import gamemaker.Panels.XMLable;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Panel;
import java.awt.Point;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import org.jdom.Element;
import java.util.List;


/**
 *
 * @author ug77alw
 */
public class Sprite implements XMLable{

    public final int NORTH =        0;
    public final int NORTHEAST =    1;
    public final int EAST =         2;
    public final int SOUTHEAST =    3;
    public final int SOUTH =        4;
    public final int SOUTHWEST =    5;
    public final int WEST =         6;
    public final int NORTHWEST =    7;
    private final int NUMDIRECTIONS = 8;


    private Point spritePosition;
    //private Coordinate spritePosition;
    private int currentMap;
    private GameMap currentGameMap;
   
   

    private Image[][] spriteImages;
    private boolean imageSet = false;
    private String spriteNumber;

    //the current direction that the sprite is facing.
    private int currentDirection = SOUTH;
    //the current frame to display in the walking motion
    private int currentStep = 0;
    //how fast the sprite moves. This is in pixels per second.
    double movespeed = 50;
    private String workingDir = "";

    private int[] spriteVars;



    public Sprite(){
        spriteImages = new Image[8][];
        spriteVars = new int[10];
        //by default the sprite starts at position 300,300 on map 0
        this.spritePosition = new Point(500,500);
        
        this.currentMap = 0;

        workingDir = this.getClass().getResource("").getPath();
        workingDir = workingDir.replace("%20", " ") + "files/";
    }

    public Image[][] getImageArray(){
        return spriteImages;
    }
    public void setImageArray(Image[][] images){
        this.spriteImages = images;
    }

    public int getSpriteVar(int index){
        return spriteVars[index];
    }
    public void setSpriteVar(int index, int value){
        spriteVars[index] = value;
    }
    public int getNumSpriteVars(){
        return spriteVars.length;
    }


    public Point getSpritePosition(){
        return spritePosition;
    }
    public Coordinate getSpritePositionCoord(){
         return new Coordinate(spritePosition.getX(),spritePosition.getY());
    }
    public void setSpritePosition(Point c){
         this.spritePosition = c;
    }


    public void setVariable(VariableOperation operation){
        operation.applyChange(this);
    }

    public void setStartPosition(GameMap map, int mapIndex, Point c){
        this.currentGameMap = map;
        this.spritePosition = c;
        setCurrentMap(mapIndex);
    }

    public void setCurrentGameMap(GameMap map){
        this.currentGameMap = map;
   
  
    }
    public void setCurrentMap(int mapID){
        currentMap = mapID;
    }
    public int getCurrentMap(){
        return currentMap;

    }
    public GameMap getCurrentGameMap(){
        return this.currentGameMap;
    }

    /**
     *
     * @param speed how fast the sprite will move. This is measured in 100
     * pixels per second. ie a value of 100 will result in the sprite moving at
     * a speed of 100 pixels per second
     */
    public void setSpriteSpeed(double movespeed){
        this.movespeed = movespeed;
        //spriteImages = new Image[8][];
    }

    public double getSpriteSpeed(){
        return this.movespeed;
    }

    /**
     * 
     * @param direction the direction that the sprite will face
     * See the public variables for NORTH, SOUTH etc as these
     * are what you pass to this method
     */

    public Image getSpriteImage(){
        return spriteImages[currentDirection][currentStep];
    }

    //returns an image that is the sprites size in relation to the coordinate that it
    //is at and a perspective.
    public Image getSpriteImage(Point point, Perspective perspective) throws SpriteNotSetException{

        if(spriteImages[currentDirection][currentStep] != null &&
                spriteImages[currentDirection] != null &&
                spriteImages != null){
            Panel p = new Panel();
            int height = spriteImages[currentDirection][currentStep].getHeight(p);
            int width = spriteImages[currentDirection][currentStep].getWidth(p);
         

            Dimension dimension = perspective.getPerspetiveDimensions(new Dimension(width,height), point);
            if ((int)dimension.getWidth() != 0 && (int)dimension.getHeight() != 0) {
                return spriteImages[currentDirection][currentStep].getScaledInstance((int)dimension.getWidth(), (int)dimension.getHeight(), 1);
            }else{
                return spriteImages[currentDirection][currentStep];
            }
        }else{
            String error = "Coordinate:" + point + " Perspective:" + perspective ;
            error = error + " currentDirection: " + currentDirection + " CurrentStep: " + currentStep;
            throw new SpriteNotSetException(error);
        }
    }

    public void setSpriteFacing(int direction){
        currentDirection = direction % NUMDIRECTIONS; //ensures no more than 8 possible directions
    }
    /**
     * Makes the sprite take a step in any direction it is facing. It changes the image
     * to make the sprite look like it is walking
     */
    public void takeStep(){
        currentStep = (currentStep + 1) % spriteImages[currentDirection].length;
        if(currentStep == 0){currentStep = currentStep +1;}
 
    }

    public void stopWalk(){
        currentStep = 0;
    }
    public boolean isImageSet(){
        return this.imageSet;
    }

    public void loadDefaultSprite()throws IOException{
        spriteNumber = "0001";
        setSpriteFromFolder(spriteNumber);
    }
    public void setSpriteFromFolder(String spriteDir) throws IOException{
        spriteNumber = spriteDir;
        spriteImages[NORTH] =     loadDirection("sprite/"+spriteDir+"/N/");
        spriteImages[NORTHEAST] = loadDirection("sprite/"+spriteDir+"/NE/");
        spriteImages[EAST] =      loadDirection("sprite/"+spriteDir+"/E/");
        spriteImages[SOUTHEAST] = loadDirection("sprite/"+spriteDir+"/SE/");
        spriteImages[SOUTH] =     loadDirection("sprite/"+spriteDir+"/S/");
        spriteImages[SOUTHWEST] = loadDirection("sprite/"+spriteDir+"/SW/");
        spriteImages[WEST] =      loadDirection("sprite/"+spriteDir+"/W/");
        spriteImages[NORTHWEST] = loadDirection("sprite/"+spriteDir+"/NW/");
        imageSet = true;
    }

    /** loads the images from within a directory into an array of
     * images
     * @param path
     * @return
     * @throws IOException
     */
    private Image[] loadDirection(String path) throws IOException{


        File f = new File(FileHandler.getFilesLocation()+path);

        ArrayList<Image> images = new ArrayList<Image>();
        //names will be used when sorting the images
        ArrayList<String> names = new ArrayList<String>();


        int counter = 0;

        //loops through all files in the directory
        for(int i = 0; i< f.listFiles().length; i++){

             //only adds to the image array if it is an image
             if(isImageExtension(getFileExtension(f.listFiles()[i].getName()))){
                 images.add(ImageIO.read(f.listFiles()[i]));
                 //names will be used for sorting
                 names.add(f.listFiles()[i].getName());
                 counter ++;
             }

        }


        /**
         * A Simple selection sort alogorithm to sort the images by their
         * file name. There will never be very many images (10 at most) in a folder, so this
         * sorting method is acceptable.
         *
         * The sort ensures that the sprite will display the frames in numerical order
         */
        Image tempI;
        String tempS;
        for(int i = 0; i< counter; i++){
            for(int j = 0; j< counter; j++){

                //this comparison will sort in ascending order
                if(names.get(i).compareTo(names.get(j))< 0){

                    tempI = images.get(i);
                    images.set(i, images.get(j));
                    images.set(j, tempI);



                    tempS = names.get(i);
                    names.set(i, names.get(j));
                    names.set(j, tempS);

                }
            }
        }


        Image[] image = new Image[counter];
        for(int i = 0; i< counter; i++){

            image[i] = images.get(i);
        }

        return  image;
   
        

    }

    /**
     * Tells if a file extension is an image file extension
     * @param extension the extension to check if it is an image
     * @return true if it is, false if it isnt
     */
    private boolean isImageExtension(String extension){
        if(    extension.equals("png") || extension.equals("PNG") ||
               extension.equals("GIF") ||extension.equals("gif") ||
               extension.equals("jpeg") || extension.equals("jpg") ||
               extension.equals("JPEG") || extension.equals("JPG")){
                 return true;
        }else{
                return false;
        }

    }
    private String getFileExtension(String fileName){
        String[] parts = fileName.split("\\.");
        return parts[parts.length-1];
    }


    public void fromXML(Element spriteElement)throws GameXmlIncorrectException{

        //get the elements for the sprites location and location of its images
        Element location = XMLSimplify.getStrictlySingleElement(spriteElement, "location");
        Element appearance = XMLSimplify.getStrictlySingleElement(spriteElement, "appearance");
        Element variables = XMLSimplify.getStrictlySingleElement(spriteElement, "appearance");



        //sets the variables to equal their XML attribute equivilent
        int mapid = (int)XMLSimplify.getXMLDouble(location, "mapid");
        int xaxis = (int)XMLSimplify.getXMLDouble(location, "xaxis");
        int yaxis = (int)XMLSimplify.getXMLDouble(location, "yaxis");


        String spriteRef = appearance.getAttribute("ref").getValue();
        try{
            this.setSpriteFromFolder(spriteRef);
        }catch(IOException dce){
            throw new GameXmlIncorrectException("The sprite reference folder does not exist.");
        }


        List<Element> elements =  variables.getChildren("gamemap");
         for (int i = 0; i < elements.size(); i++) {
             
            this.setSpriteVar(i, XMLSimplify.getXMLInt(elements.get(i), "variable"));

        }

        
        //set the location of the sprite
        this.setCurrentMap(mapid);
        this.setSpritePosition(new Point(xaxis,yaxis));
        



    }
    public String toXML(){
        return this.toXML("<sprite>","</sprite>");
    }
    public String toXML(String openTag, String closeTag){
 
        
        String xml = "";
        xml += openTag+"\n";

        xml += "<location mapid=\""+currentMap+"\" xaxis=\""+spritePosition.getX()+"\" yaxis=\""+spritePosition.getY()+"\"/>"+ "\n";
        xml += "<appearance ref=\""+spriteNumber+"\" />"+ "\n";
        //spriteVars
        xml += "<variables>";

        for(int i = 0; i < this.getNumSpriteVars(); i++){
            xml += "<variable index=\""+i+"\" value=\""+getSpriteVar(i)+"\" />";
        }

        xml += "</variables>";


        xml += closeTag+ "\n";
        
       
        return xml;
    }
    public String toString(){
        String s = "Sprite coordinates:"+spritePosition+" currentMapID:"+currentMap+".\n";
        for(int i = 0; i < this.getNumSpriteVars(); i++){
             s += "Var "+i+":"+this.getSpriteVar(i)+"\n";
        }

       return s;
   }

  
}















