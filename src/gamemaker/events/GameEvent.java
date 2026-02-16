/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gamemaker.events;

import gamemaker.*;
import gamemaker.Panels.EventPanel;
import gamemaker.Panels.XMLable;
import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import gamemaker.Exceptions.GameXmlIncorrectException;
import gamemaker.Exceptions.IllegalReferenceValueException;
import java.util.List;
import javax.swing.JOptionPane;
import org.jdom.Element;

/**
 *
 * @author ug77alw
 */
public abstract class GameEvent implements XMLable{

    public final int TRIGGER_WALKOVER = 0;
    public final int TRIGGER_ACTIONBUTTON = 1;
    public final int TRIGGER_ENTERMAP = 2;

    public final int TYPE_AREA = 1;
    public final int TYPE_SQUARE = 2;
    public final int TYPE_CIRCLE = 3;
    public final int TYPE_NOAREA = 4;


    //public final int SHAPE = 1;
    //public final int AREA = 2;


    //private int eventShape = SHAPE;
    private int eventTrigger = TRIGGER_ACTIONBUTTON;
    private int eventType = TYPE_AREA;

    private Coordinate initialPoint = new Coordinate(0,0);

    private GradientPaint fillGradient = new GradientPaint(0, 3, new Color(0,0,100), 3, 0, new Color(100,0,0), true);

    
    private Shape theShape;
    private Area eventArea;

    private String eventFunction = "undefined";

    private EventCondition eventCondition = new EventCondition();

    //this is used when creating events. If the event is made, but is never populated with
    //information then this will be set to true so that the program knows to delete
    // the event
    //private boolean creationCancelled = false;

    
    protected EventPanel eventPanel;
    
     public GameEvent(EventPanel eventPanel, String eventFunction){
        this.eventFunction = eventFunction;
        this.eventPanel =  eventPanel;



    }

     public boolean isEventActionAbleToRun(){
         return this.eventCondition.isConditionSatisfied(eventPanel.getGameMaps().getSprite());
     }

     public void setEventArea(Area a){
         eventArea = a;
         eventType = TYPE_AREA;
         //eventShape = AREA;
     }


     public void setEventShape(Coordinate position, int width, int shapeType){
         setEventShape( position,  width, width,  shapeType);
     }
     public void setEventShape(Coordinate position, int width, int height, int shapeType){
      
         //eventShape = SHAPE;
         if(shapeType == this.TYPE_SQUARE){
            this.theShape= new Rectangle2D.Double(position.getX(),position.getY(),width,height);
            eventType = this.TYPE_SQUARE;
        }else if(shapeType == this.TYPE_CIRCLE){
            this.theShape=new Ellipse2D.Double(position.getX(),position.getY(),width,height);
            eventType = this.TYPE_CIRCLE;
        }else{
             System.out.println("Error in shape definition");

        }
     }


    public Area getEventArea(){
        return eventArea;
    }
    public String getEventAreaShapeXML(){
        String trigger = "action";
        String xml = "";
        if(eventTrigger == this.TRIGGER_ACTIONBUTTON){
            trigger = "action";
        }else if(eventTrigger == this.TRIGGER_ENTERMAP){
            trigger = "entermap";
        }else if(eventTrigger == this.TRIGGER_WALKOVER){
            trigger = "walkover";
        }

      
        if(eventType == TYPE_AREA && eventArea != null){

            xml += eventArea.toXML("<eventshape shape=\"area\">","</eventshape>");

        }else if(theShape != null){
            if(eventType == TYPE_SQUARE){
                //square
                xml += "<eventshape shape=\"square\">";
            }else{
                //else circle
                xml += "<eventshape shape=\"circle\">";
            }
            xml += "\n<position width=\""+theShape.getBounds().getWidth()+"\" height=\""+theShape.getBounds().getHeight()+"\" xaxis=\""+theShape.getBounds().getMinX()+"\" yaxis=\""+theShape.getBounds().getMinY()+"\" />";
            xml += "\n</eventshape>\n";

        }else{
            xml += "<error />";
        }
        return xml;
    }


    public String getEventFuntion(){
        return eventFunction;
    }
  


    /**
     * removes the last created event from the event panel. The last created event will
     * be the current event being created. so this should be used if you want to remove an
     * event that has not been completed
     */
    public void deleteMyself(){
        eventPanel.removeCurrentEvent();
    }
    /**
     * This method should be run to configure the event.
     * It returns true if the event confuguration was successful.
     * @return was the event configuration successful
     */

    public void defineEventCondition(){

    }

    /**
     *creates the condition that the even will need to comply to. IF it has been
     * set to be required
     * @param requireCondition
     * @return if the condition was required and the used clicks cancel, then false is
     * returned, else true
     */
    protected boolean createCondition(boolean requireCondition){
        if(requireCondition == true){
        VariableConditionInput vcm = new VariableConditionInput();
            try{
                this.eventCondition = vcm.getInput();
            }catch(IllegalReferenceValueException irve){
                System.err.println(irve);
                irve.printStackTrace();
            }
            //System.out.println("actibe: "+this.eventCondition.isConditionEnabled());
            if(this.eventCondition.isConditionEnabled() == false){
                //at this stage if the condition is false then
                //the user has definitely cancelled
                return false;
            }
        }
        
        return true;
        


    }

    public void configureEvent(boolean requireCondition){
     
            createCondition(requireCondition);
        
    }

   
    public boolean isPointWithin(double x, double y){

        if(this.eventType == this.TYPE_AREA){
            eventArea.isPointWithin((int)x, (int)y);
        }if(theShape != null){
            return theShape.contains(x, y);
        }else{
            return false;
        }

        
    }

  

    public int getTriggerMode(){
        return eventTrigger;
    }

    public void setShapeDiameter(Coordinate c){

        if(eventType != this.TYPE_AREA){
            int x1 = initialPoint.getX();
            int y1 = initialPoint.getY();
            int width = Math.abs(x1 - c.getX());
            int height = Math.abs(y1 -c.getY());

            
            if(x1 - c.getX() > 0){
                x1 = x1 - width;
            }
            if(y1 - c.getY() > 0){
                y1 = y1 - height;
            }

            if(this.eventType == this.TYPE_SQUARE){
                this.theShape = new Rectangle2D.Double(x1,y1,width,height);
            }else  if(this.eventType == this.TYPE_CIRCLE){
                this.theShape = new Ellipse2D.Double(x1,y1,width,height);
            }
           
        }





    }

    public void setDrawShape(Coordinate startPoint,int type){

        //eventShape = SHAPE;
        this.eventType = type;
        initialPoint = startPoint;

        if(this.eventType == this.TYPE_SQUARE){
        
            this.theShape= new Rectangle2D.Double(startPoint.getX(),startPoint.getY(),0,0);
        }else if(this.eventType == this.TYPE_CIRCLE){
          
            this.theShape=new Ellipse2D.Double(startPoint.getX(),startPoint.getY(),0,0);
        }

    }
    public void setDrawMode(int mode){
        //eventShape = mode;
        this.eventType = mode;
        
    }
    public int getDrawMode(){
        return this.eventType;
    }
    
    public void setTriggerMode(int trigger){
        if(trigger <= 2 && trigger >=0){
            this.eventTrigger = trigger;
        }

    }

    /**
     * this function is only really neccessary if the current event is an area
     * because of this any other shape will always return true
     * @return if the area has been completed
     */
    public boolean isComplete(){

        if(this.eventType == this.TYPE_AREA){
            return eventArea.isComplete();
        }else{
            return true;
        }
    }
    public void setComplete(){
        if(eventArea != null){
            eventArea.setComplete();
        }
    }


    public void addPoint(Coordinate c){
        
        //if(eventArea == null || this.eventType != this.TYPE_AREA || eventArea.isComplete()){
        if(eventArea == null ||  eventArea.isComplete()){
            initialPoint.setX(c.getX());
            initialPoint.setY(c.getY());
            eventArea = new Area(eventPanel);
            eventArea.setFillGradient(Color.red, Color.BLUE);
            this.eventType = this.TYPE_AREA ;
            eventArea.addPoint(c);
            
        }else{
            eventArea.addPoint(c);
        }

       
    }
    public boolean isShapeInProgress(){
        if(this.eventType == this.TYPE_AREA){
            return eventArea.isComplete();
        }else{
            return false;
        }

    }
    public boolean isPointWithin(Coordinate c){
        if(this.eventType == this.TYPE_AREA){
            return eventArea.isPointWithin(c.getX(), c.getY());
        }else if(this.eventType == this.TYPE_NOAREA){
            return false;
        }else{
            return theShape.contains(new Point(c.getX(),c.getY()));
        }
    }


    public void drawEvent(Graphics2D g2){


        if(this.eventType == this.TYPE_AREA && eventArea!= null){
           eventArea.setFillGradient(fillGradient);
 
            eventArea.drawArea(g2);
            
        }else{
            if(this.theShape!=null){
                g2.setPaint(fillGradient);
                float alpha = .5f;
                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));

                g2.fill(this.theShape);

                g2.setColor(Color.BLACK);
                g2.setStroke(new BasicStroke(2,BasicStroke.CAP_ROUND,2));
                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));

                g2.draw(this.theShape);
            }
        }

    }



    public boolean eventAction(){
        return false;
    }

    public boolean isPlaced(){
        boolean eventPlaced = true;

        if(this.eventType == this.TYPE_AREA){
            theShape = eventArea.getFinalShape();
        }

        if(theShape != null && eventTrigger != TRIGGER_ENTERMAP){
            eventPlaced = false;
        }
        return eventPlaced;
    }



    public String eventTriggerToString(){
        return eventTriggerToString(this.eventTrigger);
    }


    public void setEventTrigger(String trigger){
        if(trigger.equals("action")){
             setTriggerMode(this.TRIGGER_ACTIONBUTTON);
        }else if(trigger.equals("walkover")){
             setTriggerMode(this.TRIGGER_WALKOVER);
        }else if(trigger.equals("entermap")){
             setTriggerMode(this.TRIGGER_ENTERMAP);
        }else{
            System.out.println("ERROR. Trigger not specified correctly.");
        }
    }
    public String eventTriggerToString(int trigger){
        if(trigger==this.TRIGGER_ACTIONBUTTON){
            return "action";
        }else if(trigger==this.TRIGGER_WALKOVER){
            return "walkover";
        }else if(trigger==this.TRIGGER_ENTERMAP){
            return "entermap";
        }else{
            return "ERROR. Trigger not specified correctly.";
        }
    }

    public void fromXML(Element rootElement)throws GameXmlIncorrectException{
         //assumes the element recieved is an <eventshape> element

        this.eventCondition = new EventCondition(rootElement);


        rootElement = XMLSimplify.getStrictlySingleElement(rootElement, "eventshape");
        String shapeType = rootElement.getAttributeValue("shape");
 
        if(shapeType.equals("area")){

              this.eventType = this.TYPE_AREA;
              //if it is an area then it needs to go down another level into
              //the points elements and their attributes.
              List<Element> points = rootElement.getChildren("point");

              Area area= new Area(this.eventPanel);

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
                  //sets the area if points have been added
                  eventArea = area;
                  
                  
              }




        }else if(shapeType.equals("circle") || shapeType.equals("square")){

             Element position = XMLSimplify.getStrictlySingleElement(rootElement, "position");

             int type = this.TYPE_CIRCLE;
             if(shapeType.equals("circle")){
                 type = this.TYPE_CIRCLE;
          
             }else if(shapeType.equals("square")){
                 type = this.TYPE_SQUARE;
       
             }

             double xaxis = XMLSimplify.getXMLDouble(position, "xaxis");
             double yaxis = XMLSimplify.getXMLDouble(position, "yaxis");
             double width = XMLSimplify.getXMLDouble(position, "width");
             double height = XMLSimplify.getXMLDouble(position, "height");

             //sets the shape to the correct size and position
             setEventShape(new Coordinate(xaxis,yaxis),
                     (int)Math.round(width),(int)Math.round(height), type);
             
        }
    
    }

    public String alwaysToXML(){
        String xml = "";
        xml += this.getEventAreaShapeXML();
        xml += this.eventCondition.toXML();
        return xml;
    }

    public String toXML(){
        return this.toXML("","");
    }

    public String toXML(String openTag, String closeTag){
        String xml = "";
        xml += openTag+"\n";

        

        xml += closeTag+"\n";
        return xml;
    }


}
