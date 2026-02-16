/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gamemaker;

import gamemaker.Panels.GamePanel;
import gamemaker.Panels.XMLable;
import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.util.ArrayList;

/**
 *
 * @author Andy
 */
public class Area implements XMLable{

    private ArrayList<Coordinate> area;
    private boolean shapeComplete;

    private Polygon finalShape;
    private GamePanel panel;

    //default shape colors and styles
    private Color strokeColor = Color.red;
    private BasicStroke strokeStyle = new BasicStroke(3,BasicStroke.CAP_ROUND,2);
    private float fillOpacity = .3f;
    private GradientPaint fillGradient = new GradientPaint(0, 0, new Color(0,0,0), 3, 3, new Color(255,255,255), true);

    public Area(GamePanel panel){
        area = new ArrayList(25);
        shapeComplete = false;
        this.panel = panel;
    }
    public Area(Coordinate startPoint,GamePanel panel){
        area = new ArrayList(25);
        addPoint(startPoint);
        shapeComplete = false;
         this.panel = panel;
    }

 
    public Polygon getFinalShape(){
        return finalShape;
    }
    public int getSize(){
        return area.size();
    }

    public Coordinate getCoordinate(int index){
        return area.get(index);
    }

    public void addPoint(Coordinate point){
        //starts again if the shape had already been completed
        if(shapeComplete == true){
            clearPoints();
            shapeComplete = false;
        }
        area.add(point);
    }
    public void removeLastPoint(){
        int lastPoint = area.size() -1;
        if(lastPoint>=0){
            area.remove(lastPoint);
        }
    }
    public void clearPoints(){
        area.clear();
        finalShape = null;
    }
    public boolean isComplete(){
        return this.shapeComplete;

    }

    public boolean isPointWithin(int x, int y){
        if(finalShape == null){
            return false;
        }else{
            return finalShape.contains(x, y);
        }
    }

    private void createFinalShape(){
        int[] x = new int[area.size()];
        int[] y = new int[area.size()];
        for(int i = 0; i < area.size(); i++){
            x[i] = area.get(i).getX();
            y[i] = area.get(i).getY();
        }
        finalShape = new Polygon(x,y,area.size());
    }

    public void setComplete(){
        this.shapeComplete = true;
        createFinalShape();
    }

    public void setStrokeColor(Color c){
        strokeColor = c;
    }
    public void setStrokeStyle(BasicStroke strokeStyle){
        this.strokeStyle = strokeStyle;
    }
    public void setFillOpacity(float opacity){
        fillOpacity = opacity;
    }
    public void setFillGradient(Color c1, Color c2){
        fillGradient=new GradientPaint(0, 0, c1, 3, 3, c2, true);
    }
    public void setFillGradient(GradientPaint f){
        fillGradient = f;
    }
    public void setFill(Color c1){
        fillGradient=new GradientPaint(0, 0, c1, 3, 3, c1, true);
    }


    public void drawArea(Graphics2D g2){
        
        g2.setColor(strokeColor);
        g2.setStroke(strokeStyle);
        if(isComplete() == false){
       

            for(int i = 0; i<getSize()-1; i++){
                //x1 y1 x2 y2
                int thisIndex = i % getSize();
                int nextIndex = (i + 1) % getSize();

                g2.drawLine(getCoordinate(thisIndex).getX(), getCoordinate(thisIndex).getY(),
                            getCoordinate(nextIndex).getX(), getCoordinate(nextIndex).getY());
            }

        }
        //if shape is complete then it closes the edge of the shape
        if(isComplete() == true && getSize() > 0){
             //g2.drawLine(getCoordinate(0).getX(), getCoordinate(0).getY(),
             //           getCoordinate(getSize()-1).getX(), getCoordinate(getSize()-1).getY());

            

            g2.setPaint(fillGradient);
            // Set alpha.  0.0f is 100% transparent and 1.0f is 100% opaque.
       
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, fillOpacity));
            g2.fill(finalShape);


            g2.setColor(Color.BLACK);
         
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
            g2.draw(finalShape);

        }else if(getSize() > 0 && isComplete() == false){

              g2.drawLine(getCoordinate(getSize()-1).getX(), getCoordinate(getSize()-1).getY(),
                      panel.getLastMousePosition().getX(), panel.getLastMousePosition().getY());
        }



    }
    /**
     * The area is only valid if it consists of 3 or more coordinates
     * @return if the area is valid
     */
    public boolean isValid(){
        if(getSize() >= 3){
            return true;
        }else{
            return false;
        }
    }

    
    public String toXML(){
        return this.toXML("<area>","</area>");
    }
    public String toXML(String openTag, String closeTag){
        String xml = "";
        xml += openTag + "\n";

        for(int i = 0; i < area.size(); i++){
            xml += "<point index=\""+i+"\" xaxis=\""+area.get(i).getX()+"\" yaxis=\""+area.get(i).getY()+"\" />";
            xml += "\n";
        }

        xml += closeTag+ "\n";
        return xml;
    }

    public String toString(){
        String theString ="";
        for(int j = 0; j < area.size(); j++){
            theString += ""+area.get(j).toString()+"\n";
        }
     
        return theString;
    }

}
