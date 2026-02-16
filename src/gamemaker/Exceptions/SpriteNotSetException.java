/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gamemaker.Exceptions;

/**
 *
 * @author ug77alw
 */
public class SpriteNotSetException extends Exception {
    private String error;
    public SpriteNotSetException(String error){
        this.error = error;
    }
    public String toString(){
        return "The sprite can not be retrieved as it has not yet been set. "+error;
    }
}
