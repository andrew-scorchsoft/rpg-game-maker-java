/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gamemaker;

import gamemaker.Exceptions.GameXmlIncorrectException;
import gamemaker.Panels.XMLable;
import java.awt.Point;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.jdom.Element;

/**
 *
 * @author ug77alw
 */
public class GameMaps implements XMLable{

    //A list of all the maps in the game
    private ArrayList<GameMap> maps;
    //the index of the current map being played/edited
    private int currentMap;
    //the frame that is containing the game maps at the moment
    private MapsContainer mapContainer;
    //the sprite that is persistant across the game
    private Sprite gameSprite;

    private boolean enableConditionalEvents = false;
    private PaintPanel paintPanel; //used for double buffering

  


    public void setEventsConditional(boolean status){
        enableConditionalEvents = status;
    }
    public boolean getEventsConditional(){
        return enableConditionalEvents;
    }


    public GameMaps(MapsContainer r){


        maps = new ArrayList<GameMap>();
        setMapsContainer(r);
        this.gameSprite = new Sprite();
        currentMap = 0; //defaults to the first map
        addNewMap();
        //by default sets the sprite to be on the first map
        this.gameSprite.setCurrentGameMap(getCurrentMap());
        paintPanel = new PaintPanel(this);
        
        
    }
    public PaintPanel getPaintPanel(){
        return paintPanel;
    }
    public boolean isPaintPanelSet(){
        if(paintPanel != null){
            return true;
        }else{
            return false;
        }
    }
    

    public void setMapsContainer(MapsContainer r){
        this.mapContainer = r;
        for(int i = 0; i<maps.size(); i++){
            maps.get(i).setMapsContainer(r);
        }
    }

    /**
     *
     * Removes all events that reference the map index mapIndex
     * @param mapIndex
     */
    public void removeEventReferences(int mapIndex){
        for(int i = 0; i < maps.size(); i++){
            maps.get(i).getEventPanel().deleteEvents(mapIndex);
        }
    }

    public void removeMap(int index){
        removeEventReferences(index);
        this.maps.remove(index);
        currentMap = currentMap -1;

        if(this.getSprite().getCurrentMap() == index){
            this.getSprite().setCurrentGameMap(maps.get(0));
            this.getSprite().setCurrentMap(0);
            this.getSprite().setSpritePosition(new Point(200,200));
        }
    }

    public void removeCurrentMap(){
        if(maps.size() >=2 && currentMap >=0){
            removeMap(this.currentMap);
            this.currentMap = maps.size() -1;
        }
    }

    /**
    * Saves the images used by all of the maps within the game.
    * It does so to a default location
    */
    public void saveImages()throws IOException{
        saveImages(FileHandler.getFilesLocation());
    }

    

   

    /**
    * Saves the images used by all of the maps within the game
    * @param location   the location to save the images EXCLUDING their file name
    * @throws IOException if there is an issue saving the files
    */
    public void saveImages(String location)throws IOException{

        for(int i = 0; i < maps.size(); i++){
            maps.get(i).saveImages(location+"gamesave"+File.separator);
        }
    }
    /**
     *
     * @return the map container containing the game maps at the moment
     */
    public MapsContainer getMapsContainer(){
        return mapContainer;
    }
 

    public Sprite getSprite(){
        return gameSprite;
    }

    public void setDefaultSpriteLocation(Coordinate c){
        gameSprite.setStartPosition(this.getCurrentMap(),this.getCurrentMapIndex(), c.getPoint());
    }
    public void setCurrentMap(int index){
        if(index <= maps.size()-1 && index >-1){
            currentMap = index;
         
            this.paintPanel.setSize(this.getCurrentMap().getBackgroundPanel().getSize());
            this.paintPanel.setPreferredSize(this.getCurrentMap().getBackgroundPanel().getSize());
            this.paintPanel.updateImage();
        }else{
            System.err.println("GameMaps Error, index out of range");
        }
    }
    public int getCurrentMapIndex(){
        if(currentMap >= maps.size()){
            currentMap = -1;
        }
        return currentMap;
    }
    public GameMap getCurrentMap(){
        if(maps.size() > 0){
            return maps.get(getCurrentMapIndex());
        }else{
            return null;
        }
    }
    public int size(){
        return maps.size();
    }
    public GameMap getMap(int i){
        return maps.get(i);
    }
    public void addNewMap(){

        GameMap newMap = new GameMap(mapContainer,gameSprite,this);
        newMap.setBackgroundImageDefault();
        maps.add(newMap);

        currentMap = maps.size()-1;



    }
    public void repaint(){
        for(int i =0; i< maps.size(); i++){
            maps.get(i).repaint();
        }
    }
     public void superRepaint(){
        for(int i =0; i< maps.size(); i++){
            maps.get(i).superRepaint();
        }
    }
     public void clearAllMarkers(){
        for(int i =0; i< maps.size(); i++){
            maps.get(i).getGlassPanel().clearMarker();
        }
    }
    public void repaintImmediately(){
        for(int i =0; i< maps.size(); i++){
            maps.get(i).paintImmediately(0, 0, maps.get(i).getWidth(), maps.get(i).getHeight());
        }
    }
    /**
     * Sets all maps to their play display mode
     * this is because not all layers are required to be painted
     * in this mode
     */
    public void setPlayMode(){
        for(int i =0; i< maps.size(); i++){
            maps.get(i).setPlayMode();
        }
    }
    public void setEditMode(){
         for(int i =0; i< maps.size(); i++){
            maps.get(i).setEditMode();
        }
    }

    public String toXML(){
        return this.toXML("<game>","</game>");
    }

    public void removeAllMaps(){
         for (int i = 0; i < maps.size(); i++) {
             maps.remove(i);
         }

    }

    public void fromXML(Element rootElement)throws GameXmlIncorrectException{


        removeAllMaps();
        List<Element> sprite = rootElement.getChildren("sprite");

        //Only one sprite tag is allowed. And there must be at least one
        if(sprite.size() > 1){
            throw new GameXmlIncorrectException("Game XML must define only one sprite.");
        }else if(sprite.size() < 1){
            throw new GameXmlIncorrectException("Game XML must define a sprite");
        }
        this.getSprite().fromXML(sprite.get(0));


        List<Element> elements = rootElement.getChildren("gamemap");
        //  loops through the elements that it needs to create
        // for the maps.

        for (int i = 0; i < elements.size(); i++) {
            
            addNewMap();
            this.maps.get(i).fromXML(elements.get(i));
           
        }

        // sets the map that the sprite will appear on to be the same map that is
        // referenced in the sprites currentmap index.
        
        this.gameSprite.setCurrentGameMap(maps.get(this.gameSprite.getCurrentMap()));
  




    }
    public String toXML(String openTag, String closeTag){
        String xml = "<?xml version=\"1.0\" encoding=\"utf-8\" ?>";

        xml += "\n"+openTag+"\n";
        //this.backgroundPanel.to

        xml +=this.getSprite().toXML();
        xml += "\n";
        for(int i =0; i< maps.size(); i++){
             xml += maps.get(i).toXML("<gamemap id=\""+i+"\">","</gamemap>");
        }


        xml += closeTag+"\n";
        return xml;
    }

}
