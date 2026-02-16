/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gamemaker;

/**
 *
 * @author Andy
 */
public class NumberFunctions {

    public static boolean validateInteger(String value){
            try{
                //only integers are accepted in the text box
                Integer.parseInt(value);
            }catch(NumberFormatException nfe){
                return false;
            }
            return true;
    }
}
