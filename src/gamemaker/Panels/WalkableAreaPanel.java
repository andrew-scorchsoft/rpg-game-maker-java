/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gamemaker.Panels;

import gamemaker.*;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import gamemaker.Exceptions.GameXmlIncorrectException;
import java.awt.Point;
import java.util.List;
import org.jdom.Element;

/**
 *
 * @author ug77alw
 */
public class WalkableAreaPanel extends GamePanel implements XMLable {

    ArrayList<Area> areas;


    // int currentArea;

    public WalkableAreaPanel(){
        super();

        //currentArea = -1;
        //makes the panel transparent
        this.setOpaque(false);
        areas = new ArrayList(20);
    }
    
   public void newWalkableArea(){
        areas.add(new Area(this));
        //currentArea = areas.size();
    }



    public void addPoint(Coordinate c){
        if(areas.size() <= 0 ){
            newWalkableArea();
        }

        if(areas.get(areas.size()-1).isComplete()){
            //if the latest area has been completed, a new area will need to be created
            newWalkableArea();
            areas.get(areas.size()-1).addPoint(c);
        }else{
            //if the latest area isn't completed
            //then a new point is added to the area.
            areas.get(areas.size()-1).addPoint(c);
        }
    }
    public void deleteShapeAtPoint(Coordinate c){
        int i = areas.size()-1;
        //defaults to within a shape
        boolean within = false;
        //exits the loop if found to be within a shape
        //goes from last shape added backwards
        while(i>=0 && within == false){

            if(areas.get(i).isPointWithin(c.getX(), c.getY()) == true){
                within = true;
            }

            if(within == false){
                i--;
            }
        }

        //if within = true then i is the shape to be deleted
        if(within ==true){
            areas.remove(i);
        }


    }

    public boolean isShapeInProgress(){
        if(areas.size()<=0 ){
            return true;
        }else{
            return areas.get(areas.size()-1).isComplete();
        }
    }

    public void setCurrentComplete(){
        if(areas.size() > 0){
            areas.get(areas.size()-1).setComplete();
        }
    
    }

    public boolean isPointWithin(Point p){
         return isPointWithin(new Coordinate(p.getX(),p.getY()));

    }

    public boolean isPointWithin(Coordinate c){
        int i = 0;
        //defaults to within a shape
        boolean within = false;
        //exits the loop if found to be within a shape
        while(i<areas.size() && within == false){

            if(areas.get(i).isPointWithin(c.getX(), c.getY()) == true){
                within = true;
            }

            i++;
        }
        return within;
    }


    public void fromXML(Element rootElement)throws GameXmlIncorrectException{
         List<Element> areas = rootElement.getChildren("area");

         for(int i = 0; i < areas.size(); i++){

              List<Element> points = areas.get(i).getChildren("point");
              

              Area area= new Area(this);
              
              for(int j = 0; j < points.size(); j++){
                
                int xaxis = XMLSimplify.getXMLInt(points.get(j), "xaxis");
                int yaxis = XMLSimplify.getXMLInt(points.get(j), "yaxis");

                area.addPoint(new Coordinate(xaxis,yaxis));

                if(j == points.size()-1){
                    //completes the area if the last element has been added
                    area.setComplete();
                }
                  
              }
              //only adds the area if it contains points
              if(points.size() > 0){
                  this.areas.add(area);
              }

         }

    }

    public String toXML(){
        return this.toXML("<walkover>","</walkover>");
    }
    public String toXML(String openTag, String closeTag){
        String xml = "";
        xml += openTag+"\n";

        for(int i = 0; i<areas.size(); i++){
            xml += areas.get(i).toXML()+"\n";
        }
      


        xml += closeTag+"\n";
        return xml;
    }

  
    public void paintComponent(Graphics g){
        Graphics2D g2 = (Graphics2D)g;
  

        for(int i = 0; i < areas.size(); i++){
            areas.get(i).drawArea(g2);
        }

    }

}
