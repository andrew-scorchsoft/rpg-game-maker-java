/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gamemaker.events;

import gamemaker.Exceptions.PanelNotPassedException;
import gamemaker.GameMap;
import gamemaker.MessageInput;
import gamemaker.Panels.EventPanel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 *
 * @author ug77alw
 */
public class soutEvent extends GameEvent implements ActionListener{
    String theMessage = "";
    private GameMap gameMap;
    private MessageInput messageInput;


    public soutEvent(EventPanel eventPanel, GameMap gameMap){
        super(eventPanel,"message");
        this.gameMap = gameMap;

    }

     public void setMessageText(String s){
         this.theMessage = s;
     }

    @Override
    public void configureEvent(boolean requireCondition){
   
        if(createCondition(requireCondition)){
            messageInput = new MessageInput(this);
            theMessage = messageInput.getInput();
        }else{
            this.deleteMyself();
        }

     
    }

    @Override
    public boolean eventAction(){
        if(isEventActionAbleToRun()){
            try{
                 
                    /**
                     * The new message must be altered every time because
                     * the variables can change whilst the game is being
                     * played
                     */
                    String[] parts = theMessage.split("[{|}]");
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
                        eventPanel.getDialoguePanel().setMessage(theMessage);
                    }


                    


                    
                    gameMap.toggleDialogue();

            }catch(PanelNotPassedException pnpe){
                System.err.println(pnpe);
                pnpe.printStackTrace();
            }
            return true;
        }else{
            return false;
        }
    }
    public void actionPerformed(ActionEvent e){
        if(e.getActionCommand().equals("setmessage_ok")){
            //nothing, just closes textbox.
            //theMessage is already set via message.getInput(); in configureEvent()
            if(messageInput != null){
                messageInput.closeDialogue();
            }
           
        }else if(e.getActionCommand().equals("setmessage_cancel")){
            this.deleteMyself();
            if(messageInput != null){
                messageInput.closeDialogue();
            }
        }
     }
    public String toXML(){

        String opening = "<event type=\""+this.getEventFuntion()+"\" " +
                "trigger=\""+this.eventTriggerToString()+"\" >";
        String closing = "</event>";
        return toXML(opening,closing);
    }

    public String toXML(String openTag, String closeTag){
        String xml = "";

        xml += openTag+"\n";
        //String opening = "<eventshape type=\""+this.getEventFuntion()+"\" trigger=\""+this.eventTriggerToString()+"\"/>";
        //String closing = "</eventshape>";

        //xml += this.getEventAreaShapeXML();
        xml += alwaysToXML();
    
        xml += "\n"+theMessage+"\n";
        xml += closeTag+"\n";
        return xml;
    }


    public static void main(String[] args) {
        String s = "hello } how";
         String[] parts = s.split("[{|}]");

         System.out.println(parts[0]);
    }
}
