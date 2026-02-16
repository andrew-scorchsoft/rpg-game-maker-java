/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gamemaker;
import gamemaker.Exceptions.GameXmlIncorrectException;
import java.util.List;
import org.jdom.DataConversionException;
import org.jdom.Element;
/**
 *
 * @author Andy
 */
public class XMLSimplify {



    public static double getXMLDouble(Element e, String attribute)throws GameXmlIncorrectException{
        double theDouble;

        try{
            theDouble = e.getAttribute(attribute).getDoubleValue();
        }catch(DataConversionException dce){
            throw new GameXmlIncorrectException("The attribute '"+attribute+"' must be set as an int. ");
        }

        return theDouble;
    }
     public static int getXMLInt(Element e, String attribute)throws GameXmlIncorrectException{
        int theInt;

        try{
            theInt = e.getAttribute(attribute).getIntValue();
        }catch(DataConversionException dce){
            throw new GameXmlIncorrectException("The attribute '"+attribute+"' must be set as an int. ");
        }

        return theInt;
    }
     public static boolean getXMLBool(Element e, String attribute)throws GameXmlIncorrectException{
        boolean theBool = false;

      
        String s = e.getAttribute(attribute).getValue();
        s = s.toUpperCase();
        if(s.equals("TRUE") || s.equals("T") || s.equals("1")){
            theBool = true;
        }else if(s.equals("FALSE") || s.equals("F") || s.equals("0")){
            theBool = false;
        }else{
            throw new GameXmlIncorrectException("boolean incorrectly specified.");
        }


     

        return theBool;
    }

    /**
     * For retrieving elements where there should strictly be only one instance of that element
     * @param root the root element to inspect for children elements
     * @param child the name of the child element that you are looking for
     * @return the element if found and if exclusive
     * @throws GameXmlIncorrectException the there is no element or if there is
     * more than one element of name child within the root
     */
    public static Element getStrictlySingleElement(Element root, String child)throws GameXmlIncorrectException{
        List<Element> elements = root.getChildren(child);

        //Only one sprite tag is allowed. And there must be at least one
        if(elements.size() > 1){
            throw new GameXmlIncorrectException("Game XML must define only one '"+child+"' element.");
        }else if(elements.size() < 1){
            throw new GameXmlIncorrectException("Game XML must define at least one '"+child+"' element.");
        }

        return elements.get(0);

    }





}
