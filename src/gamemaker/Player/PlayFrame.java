/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gamemaker.Player;

import gamemaker.Coordinate;
import gamemaker.Exceptions.ImageNotSelectedException;
import gamemaker.Exceptions.PanelNotPassedException;
import gamemaker.GameMaps;
import gamemaker.ImageWithDetails;
import gamemaker.MapsContainer;
import gamemaker.Ribbon;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.DisplayMode;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.net.URL;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JFrame;

/**
 *
 * @author ug77alw
 */
public class PlayFrame extends JFrame implements MapsContainer, WindowListener, KeyListener {

    //the ribbon that opened this frame
    private Ribbon passingRibbon;
    private GameMaps maps;
    private MovementThread movementHandler;
    private RepaintThread repaintHandler;
    //private GameMap currentMap;
    //private JScrollPane contentScroll;

    private Clip audioClip;
    AudioInputStream ais;

    /**
     * Variables used to handle the pressing of multiple keys at the
     * same time. This allows the sprite to walk diagonally
     */
    private final int UP = 0;
    private final int RIGHT = 1;
    private final int DOWN = 2;
    private final int LEFT = 3;
    private boolean[] keypressed = new boolean[4];
    private GraphicsDevice graphicsDevice;

    //PaintPanel painter;

    private final int WIDTH = 1024;
    private final int HEIGHT = 768;
    
    public String getContainterType(){
        return "playframe";
    }
  
    public PlayFrame(){
        
        this.setSize(WIDTH,HEIGHT);
        this.setVisible(true);
        this.setMinimumSize(new Dimension(640,480));
        
        this.setTitle("Play Game");
        
       

      
        
    }
    public void setUpPlayFrame(){
        setUpPlayFrame(this.maps);
    }
    public void setUpPlayFrame(GameMaps maps){
        
        graphicsDevice = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        previousDisplayMode = graphicsDevice.getDisplayMode();
        this.setLocationRelativeTo(null);
        this.addWindowListener(this);
        this.addKeyListener(this);
        System.out.println("ok");



        maps.setPlayMode();
        maps.setMapsContainer(this);
        this.maps = maps;

        //this.setMaximumSize(new Dimension(WIDTH,HEIGHT));
       // this.setResizable(false);

        //maps.getPaintPanel().setPaintSize(WIDTH,HEIGHT);

        this.setTitle("Play Game");

        this.add(maps.getPaintPanel(),BorderLayout.CENTER);

        movementHandler = new MovementThread(maps.getSprite().getCurrentGameMap());
        repaintHandler = new RepaintThread(this);
        movementHandler.setMoveSpeed((int)maps.getCurrentMap().getSpritePanel().getSprite().getSpriteSpeed());
        movementHandler.start();
        repaintHandler.start();


        this.setBackground(Color.black);
        this.repaint();


        




        
    }

      public void setMusic(URL url) throws IOException, LineUnavailableException, UnsupportedAudioFileException{
        audioClip = AudioSystem.getClip();
        URL clipURL = new URL("file://C:/WINDOWS/Media/CHIMES.wav");
        /* Open our URL as an AudioStream */
        ais = AudioSystem.getAudioInputStream(clipURL);
    }

    public void stopMusic(){
        audioClip.stop();
    }
    public void changeMusic(URL url) throws IOException, LineUnavailableException, UnsupportedAudioFileException{
        audioClip.stop();
        setMusic(url);
        playMusic();
    }
    public void playMusic() throws LineUnavailableException,IOException{
         /* Put out audio input stream into our clip */
        audioClip.open(ais);

        /* Play the clip */
        audioClip.start();
    }


    

    private int width = 500;
    //private Rectangle rect = new Rectangle(0,0,500,500);
    //private Rectangle rect2 = new Rectangle(0,0,500,500);

    /**
     * This method must be lightweight as it is called frequently
     * @param c
     */
    public void setFocusOnSprite(){
        setFocusOnSprite(maps.getSprite().getSpritePositionCoord());
    }
    public void setFocusOnSprite(Coordinate c){

        maps.getPaintPanel().repaint();
       
    
    }

    public void setRibbon(Ribbon r){
        this.passingRibbon = r;
    }




    @Override
    public void repaint(){
        super.repaint();
        if(maps != null){
            if(maps.isPaintPanelSet()){
                maps.getPaintPanel().repaint();
            }
            if(maps.getCurrentMap() !=null && movementHandler !=null){
                movementHandler.setMap(maps.getCurrentMap());
                
            }
        }
       


       
   }

    public void setCursorCrosshair(){
        Cursor hourglassCursor = new Cursor(Cursor.CROSSHAIR_CURSOR);
        setCursor(hourglassCursor);
    }

    public void setCursorNormal(){
        Cursor normalCursor = new Cursor(Cursor.DEFAULT_CURSOR);
        setCursor(normalCursor);
    }

    //public void repaint(){};
 
    public ImageWithDetails getImageChooser() throws ImageNotSelectedException, IOException{
        return null;
    };

    public void windowClosing ( WindowEvent w )
      {
           if(passingRibbon != null){
               passingRibbon.setVisible(true);
               //maps.setMapsContainer(passingRibbon);
               //maps.setEditMode();
               passingRibbon.repaint();
           }
           movementHandler.endThread();
           repaintHandler.endThread();
           this.setVisible( false );
           this.dispose();
      }

    public void keyTyped(KeyEvent e){
    };

  


    //public boolean fastPressed = false;

    public void keyPressed(KeyEvent e){
       boolean setMove = false;

       if(e.getKeyCode() == e.VK_SHIFT){
           //fastPressed = true;
           this.movementHandler.setMoveFast();
       }else
       if(e.getKeyCode() == e.VK_1){
          this.setExtendedState(this.MAXIMIZED_BOTH);
      
       }else if(e.getKeyCode() == e.VK_ENTER || e.getKeyCode() == e.VK_SPACE){
           try{
                maps.getCurrentMap().action();
                maps.getPaintPanel().updateUI();
                maps.getPaintPanel().repaint();
                maps.getPaintPanel().updateImage();
              
           }catch(PanelNotPassedException pnpe){
               System.err.println(pnpe);
           }
 

        }else if(e.getKeyCode() == e.VK_UP){
            keypressed[UP] = true;
            setMove = true;
         
        }else if(e.getKeyCode() == e.VK_DOWN){
            keypressed[DOWN] = true;
            setMove = true;

           
      
        }else if(e.getKeyCode() == e.VK_LEFT){
            keypressed[LEFT] = true;
            setMove = true;


        }else if(e.getKeyCode() == e.VK_RIGHT){
            keypressed[RIGHT] = true;
            setMove = true;
        }


       updateDirection();
   

        if(setMove == true){
            movementHandler.setMove(true);
        }
        
        
    };

    private void updateDirection(){
        /**
         * As a key is pressed, a flag is turned on in the array keypressed.
         * Then updateDirection is called to look at what keys are down
         * in order to know how to move the sprite.
         */
        if(keypressed[UP]){
            if(keypressed[RIGHT]){
                movementHandler.setDirection(movementHandler.DIRECTION_NE);
            }else if(keypressed[LEFT]){
                movementHandler.setDirection(movementHandler.DIRECTION_NW);
            }else {
                movementHandler.setDirection(movementHandler.DIRECTION_N);
            }
        }else

         if(keypressed[DOWN]){
            if(keypressed[RIGHT]){
                movementHandler.setDirection(movementHandler.DIRECTION_SE);
            }else if(keypressed[LEFT]){
                movementHandler.setDirection(movementHandler.DIRECTION_SW);
            }else {
                movementHandler.setDirection(movementHandler.DIRECTION_S);
            }
        }else

        if(keypressed[LEFT]){
            if(keypressed[UP]){
                movementHandler.setDirection(movementHandler.DIRECTION_NW);
            }else if(keypressed[DOWN]){
                movementHandler.setDirection(movementHandler.DIRECTION_SW);
            }else {
                movementHandler.setDirection(movementHandler.DIRECTION_W);
            }
        }else

        if(keypressed[RIGHT]){
            if(keypressed[UP]){
                movementHandler.setDirection(movementHandler.DIRECTION_NE);
            }else if(keypressed[DOWN]){
                movementHandler.setDirection(movementHandler.DIRECTION_SE);
            }else {
                movementHandler.setDirection(movementHandler.DIRECTION_E);
            }
        }

    }


    public DisplayMode previousDisplayMode ;
    public boolean fullscreenModeSet = false;
    public void keyReleased(KeyEvent e){

        if(e.getKeyCode() == e.VK_SHIFT){
           //fastPressed = true;
           this.movementHandler.setMoveNormal();
       }else
        if(e.getKeyCode() == e.VK_ESCAPE){
            if(fullscreenModeSet){ //if full screen just exit full screen
                //graphicsDevice.setFullScreenWindow(null);
                 //fullscreenModeSet = false;
                 this.setFullScreen(false);
                 //this.setSize(WIDTH, HEIGHT);
                 //this.setPreferredSize(new Dimension(WIDTH, HEIGHT));



            }else{
                //close threads and window

                if(passingRibbon != null){
                   passingRibbon.setVisible(true);
                   passingRibbon.repaint();
                }
                this.setVisible(false);
                 movementHandler.endThread();
                 repaintHandler.endThread();
                this.dispose();
            }
        }else
        if(e.getKeyCode() == e.VK_F11){
            /**full screens**/


            GraphicsConfiguration gc = graphicsDevice.getDefaultConfiguration();
            double ratio = gc.getBounds().getHeight()/gc.getBounds().getWidth();


            

           
        



            if(graphicsDevice.isFullScreenSupported()){
             
                if(fullscreenModeSet == true){

                    this.setFullScreen(false);

                }else{

                    this.setFullScreen(true);
                   
                }
              
            }

            /** exits full screen**/
        }

        if(e.getKeyCode() == e.VK_UP){
            keypressed[UP] = false;

        }else if(e.getKeyCode() == e.VK_DOWN){
            keypressed[DOWN] = false;


        }else if(e.getKeyCode() == e.VK_LEFT){
            keypressed[LEFT] = false;

        }else if(e.getKeyCode() == e.VK_RIGHT){
            keypressed[RIGHT] = false;
        }
        

        boolean stopMove = true;
        int i = 0;
        while(i<4 && stopMove == true){
            if(keypressed[i] == true){
                stopMove = false;
            }
            i++;
        }
        //only stops movement if no keys are pressed
        if(stopMove == true){
            movementHandler.setMove(false);
            maps.getCurrentMap().getSpritePanel().repaint();
        }else{
            updateDirection();
        }
       
    };


    private void setFullScreen(Boolean set){


        if(set == false){
            //remove full screen


            //removes notify so that it can be set undecorated
            this.removeNotify();

            this.setVisible(false);
            this.setUndecorated(false);


            this.setResizable(true);

             graphicsDevice.setFullScreenWindow(null);
             //graphicsDevice.setDisplayMode(previousDisplayMode);
             fullscreenModeSet = false;
             this.setSize(WIDTH, HEIGHT);
             this.setPreferredSize(new Dimension(WIDTH, HEIGHT));

             //allows the frame to be displayed again.
             this.addNotify();
             this.setVisible(true);
        }else{
            //set full screen

            DisplayMode mode = new DisplayMode(WIDTH, HEIGHT, 32, DisplayMode.REFRESH_RATE_UNKNOWN);
             //removes notify so that it can be set undecorated
            this.removeNotify();
            this.setVisible(false);


            this.setResizable(false);
            //removes title bar and border of frame
            this.setUndecorated(true);

            graphicsDevice.setFullScreenWindow(this);
            graphicsDevice.setDisplayMode(mode);
            fullscreenModeSet = true;

             //this.setUpPlayFrame();

            //allows the frame to be displayed again. This time in full screen
             this.addNotify();
             this.setVisible(true);
        }


    }



    public void windowOpened(WindowEvent e){};

    public void windowClosed(WindowEvent e){
         movementHandler.endThread();
         repaintHandler.endThread();
         //a lot of resource can be claimed at this point so
         //force garbage collection
         System.gc();
    };

    public void windowIconified(WindowEvent e){};

    public void windowDeiconified(WindowEvent e){};

    public void windowActivated(WindowEvent e){};

    public void windowDeactivated(WindowEvent e){};

    public String toString(){
             return "Playframe";
    }


}
