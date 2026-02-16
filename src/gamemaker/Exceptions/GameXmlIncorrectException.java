/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gamemaker.Exceptions;

/**
 *
 * @author Andy
 */
public class GameXmlIncorrectException extends Exception{
 private String error;

    public GameXmlIncorrectException(){
        this("");
    }
    public GameXmlIncorrectException(String error){
        this.error = error;
    }
     public String toString(){
        return "There is an error in the Game XML File. "+error;
    }
}
