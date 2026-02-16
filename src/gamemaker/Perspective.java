/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gamemaker;

import java.awt.Dimension;
import java.awt.Point;

/**
 *
 * @author ug77alw
 */
public class Perspective {

    private double frontPercentage;
    private double backPercentage;
    //the logical height of the screen for perspective calculations
    private double logicalHeight;
    private double top = 300;
    private final double standardSpriteWidth = 70; //in pixels
   

    public Perspective(){

        this(0.6,1.8);
        //this(0.1,0.3);

        
    }
    public Perspective(double backPercentage, double frontPercentage){
        this.backPercentage = backPercentage;
        this.frontPercentage = frontPercentage;
         logicalHeight = 600;
    }
    public double getStandardSpriteWidth(){
        return standardSpriteWidth;
    }

    public void setDefaultPerspective(){
        backPercentage = 0.6;
        frontPercentage = 1.8;
    }

    /**
     * this method returns the dimensions of an area based on its position on
     * the map and the perspective values
     * @param d the dimensions of the shape to scale
     * @param c the coordinate that the shape is at
     * @return the new dimensions of the shape at that position on the map
     */
    public Dimension getPerspetiveDimensions(Dimension d,Point p){
        double perspective = getPerspectiveAt(p);
        double widthRatio = (d.getHeight()+0.0) / (d.getWidth()+0.0);
        return new Dimension(   (int)(standardSpriteWidth * perspective),
                                (int)(standardSpriteWidth * widthRatio* perspective));
    }

    public void setHorizon(int y){
        this.top = y;
    }

    public double getHorizon(){
        return this.top;
    }

    public double getPerspectiveAt(Point p){
        double position;



        if(p.getY() > 0){
            position = p.getY();
        }else{
            position = 0;
        }
        if(p.getY()<logicalHeight){
            position = p.getY();
        }else{
            position = logicalHeight;
        }

        if(position < top){
            //if the sprite is beyond the horizon, just set their perspective
            //to the absolute back percentage
            return backPercentage;
            
        }else{

            double difference = Math.abs(frontPercentage - backPercentage);

            double positionAsPercentage = (position-top)/((logicalHeight-top)/100);

 
            difference = difference * positionAsPercentage/100;

            double thePerspective;
            if(frontPercentage > backPercentage){
                thePerspective = backPercentage + difference;
            }else{
                thePerspective = backPercentage - difference;
            }
            return thePerspective;
        }


    }

    /**
     * This is to set the logical height of the perspective for
     * calculations. This should be set to the height of the map.
     */
    public void setLogicalHeight(double height){
        logicalHeight = Math.abs(height);
    }

    public void setFrontPercentage(double frontPercentage){
        this.frontPercentage = frontPercentage;
    }
    public  double getFrontPercentage(){
        return frontPercentage;
    }
    public  void setBackPercentage(double backPercentage){
        this.backPercentage = backPercentage;
    }
    public  double getBackPercentage(){
        return backPercentage;
    }
    public String toXML(){
        return this.toXML("","");
    }

    public String toXML(String openTag, String closeTag){
        String xml = "";

        xml += openTag+"\n";

        xml += "<perspective front=\""+this.frontPercentage+"\" back=\""+this.backPercentage+"\" horizon=\""+this.top+"\" />";

        xml += closeTag+"\n";
        return xml;
    }


    /**
    public static void main(String[] args) {
        Perspective p = new Perspective();
        p.setLogicalHeight(100);
        System.out.println(p.getPerspectiveAt(new Coordinate(100,100)));
        
    }**/

}
