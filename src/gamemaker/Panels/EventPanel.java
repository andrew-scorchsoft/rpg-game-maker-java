/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gamemaker.Panels;

import gamemaker.*;
import gamemaker.Exceptions.PanelNotPassedException;
import gamemaker.events.GameEvent;
import gamemaker.events.soutEvent;
import gamemaker.events.teleportEvent;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import gamemaker.Exceptions.GameXmlIncorrectException;
import gamemaker.events.VariableChangeEvent;
import java.awt.Point;
import java.util.List;
import org.jdom.Element;

/**
 *
 * @author ug77alw
 */
public class EventPanel extends GamePanel implements XMLable{

    private ArrayList<GameEvent> events;

    public final int AREA = 1;
    public final int SQUARE = 2;
    public final int CIRCLE = 3;

    public final int SOUT_EVENT = 1;
    public final int EVENT_TELEPORT = 2;
    public final int EVENT_VARIABLE = 3;

    //private boolean drawingEvent = false;
    private int currentShape = AREA;
    private int currentEventType = SOUT_EVENT;

    private DialoguePanel dialoguePanel;
    private GameMap gameMap;
    private GameMaps gameMaps;


    /**
     * This function deletes all teleport events that teleport to the specified
     * map index. This function is intended to be used when deleting a map
     * and making sure that there are no references to it.
     * @param mapIndex the mapindex to delete events that teleport to it
     */
    public void deleteEvents(int mapIndex){

        for(int i = 0; i<events.size(); i++ ){
            if(events.get(i).getEventFuntion().equals("teleport")){
                TeleportLocation tl = ((teleportEvent)events.get(i)).getTeleportLocation();
                if(tl.getMapIndex() == mapIndex){
                    events.remove(i);
                }
            }
        }

    }

    //private int shapeWidth = 30;
   // private int shapeHeight = 30;

    public EventPanel(GameMap gameMap, GameMaps maps){
        super();
        this.setOpaque(false);
        events = new ArrayList<GameEvent>(20);
        this.gameMap = gameMap;
        this.gameMaps = maps;
    }

    public GameEvent getCurrentEvent(){
        return events.get(events.size()-1);
    }
    public void removeCurrentEvent(){
        if(events.size() > 0){
            events.remove(events.size()-1);
        }else{
            System.err.println("There is no event to delete");
        }
    }

    public void setDialoguePanel(DialoguePanel dialoguePanel){
        this.dialoguePanel = dialoguePanel;
    }
    public DialoguePanel getDialoguePanel() throws PanelNotPassedException{

        if(dialoguePanel == null){
            throw new PanelNotPassedException("DialoguePanel");
        }
        return dialoguePanel;
    }

    public boolean isAreaInProgress(){
        if(events.size()<=0 ){
            return false;
        }else{
            if(events.get(events.size()-1).getDrawMode() == events.get(events.size()-1).TYPE_AREA){
                return events.get(events.size()-1).isComplete();
            }else{
                return false;
            }
        }
    }

    public void triggerAnyEventAtPoint(Coordinate c){
        for(int i = 0; i < events.size(); i++){
            if(events.get(i).isPointWithin(c)){
                events.get(i).eventAction();
            }
        }
    }
    public void triggerActionEventAtPoint(Point p){
        triggerActionEventAtPoint(new Coordinate(Math.round(p.getX()),Math.round(p.getY())));
    }
    public void triggerActionEventAtPoint(Coordinate c){

        boolean stopLoop = false;
        int i = 0;
        //only allows one event to be triggered at a time to prevent issues
        //with dialogue boxes displaying.
        while(i<events.size() && stopLoop == false){

            if(events.get(i).isPointWithin(c) && events.get(i).getTriggerMode() ==  events.get(i).TRIGGER_ACTIONBUTTON){
                stopLoop = events.get(i).eventAction();
            }
            i++;
        }
    }
    public void triggerWalkoverEventAtPoint(Coordinate c){
        for(int i = 0; i < events.size(); i++){
            if(events.get(i).isPointWithin(c) && events.get(i).getTriggerMode() ==  events.get(i).TRIGGER_WALKOVER){
                events.get(i).eventAction();
            }
        }
    }


    public void setEventType(int eventType){
        this.currentEventType = eventType;
    }
    public int getEventType(){
        return this.currentEventType;
    }
   
    public void setCurrentShapeDiameter(Coordinate c){
        if(events.size()>=1 ){
           
            events.get(events.size()-1).setShapeDiameter(c);
        }
        
    }

    public void removeEvent(Coordinate c){
        
        int i = events.size()-1;
        boolean found = false;
        while( i>=0 && found == false){
            if(events.get(i).isPointWithin(c)){
                events.remove(i);
                found = true;
            }
            i--;
        }
        
    }

    public void addEvent(Coordinate position){
 
        

        //int x1 = position.getX() - shapeWidth/2;
        //int y1 = position.getY() - shapeHeight/2;

        //creates a new event of the right type
        
        GameEvent latestEvent = createNewEvent();
      
     
      
        events.add(latestEvent);
        


        //System.out.println("currshape:"+currentShape+" area: "+AREA);

        if(this.currentShape == AREA){
            latestEvent.addPoint(position);
        }else if(this.currentShape == SQUARE){
  
            latestEvent.setDrawShape(position,latestEvent.TYPE_SQUARE);
        }else if(this.currentShape == CIRCLE){

            latestEvent.setDrawShape(position,latestEvent.TYPE_CIRCLE);
        }else{
            System.out.println("ERROR SHAPE NOT DEFINED");
        }

    }


    /**
     *
     * This adds a point to the current polygon being drawn. It only adds a point
     * if the current element being drawn is of type AREA. Otherwise it will just
     * add a shape to the point
     *
     * @param c
     */
    public void addPoint(Coordinate c){
      

   
            if(currentShape == AREA){
                //creates a new event area if the current event area has already been completed
                if(events.size() >= 1){

                    GameEvent latestEvent = events.get(events.size()-1);
                    if(latestEvent.isComplete()){
                        this.addEvent(c);
                     
                    }else{
                        latestEvent.addPoint(c);
                   
                    }

                }else{
                    this.addEvent(c);
              
                }
            }else{
                addEvent(c);
               
            }

        
    }

    public boolean isAreaComplete(){
       
        if(events.size() >=1){
           
            return events.get(events.size()-1).isComplete();

        }else{

            return true;
        }
    }

    /**
     * As the current area is always the last area to be edited
     * it sets the current area in the events array to be complete.
     */
    public void setCurrentAreaComplete(){
        if(events.size() >=1){
            //only sets complete if the area isn't already complete
            if(events.get(events.size()-1).isComplete() == false){
                events.get(events.size()-1).setComplete();
                this.repaint();
                events.get(events.size()-1).configureEvent(this.gameMaps.getEventsConditional());
              
            }
        }
    }
    public void setShapeComplete(){
        if(currentShape != AREA){
            events.get(events.size()-1).configureEvent(this.gameMaps.getEventsConditional());
        
        }
    }



    public void paintComponent(Graphics g){
        Graphics2D g2 = (Graphics2D)g;


        for(int i = 0; i < events.size(); i++){
            if(events.get(i) != null){
                events.get(i).drawEvent(g2);
            }
        }

    }

    /**
     * Creates a new event in accordance to the
     * event trigger and event shape variables
     * @return the new event
     */
    private GameEvent createNewEvent(){
        //the new event to create


        GameEvent event;
        if(currentEventType == SOUT_EVENT){
            event = new soutEvent(this,gameMap);

            event.setTriggerMode(this.currentEventType);
           
            
        }else if(currentEventType == EVENT_TELEPORT){

            event = new teleportEvent(this,gameMaps);
            //always triggered by action button
            event.setTriggerMode(event.TRIGGER_ACTIONBUTTON);
           
        }else if(currentEventType == EVENT_VARIABLE){

            event = new VariableChangeEvent(gameMaps.getCurrentMap());
            //always triggered by action button
            event.setTriggerMode(event.TRIGGER_ACTIONBUTTON);

        }else{
            event = new soutEvent(this,gameMap);
        }



        if(this.currentShape == this.AREA){
                event.setDrawMode(event.TYPE_AREA);
        }else if(this.currentShape == this.CIRCLE){
                event.setDrawMode(event.TYPE_CIRCLE);
        }else if(this.currentShape == this.SQUARE){
                event.setDrawMode(event.TYPE_SQUARE);
        }
        return event;
    }

    
   /**
    * This sets the current draw mode of the events to be created from now on.
    * @param shapeCode the shape code to set the draw mode to. See public variables
    */
    public void setDrawMode(int shapeCode){
        this.currentShape = shapeCode;


        // if drawmode is changed and an area is in progress then
        // the current event is automatically finished.
        if(events.size() > 0){
            GameEvent latestEvent = events.get(events.size()-1);
            if(latestEvent.isShapeInProgress()){
                latestEvent.setComplete();
            }
        }
    }
    public int getEventShapeMode(){
        return this.currentShape;
    }

    public void fromXML(Element rootElement)throws GameXmlIncorrectException{
        List<Element> xmlEvents = rootElement.getChildren("event");

        //process the events one by one and add them to the panel
        for(int i = 0; i<xmlEvents.size(); i++){

            GameEvent e;

            String type = xmlEvents.get(i).getAttributeValue("type");
            String trigger = xmlEvents.get(i).getAttributeValue("trigger");
           

            //creates the correct events based on the type specified in the xml
            if(type.equals("teleport")){
                
                e = new teleportEvent(this, this.gameMaps);


                int mapIndex = XMLSimplify.getXMLInt(xmlEvents.get(i), "mapid");
                int xaxis = XMLSimplify.getXMLInt(xmlEvents.get(i), "xaxis");
                int yaxis = XMLSimplify.getXMLInt(xmlEvents.get(i), "yaxis");
                
                ((teleportEvent)e).setTeleportLocation(mapIndex, new Coordinate(xaxis,yaxis));

            }else if(type.equals("message")){
    
                e = new soutEvent(this, this.gameMap);

                ((soutEvent)e).setMessageText(xmlEvents.get(i).getText().trim());


            }else if(type.equals("variablechange")){

                e = new VariableChangeEvent(this.gameMap);
                ((VariableChangeEvent)e).fromXML(xmlEvents.get(i));

            }else{
                throw new GameXmlIncorrectException("Event attempted to be added that does not exist.");
            }

            e.setEventTrigger(trigger);


            //lets the event handle the processing of the eventshape tag
            e.fromXML(xmlEvents.get(i));



            events.add(e);
        }

    }
     public String toXML(){
        return this.toXML("<events>","</events>");
    }

    public String toXML(String openTag, String closeTag){
        String xml = "";
        xml += openTag+"\n";

        for(int i = 0; i < events.size(); i++){
            if(events.get(i) != null){
                 xml += events.get(i).toXML();
            }
        }

        xml += closeTag+"\n";
        return xml;
    }

    public GameMaps getGameMaps(){
        return gameMaps;
    }


}
