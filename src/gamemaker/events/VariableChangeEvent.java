/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gamemaker.events;

import gamemaker.Exceptions.GameXmlIncorrectException;
import gamemaker.Exceptions.PanelNotPassedException;
import gamemaker.GameMap;
import gamemaker.VariableChangeInput;
import gamemaker.XMLSimplify;
import java.util.ArrayList;
import java.util.List;
import org.jdom.Element;

/**
 *
 * @author ug77alw
 */
public class VariableChangeEvent extends GameEvent{

    private GameMap gameMap;
    private VariableChangeInput vci;
    private VariableOperations variableOperations;

    public VariableChangeEvent(GameMap gameMap){
        super(gameMap.getEventPanel(), "variablechange");
        this.gameMap = gameMap;
        variableOperations = new VariableOperations();
    }

    @Override
    public boolean eventAction(){

        if(isEventActionAbleToRun()){


            
            if(variableOperations.isMessageEnabled() == true){
               
                try{
                 

                     /**
                     * The new message must be altered every time because
                     * the variables can change whilst the game is being
                     * played
                     */
                    String[] parts = variableOperations.getMessage().split("[{|}]");
                    String newMessage = "";
                    int index = 0;
                    boolean changeTheMessage = false;
                    for(int i = 0; i< parts.length; i++){

                        if(i%2 == 1){//for every odd position

                            try{
                                index = Integer.parseInt(parts[i]);
                                index = index % 10;

                                newMessage = newMessage + this.gameMap.getSpritePanel().getSprite().getSpriteVar(index);
                                changeTheMessage = true;
                            }catch (NumberFormatException nfe){
                                //if error, discard variable

                            }

                        }else{
                            newMessage = newMessage + parts[i];
                        }
                    }

                    if( changeTheMessage == true){
                        eventPanel.getDialoguePanel().setMessage(newMessage);
                    }else{
                        eventPanel.getDialoguePanel().setMessage(variableOperations.getMessage());
                    }

             
                    gameMap.toggleDialogue();
                }catch(PanelNotPassedException pnpe){
                    System.err.println(pnpe);
                    pnpe.printStackTrace();
                }
            }

            for(int i = 0; i<variableOperations.getArrayList().size(); i++){
                //updates the variable within the sprite
                gameMap.getSpritePanel().getSprite().setVariable(variableOperations.getArrayList().get(i));
            }
         
            return true;
        }else{
            return false;
        }
    }

    @Override
    public void configureEvent(boolean requireCondition){

        if(createCondition(requireCondition)){
            vci = new VariableChangeInput();
            variableOperations = vci.getInput();

            if(variableOperations.getArrayList().size() <= 0){
                //if here then there has been nothing specified therefore
                //the event is redundant and doesn't need to exist
                this.deleteMyself();

            }
        }else{
            this.deleteMyself();
        }



        


    }

    @Override
    public void fromXML(Element rootElement)throws GameXmlIncorrectException{

        //makes sure event shape is handled
        super.fromXML(rootElement);
        //super.fromXML(XMLSimplify.getStrictlySingleElement(rootElement, "eventshape"));

        String messageEnabled = rootElement.getAttributeValue("messageenabled");
        boolean boolmessageEnabled = false;
        if(messageEnabled.toLowerCase().equals("true")){
             boolmessageEnabled =true;
        }else if(messageEnabled.toLowerCase().equals("false")){
            boolmessageEnabled =false;
        }else{
            boolmessageEnabled = false;
            throw new GameXmlIncorrectException("Incorrect message enabled attribute.");
        }


        variableOperations.setArrayList(new ArrayList<VariableOperation>(10));
        
        //gets the operations element
        Element element = XMLSimplify.getStrictlySingleElement(rootElement, "operations");

        //gets all of the variable elements contained within the operations element
        List<Element> elements = element.getChildren("variable");

        for(int i = 0; i< elements.size(); i++){

            //gets the attributes within the variable element
            int index = XMLSimplify.getXMLInt(elements.get(i), "index");
            int value = XMLSimplify.getXMLInt(elements.get(i), "value");
            String operation = elements.get(i).getAttributeValue("operation");

            //add a new operation
            variableOperations.getArrayList().add(new VariableOperation(index,value,operation));

        }
        if(boolmessageEnabled){
            variableOperations.setMessageEnabled(true);
            variableOperations.setMessage(rootElement.getText().trim());
        }else{
            variableOperations.setMessageEnabled(false);
            variableOperations.setMessage("");

        }



    }

    public String toXML(){

        String opening = "<event type=\""+this.getEventFuntion()+"\" " +
                "trigger=\""+this.eventTriggerToString()+"\" messageenabled=\""+variableOperations.isMessageEnabledText()+"\">";
        String closing = "</event>";
        return this.toXML(opening,closing);
    }

    public String toXML(String openTag, String closeTag){
        String xml = "";

        xml += openTag+"\n";
        //String opening = "<eventshape type=\""+this.getEventFuntion()+"\" trigger=\""+this.eventTriggerToString()+"\"/>";
        //String closing = "</eventshape>";

        //xml += this.getEventAreaShapeXML();
        xml += alwaysToXML();

        xml += "<operations>";
        for(int i = 0; i < variableOperations.getArrayList().size(); i++){
            xml += variableOperations.getArrayList().get(i).toXML();
        }
        xml += "</operations>";
        xml += variableOperations.getMessage();

        xml += closeTag+"\n";
        return xml;
    }

}
