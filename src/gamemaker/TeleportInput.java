/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gamemaker;

import gamemaker.RightPanels.MapList_RightPanel;
import gamemaker.events.ListActionListener;
import java.awt.BorderLayout;
import java.awt.Dialog.ModalityType;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionEvent;

/**
 *
 * @author Andy
 */
public class TeleportInput implements ListActionListener{

    private JDialog theDialog;

    private JScrollPane scrollPane;
    private MapList_RightPanel rightPanel;
    private JPanel buttonPanel;
    private JPanel infoPanel;
    private JLabel label;
    private GameMaps maps;
    private TeleportLocation teleportTo;
    private int initialMap;
    private Image markerImage;


    public TeleportInput(GameMaps maps){
  
        this.maps = maps;
        teleportTo = new TeleportLocation(100,100,0);
        initialMap = maps.getCurrentMapIndex();
        label = new JLabel(teleportTo.toString());

        infoPanel = new JPanel();
         infoPanel.add(label);
         buttonPanel = new JPanel();
         rightPanel = new MapList_RightPanel(maps,this);

         try{
            markerImage= ImageIO.read(new File(FileHandler.getFilesLocation()+"mapmarker.png"));

            
            if(markerImage != null){
                maps.getCurrentMap().getGlassPanel().setMarker(markerImage);
            }
        }catch(IOException ioe){
            System.err.println(ioe);
        }

    }
    private void constructFrame(){

        //defaults to position 100,100
         teleportTo = new TeleportLocation(100,100,0);
         maps.getCurrentMap().getGlassPanel().setMarkerPosition(new Coordinate(100,100));

         //Sets the label to tell the user where the teleport is set for
         label.setText(teleportTo.toString());

         //This is the dialogue box that will hold the panels
         //that will be required to set the teleport location
         theDialog = new JDialog();
         //this makes only the dialogue box active
         theDialog.setModalityType(ModalityType.APPLICATION_MODAL);

         theDialog.setSize(800, 600);
         theDialog.setTitle("Please Select A map to teleport to");
         theDialog.setDefaultCloseOperation(theDialog.DISPOSE_ON_CLOSE);
         theDialog.setLocationRelativeTo(null);

         //sets the dialogue box to use a bordered layout
         theDialog.setLayout(new BorderLayout());
         
         scrollPane = new JScrollPane(maps.getCurrentMap());

         //a panel that displays things from left to right
         BoxLayout buttonBoxLayout = new BoxLayout(buttonPanel, BoxLayout.X_AXIS);
         //this is needed for the buttons to actually align to the right
         buttonPanel.add(Box.createHorizontalGlue());
         buttonPanel.setLayout(buttonBoxLayout);
        

         JButton ok = new JButton("OK");
         JButton cancel = new JButton("Cancel");

         //the actionlistener in this object will be used if the buttons are pressed
         ok.addActionListener(this);
         cancel.addActionListener(this);

         buttonPanel.add(ok,"align right");
         buttonPanel.add(cancel,"align right");
         

         //adds the panes to the dialogue box
         theDialog.add(scrollPane,BorderLayout.CENTER);
         theDialog.add(rightPanel,BorderLayout.EAST);
         theDialog.add(buttonPanel,BorderLayout.SOUTH);
         theDialog.add(infoPanel,BorderLayout.NORTH);

         //sets the map to listen for mouse input.
         //variables in this method will then be changed using
         //setCoordinate(Coordinate c) by the map
         maps.getCurrentMap().setTeleportLocation(this);

         //repaints and sets the visibility of everything
         theDialog.setVisible(true);
         scrollPane.setVisible(true);
         scrollPane.repaint();
         theDialog.repaint();
         
    }
    public TeleportLocation getInput(){

        
         constructFrame();
         
        
       
        
        maps.getCurrentMap().unsetTeleportLocation();
        maps.setCurrentMap(initialMap);
        maps.repaint();
        maps.getMapsContainer().repaint();
        //clears the red marker from all maps so that it doesn't appear
        maps.clearAllMarkers();

        return teleportTo;


    }

    /**
     *  Sets the coordinate that the sprite will teleport to on
     * THE CURRENT MAP that is referenced
     * @param c the coordinate to teleport to
     */
    public void setCoordinate(Coordinate c){
        teleportTo.setX(c.getX());
        teleportTo.setY(c.getY());
        maps.getCurrentMap().getGlassPanel().setMarkerPosition(c);
        
        maps.repaint();
        label.setText(teleportTo.toString());
    }

     public void actionPerformed(ActionEvent e){

         if(e.getActionCommand().equals("OK")){
            //nothing, just closes textbox.
            //theMessage is already set via message.getInput(); in configureEvent()
            if(theDialog != null){
                this.closeDialogue();
            }
            maps.repaint();
            maps.getMapsContainer().repaint();
            
        }else if(e.getActionCommand().equals("Cancel")){
            teleportTo = null; //null will be returned if cancel is pressed
            if(theDialog != null){
                this.closeDialogue();
            }
            maps.repaint();
            maps.getMapsContainer().repaint();
         
        }


     }
     public void closeDialogue(){
        theDialog.setVisible(false);
        theDialog.dispose();
    }
     public void valueChanged(ListSelectionEvent e){
         if (e.getValueIsAdjusting() == false) {

            if (rightPanel != null && rightPanel.getList().getSelectedIndex() >= 0) {


                //stops the previous map looking for mouse input from this input box
                maps.getCurrentMap().unsetTeleportLocation();
                maps.getCurrentMap().getGlassPanel().clearMarker();


                scrollPane.remove(maps.getCurrentMap());
                //updates the current map to be what is selected in the list
                maps.setCurrentMap(rightPanel.getList().getSelectedIndex());
                
                //adds the new map to the scroll pane
                scrollPane.add(maps.getCurrentMap());
                scrollPane.setViewportView(maps.getCurrentMap());

                //sets up the map to look for mouse input and pass it to this method
                maps.getCurrentMap().setTeleportLocation(this);

                teleportTo.setMapIndex(maps.getCurrentMapIndex());
                label.setText(teleportTo.toString());

                maps.getCurrentMap().getGlassPanel().setMarker(markerImage);
                maps.getCurrentMap().getGlassPanel().setMarkerPosition(new Coordinate(teleportTo.getX(),teleportTo.getY()));


            }
        }

     }

}
