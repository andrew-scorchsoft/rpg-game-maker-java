/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gamemaker.events;

import gamemaker.Coordinate;
import gamemaker.GameMaps;
import gamemaker.Panels.EventPanel;
import gamemaker.TeleportInput;
import gamemaker.TeleportLocation;
import java.awt.Point;

/**
 *
 * @author ug77alw
 */
public class teleportEvent extends GameEvent {
    private GameMaps maps;
    //private int toMap = -1;
    //private Coordinate toPlace;
    private TeleportLocation teleportLocation;

    public teleportEvent(EventPanel eventPanel, GameMaps maps){
        super(eventPanel,"teleport");
        this.maps = maps;
        teleportLocation = new TeleportLocation(100,100,-1); //default teleport location to a map that will never exist
    }

    public void setTeleportLocation(TeleportLocation t){
        this.teleportLocation = t;
    }

    public void setTeleportLocation(int mapIndex, Point p){
        teleportLocation = new TeleportLocation((int)p.getX(),(int)p.getY(),mapIndex);
    }
    public void setTeleportLocation(int mapIndex, Coordinate c){
        setTeleportLocation(mapIndex, c.getPoint());
    }
    public TeleportLocation getTeleportLocation(){
        return teleportLocation;
    }

    /**
    public void setTeleportTo(int toMap, Coordinate c){

        teleportLocation.setMapIndex(toMap);
        teleportLocation.setX(c.getX());
        teleportLocation.setY(c.getY());
    }**/

    /**
     * This is what happens when the event is run whilst the game is being played
     * in this case the sprite will teleport to the map and location on that map
     * specified in teleportLocation
     */
    @Override
    public boolean eventAction(){
        if(isEventActionAbleToRun()){
            if(teleportLocation.getMapIndex() != -1){

                maps.setCurrentMap(teleportLocation.getMapIndex());

                maps.getSprite().setCurrentGameMap(maps.getCurrentMap());
                maps.getSprite().setCurrentMap(maps.getCurrentMapIndex());
            

                 maps.getSprite().setSpritePosition(teleportLocation.getPoint());


                //maps.getMapsContainer().repaint();
                //maps.getCurrentMap().paintImmediately(0, 0,  maps.getCurrentMap().getWidth(), maps.getCurrentMap().getHeight());

               // maps.getMapsContainer().setFocusOnSprite(teleportLocation);
                maps.getMapsContainer().setMaximumSize(maps.getCurrentMap().getBackgroundPanel().getSize());
                maps.getMapsContainer().repaint();
                //maps.getCurrentMap().setFocusOnSprite(teleportLocation, maps.getCurrentMap().getMapContainer());


            }else{
                System.err.println("Problem teleporting to map");
            }
            return true;
        }else{
            return false;
        }
    }

    @Override
     public void configureEvent(boolean requireCondition){
        if(createCondition(requireCondition)){

            TeleportInput teleInput = new TeleportInput(maps);
            teleportLocation = teleInput.getInput();

            //deletes itself from the event list if the creation is cancelled
            if(teleportLocation == null){
                deleteMyself();
            }
            maps.repaint();
        }else{
            this.deleteMyself();
        }


    }

    @Override
    public String toXML(){
        String opening = "<event type=\""+this.getEventFuntion()+"\" " +
                "trigger=\""+this.eventTriggerToString()+"\" " +
                "mapid=\""+teleportLocation.getMapIndex()+"\" xaxis=\""+teleportLocation.getX()+"\" yaxis=\""+teleportLocation.getY()+"\">";
        String closing = "</event>";
        return this.toXML(opening,closing);
    }

    @Override
    public String toXML(String openTag, String closeTag){
        String xml = "";
        this.teleportLocation.getMapIndex();
        xml += openTag+"\n";

        
        xml += alwaysToXML();


        xml += closeTag+"\n";
        return xml;
    }


}
