/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gamemaker.Panels;

import gamemaker.Coordinate;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.util.ArrayList;
import javax.swing.JPanel;

/**
 *
 * @author ug77alw
 */
public class GlassPanel extends JPanel{

    //allows multiple panels to use the glasspanel
    //HashMap<String,GlassPaneDrawer> panels;
    private ArrayList<GlassPaneDrawer> panels;
    private Image marker;
    private Coordinate markerPlace;

    public GlassPanel(){
        super();
        this.setOpaque(false);
        //panels = new HashMap(10);
        panels = new ArrayList(10);
    }

    //assigns a panel to use the glasspane
    public void addAssociation(GlassPaneDrawer panel){
        if (panels.contains(panel) == false){
            panels.add(panel);
        }
        /**
        if(!panels.containsValue(panel)&& !panels.containsKey(name)){
            panels.put(name,panel);
       
        }**/
    }
    public void setMarker(Image i){
        marker= i;
    }
     public void setMarker(Image i, Coordinate c){
        marker= i;
        markerPlace = c;
    }
      public void setMarkerPosition(Coordinate c){

        markerPlace = c;
    }
    public void clearMarker(){
        this.markerPlace = null;
    }

    public void paintComponent(Graphics g){
        Graphics2D g2 = (Graphics2D)g;
            //if a panel has been assigned, it draws their glasspane
            //function onto the glass pane
            if(marker != null && markerPlace != null){
                g2.drawImage(marker,markerPlace.getX()-(marker.getWidth(this)/2), markerPlace.getY()-marker.getHeight(this), this);
            }
            if(panels.size()>0){
                //Object[] p = panels.values().toArray();
                for(int i = 0; i<panels.size(); i++){
                    panels.get(i).drawOnGlass(g2);
                }

            }
    }


}
