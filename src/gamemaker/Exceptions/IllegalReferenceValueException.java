/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gamemaker.Exceptions;

/**
 *
 * @author Andy
 */
public class IllegalReferenceValueException extends Exception {
    private String error;

    public IllegalReferenceValueException(){
        error = "";
    }
    public IllegalReferenceValueException(String error){
        this.error = error;
    }

    public String toString(){
        return "Illegal reference value used. "+error;
    }


}
