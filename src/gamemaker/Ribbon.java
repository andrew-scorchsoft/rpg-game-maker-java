/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gamemaker;

import gamemaker.Exceptions.GameXmlIncorrectException;
import gamemaker.Exceptions.ImageNotSelectedException;
import gamemaker.Player.PlayFrame;
import gamemaker.RightPanels.RightPanel;
import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.imageio.ImageIO;
import javax.swing.JCheckBox;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;


import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.filechooser.FileFilter;
import org.jdom.Document;
import org.jdom.Element;

import org.jdom.JDOMException;

import org.jvnet.flamingo.common.JCommandButton;
import org.jvnet.flamingo.common.JCommandToggleButton;
import org.jvnet.flamingo.common.StringValuePair;
import org.jvnet.flamingo.common.icon.ImageWrapperResizableIcon;
import org.jvnet.flamingo.common.icon.ResizableIcon;
import org.jvnet.flamingo.ribbon.*;
import org.jvnet.flamingo.ribbon.resize.CoreRibbonResizePolicies;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;






/**
 *
 * @author ug77alw
 */
public class Ribbon extends JRibbonFrame implements ActionListener, MapsContainer{

          private GameMaps maps;
          private JScrollPane contentScroll;
          private RightPanel rightPanel;
          //private String workingDir;
          //private String workingURL;
          private File nullFile = new File("This a null file alsbalibflaibflabh9892214");
          private File lastSaveorLoadLocation = nullFile;
          private JCheckBox conditional;

          private JCommandToggleButton squareButton;
          private JCommandToggleButton circleButton;
          private JCommandToggleButton areaButton;

          private JCommandToggleButton messageButton;
          private JCommandToggleButton teleportButton;
          private JCommandToggleButton variableButton;

          private JRibbonBand eventShapeBand;

          // icons from http://www.iconspedia.com/
          


          public GameMaps getGameMaps(){
              return maps;
          }
          public JScrollPane getJScrollPane(){
              return contentScroll;
          }
            public String getContainterType(){
                return "ribbon";
            }

          public void updatePreferredSizes(){
                this.repaint();

                int newWidth = (int)this.getSize().getWidth()-(int)rightPanel.getPreferredSize().getWidth()-17;
                int newHeight = this.getHeight()-this.getRibbonHeight()-38;
          
               contentScroll.setPreferredSize( new Dimension(newWidth,newHeight));
               this.pack();
          }
      

         
    public Ribbon() {
 


                //sets the working directory
      
                //workingDir = this.getClass().getResource("").getPath();
                //workingURL = this.getClass().getResource("files/")
                //workingDir = workingDir.substring(1); //cuts the first / that mysteriously appears
                //workingDir = workingDir.replace("%20", " ") + "files/";
                
                  try{

                    //band.setResizePolicies(CoreRibbonResizePolicies.getCorePoliciesRestrictive(band));

                    this.setTitle("Game Maker");
              
                   // Image iconImage = FileHandler.imageFromFileOnly(this.getClass().getResource("icons/gamelogo.png").g);
                    Image iconImage = FileHandler.imageFromFileOnly(FileHandler.getFilesLocation()+"gamelogo.png");
                    this.setIconImage(iconImage);
                    

                    RibbonTask homeTask = new RibbonTask(   "Tools",
                                                        getCommonToolsBand(),
                                                        //getZoomBand(),
                                                        //getPlayBand(),
                                                         getMapEditBand(),
                                                        getLayerBand());

                    RibbonTask mapTask = new RibbonTask(    "Events",
                                                            
                                                            getEventsBand(),
                                                            getEventShapeBand(),
                                                            getEventConfigBand()
                                                             );

                    this.getRibbon().addTask(homeTask);
                    this.getRibbon().addTask(mapTask);

                    configureTaskBar();
               
                    //this.setMinimumSize(new java.awt.Dimension(800, 600));
                    //this.setPreferredSize(new java.awt.Dimension(1024,768));
                   // this.setPreferredSize(new Dimension(800,600));
                    this.setSize(800, 600);
                    this.setLocationRelativeTo(null);
                    this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);


              
                    //this.add(gameMap);
                    //this.repaint();
                    //gameMap.setVisible(true);

               

                 
                    maps = new GameMaps(this);
              
                    contentScroll= new JScrollPane();
                    contentScroll.add(maps.getCurrentMap());
                    contentScroll.setViewportView(maps.getCurrentMap());
            
                    //contentScroll.setEnabled(true);

                    contentScroll.setVisible(true);
                    this.repaint();
                    //contentScroll.repaint();
                 
                   // this.add(gameMap,BorderLayout.CENTER);
                   this.add(contentScroll,BorderLayout.CENTER);

                    
                    rightPanel = new RightPanel(this);
                    this.add(rightPanel,BorderLayout.EAST);

                    //full screens the window
                    this.setExtendedState(this.MAXIMIZED_BOTH);
                     

                     }catch(MalformedURLException e){
                            System.err.println(e.toString());

                    }catch(IOException ioe){
                            System.err.println(ioe);
                            ioe.printStackTrace();
                    }

	}

        public void newGame()throws IOException, JDOMException{

            int reply = JOptionPane.showConfirmDialog(this, "Are you sure that you want to start a new game?");

            if(reply == JOptionPane.OK_OPTION){

                reply = JOptionPane.showConfirmDialog(this, "Do you want to save your game?\n" +
                        "If not then all game data will be lost.");
                boolean saveSuccess = true;
                if(reply == JOptionPane.OK_OPTION){
                    saveSuccess = this.saveGame();
                }

                //only creates a new game if the user has successfully saved, or says that they don't want to save
                if((reply == JOptionPane.OK_OPTION && saveSuccess == true)|| reply == JOptionPane.NO_OPTION){

                    lastSaveorLoadLocation = nullFile;
                    maps = new GameMaps(this);

                    contentScroll.remove(maps.getCurrentMap());
                    //this.remove(contentScroll);
                    this.remove(rightPanel);

                    contentScroll.remove(maps.getCurrentMap());


                    //contentScroll= new JScrollPane();
                    contentScroll.add(maps.getCurrentMap());
                    contentScroll.setViewportView(maps.getCurrentMap());

                    contentScroll.setEnabled(true);

                    contentScroll.setVisible(true);

                   this.add(contentScroll,BorderLayout.CENTER);


                    rightPanel = new RightPanel(this);
                    this.add(rightPanel,BorderLayout.EAST);

                    this.updatePreferredSizes();
                }
            }
        }

        private JRibbonBand getEventConfigBand()throws MalformedURLException{
            JRibbonBand eventConfigShapeBand = new JRibbonBand("Event Creation", makeIcon("icons/thunder.png"));
            conditional = new JCheckBox("Conditional Events");
            JRibbonComponent conditionalWrapper = new JRibbonComponent(conditional);
             eventConfigShapeBand.addRibbonComponent(conditionalWrapper);
             conditional.addActionListener(this);
             return eventConfigShapeBand;
        }


        private JRibbonBand getEventShapeBand() throws MalformedURLException{
             eventShapeBand = new JRibbonBand("Event Shape", makeIcon("icons/thunder.png"));

             ResizableIcon SquareIcon = makeIcon("icons/square.png");
             ResizableIcon circleIcon = makeIcon("icons/circle.png");
             ResizableIcon areaIcon = makeIcon("icons/area.png");

             squareButton = new JCommandToggleButton("Square",  SquareIcon);
             circleButton = new JCommandToggleButton("circle",  circleIcon);
             areaButton = new JCommandToggleButton("Area",  areaIcon);

             squareButton.addActionListener(this);
             squareButton.setActionCommand("SQUARE_EVENTSHAPE");
             circleButton.addActionListener(this);
             circleButton.setActionCommand("CIRCLE_EVENTSHAPE");
             areaButton.addActionListener(this);
             areaButton.setActionCommand("AREA_EVENTSHAPE");

             //this dictates how many icons show up in the gallery
             //indicates the priority that the gallery stays big depending on how many icons are show
             Map<RibbonElementPriority, Integer> buttonCount = new HashMap<RibbonElementPriority, Integer>();
		buttonCount.put(RibbonElementPriority.LOW, 2);
		buttonCount.put(RibbonElementPriority.MEDIUM,3);
		buttonCount.put(RibbonElementPriority.TOP, 3);



            //adds buttons to the list of buttons. This list is what will be displayed
            List<JCommandToggleButton> galleryButtonsList = new ArrayList<JCommandToggleButton>();
            galleryButtonsList.add(areaButton);
            galleryButtonsList.add(squareButton);
            galleryButtonsList.add(circleButton);

            // For some reason you have to wrap the list of JCommandToggleButton within another list
            // before you can add it+
            List<StringValuePair<List<JCommandToggleButton>>> galleryButtons = new ArrayList<StringValuePair<List<JCommandToggleButton>>>();
            galleryButtons.add(new StringValuePair<List<JCommandToggleButton>>(null,galleryButtonsList));
            eventShapeBand.addRibbonGallery("Event Shapes",
                            galleryButtons, buttonCount,
                            3, 3, RibbonElementPriority.MEDIUM);

            eventShapeBand.setSelectedRibbonGalleryButton("Event Shapes", areaButton);


            return eventShapeBand;
        }


        public void deselectEventButtons(){
            messageButton.getActionModel().setSelected(false);
            teleportButton.getActionModel().setSelected(false);
            variableButton.getActionModel().setSelected(false);
        }
         private JRibbonBand getEventsBand() throws MalformedURLException{
                //Icon passed to Band will be displayed when window is too small to show whole band
		JRibbonBand transitionBand = new JRibbonBand("Events", makeIcon("icons/thunder.png"));

                //creating icons
                ResizableIcon bubbleIcon = makeIcon("icons/bubble.png");
                ResizableIcon keyIcon = makeIcon("icons/threeballs4.png");
                ResizableIcon teleportIcon = makeIcon("icons/thunder.png");
                    



                messageButton = new JCommandToggleButton("Message",  bubbleIcon);
                messageButton.addActionListener(this);
                messageButton.setActionCommand("Message_Event");

                teleportButton = new JCommandToggleButton("Teleport",  teleportIcon);
                teleportButton.addActionListener(this);
                teleportButton.setActionCommand("Message_Teleport");

                variableButton = new JCommandToggleButton("Variable",  keyIcon);
                variableButton.addActionListener(this);
                variableButton.setActionCommand("Variable_Event");
          



                //this dictates how many icons show up in the gallery
                //indicates the priority that the gallery stays big depending on how many icons are show
                Map<RibbonElementPriority, Integer> buttonCount = new HashMap<RibbonElementPriority, Integer>();
		buttonCount.put(RibbonElementPriority.LOW, 1);
		buttonCount.put(RibbonElementPriority.MEDIUM,2);
		buttonCount.put(RibbonElementPriority.TOP, 3);


                
                //adds buttons to the list of buttons. This list is what will be displayed
                List<JCommandToggleButton> galleryButtonsList = new ArrayList<JCommandToggleButton>();
                galleryButtonsList.add(messageButton);
                galleryButtonsList.add(variableButton);
                galleryButtonsList.add(teleportButton);
                


                // For some reason you have to wrap the list of JCommandToggleButton within another list
                // before you can add it+
                List<StringValuePair<List<JCommandToggleButton>>> galleryButtons = new ArrayList<StringValuePair<List<JCommandToggleButton>>>();
                galleryButtons.add(new StringValuePair<List<JCommandToggleButton>>(null,galleryButtonsList));
                transitionBand.addRibbonGallery("Transitions",
				galleryButtons, buttonCount,
				6, 4, RibbonElementPriority.MEDIUM);


                return transitionBand;
         }

         private JRibbonBand getMapEditBand() throws MalformedURLException{
             ResizableIcon musicIcon = makeIcon("icons/music.png");
             ResizableIcon locationIcon = makeIcon("icons/location.png");
             ResizableIcon bgImageIcon = makeIcon("icons/bgimage.png");
             //ResizableIcon barrierIcon = makeIcon("icons/barrier.png");

              //Icon passed to Band will be displayed when window is too small to show whole band
             JRibbonBand mapTools = new JRibbonBand("Map Tools", locationIcon);

             JCommandButton addForegroundImage = new JCommandButton("Add Foreground",  bgImageIcon);
             addForegroundImage.setActionCommand("AddForeground");
             addForegroundImage.addActionListener(this);

             JCommandButton setDefaultLocation = new JCommandButton("Default Location",  locationIcon);
             setDefaultLocation.setActionCommand("DefaultLocation");
             setDefaultLocation.addActionListener(this);

             JCommandButton setBackgroundImage = new JCommandButton("Background Image",  bgImageIcon);
             setBackgroundImage.setActionCommand("Background");
             setBackgroundImage.addActionListener(this);

             //JCommandButton setBackgroundMusic = new JCommandButton("Background Music",  musicIcon);
             //setBackgroundMusic.setActionCommand("Background Music");
             //setBackgroundMusic.addActionListener(this);

             mapTools.addCommandButton(addForegroundImage,RibbonElementPriority.LOW);
             //mapTools.addCommandButton(setBackgroundMusic,RibbonElementPriority.LOW);
             mapTools.addCommandButton(setDefaultLocation, RibbonElementPriority.LOW);
             mapTools.addCommandButton(setBackgroundImage,RibbonElementPriority.LOW);
            // mapTools.addCommandButton(new JCommandButton("Insert Barrier",  barrierIcon),RibbonElementPriority.MEDIUM);

             return mapTools;
         }
          
        private JRibbonBand getCommonToolsBand() throws MalformedURLException{
            //System.out.println("working: " + workingDir + "\n");
            ResizableIcon saveIcon = makeIcon("icons/save.png");
            ResizableIcon saveAsIcon = makeIcon("icons/saveas.png");
            ResizableIcon loadIcon = makeIcon("icons/load2.png");
            ResizableIcon newProjectIcon = makeIcon("icons/newproject.png");
            //ResizableIcon closeIcon = makeIcon("icons/close.png");
            ResizableIcon newMapIcon = makeIcon("icons/newMap.png");
            ResizableIcon deleteIcon = makeIcon("icons/delete.png");
            ResizableIcon playIcon = makeIcon("icons/play_1.png");




            JCommandButton newMapButton = new JCommandButton("New Map", newMapIcon);
            newMapButton.setActionCommand("Add New Map");
            newMapButton.addActionListener(this);

            JCommandButton saveGameIcon = new JCommandButton("Save", saveIcon);
            saveGameIcon.setActionCommand("Save Game");
            saveGameIcon.addActionListener(this);

            JCommandButton saveAsGameIcon = new JCommandButton("Save As", saveAsIcon);
            saveAsGameIcon.setActionCommand("Save Game As");
            saveAsGameIcon.addActionListener(this);

            JCommandButton loadGameIcon = new JCommandButton("Load", loadIcon);
            loadGameIcon.setActionCommand("Load Game");
            loadGameIcon.addActionListener(this);

            JCommandButton testPlay = new JCommandButton("Test Play", playIcon);
            testPlay.setActionCommand("Test Play");
            testPlay.addActionListener(this);

            JCommandButton newGame = new JCommandButton("New Game", newProjectIcon);
            newGame.setActionCommand("New Game");
            newGame.addActionListener(this);

            JCommandButton deleteMap = new JCommandButton("Delete Map", deleteIcon);
            deleteMap.setActionCommand("Delete Map");
            deleteMap.addActionListener(this);

             //Icon passed to Band will be displayed when window is too small to show whole band
            JRibbonBand commonTools = new JRibbonBand("Common Tools", newProjectIcon);
            commonTools.addCommandButton(saveGameIcon, RibbonElementPriority.MEDIUM);
            commonTools.addCommandButton(saveAsGameIcon, RibbonElementPriority.MEDIUM);
            commonTools.addCommandButton(loadGameIcon,RibbonElementPriority.MEDIUM);
            commonTools.addCommandButton(newGame,RibbonElementPriority.MEDIUM);
           
            //commonTools.addCommandButton(new JCommandButton("Close Game", closeIcon),RibbonElementPriority.MEDIUM);
            commonTools.addCommandButton(newMapButton, RibbonElementPriority.MEDIUM);
            commonTools.addCommandButton(deleteMap,RibbonElementPriority.MEDIUM);
            commonTools.addCommandButton(testPlay, RibbonElementPriority.MEDIUM);

            return commonTools;
        }

        private JRibbonBand getZoomBand() throws MalformedURLException{

            ResizableIcon zoomIcon = makeIcon("icons/zoom.png");
            ResizableIcon zoomIconSmall = makeIcon("icons/zoom.png");

             //Icon passed to Band will be displayed when window is too small to show whole band
            JRibbonBand zoomBand = new JRibbonBand("Zoom",zoomIcon);

            JCommandButton formatButton = new JCommandButton("Zoom",zoomIcon);
            zoomBand.addCommandButton(formatButton, RibbonElementPriority.TOP);

            zoomBand.addCommandButton(new JCommandButton("100%", zoomIconSmall),
                            RibbonElementPriority.MEDIUM);
            zoomBand.addCommandButton(new JCommandButton("200%", zoomIconSmall),
                            RibbonElementPriority.MEDIUM);
            zoomBand.addCommandButton(new JCommandButton("400%", zoomIconSmall),
                            RibbonElementPriority.MEDIUM);

            zoomBand.setResizePolicies(CoreRibbonResizePolicies
                	.getCorePoliciesRestrictive(zoomBand));

            return zoomBand;

        }
        private JRibbonBand getPlayBand() throws MalformedURLException{
            ResizableIcon playIcon = makeIcon("icons/play_1.png");
            ResizableIcon buildIcon = makeIcon("icons/build2.png");

            JCommandButton testPlay = new JCommandButton("Test Play", playIcon);

            //Icon passed to Band will be displayed when window is too small to show whole band
             JRibbonBand playBand = new JRibbonBand("Build Tools",playIcon);

              playBand.addCommandButton(testPlay,RibbonElementPriority.MEDIUM);
              testPlay.addActionListener(this);

              playBand.addCommandButton(new JCommandButton("Make Game", buildIcon),
                            RibbonElementPriority.MEDIUM);
              playBand.setResizePolicies(CoreRibbonResizePolicies
                	.getCorePoliciesRestrictive(playBand));

              return playBand;
        }

        protected void configureTaskBar() throws MalformedURLException{
            ResizableIcon saveasIcon = makeIcon("icons/saveas.png");
            ResizableIcon saveIcon = makeIcon("icons/save.png");
            ResizableIcon loadIcon = makeIcon("icons/load2.png");
            ResizableIcon testPlayIcon = makeIcon("icons/play_1.png");
            ResizableIcon finishJobIcon = makeIcon("icons/tick.png");

            JCommandButton taskbarButtonSaveAs = new JCommandButton("",saveasIcon);
            taskbarButtonSaveAs.setActionCommand("Save Game As");
            taskbarButtonSaveAs.addActionListener(this);

            JCommandButton taskbarButtonSave = new JCommandButton("",saveIcon);
            taskbarButtonSave.setActionCommand("Save Game");
            taskbarButtonSave.addActionListener(this);

            JCommandButton taskbarButtonLoad = new JCommandButton("",loadIcon);
            taskbarButtonLoad.setActionCommand("Load Game");
            taskbarButtonLoad.addActionListener(this);

            JCommandButton taskbarButtonTestPlay = new JCommandButton("",testPlayIcon);
            taskbarButtonTestPlay.setActionCommand("Test Play");
            taskbarButtonTestPlay.addActionListener(this);


            JCommandButton taskbarfinishJob = new JCommandButton("",finishJobIcon);
            taskbarfinishJob .setActionCommand("Done Editing");
            taskbarfinishJob .addActionListener(this);

            this.getRibbon().addTaskbarComponent(taskbarButtonSave);
            this.getRibbon().addTaskbarComponent(taskbarButtonSaveAs);
            this.getRibbon().addTaskbarComponent(taskbarButtonLoad);
            this.getRibbon().addTaskbarComponent(new JSeparator(JSeparator.VERTICAL));
            this.getRibbon().addTaskbarComponent(taskbarButtonTestPlay);
            this.getRibbon().addTaskbarComponent(new JSeparator(JSeparator.VERTICAL));
            this.getRibbon().addTaskbarComponent(taskbarfinishJob );
        }


         private JRibbonBand getLayerBand() throws MalformedURLException{
            ResizableIcon layersIcon = makeIcon("icons/layers.png");

            ResizableIcon walkoverIcon = makeIcon("icons/Wo.png");
            ResizableIcon backgroundIcon = makeIcon("icons/Bg.png");
            ResizableIcon eventIcon = makeIcon("icons/Ev.png");
            ResizableIcon foregroundIcon = makeIcon("icons/Fg.png");
            ResizableIcon PerspectiveIcon = makeIcon("icons/Pe.png");

            ResizableIcon finishJobIcon = makeIcon("icons/tick.png");



             JCommandButton walkOverButton = new JCommandButton("Walk-Over", walkoverIcon);
             walkOverButton.addActionListener(this);
            //walkOverButton.ad("Edit Walk Over Area");


             JCommandButton backgroundLayerButton = new JCommandButton("Background", backgroundIcon);
             backgroundLayerButton.addActionListener(this);

             JCommandButton eventLayerButton = new JCommandButton("Event", eventIcon);
             eventLayerButton.addActionListener(this);

             JCommandButton foregroundButton = new JCommandButton("Foreground", foregroundIcon);
             foregroundButton.setActionCommand("EditForeground");
             foregroundButton.addActionListener(this);

             JCommandButton perspectiveButton = new JCommandButton("Perspective", PerspectiveIcon);
             perspectiveButton.setActionCommand("Edit Perspective");
             perspectiveButton.addActionListener(this);

            JCommandButton taskbarfinishJob = new JCommandButton("Finish Current Edit",finishJobIcon);
            taskbarfinishJob.setActionCommand("Done Editing");
            taskbarfinishJob.addActionListener(this);
             //Icon passed to Band will be displayed when window is too small to show whole band
             JRibbonBand layerBand = new JRibbonBand("Layer To Edit",layersIcon);

            

              layerBand.addCommandButton(foregroundButton, RibbonElementPriority.MEDIUM);
              layerBand.addCommandButton(eventLayerButton,RibbonElementPriority.MEDIUM);

              layerBand.addCommandButton(perspectiveButton,RibbonElementPriority.MEDIUM);
              layerBand.addCommandButton(walkOverButton,RibbonElementPriority.MEDIUM);
              layerBand.addCommandButton(backgroundLayerButton,RibbonElementPriority.MEDIUM);
                layerBand.addCommandButton(taskbarfinishJob,RibbonElementPriority.MEDIUM);
                
              layerBand.setResizePolicies(CoreRibbonResizePolicies
                	.getCorePoliciesRestrictive(layerBand));

              return layerBand;
        }

      


        private ResizableIcon makeIcon(String iconLocation) throws MalformedURLException{
     
                return makeIcon(this.getClass().getResource(iconLocation));

        }
        private ResizableIcon makeIcon(URL location) throws MalformedURLException{

            Dimension initialDim = new Dimension(150,150);
            return ImageWrapperResizableIcon.getIcon(location,initialDim);
        }



        public boolean saveGame(String location)throws IOException, JDOMException{
            return saveGame(new File(location));
        }
        public boolean saveGame() throws IOException, JDOMException{
            JFileChooser jfc = new JFileChooser();
            jfc.setSelectedFile(new File(FileHandler.getFilesLocation()+File.separator+"files"));
            jfc.setDialogTitle("Please select a save location");
            jfc.setSelectedFile(new File("mygame.game"));
            jfc.setFileFilter(new MyFileFilter("game","Game Files (.game)"));
            int result = jfc.showDialog(this, "Save");

            File f = jfc.getSelectedFile();

     

            if(result == jfc.APPROVE_OPTION){
                saveGame(f);
                lastSaveorLoadLocation = f;
                
                return true;
            }else if(result == jfc.CANCEL_OPTION || result == jfc.ERROR_OPTION ){
                System.out.println("Save was not competed.");
                return false;
            }
            return true;


        }
        /**
         *
         * @param location
         * @return true if game save was a success
         * @throws IOException
         * @throws JDOMException
         */
        public boolean saveGame(File location) throws IOException, JDOMException{
            

            //gets the file extension as lower case
            String fileExtension = "canceledfileselection";
            if(location!=null){
                 fileExtension= FileHandler.getFileExtension(location).toLowerCase();
            }

           //only checks if the user has actually selected a file.
            if(!fileExtension.equals("canceledfileselection")){

                String s =maps.toXML();
                // SAX builder takes a boolean value meaning validation mode:
                SAXBuilder builder = new SAXBuilder(false);

                //load the document::
                Document document = builder.build(new StringReader(s));




                //deletes the save folder first
                FileHandler.deleteDirectory(FileHandler.getFilesLocation()+"gamesave"+File.separator);
                //recreates the save folder so that it is FRESH XD
                FileHandler.createDefaultSaveDirectories();
                //saves the images to "gamesave/" within their corresponding folders
                maps.saveImages();

                ////////////////////////////////////////////////////////
                ///////////Handles the saving of the XML file itself

                    //this will export the XML
                    XMLOutputter x = new XMLOutputter();
                    //makes sure the format is nice when it saves
                    x.setFormat(Format.getPrettyFormat());
                    //this sets up a stream on the file at location "gamesave/gamesave.xml";
                    //the file is automatically overridden if it already exists (which it wont
                    // as it is deleted earler
                    FileWriter fileWrite = new FileWriter(FileHandler.getFilesLocation()+"gamesave"+File.separator+"gamesave.xml");
                    BufferedWriter buffWrite = new BufferedWriter(fileWrite);
                    //outputs the XML document using the buffered writer
                    x.output(document, buffWrite);

                    //FileHandler.zipDir("gamesave","gamesave.game");
                    FileHandler.zipDir(FileHandler.getFilesLocation()+"gamesave",location.getAbsolutePath());

                    //closes the writers as we are done with them now.
                    fileWrite.close();
                    buffWrite.close();

                ////////////////////////////////////////////////////////

                    //updates the last file to have been saved/loaded
                    lastSaveorLoadLocation = location;
            }else{
                return false;
            }
            return true;
        }

     
        public void repaint(){
            super.repaint();
            if(contentScroll != null){
                contentScroll.setViewportView(maps.getCurrentMap());
                contentScroll.repaint();
            }
        }



        public void loadGameFileIntoPlayFrame(String fileLocation, PlayFrame playFrame)throws IOException, JDOMException, GameXmlIncorrectException{
            FileHandler.deleteDirectory(FileHandler.getFilesLocation()+"gamesave/");
            FileHandler.createDefaultSaveDirectories();
            FileHandler.unzipDir(fileLocation,FileHandler.getFilesLocation()+"gamesave");

            // SAX builder takes a boolean value meaning validation mode:
            SAXBuilder builder = new SAXBuilder(false);

            //load the document::

            Document document = builder.build(FileHandler.getFilesLocation()+"gamesave/gamesave.xml");

            Element gameElement = document.getRootElement();
            // use saxbuilder http://www.java2s.com/Code/Java/XML/UseSAXBuilderfromJDOM.htm
            //Iterator it = game.getChildren().iterator();

            //creates new empty gameMaps
            GameMaps newMaps = new GameMaps(playFrame);
            newMaps.fromXML(gameElement);
            newMaps.setPlayMode();
            newMaps.setCurrentMap(newMaps.getSprite().getCurrentMap());
            playFrame.setUpPlayFrame(newMaps);
            newMaps.setMapsContainer(playFrame);
            newMaps.getSprite().toString();
 
            


        }
        public void loadGameFile(String fileLocation) throws IOException, JDOMException, GameXmlIncorrectException{
            FileHandler.deleteDirectory(FileHandler.getFilesLocation()+"gamesave"+File.separator);
            FileHandler.createDefaultSaveDirectories();
            FileHandler.unzipDir(fileLocation,FileHandler.getFilesLocation()+"gamesave");

            // SAX builder takes a boolean value meaning validation mode:
            SAXBuilder builder = new SAXBuilder(false);

            //load the document::

            Document document = builder.build(FileHandler.getFilesLocation()+"gamesave"+File.separator+"gamesave.xml");

            Element game = document.getRootElement();
            // use saxbuilder http://www.java2s.com/Code/Java/XML/UseSAXBuilderfromJDOM.htm
            //Iterator it = game.getChildren().iterator();

            //creates new empty gameMaps
            GameMaps temp = maps;
            this.maps = new GameMaps(this);


            this.maps.fromXML(game);
             //removes the default map that is added when you create a new gamemaps object.

            contentScroll.remove(temp.getCurrentMap());
            contentScroll.add(maps.getCurrentMap());
            contentScroll.setViewportView(maps.getCurrentMap());
            maps.repaint();

            this.remove(rightPanel);
            rightPanel = new RightPanel(this);
            this.add(rightPanel,BorderLayout.EAST);


            this.updatePreferredSizes();

            //if the file didn't exist then it would have thrown an exception by now
             lastSaveorLoadLocation = new File(fileLocation);
  

        }
        public void actionPerformed(ActionEvent e){


                //System.out.println("Action Command: "+e.getActionCommand());New Game
            if(e.getActionCommand().equals("New Game")){
                try{
                    this.newGame();
                }catch(IOException ioe){
                    ioe.printStackTrace();
                }catch(JDOMException jdome){
                    jdome.printStackTrace();
                }
            }else if(e.getActionCommand().equals("Delete Map")){


                int reply = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete the current map?\n" +
                        "All events referencing it will also be deleted.");
 
                if(reply == JOptionPane.OK_OPTION){

                    if(this.maps.size() >= 2){
                        this.maps.removeCurrentMap();
                        rightPanel.updatePanelData();
                        rightPanel.repaint();
                        repaint();
                    }else{
                        JOptionPane.showMessageDialog(this, "You can not delete the last remaining map.\n" +
                                "Please add a new map first.");
                    }

                }


                
            }else
            if(e.getActionCommand().equals("Done Editing")){
                this.maps.getCurrentMap().setIdle();
            }else
             if(e.getActionCommand().equals("Conditional Events")){

                 if(conditional.isSelected()){
                    //this.maps.getCurrentMap().getEventPanel().setConditional(true);
                     this.maps.setEventsConditional(true);
                 }else{
                     this.maps.setEventsConditional(false);
                     //this.maps.getCurrentMap().getEventPanel().setConditional(false);
                 }
             }
             if(e.getActionCommand().equals("Load Game")){
                try {


                    JFileChooser jfc = new JFileChooser();
                    jfc.setSelectedFile(new File(FileHandler.getFilesLocation()+File.separator+"files"));
                    jfc.setFileFilter(new MyFileFilter("game","Game Files (.game)"));
                    jfc.showOpenDialog(jfc);
                    

                    File f = jfc.getSelectedFile();
                   
                    //gets the file extension as lower case
                    String fileExtension = "canceledfileselection";
                    if(f!=null){
                         fileExtension= FileHandler.getFileExtension(f).toLowerCase();
                    }

                   //only checks if the user has actually selected a file.
                    if(!fileExtension.equals("canceledfileselection")){
                        //only loads the file if the extension is correct
                        if(!fileExtension.equals("game")){
                            System.out.println("You must load a file of type .game");
                            JOptionPane.showMessageDialog(this, "You must load a file of type .game");
                        }else {
                            loadGameFile(f.getAbsolutePath());
                            lastSaveorLoadLocation = jfc.getSelectedFile();
                        }
                    }

                } catch (IOException ioe) {
                    ioe.printStackTrace();
                    JOptionPane.showMessageDialog(this, "Error loading the game file.");
                } catch (JDOMException jdome){
                    jdome.printStackTrace();
                    JOptionPane.showMessageDialog(this, "Error with the game XML formatting.");
                } catch (GameXmlIncorrectException gxie){
                    gxie.printStackTrace();
                    JOptionPane.showMessageDialog(this, "Error with the game XML formatting.");
                }

             }else if(e.getActionCommand().equals("Save Game As")){
                if(maps.getCurrentMap().getCurrentTask() == GameMap.TASK_IDLE){
                     try {

                        saveGame();

                    } catch (IOException ioe) {
                        ioe.printStackTrace();
                        JOptionPane.showMessageDialog(this, "Error saving the game file.");
                    } catch (JDOMException jdome){
                        jdome.printStackTrace();
                        JOptionPane.showMessageDialog(this, "Error with the game XML formatting.");
                    }
                }else{
                    JOptionPane.showMessageDialog(this,"Finish what you are doing first");
                }
             }else if(e.getActionCommand().equals("Save Game")){
                if(maps.getCurrentMap().getCurrentTask() == GameMap.TASK_IDLE){
                    try {
                        //only saves to existing location if it has been set.
                        //Otherwise a dialogue will be shown.
                        if( lastSaveorLoadLocation.equals(nullFile)){
                             saveGame();

                        }else{
                            saveGame(lastSaveorLoadLocation);
                             System.out.println("save in last location");

                        }
                    } catch (IOException ioe) {
                        ioe.printStackTrace();
                        JOptionPane.showMessageDialog(this, "Error saving the game file.");
                    } catch (JDOMException jdome){
                        jdome.printStackTrace();
                        JOptionPane.showMessageDialog(this, "Error with the game XML formatting.");
                }

               }else{
                   JOptionPane.showMessageDialog(this,"Finish what you are doing first");
               }

            }else if(e.getActionCommand().equals("Walk-Over")){
                maps.getCurrentMap().editWalkable();

            }else if(e.getActionCommand().equals("Background Music")){
                try{
                    URL musicURL = getMusicURL();
                    maps.getCurrentMap().setMusic(musicURL);
                }catch(MalformedURLException mue){
                    System.err.println("url or file incorrect");
                    JOptionPane.showMessageDialog(this, "URL or File Incorrect.");
                }
                
            }else if(e.getActionCommand().equals("DefaultLocation")){
                maps.getCurrentMap().setStartLocation();
            }else if(e.getActionCommand().equals("Test Play")){

               if(maps.getCurrentMap().getCurrentTask() == GameMap.TASK_IDLE){


                   //currently is set to always start play on map 1
                   maps.setCurrentMap(maps.getSprite().getCurrentMap());



                        File tempGame = new File(FileHandler.getFilesLocation()+"testplay.game");
                        //System.out.println(FileHandler.getFilesLocation()+"testplay.game");
                        try{

                            /**
                             * creates a new instance of gamemaps by saving and loading the
                             * current gamemaps. This is the fastest way to implement this
                             * as gamemaps can not be cloned easily. This needs to be done
                             * also so that the playing of the game doesnt effect the
                             * edit version.
                             */
                            File temp = lastSaveorLoadLocation;
                            saveGame(tempGame);
                            loadGameFile(tempGame.getAbsolutePath());
                            PlayFrame testPlay = new PlayFrame();
                            this.loadGameFileIntoPlayFrame(tempGame.getAbsolutePath(), testPlay);

                            this.setVisible(false);
                            testPlay.setRibbon(this);
                     
                            this.conditional.setSelected(false);
                            this.areaButton.getActionModel().setSelected(true);
                            lastSaveorLoadLocation = temp;


                        }catch(IOException ioe){
                            ioe.printStackTrace();
                            JOptionPane.showMessageDialog(this, "Error saving the game file for test play.");
                        }catch (JDOMException jdome){
                            jdome.printStackTrace();
                            JOptionPane.showMessageDialog(this, "Error in the games XML.");
                        }catch (GameXmlIncorrectException gxie){
                            gxie.printStackTrace();
                            JOptionPane.showMessageDialog(this, "Error in the games XML.");
                        }

               }else{
                   JOptionPane.showMessageDialog(this,"Finish what you are doing first");
               }
                    
            
               

            }else if(e.getActionCommand().equals("Edit Perspective")){
                
                maps.getCurrentMap().editPerspective();

               

            }else if(e.getActionCommand().equals("Add New Map")){

               int shape = maps.getCurrentMap().getEventCreatorShape();
                maps.getCurrentMap().setIdle();
                contentScroll.remove(maps.getCurrentMap());
             
                maps.addNewMap();
                


                contentScroll.add(maps.getCurrentMap());
                rightPanel.updatePanelData();
                rightPanel.repaint();
                repaint();
                maps.getCurrentMap().setEventCreatorShape(shape);

                messageButton.getActionModel().setSelected(false);
                teleportButton.getActionModel().setSelected(false);
                variableButton.getActionModel().setSelected(false);
                //button.getActionModel()
                /**
                try{
                eventShapeBand.setSelectedRibbonGalleryButton("Event Shapes", new JCommandToggleButton("", makeIcon("icons/load2.png")));
                }catch(MalformedURLException mue){
                    mue.printStackTrace();
                }**/

            }else if(e.getActionCommand().equals("EditForeground")){
                maps.getCurrentMap().editForeground();
            
            }else if(e.getActionCommand().equals("AddForeground")){
                maps.getCurrentMap().addForeground();
            }else if(e.getActionCommand().equals("Background")){
                maps.getCurrentMap().setBackgroundImage();
                rightPanel.updatePanelData();
                rightPanel.repaint();
            }else if(e.getActionCommand().equals("Message_Teleport")){



               
                

         
                
                maps.getCurrentMap().editEvents();
                
                maps.getCurrentMap().setEventCreator(maps.getCurrentMap().EVENT_TELEPORT);

                
            


                 
                  
             
                /**
                 private JCommandToggleButton messageButton;
          private JCommandToggleButton teleportButton;
          private JCommandToggleButton variableButton;
                 * **/
              

            }else if(e.getActionCommand().equals("Variable_Event")){



                /**
                 * This if statement ensures that the button doesnt deselect when
                 * moving from one selected event to another
                 */
                 if(maps.getCurrentMap().getEventCreator() != maps.getCurrentMap().EVENT_VARIABLE){
                     //variableButton.getActionModel().setSelected(true);
                     maps.getCurrentMap().setCurrentTask(GameMap.TASK_IDLE);
                     
                 }

                 maps.getCurrentMap().editEvents();
                 maps.getCurrentMap().setEventCreator(maps.getCurrentMap().EVENT_VARIABLE);
                 
            
                
            }else if(e.getActionCommand().equals("Message_Event")){


                /**
                 * This if statement ensures that the button doesnt deselect when
                 * moving from one selected event to another
                 */
                 if(maps.getCurrentMap().getEventCreator() != maps.getCurrentMap().EVENT_SOUT){
                     //variableButton.getActionModel().setSelected(true);
                     maps.getCurrentMap().setCurrentTask(GameMap.TASK_IDLE);

                 }
                maps.getCurrentMap().editEvents();
                maps.getCurrentMap().setEventCreator(maps.getCurrentMap().EVENT_SOUT);
                
              

            }else if(e.getActionCommand().equals("SQUARE_EVENTSHAPE")){
                maps.getCurrentMap().setEventCreatorShape(maps.getCurrentMap().SHAPE_SQUARE);
      
                //for some reason setting selected to a different button and then immediately
                //refocusing is the only way to allow the button the retain its selection if
                //it has been pressed twice

                 this.squareButton.getActionModel().setSelected(true);

            }else if(e.getActionCommand().equals("CIRCLE_EVENTSHAPE")){
                maps.getCurrentMap().setEventCreatorShape(maps.getCurrentMap().SHAPE_CIRCLE);
            

                this.circleButton.getActionModel().setSelected(true);

            }else if(e.getActionCommand().equals("AREA_EVENTSHAPE")){
                maps.getCurrentMap().setEventCreatorShape(maps.getCurrentMap().SHAPE_AREA);

                this.areaButton.getActionModel().setSelected(true);
              

            }else if(e.getActionCommand().equals("Event")){
                maps.getCurrentMap().editEvents();
              
            }

        }
/**
        private void setSelectedRibbonGalleryButton(String galleryName, JCommandToggleButton button){
            eventShapeBand.setSelectedRibbonGalleryButton(galleryName, n);

            eventShapeBand.setSelectedRibbonGalleryButton(galleryName, button);

        }**/

        public void setCursorCrosshair(){
            Cursor hourglassCursor = new Cursor(Cursor.CROSSHAIR_CURSOR);
            setCursor(hourglassCursor);
        }

        public void setCursorNormal(){
            Cursor normalCursor = new Cursor(Cursor.DEFAULT_CURSOR);
            setCursor(normalCursor);
            this.deselectEventButtons();
        }
        public void paintComponent(Graphics g){
           
            int height = (int)this.getSize().getHeight() - this.getRibbonHeight();
            int width = (int)this.getSize().getWidth()-100;
            contentScroll.setPreferredSize(new Dimension(width,height));


            
        }


        public URL getMusicURL() throws MalformedURLException{
            URL theURL = null;
            JFileChooser chooser = new JFileChooser();
            //FileFilter filter = new ImageFileFilter();
            //chooser.setFileFilter(filter);
            int returnVal = chooser.showOpenDialog(this);


            if(returnVal == JFileChooser.APPROVE_OPTION) {

                
                    theURL = new URL("file://"+chooser.getSelectedFile().getPath());
                    

           }
            
           return theURL;

        }
         public ImageWithDetails getImageChooser() throws ImageNotSelectedException, IOException{
                JFileChooser chooser = new JFileChooser();
                chooser.setSelectedFile(new File(FileHandler.getFilesLocation()+File.separator+"files"));
                FileFilter filter = new MyFileFilter();
                chooser.setFileFilter(filter);


                BufferedImage theImage = null;
                
                ImageWithDetails theImageWithDetails = null;
                int returnVal = chooser.showOpenDialog(this);
                if(returnVal == JFileChooser.APPROVE_OPTION) {

             


                    // Read from a file
                    theImage = ImageIO.read(chooser.getSelectedFile());
                    theImageWithDetails = new ImageWithDetails(theImage);
                    theImageWithDetails .setFileName(chooser.getSelectedFile().getName());
                   
                   
                }
            
                return theImageWithDetails;
          }
    @Override
         public String toString(){
             return "Ribbon";
         }


       

    
}
