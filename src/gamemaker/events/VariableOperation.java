/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gamemaker.events;

import gamemaker.*;

/**
 *
 * @author ug77alw
 */
public class VariableOperation {



    private int indexToChange;
    private int value;
    private int operation;

    public static final int NOTHING = -1;
    public static final int ADD = 0;
    public static final int SUBTRACT = 1;
    public static final int SET = 2;
    

    public VariableOperation(){
        indexToChange = -1;
        value = 0;
        operation = 0;

    }
    public VariableOperation(int indexToChange, int value, int operation){
        setUp(indexToChange, value, operation);
    }
    public VariableOperation(int indexToChange, int value, String operation){
        setUp(indexToChange, value, this.operationFromString(operation));
    }
  

    public void setUp(int indexToChange, int value, int operation){
        this.value = value;
        this.indexToChange = indexToChange;
        this.operation = operation;
    }

    public void applyChange(Sprite s)throws ArrayIndexOutOfBoundsException{

        if(indexToChange < s.getNumSpriteVars()&& indexToChange >= 0 ){

            if(operation == this.ADD){
                
                s.setSpriteVar(indexToChange, s.getSpriteVar(indexToChange)+value);
            
            }else if(operation == this.SUBTRACT){

                s.setSpriteVar(indexToChange, s.getSpriteVar(indexToChange)-value);
            
            }else if(operation == this.SET){

                s.setSpriteVar(indexToChange, value);

            }

        }else{
            throw new ArrayIndexOutOfBoundsException("incorrect sprite variable index.");
        }


    }

    public int operationFromString(String string){

        if(string.equals("add")){
            return this.ADD;
        }else if(string.equals("subtract")){
            return  this.SUBTRACT;
        }if(string.equals("set")){
            return  this.SET;
        }else{
            return  this.NOTHING;
        }

    }
    public String operationToString(){
        String s = "nothing";
        if(operation == this.ADD){

               s = "add";

        }else if(operation == this.SUBTRACT){

               s = "subtract";

        }else if(operation == this.SET){

               s = "set";

        }
        return s;
    }


     public String toXML(){

        String opening = "<variable index=\""+this.indexToChange+"\" " +
                "value=\""+this.value+"\" " +
                "operation=\""+this.operationToString()+"\" />";
        String closing = "";
        return this.toXML(opening,closing);
    }

    public String toXML(String openTag, String closeTag){
        String xml = "";
        xml += openTag;
        xml += closeTag;

        return xml;
    }


}
