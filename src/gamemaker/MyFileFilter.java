/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gamemaker;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.filechooser.FileFilter;


/**
 *
 * @author ug77alw
 */
public class MyFileFilter extends FileFilter{

    private ArrayList<String> acceptedExtensions;
    private String description = "";

    public MyFileFilter(String acceptedExtension, String description){
        acceptedExtensions = new ArrayList(20);
        acceptedExtensions.add(acceptedExtension);
        this.description = description;
    }
    public MyFileFilter(String[] acceptedExtensions,String description){
        super();
        //converts the string array to an array list
        this.acceptedExtensions = new ArrayList(Arrays.asList(acceptedExtensions));
        this.description = description;
    }
    public MyFileFilter(ArrayList<String> acceptedExtensions,String description){
        super();
        this.acceptedExtensions = acceptedExtensions;
        this.description = description;
    }
    public MyFileFilter(){
        super();
        acceptedExtensions = new ArrayList(20);

        //by default it is set to accept images
        acceptedExtensions.add("jpg");
        acceptedExtensions.add("jpeg");
        acceptedExtensions.add("png");
        acceptedExtensions.add("gif");
        this.description = "Images (.jpeg, .jpg, .png, .gif)";
 

    }
    public boolean accept(File f) {

        boolean accept = false;

        int i = 0;

        //accepts if item is a directory
        if(f.isDirectory()){
            return true;
        }

        String extension = FileHandler.getFileExtension(f);

        if(!extension.equals("")){
            //only loop if the file has an extension
            while(i < acceptedExtensions.size() && accept == false){

                //if the file extension is valid
                if(extension.equals(acceptedExtensions.get(i).toLowerCase())){
                    accept = true;
                }

                i++;
            }
        }

        return accept;
    }

    public String getDescription() {
        return description;
    }


}
