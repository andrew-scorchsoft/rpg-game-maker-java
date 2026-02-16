/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gamemaker;

import gamemaker.Exceptions.GameXmlIncorrectException;
import gamemaker.Panels.GlassPanel;
import gamemaker.Exceptions.ImageNotSelectedException;
import gamemaker.Exceptions.PanelNotPassedException;
import gamemaker.Panels.WalkableAreaPanel;
import gamemaker.Panels.EventPanel;
import gamemaker.Panels.BackgroundPanel;
import gamemaker.Panels.DialoguePanel;
import gamemaker.Panels.ForegroundPanel;
import gamemaker.Panels.PerspectivePanel;
import gamemaker.Panels.SpritePanel;
import gamemaker.Panels.XMLable;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import org.jdom.Element;

/**
 *
 * @author ug77alw
 */
public class GameMap extends JLayeredPane implements MouseListener, MouseMotionListener, XMLable{

  private JPanel background;
  // private JPanel foreground;

  public static final int BOTTOM_LAYER = 1;
  public static final int BACKGROUND_LAYER = 2;
  
  public static final int HIDE_LAYER = 3;
 
  public static final int EVENT_LAYER = 4;
  public static final int SPRITE_LAYER = 5;
  public static final int FOREGROUND_LAYER = 6;
  public static final int WALKABLE_LAYER = 7;
  public static final int PERSPECTIVE_LAYER = 8;
  public static final int DIALOGUE_LAYER = 9;
  public static final int GLASS_LAYER = 10;

  public static final int TASK_IDLE = 0;
  public static final int TASK_EDITWALKABLE = 1;
  public static final int TASK_ADDEVENT = 2;
  public static final int TASK_ADDFOREGROUNDITEM = 3;
  public static final int TASK_EDITFOREGROUND = 4;
  public static final int TASK_EDITPERSPECTIVE = 5;
  public static final int TASK_SETSPRITELOCATION = 6;
  public static final int TASK_PLAYING = 7;
  public static final int TASK_DIALOGUE = 8;
  public static final int TASK_SELECTTELEPORTLOCATION = 9;

  private MapsContainer containingFrame;

  private WalkableAreaPanel walkableAreaPanel;
  private BackgroundPanel backgroundPanel;
  private EventPanel eventPanel;
  private ForegroundPanel foregroundPanel;
  private GlassPanel glassPanel;
  private PerspectivePanel perspectivePanel;
  private SpritePanel spritePanel;
  private DialoguePanel dialoguePanel;

  private boolean moveForeground = false;
  //-1 = false
  private int resizeForeground = -1;
  private Coordinate resizeForgroundFrom;

  private Coordinate resizePerspectiveFrom;

  private Cursor theCursor;
  private boolean temporaryCursor = false;
  private URL musicURL;


  private Coordinate moveForegroundFrom;

  private TeleportInput teleportInput;

  private int currentTask;


  private int width;
  private int height;
  
  public static final int EVENT_SOUT = 1;
  public static final int EVENT_TELEPORT = 2;
  public static final int EVENT_VARIABLE = 3;
  
  public final int SHAPE_SQUARE = 1;
  public final int SHAPE_CIRCLE = 2;
  public final int SHAPE_AREA = 3;

  public final int EDIT_DISPLAYMODE = 0;
  public final int PLAY_DISPLAYMODE = 1;


  private int displayMode = EDIT_DISPLAYMODE;
  private GameMaps gameMaps;

  



  public GameMaps getGameMaps(){
      return gameMaps;
  }
  public void setMapDimensions(int width, int height){
      this.width = width;
      this.height = height;

  }
  public void setMapsContainer(MapsContainer mapsc){
      this.containingFrame = mapsc;
  }

   public GameMap(MapsContainer frame, Sprite sprite, GameMaps maps){
       


       this.containingFrame = frame;
       currentTask = TASK_IDLE;
       this.gameMaps = maps;
       addMouseMotionListener(this);
       

       //default height is 800x600
       width = 800;
       height = 600;


       walkableAreaPanel = new WalkableAreaPanel();

       backgroundPanel = new BackgroundPanel(this);

       eventPanel = new EventPanel(this,maps);

       dialoguePanel = new DialoguePanel(this);



       try{
            spritePanel = new SpritePanel(sprite,this);
       }catch(IOException ioe){
           System.err.println(ioe + ". Sprite panel io error.");
       }

       try{
       perspectivePanel = new PerspectivePanel(spritePanel);
       }catch(IOException ioe){
           System.err.println(ioe + ". perspective panel io error.");
       }



       

       glassPanel = new GlassPanel();
       foregroundPanel = new ForegroundPanel(this,glassPanel);
       glassPanel.addAssociation(foregroundPanel);

   

   
       setAndAddLayers();
 
    


       //this.setBackground(new Color(239,239,239));
       this.setBackground(new Color(0,0,0));
       //this.setBackground(Color.red);

       updateSizeOfAll();

       this.addMouseListener(this);


       spritePanel.setEventPanel(eventPanel);

       eventPanel.setDialoguePanel(dialoguePanel);

       
        
            //TestThread t = new TestThread(spritePanel);
           //t.start();
  
       

       
   }

    // private int rectwidth = 500;
    private Rectangle rect = new Rectangle(0,0,500,500);
    public void setFocusOnSprite(Coordinate c, MapsContainer cont){
      //  setFocusOnSprite(c.getPoint(), cont);
    }
    public void setFocusOnSprite(Point c, MapsContainer cont){

         //   rect.setBounds((int)Math.ceil(c.getX()-(cont.getHeight()/2.0)),
            //        (int)Math.ceil(c.getY()-(cont.getWidth()/2.0)),
             //       cont.getWidth(),
          //          cont.getHeight());
          //  //rect.setBounds((int)Math.round(c.getX()-(width/2.0)),(int)Math.round(c.getY()-(width/2.0)),(width),(width));
          //  this.getBackgroundPanel().scrollRectToVisible(rect);

       
            //maps.getCurrentMap().scrollRectToVisible(rect);


            //ensures smooth scrolling
//            int x = cont.getJScrollPane().getHorizontalScrollBar().getValue();
           // int y = cont.getJScrollPane().getHorizontalScrollBar().getValue();
          //  Rectangle r = new Rectangle(x,y,cont.getJScrollPane().getWidth(),cont.getJScrollPane().getHeight());
          //  cont.getJScrollPane().repaint(r);
          //  cont.repaint();
    }

   public void superRepaint(){

      walkableAreaPanel.repaint();
      backgroundPanel.repaint();
      eventPanel.repaint();
      foregroundPanel.repaint();
      glassPanel.repaint();
      perspectivePanel.repaint();
      spritePanel.repaint();
      dialoguePanel.repaint();
      this.repaint();
  }




   /**
    * Saves the images used by all of the panels within this game map.
    * It saves these images to a default location
    * @throws IOException if there is an issue saving the files
    */
   public void saveImages()throws IOException{
       saveImages(FileHandler.getFilesLocation());
   }
   
   public void saveImages(String location)throws IOException{

       //saves all of the background images
       this.getBackgroundPanel().saveImage(location);
       //saves all of the foreground images
       this.getForegroundPanel().saveImages(location);
   }

   public void setMusic(URL musicFileURL){
       this.musicURL = musicFileURL;
   }
   public URL getMusicURL(){
       return this.musicURL;
   }

   public ForegroundPanel getForegroundPanel(){
       return this.foregroundPanel;
   }
   public GlassPanel getGlassPanel(){
       return  glassPanel;
   }
   public PerspectivePanel getPerspectivePanel(){
       return perspectivePanel;
   }
   public BackgroundPanel getBackgroundPanel(){
       return this.backgroundPanel;
   }
   public SpritePanel getSpritePanel(){
       return spritePanel;
   }
   public WalkableAreaPanel getWalkableAreaPanel(){
       return walkableAreaPanel;
   }
   public DialoguePanel getDialoguePanel(){
       return dialoguePanel;
   }
   public EventPanel getEventPanel(){
       return this.eventPanel;
   }

   /**
    * this sets and adds all of the maps layers to the map.
    */
   private void setAndAddLayers(){

       this.add(backgroundPanel,1);
       this.add(foregroundPanel,1);
       this.add(spritePanel,1);

       //   doesnt need to display these layers if it is in play mode
       //   only has to use information from them
       if(displayMode == EDIT_DISPLAYMODE){
           this.add(walkableAreaPanel, 1);
           this.add(eventPanel,1);
           this.add(glassPanel,1);
           this.add(perspectivePanel,1);

       }else if(displayMode == PLAY_DISPLAYMODE){
           this.add(dialoguePanel);
       }

       this.setLayer(backgroundPanel, BACKGROUND_LAYER);
       this.setLayer(spritePanel, SPRITE_LAYER);
       this.setLayer(foregroundPanel, FOREGROUND_LAYER);


        //   doesnt need to display these layers if it is in play mode
       //   only has to use information from them
       if(displayMode == EDIT_DISPLAYMODE){
           this.setLayer(walkableAreaPanel, WALKABLE_LAYER);
           this.setLayer(eventPanel, EVENT_LAYER);
           this.setLayer(perspectivePanel, PERSPECTIVE_LAYER);
           this.setLayer(glassPanel, GLASS_LAYER);

       }else if(displayMode == PLAY_DISPLAYMODE){
            this.setLayer(dialoguePanel, DIALOGUE_LAYER);
       }


   }

   public void toggleDialogue(){
        if(currentTask == TASK_DIALOGUE){
            this.dialoguePanel.setDialogueOn(false);
            currentTask = TASK_PLAYING;
       }else{
           currentTask = TASK_DIALOGUE;
           this.dialoguePanel.setDialogueOn(true);
       }
   }



   public void setDialogue(boolean onOff){

       if(currentTask == TASK_DIALOGUE && onOff == false){
            this.dialoguePanel.setDialogueOn(false);
            currentTask = TASK_PLAYING;
       }else if(onOff == false && currentTask == TASK_PLAYING){
           currentTask = TASK_DIALOGUE;
           this.dialoguePanel.setDialogueOn(true);
       }
   }

    public void action() throws PanelNotPassedException{
      if(currentTask == TASK_PLAYING){
          //spritePanel.triggerActionEvent();
          eventPanel.triggerActionEventAtPoint(spritePanel.getSprite().getSpritePosition());

      }else if(currentTask == TASK_DIALOGUE){
          this.toggleDialogue();
          dialoguePanel.action();
      }
  }
    public void setCurrentTask(int taskCode){
        currentTask = taskCode;
    }

   public void setPlayMode(){
       this.removeAll();
       this.displayMode = this.PLAY_DISPLAYMODE;
       setAndAddLayers();
       currentTask = TASK_PLAYING;
       containingFrame.setCursorNormal();
   }
   public void setEditMode(){
       this.removeAll();
       this.displayMode = this.EDIT_DISPLAYMODE;
       setAndAddLayers();
       currentTask = TASK_IDLE;
       containingFrame.setCursorNormal();
   }


 public void setEventCreator(int eventCode){
      if(eventCode == EVENT_SOUT){
          eventPanel.setEventType(eventPanel.SOUT_EVENT);
      }else if(eventCode == EVENT_TELEPORT){
          eventPanel.setEventType(eventPanel.EVENT_TELEPORT);
      }else if(eventCode == EVENT_VARIABLE){
          eventPanel.setEventType(eventPanel.EVENT_VARIABLE);
      }
  }
  public int getEventCreator(){
      int type = eventPanel.getEventType();
      if(type == eventPanel.SOUT_EVENT){
          type = EVENT_SOUT;
      }else if(type == eventPanel.SOUT_EVENT){
          type = EVENT_TELEPORT;
      }else if(type == eventPanel.SOUT_EVENT){
          type = EVENT_VARIABLE;
      }
      return type;
  }

   public void setTeleportLocation(TeleportInput teleportInput){
       this.teleportInput = teleportInput;
       currentTask = TASK_SELECTTELEPORTLOCATION;
       
   }
   public void unsetTeleportLocation(){

   
           currentTask = TASK_ADDEVENT;

   }

  public Image getBackgroundImageThumb(){
      return backgroundPanel.getBackgroundImageThumb();
  }


  public int getEventCreatorShape(){
      int code = eventPanel.getEventShapeMode();

      if(code == eventPanel.SQUARE){
          code = SHAPE_SQUARE;
      }else if(code == eventPanel.CIRCLE){
          code = SHAPE_CIRCLE;
      }else if(code == eventPanel.AREA){
           code = SHAPE_AREA;
      }

      return code;
  }
  public void setEventCreatorShape(int shapeCode){
      if(shapeCode == SHAPE_SQUARE){
          eventPanel.setDrawMode(eventPanel.SQUARE);
      }else if(shapeCode == SHAPE_CIRCLE){
          eventPanel.setDrawMode(eventPanel.CIRCLE);
      }else if(shapeCode == SHAPE_AREA){
           eventPanel.setDrawMode(eventPanel.AREA);
      }
  }


    public MapsContainer getMapContainer(){
        return containingFrame;
   }

   public void updateSizeOfAll(){
       walkableAreaPanel.setBounds(0,0,width,height);
       Dimension newSize = new Dimension(width,height);
       walkableAreaPanel.setPreferredSize(newSize);


       backgroundPanel.setBounds(0,0,width,height);
       backgroundPanel.setPreferredSize(newSize);


       eventPanel.setBounds(0,0,width,height);
       eventPanel.setPreferredSize(newSize);

       foregroundPanel.setBounds(0,0,width,height);
       foregroundPanel.setPreferredSize(newSize);

       perspectivePanel.setBounds(0,0,width,height);
       perspectivePanel.setPreferredSize(newSize);

       spritePanel.setBounds(0,0,width,height);
       spritePanel.getPerspective().setLogicalHeight(height);

       dialoguePanel.setBounds(0,0,width,height);
       dialoguePanel.setPreferredSize(newSize);
       
       perspectivePanel.getPerspective().setLogicalHeight(height);

    

       glassPanel.setBounds(0,0,width,height);
       //glassPanel.setSize(new Dimension(width,height));

       this.setPreferredSize(new Dimension(width,height));
       //this.setSize(new Dimension(width,height));
       this.setBounds(new Rectangle(0,0,width,height));

       //all the updates to display things properly
       containingFrame.repaint();
       this.repaint();
       this.setVisible(true);
       this.setEnabled(true);
       this.setOpaque(true); //this line fixes the pane not displaying issue
   }


   public void setBackgroundImageDefault(){
       setBackgroundImage("maps/default.png");
   }
   public void setBackgroundImage(String fileLocation){
         try{
             File f = new File(FileHandler.getFilesLocation()+fileLocation);
       
            ImageWithDetails theImage = new ImageWithDetails(ImageIO.read(f));
            theImage.setFileName(f.getName());

            if(theImage != null){
                backgroundPanel.setImage(theImage);
                width = backgroundPanel.getWidth();
                height = backgroundPanel.getHeight();
                updateSizeOfAll();
            }
 
        }catch(IOException ioe){
            System.err.println(ioe);
        }
   }

   /**
    * Sets the background image by showing a file chooser to the user
    * If the user doesn't select a file, or if that file is incorrect, or errors
    * then the background image is simply not added
    */
   public void setBackgroundImage(){
      
           setIdle();

       try{
            ImageWithDetails theImage = containingFrame.getImageChooser();
            //only adds the image if the background has been selected
            if(theImage != null){
                backgroundPanel.setImage(theImage);
                width = backgroundPanel.getWidth();
                height = backgroundPanel.getHeight();
                updateSizeOfAll();
            }
       }catch(IOException ioe){
            System.err.println(".getNewImage() io error" + ioe);
       }catch(ImageNotSelectedException inse){
           System.err.println("No image was selected" + inse);
       }
   }



 

    public void editPerspective(){


        if(currentTask == TASK_EDITPERSPECTIVE){

           setIdle();
           perspectivePanel.setIsPanelOn(false);

       }else{
            setIdle();
            currentTask = TASK_EDITPERSPECTIVE;
           containingFrame.setCursorCrosshair();
           perspectivePanel.setIsPanelOn(true);

       }
       this.repaint();


   }
     public void setStartLocation(){

       if(currentTask == TASK_IDLE){
           
           currentTask = TASK_SETSPRITELOCATION;
           containingFrame.setCursorCrosshair();

       }else if(currentTask == TASK_SETSPRITELOCATION){
           setIdle();

       }
       this.repaint();


   }
   public void editWalkable(){
       /**
       if(currentTask == TASK_IDLE){
           currentTask = TASK_EDITWALKABLE;
           containingFrame.setCursorCrosshair();
       }else if(currentTask == TASK_EDITWALKABLE){
           setIdle();
       }**/

       if(currentTask == TASK_EDITWALKABLE){
           setIdle();
       }else{
           setIdle();
           currentTask = TASK_EDITWALKABLE;
           containingFrame.setCursorCrosshair();

       }
   }
   public void editEvents(){
 

       if(currentTask == TASK_ADDEVENT){
           setIdle();
       }else{
           setIdle();
           currentTask = TASK_ADDEVENT;
           containingFrame.setCursorCrosshair();

       }
   }
   public int getCurrentTask(){
       return currentTask;
   }
   public void editForeground(){


       if(currentTask == TASK_EDITFOREGROUND){
           setIdle();
       }else{
           setIdle();
           currentTask = TASK_EDITFOREGROUND;
           containingFrame.setCursorCrosshair();

       }

   }
   public void addForeground(){

       if(currentTask == TASK_ADDFOREGROUNDITEM){
           setIdle();
       }else{
           setIdle();
           currentTask = TASK_ADDFOREGROUNDITEM;
           containingFrame.setCursorCrosshair();

       }

   }


   /**
    * finishes all of the current jobs and sets the current task
    * to idle
    */
   public void setIdle(){
       if(currentTask == TASK_EDITPERSPECTIVE){

           perspectivePanel.setIsPanelOn(false);
       }else
        if(currentTask == TASK_EDITWALKABLE){
          walkableAreaPanel.setCurrentComplete();
           walkableAreaPanel.repaint();
       }else
       if(currentTask ==TASK_ADDEVENT){

           eventPanel.setCurrentAreaComplete();
           eventPanel.repaint();
       }
       this.foregroundPanel.deSelectAll();
       foregroundPanel.repaint();

       currentTask = TASK_IDLE;
       containingFrame.setCursorNormal();
   }



   

    public void mouseClicked(MouseEvent e){
       
    };

    public void mousePressed(MouseEvent e){

        Coordinate here = new Coordinate((int) e.getPoint().getX(), (int) e.getPoint().getY());

        if(currentTask == TASK_SELECTTELEPORTLOCATION){
            if(e.getButton() == e.BUTTON1){

                teleportInput.setCoordinate(here);
            }


        }else if(currentTask == TASK_EDITPERSPECTIVE){
            if(e.getButton() == e.BUTTON1){
            
                perspectivePanel.changePerspective(here.getPoint(), here.getPoint());
                resizePerspectiveFrom = here;
            }
             if(e.getButton() == e.BUTTON3){

                perspectivePanel.setHorizon(e.getPoint());
            }

            if(e.getButton() == e.BUTTON2){

                editPerspective();




            }


        }else if(currentTask == TASK_SETSPRITELOCATION){
            if(e.getButton() == e.BUTTON1){

                gameMaps.setDefaultSpriteLocation(new Coordinate(e.getPoint()));
                //this.getSpritePanel().getSprite().setStartPosition(this, new Coordinate(e.getPoint()));
        
                this.spritePanel.repaint();
            }
            if(e.getButton() == e.BUTTON2){
                 this.setStartLocation();
            }
        }else if(currentTask == TASK_ADDFOREGROUNDITEM){
            if(e.getButton() == e.BUTTON1){
                try{
                    ImageWithDetails theImage = containingFrame.getImageChooser();
                    
                    //if image is null then the opening was cancelled
                    if(theImage != null){
                        foregroundPanel.addImage(new Coordinate(e.getX(),e.getY()),theImage);
                    }
                    

                    
                }catch(IOException ioe){
                    System.err.println("add foreground image. "+ioe);
                }catch(ImageNotSelectedException inse){
                    System.err.println("add foreground image. "+inse);
                }
            }
            if(e.getButton() == e.BUTTON2){

                currentTask = TASK_IDLE;
                 foregroundPanel.deSelectAll();
                
                 containingFrame.setCursorNormal();



            }
            if(e.getButton() == e.BUTTON3){
                foregroundPanel.removeImage(here);
                foregroundPanel.repaint();

            }

        }else if(currentTask == TASK_EDITFOREGROUND){
            if(e.getButton() == e.BUTTON1){
                //selects any items under the mouse, also remembers if a selection
                // was made in itemSelected
                boolean itemSelected = foregroundPanel.select(new Coordinate(e.getX(),e.getY()));
                this.repaint();
                
                moveForegroundFrom = new Coordinate(e.getX(),e.getY());
                //So that it only moves if an item was selected
                if(itemSelected == true && foregroundPanel.checkSelectedResizer(here) == -1){
                    moveForeground = true;
                }

                if(itemSelected == true){
                    
                    resizeForeground = foregroundPanel.checkSelectedResizer(here);
                    resizeForgroundFrom = here;
                }


                foregroundPanel.repaint();

            }
            if(e.getButton() == e.BUTTON2){

                currentTask = TASK_IDLE;
                foregroundPanel.deSelectAll();
                this.glassPanel.repaint();
                 containingFrame.setCursorNormal();



            }
            if(e.getButton() == e.BUTTON3){
                foregroundPanel.removeImage(here);
                foregroundPanel.repaint();
          
            }
        }else if(currentTask == TASK_ADDEVENT){

         
            if(e.getButton() == e.BUTTON1){

                if (e.getClickCount() == 2) {
                    //double click
                    eventPanel.setCurrentAreaComplete();
                }else{

                 
                    if(eventPanel.isAreaComplete() == true){
               
                        eventPanel.addEvent(new Coordinate(e.getX(),e.getY()));
              
                    }else{
                        eventPanel.addPoint(new Coordinate(e.getX(),e.getY()));
        
                    }
                    


                }
            }
            if(e.getButton() == e.BUTTON2){

                currentTask = TASK_IDLE;

                 walkableAreaPanel.setCurrentComplete();
                 eventPanel.setCurrentAreaComplete();
                 containingFrame.setCursorNormal();



            }
            if(e.getButton() == e.BUTTON3){
               eventPanel.removeEvent(new Coordinate(e.getX(),e.getY()));
            }
            eventPanel.repaint();

            
        }else if(currentTask == TASK_EDITWALKABLE){
            
             //if right mouse button is pressed
            if(e.getButton() == e.BUTTON1){

                if (e.getClickCount() == 2) {
                    //double click
                    walkableAreaPanel.setCurrentComplete();
                }else{
                    //single click
                  walkableAreaPanel.addPoint(new Coordinate(e.getX(),e.getY()));
                }
               
            }
            
            if(e.getButton() == e.BUTTON3){
                walkableAreaPanel.deleteShapeAtPoint(new Coordinate(e.getX(),e.getY()));
            }

             //if left mouse button is pressed
            if(e.getButton() == e.BUTTON2){
                
                currentTask = TASK_IDLE;

                 walkableAreaPanel.setCurrentComplete();
                 containingFrame.setCursorNormal();

                

            }
      
             walkableAreaPanel.repaint();
        }

        /** This is used to test that events can be triggered without having to
         * actually play the game.
        else{

            boolean walk = walkableAreaPanel.isPointWithin(new Coordinate(e.getX(), e.getY()));
            if(walk == true){
                System.out.println("walkable");
            }else{
                System.out.println("Non-walkable");
            }
            eventPanel.triggerAnyEventAtPoint(new Coordinate(e.getX(),e.getY()));
        
        }**/

        
       
     
   
    };

    public void mouseReleased(MouseEvent e){

        if(resizeForeground >-1){
            foregroundPanel.setFinishResize();
            resizeForeground = -1;
        }
         if(currentTask == this.TASK_ADDEVENT){
           eventPanel.setShapeComplete();
           eventPanel.repaint();
        }

        
        moveForeground = false;
        
    
    };

    public void mouseEntered(MouseEvent e){
    }

    public void mouseExited(MouseEvent e){
    }

   
    public void mouseMoved(MouseEvent me) {
        //resets the cursor if a temporary cursor is no longer set
        if(temporaryCursor == true){
            temporaryCursor = false;
            containingFrame.setCursor(theCursor);
        }


         
        
        if(currentTask == TASK_EDITWALKABLE){

            walkableAreaPanel.setLastMousePosition((int) me.getPoint().getX(), (int) me.getPoint().getY());

            if(walkableAreaPanel.isShapeInProgress() == false){
                walkableAreaPanel.repaint();
            }

           

         }else if(currentTask == this.TASK_ADDEVENT){
             eventPanel.setLastMousePosition((int) me.getPoint().getX(), (int) me.getPoint().getY());
             if(eventPanel.isAreaInProgress() == false){
                 
                eventPanel.repaint();
             }
         }else if(currentTask == this.TASK_EDITFOREGROUND){
             if (foregroundPanel.getSelectedItem() > -1){
                
                 int found = foregroundPanel.checkSelectedResizer(new Coordinate((int) me.getPoint().getX(), (int) me.getPoint().getY()));
                 //stores the previous cursor if this is the first highlight
                 if(found > -1){
                     if(temporaryCursor == false){
                        theCursor = containingFrame.getCursor();
                     }
                     if(found == 0){
                        containingFrame.setCursor(new Cursor(Cursor.NW_RESIZE_CURSOR));
                     }else if(found == 1){
                         containingFrame.setCursor(new Cursor(Cursor.NE_RESIZE_CURSOR));
                     }else if(found == 2){
                         containingFrame.setCursor(new Cursor(Cursor.SW_RESIZE_CURSOR));
                     }else if(found == 3){
                         containingFrame.setCursor(new Cursor(Cursor.SE_RESIZE_CURSOR));
                     }
                     temporaryCursor = true;
                 }

             }
         }


         

    }

    public void mouseDragged(MouseEvent me) {
        Coordinate here = new Coordinate((int)me.getPoint().getX(),(int)me.getPoint().getY());
        if(currentTask == this.TASK_ADDEVENT){
           eventPanel.setCurrentShapeDiameter(here);
           eventPanel.repaint();
        }
       //eventPanel.setCurrentAreaComplete();
          
        if(currentTask == TASK_EDITPERSPECTIVE){
             if(resizePerspectiveFrom != null){
                 perspectivePanel.changePerspective(resizePerspectiveFrom.getPoint(), here.getPoint());
     
                 perspectivePanel.repaint();
             }
        }else if(currentTask == TASK_EDITFOREGROUND){

            if(moveForeground == true && moveForegroundFrom != null){
                foregroundPanel.moveSelected(moveForegroundFrom,here);
               
            }

            if(resizeForeground != -1 && resizeForeground <=3){
                foregroundPanel.resizeSelected(resizeForeground, resizeForgroundFrom,here);
                glassPanel.repaint();
            }

        }
    }

    public void fromXML(Element rootElement)throws GameXmlIncorrectException{

  
        //sets up the perspective panel
        getPerspectivePanel().fromXML(
                XMLSimplify.getStrictlySingleElement(rootElement,"perspective"));

        //sets up the foreground from XML
        getForegroundPanel().fromXML(
                XMLSimplify.getStrictlySingleElement(rootElement,"foreground"));

        //sets the background image of the map from the xml rootElement
        getBackgroundPanel().fromXML(
                XMLSimplify.getStrictlySingleElement(rootElement,"background"));

        //sets the walkable area for this map
        this.getWalkableAreaPanel().fromXML(
                XMLSimplify.getStrictlySingleElement(rootElement,"walkover"));

        //sets the events for this map
        this.getEventPanel().fromXML(
                XMLSimplify.getStrictlySingleElement(rootElement,"events"));


        
    }


    public String toXML(){
        return this.toXML("<gamemap id=\"needtospecify\">","</gamemap>");
    }

    public String toXML(String openTag, String closeTag){
        String xml = "";
    
        xml += openTag+"\n";
        //this.backgroundPanel.to

        xml += this.perspectivePanel.toXML();
        xml += this.foregroundPanel.toXML();
        xml += this.backgroundPanel.toXML();
        xml += this.walkableAreaPanel.toXML();
        xml += this.eventPanel.toXML();



        xml += closeTag+"\n";
        return xml;
    }

     


   



}
