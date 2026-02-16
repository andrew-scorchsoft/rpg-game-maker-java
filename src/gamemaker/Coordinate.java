/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gamemaker;

import java.awt.Point;

/**
 *
 * @author Andy
 */
public class Coordinate {

    int x;
    int y;

    public Coordinate(int x, int y){
        this.x = x;
        this.y = y;
    }
    public Coordinate(double x, double y){
        this.x = (int)Math.round(x);
        this.y = (int)Math.round(y);
    }
    public Coordinate(){
        this.x = 0;
        this.y = 0;
    }
     public Coordinate(Point p){
        setCoordinate(p);
    }

    

    public void setCoordinate(Point p){
        this.x = (int)Math.ceil(p.getX());
        this.y = (int)Math.ceil(p.getY());
    }

    public int getX(){
        return this.x;
    }
    public int getY(){
        return this.y;
    }
    public void setX(int x){
        this.x = x;
    }
    public void setY(int y){
        this.y = y;
    }
    public String toString(){
        return "[x:"+this.x+" y:"+this.y+"]";
    }
    public Point getPoint(){
        return new Point(this.x,this.y);
    }
    




}
