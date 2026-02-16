/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gamemaker;

import java.awt.Graphics;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.awt.image.PixelGrabber;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import org.fuin.utils4j.Utils4J;

/**
 *
 * @author Andy
 */
public class FileHandler {



    public static String getFileExtension(File f){
        return getFileExtension(f.getName());
    }
    /**
     * finds the file extension of a url defined as a string.
     * This function takes the string after the last full stop
     * in the location
     * @param s the file path to inspect
     * @return the file extension. Returns "" if nothing is found
     */
    public static String getFileExtension(String s){

         String[] parts = s.split("\\.");

         if(parts != null && parts.length >=1){
            return parts[parts.length -1];
         }else{
            return "";
         }

    }
    /**
    public static URL getFilesURL(){
        return FileHandler.class.getResource("../files/");
    }
    public static URL getFilesURL(String location){
        String ps = File.separator;
        return FileHandler.class.getResource(".."+ps+"files"+ps+location);
    }**/
    public static String getFilesLocation(){

        
        String ps = File.separator;

        if(FileHandler.class.getResource("").getFile().contains("jar!") ||
                FileHandler.class.getResource("").getFile().contains("JAR!")){
            //if running from a packaged jar file


            String[] parts = FileHandler.class.getResource("").getFile().split("!");

            //ensures that link uses forwrd slashes for the sale of the url
            String theLocation = parts[0].replace("\\", "/");

            theLocation =  theLocation.substring(0, parts[0].lastIndexOf("/"));
            theLocation =theLocation.replace("%20", " ")+ps+"files"+ps;
  
             try{
                 //url retrieved the string we need in the valid format. This solved
                 //a lot of issues I was having locating files in reference to the .jar
                 //file
                URL url = new URL(theLocation);
                theLocation = url.toExternalForm();
                theLocation = theLocation.replace("file:", "");

             }catch(MalformedURLException murle){
                 //in reality this should never be thrown
                 System.err.println(murle);
                 murle.printStackTrace();
             }
            theLocation = theLocation.replace("\\", File.separator);
            theLocation = theLocation.replace("/", File.separator);

            return theLocation;
        }else{


            //this is the location of the files folder when the game is being run through netbeans.
            //I have moved the files dir back twice so that it isn't included in the jar file
            // when it is being built
            String resource = ".././gamemaker/";
   
            String theLocation = "file:"+FileHandler.class.getResource(resource).getPath().replace("%20", " ");
            //System.out.println("thelocation "+theLocation);
            theLocation = theLocation.replace("/build/classes/gamemaker/", "/files/");
            //System.out.println("thelocation "+theLocation);

            theLocation = theLocation.replace("file:", "");
            theLocation = theLocation.replace("\\", File.separator);
            theLocation = theLocation.replace("/", File.separator);
                


            return theLocation;
            //return FileHandler.class.getResource("../files/").getFile().replace("%20", " ");
        }
       
    }

/**
     public static ImageWithDetails imageFromURL(URL location) throws IOException{


         System.out.println("imageFromURL:" + location.toString());
         File newFile = new File(location.toString());
         Image theImage = ImageIO.read(location);
        ImageWithDetails theImageWithDetails = new ImageWithDetails(theImage);
        theImageWithDetails .setFileName(newFile.getName());

        return theImageWithDetails;
     }**/
    public static ImageWithDetails imageFromFile(String location) throws IOException{

        File newFile = new File(location);
        BufferedImage theImage = ImageIO.read(newFile);
        ImageWithDetails theImageWithDetails = new ImageWithDetails(theImage);
        theImageWithDetails.setFileName(newFile.getName());

        return theImageWithDetails;
    }
    public static Image imageFromFileOnly(String location) throws IOException{
        location = location.replace("%20", " ");
      
         File newFile = new File(location);
         return imageFromFileOnly(newFile);
    }
    public static Image imageFromFileOnly(File f) throws IOException{

        
     
        Image theImage = ImageIO.read(f);
  

        return theImage;
    }




    /**
    public static BufferedImage bufferedImageFromFile(String location) throws IOException{
        File newFile = new File(location);
        BufferedImage theImage = ImageIO.read(newFile);
        ImageWithDetails theImageWithDetails = new ImageWithDetails(theImage);
        theImageWithDetails .setFileName(newFile.getName());
        return theImageWithDetails;
    }**/

    /**
     * This function saves an image to the file specified
     * @param image
     * @param locationAndName
     * @throws IOException
     */
    public static void imageToFile(Image image, String locationAndName) throws IOException{
            BufferedImage bi = FileHandler.toBufferedImage(image);
            File outputfile = new File(locationAndName);
            //File outputfile = new File("gamesave/background/saved.png");
            ImageIO.write(bi, "png", outputfile);

           
     }


     /**
      * The functions that exist in this method come from here:
      * http://www.fuin.org/utils4j/examples/zipdir.html
      * it is licenced under LGPL
      * @param fromDir the directory to zip
      * @param toFile the zip file to save as
      * @throws IOException if there is a problem saving
      */
      public static void zipDir(String fromDir, String toFile)throws IOException{

            //the file to zip
            File dirToZip = new File(fromDir);
            File zipTo = new File(toFile);
            // Run the ZIP comman
            Utils4J.zipDir(dirToZip, "", zipTo);

      }

      public static void unzipDir(String fromZip, String toDir)throws IOException{
  
            File zipFile = new File(fromZip);

            File targetDir = new File(toDir+File.separator);
            

            Utils4J.unzip(zipFile, targetDir);

    


      }


     //creates the default save directories that the XML and image files will save to.
     public static void createDefaultSaveDirectories(){

          String[] directories  = { FileHandler.getFilesLocation()+"gamesave"+File.separator };
          /**
            String[] directories  = {      FileHandler.getFilesLocation()+"gamesave"+File.separator+"foreground",
                                           FileHandler.getFilesLocation()+"gamesave"+File.separator+"background",
                                           FileHandler.getFilesLocation()+"gamesave"+File.separator+"sprite",};
                                            **/
            // Create multiple directories
            for(int i = 0; i < directories.length; i++){
                File f = new File(directories[i]);
                f.mkdirs();

           }


     }
    public static boolean deleteDirectory(String directoryName) {
        File f = new File(directoryName);
        return deleteDirectory(f);
    }
     /**
      * Deletes all of the files within a directory, as well as the directory itself
      *
      * @param dir the directory to delete
      * @return true if success, false if unsuccessful
      */
    public static boolean deleteDirectory(File directory) {
        if (directory.isDirectory()) {
            String[] subdirs = directory.list();

            for (int i=0; i<subdirs.length; i++) {

                boolean success = deleteDirectory(new File(directory, subdirs[i]));

                if (success == false) {
                    return false;
                }


            }
        }

        //now delete the root directory
        return directory.delete();
    }


     public static BufferedImage toBufferedImage(Image image) {


    
        image = new ImageIcon(image).getImage();

        // Does the image contain any transparency?
        boolean hasTransparency = hasAlphaChannel(image);

        // Create a buffered image
        BufferedImage bufferedimage = null;
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();

        try {

            // Sets if the buffered image is transparent or not
            int transparency;
            if (hasTransparency == true) {
                transparency = Transparency.BITMASK;
            }else{
                transparency = Transparency.OPAQUE;
            }

            // creates the bufferedimage
            GraphicsDevice gs = ge.getDefaultScreenDevice();
            GraphicsConfiguration gc = gs.getDefaultConfiguration();
            bufferedimage = gc.createCompatibleImage(image.getWidth(null), image.getHeight(null), transparency);
        }
        catch (HeadlessException e) {
            System.err.println(e);
        }


        // take the image and copy it into the buffered inage
        Graphics g = bufferedimage.createGraphics();

        // Paint the image onto the buffered image
        g.drawImage(image, 0, 0, null);
        g.dispose(); //close the graphics object

        return bufferedimage;
    }

      public static boolean hasAlphaChannel(Image image) {
             // If buffered image, the color model is readily available
             if (image instanceof BufferedImage) {

                 return ((BufferedImage)image).getColorModel().hasAlpha();

             }

        
             // grab a pixel to identify the color model that the image uses.
             PixelGrabber pixelGrab = new PixelGrabber(image, 0, 0, 1, 1, false);
             try {
                 pixelGrab.grabPixels();
             } catch (InterruptedException e) {
                 System.err.println(e);
             }

             // Get the image's color model
             return pixelGrab.getColorModel().hasAlpha();
         }
}

