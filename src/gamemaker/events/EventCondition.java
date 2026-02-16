/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gamemaker.events;

import gamemaker.Exceptions.GameXmlIncorrectException;
import gamemaker.Exceptions.IllegalReferenceValueException;
import gamemaker.Panels.XMLable;
import gamemaker.Sprite;
import gamemaker.XMLSimplify;
import java.util.List;
import org.jdom.Element;

/**
 *
 * @author Andy
 */
public class EventCondition implements XMLable{

    private int variableIndex1 = -1;
    private int variableIndex2 = -1;
    private int value = 0;
    private int operation = -1;
    
    private boolean conditionEnabled = true;

    /**
     * if false then reference value, 
     * if true reference variable index 2
     */
    private boolean valOrVarCondition = false;

    public static final boolean USE_VALUE = true;
    public static final boolean USE_VARIABLE = false;

    public static final int GREATER_THAN = 0;
    public static final int LESS_THAN = 1;
    public static final int EQUAL_TO = 2;
    public static final int GREATER_THAN_OR_EQUAL_TO = 3;
    public static final int LESS_THAN_OR_EQUAL_TO = 4;
    
    public static final int NOT_EQUAL_TO = 5;



    /**
     *
     * @param varIndex1 the variable index to compare against
     * @param valOrVarIndex this is either the value to compare against or the index
     * of the variable to compare against. the boolean varOrVal dictates which
     * it is and the method handles the rest
     * @param operator the reference of the comparison function to use. See public
     * variables for what these are
     * @param varOrVal whether valOrVarIndex references a variable index
     * or a value
     * @throws IllegalReferenceValueException if an incorrect operator index
     * has been used.
     */
    public EventCondition(int varIndex1, int valOrVarIndex, int operator, boolean varOrVal) throws IllegalReferenceValueException{
        variableIndex1 = varIndex1;

    
        this.variableIndex2 = valOrVarIndex;
        this.value = valOrVarIndex;

        this.valOrVarCondition = varOrVal;

        if(operator >= 0 && operator <=5){
            operation = operator;
        
        }else{
            throw new IllegalReferenceValueException("Operator must be >0 and <=5");
        }
    }
    public EventCondition(){
         conditionEnabled = false;
    }
    public EventCondition(Element e)throws GameXmlIncorrectException{
        this.fromXML(e);
         
    }

    public boolean isConditionEnabled(){
        return conditionEnabled;
    }

    /**
     * compares the values against the operation and returns if the condition
     * has been met
     * @param value1 the value to check
     * @param value2 the value to check against
     * @return if the condition is met
     */
    private boolean compare(int value1, int value2){
        if(this.operation == this.GREATER_THAN){
            if(value1 > value2){
                return true;
            }else{
                return false;
            }
        }else if(this.operation == this.LESS_THAN){
            if(value1 < value2){
                return true;
            }else{
                return false;
            }
        }else if(this.operation == this.GREATER_THAN_OR_EQUAL_TO){
            if(value1 >= value2){
                return true;
            }else{
                return false;
            }
        }else if(this.operation == this.LESS_THAN_OR_EQUAL_TO){
            if(value1 <= value2){
                return true;
            }else{
                return false;
            }
        }else if(this.operation == this.EQUAL_TO){
            if(value1 == value2){
                return true;
            }else{
                return false;
            }
        }else if(this.operation == this.NOT_EQUAL_TO){
            if(value1 != value2){
                return true;
            }else{
                return false;
            }
        }else{
            return false;
        }


        
    }

    /**
     * verified if the conditions have been met
     * @param s the sprite that contains the variables
     * @return if the conditions have been met
     */
    public boolean isConditionSatisfied(Sprite s){

        if(conditionEnabled == true){

            if(valOrVarCondition == USE_VARIABLE){
            
            
                return compare(s.getSpriteVar(this.variableIndex1),s.getSpriteVar(this.variableIndex2));

            }if(valOrVarCondition == USE_VALUE){
           
              
                return compare(s.getSpriteVar(this.variableIndex1),this.value);
            }else{
                //else will never be run as both conditions are for both boolean conditions
                return false;
            }

        }else{
            //if conditionEnabled == false then the condition is not enabled
            //therefore it will always be satisfied
            return true;
        }

        
    }


    public void enableCondition(){
        conditionEnabled = true;
    }
    public void disableCondition(){
        conditionEnabled = false;
    }


    public String toXML(){

        String opening = "";
        String closing = "";
        return this.toXML(opening,closing);
    }

    public String toXML(String openTag, String closeTag){
        String xml = "";

        xml += openTag+"\n";

        String varOrVal = "";
        int v2 = 0;
        if(valOrVarCondition == this.USE_VALUE){
            varOrVal = "value";
            v2 = value;
        }else if(valOrVarCondition == this.USE_VARIABLE){
            varOrVal = "variable";
            v2 = this.variableIndex2;
        }

        String enabled = "";
        if(this.conditionEnabled){
            enabled = "true";
        }else{
            enabled = "false";
        }



        xml += "<eventcondition v1=\""+this.variableIndex1+"\" " +
                "v2=\""+v2+"\" " +
                "varorval=\""+varOrVal+"\" " +
                "operation=\""+this.operation+"\" " +
                "enabled=\""+enabled+"\" />";


        xml += closeTag+"\n";
        return xml;
    }


    private boolean varOrValFromString(String varorval) throws GameXmlIncorrectException{
        varorval =  varorval.toLowerCase();
        if(varorval.equals("value")){
            return this.USE_VALUE;
        }else if(varorval.equals("variable")){
           return this.USE_VARIABLE;
        }else{
            throw new GameXmlIncorrectException("varorval variable incorrectly defined.");
        }

    }

    public void fromXML(Element element)throws GameXmlIncorrectException{

        List<Element> elements = element.getChildren("eventcondition");
        int size = elements.size();
        
        if(size <= 0){
            conditionEnabled = false;
        }else if(size == 1){

            this.variableIndex1 = XMLSimplify.getXMLInt(elements.get(0), "v1");
            this.variableIndex2 = XMLSimplify.getXMLInt(elements.get(0), "v2");
            this.value = this.variableIndex2;
            //this.valOrVarCondition = XMLSimplify.getXMLBool(element, "varorval");
            this.valOrVarCondition = varOrValFromString(elements.get(0).getAttribute("varorval").getValue());
            int opcode = XMLSimplify.getXMLInt(elements.get(0), "operation");
            this.conditionEnabled = XMLSimplify.getXMLBool(elements.get(0), "enabled");


            if(opcode <-1 || opcode >=6){
                throw new GameXmlIncorrectException("operation must be between 0 and 5");
            }else{
                this.operation = opcode;
            }




        }else{
            throw new GameXmlIncorrectException("There can only be one event condition element per event.");
        }

    }



}
