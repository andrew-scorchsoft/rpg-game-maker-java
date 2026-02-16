/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gamemaker.Exceptions;

import gamemaker.Coordinate;

/**
 *
 * @author ug77alw
 */
public class ImageNotSelectedException extends Exception {
    Coordinate c;
    public ImageNotSelectedException(Coordinate c){
        this.c =c;
    }
    public String toString(){
        return "The coordinates [" + c + "]are not within the map";
    }
}
