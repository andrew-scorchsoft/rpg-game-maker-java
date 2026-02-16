/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gamemaker.events;

import java.util.ArrayList;

/**
 *
 * @author ug77alw
 */
public class VariableOperations{

    ArrayList<VariableOperation> varOperations;
    private String message;
    private boolean enableMessage;


    public VariableOperations(){
        varOperations = new ArrayList();
    }

    public String isMessageEnabledText(){
        if(enableMessage == true){
            return "true";
        }else{
            return "false";
        }
    }
    
    public boolean isMessageEnabled(){
        return enableMessage;
    }
    public void setMessageEnabled(boolean enabled){
        enableMessage = enabled;
    }

    public String getMessage(){
        return message;
    }
    public void setMessage(String message){
        this.message = message;
    }
    public ArrayList<VariableOperation> getArrayList(){
        return varOperations;
    }
    public void setArrayList(ArrayList<VariableOperation> varOperations){
        this.varOperations = varOperations;
    }

}
