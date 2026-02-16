/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gamemaker;

/**
 *
 * @author Andy
 */
public class TeleportLocation extends Coordinate{
    
    private int mapIndex;

    public TeleportLocation(int x, int y, int mapIndex){
       super(x,y);
       this.mapIndex = mapIndex;
    }

    public void setMapIndex(int index){
        mapIndex = index;
    }
    public int getMapIndex(){
        return mapIndex;
    }

    @Override
    public String toString(){
        return "Teleporting to coordinates: (" +this.getX() + "," + this.getY() + ") on map id: " + this.getMapIndex()+".";
    }
}
