/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gamemaker.Panels;

import gamemaker.*;
import java.awt.Graphics2D;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.JPanel;
import gamemaker.Exceptions.GameXmlIncorrectException;
import java.awt.Graphics;
import java.io.File;
import java.util.List;
import org.jdom.Element;

/**
 *
 * @author ug77alw
 */
public class ForegroundPanel extends GamePanel implements XMLable, GlassPaneDrawer {


    private ArrayList<ForegroundItem> items;
    private GameMap gameMap;
    private int selectedItem = -1;
    private JPanel glassPanel;

    public ForegroundPanel(GameMap gameMap, JPanel glassPanel){
        super();
        items = new ArrayList<ForegroundItem>(20);
        this.gameMap = gameMap;
        this.setOpaque(false);
        this.setLayout(null);
        this.glassPanel = glassPanel;
      
    }

  
    public void paintComponentTwo(Graphics g){
        for(int i = items.size()-1; i >= 0; i--){
        
            items.get(i).paintComponentTwo(g);
        }
    }
    /**
     * Saves the images used by all of the foreground items
     * @param location the location to store then EXCLUDING the file name.
     * The file name will be automatically generated
     * @throws IOException if there is a problem saving the file
     */
    public void saveImages(String location)throws IOException{

        for(int i = 0; i<items.size(); i++){
            //xml += "<foregrounditem img=\""+items.get(i).getName()+"\" xaxis=\"300\" yaxis=\"100\" />";
            //xml += "\n";
       
            items.get(i).saveImage(location+"foreground-");
        }


    }
    public void addImage(Coordinate c, ImageWithDetails i)throws IOException{
        ForegroundItem newItem = new ForegroundItem(this.gameMap);
        newItem.setImage(c,i);
        items.add(newItem);
        this.add(newItem);
     
        this.repaint();
    }
    public void addImage(Coordinate c, String location)throws IOException{

        
        addImage(c,FileHandler.imageFromFile(FileHandler.getFilesLocation()+location));
        //addImage(c,FileHandler.imageFromURL(FileHandler.getFilesURL(location)));
        //addImage(c,FileHandler.imageFromURL(gameMap.getClass().getResource("files/"+location)));
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
    public int checkSelectedResizer(Coordinate c){
        int found = -1;
        if(selectedItem > -1 && items.size() > 0){
            found = items.get(selectedItem).checkResizers(c);
        }
        return found;
    }

    /**
     *
     * @return the id of the selected item. Returns -1 if nothing
     * is selected
     */
    public int getSelectedItem(){
        return selectedItem;
    
    }
    /**
    @Override
    public void paintComponent(Graphics g){
        for(int i = 0; i < this.getComponentCount(); i++){
            this.getComponent(i).paintAll(g);
        }
    }**/

    /**
     *
     * @param c the coordinate to check if an item under it can be selected
     * @return returns if an item has been selected
     */
    public boolean select(Coordinate c){
        int i = items.size()-1;
        boolean found = false;
        while(i>=0 && found == false){

            //if forground item is within coordinate
            if(items.get(i).isPointWithin(c)){
                //sets selected index
                selectedItem = i;
                found = true;

            }

            i--;
        }
        if(found == false){
            //this is the index for not selected
            this.deSelectAll();
        }else{
            //brings the selected item to the front
             this.setComponentZOrder(items.get(selectedItem), 0);
       
        }

        return found;
    }
    public void deSelectAll(){
        selectedItem = -1;
    }
    private void swapItemPositions(int first, int second){
        ForegroundItem temp = items.get(first);
        items.set(first, items.get(second));
        items.set(second, temp);
    }

    public void moveSelected(Coordinate from, Coordinate to){
        if(selectedItem >= 0 && selectedItem < items.size()){
            items.get(selectedItem).startMoveItem(from, to);
            this.repaint();
        }
    }

    public void resizeSelected(int ResizerIndex, Coordinate from, Coordinate to){
        items.get(selectedItem).resizeItem(ResizerIndex, from, to);
        this.repaint();
    }
    public void setFinishResize(){
        items.get(selectedItem).finishResize();
    }

    public void removeImage(Coordinate c){

        int i = items.size()-1;
        boolean found = false;
        while(i>=0 && found == false){

            if(items.get(i).isPointWithin(c)){
                
                this.remove(items.get(i));
                items.remove(i);
                found = true;

            }

            i--;
        }

    }

    public void fromXML(Element rootElement)throws GameXmlIncorrectException{
        
        List<Element> elements = rootElement.getChildren("foregrounditem");

        //foreground items are not required to be set. None may be added.
        for(int i = 0; i < elements.size(); i++){

            //the the location and the image of the foreground item
            String img = elements.get(i).getAttributeValue("img");
            int xaxis = XMLSimplify.getXMLInt(elements.get(i), "xaxis");
            int yaxis = XMLSimplify.getXMLInt(elements.get(i), "yaxis");
        

          
            try{
                this.addImage(new Coordinate(xaxis,yaxis), "gamesave"+File.separator+img);
            }catch(IOException ioe){
                throw new GameXmlIncorrectException("Unable to add foreground item. The Image '"+img+"' could not be found.");
            }


        }

    }
     public String toXML(){
        return this.toXML("<foreground>","</foreground>");
    }
    public String toXML(String openTag, String closeTag){
        String xml = "";
        xml += openTag+"\n";
        int x = 0;
        int y = 0;
        for(int i = 0; i<items.size(); i++){

            x = (int)Math.round(items.get(i).getLocation().getX()) + items.get(i).getWidth()/2;
            y = (int)Math.round(items.get(i).getLocation().getY()) + items.get(i).getHeight()/2;
            //y = items.get(i).getImageLocation().getY();
            xml += "<foregrounditem img=\"foreground-"+items.get(i).getImageHashCode()+".png\" xaxis=\""+x+"\" yaxis=\""+y+"\" />";
            xml += "\n";
        }



        xml += closeTag+"\n";
        return xml;
    }

   
    public void drawOnGlass(Graphics2D g){
        Graphics2D g2 = (Graphics2D)g;

        if(selectedItem  > -1 && selectedItem  < items.size()){
            items.get(selectedItem).drawResizers(g2);
        }
    }



}
