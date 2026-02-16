/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gamemaker.Exceptions;

/**
 *
 * @author ug77alw
 */
public class PanelNotPassedException extends Exception{
    private String panelType;
    public PanelNotPassedException(String panelType){
        this.panelType = panelType;
    }
     public String toString(){
        return "The Panel " + this.panelType + " has not been set in the object. The Function requires to use the panel ";
    }

}
