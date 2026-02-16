/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gamemaker.RightPanels;

import gamemaker.Ribbon;
import gamemaker.events.ListActionListener;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionEvent;

/**
 *
 * @author ug77alw
 */
public class RightPanel extends JScrollPane implements ListActionListener  {
    //the panel that contains the maps list
    private MapList_RightPanel mapsPanel;
    //the ranel that is shown when tehe right panel is hidden
    private Hidden_RightPanel hiddenPanel;
    private Ribbon mainRibbon;
    
    private boolean hidden = false;

    //default dimensions
    private final int CONTENT_SCROLL_WIDTH = 203;
    private final int CONTENT_PANEL_WIDTH = 200;

    public RightPanel(Ribbon r){
        super();
        mainRibbon = r;

        

        this.mapsPanel = new MapList_RightPanel(mainRibbon.getGameMaps(),this);
        this.add(mapsPanel);
        this.setPreferredSize(new Dimension(CONTENT_SCROLL_WIDTH,200));
        
        mapsPanel.setPreferredSize(new Dimension(CONTENT_PANEL_WIDTH,200));
        this.setViewportView(mapsPanel);

        hiddenPanel = new Hidden_RightPanel(this);




        this.setVisible(true);

  
        this.repaint();
     

    }


    public void updatePanelData(){

        if(hiddenPanel != null){
            hiddenPanel.repaint();
             hiddenPanel.updateUI();
        }
        if(mapsPanel !=null){
            mapsPanel.setUpPanel();
            mapsPanel.repaint();
            mapsPanel.updateUI();

            this.repaint();
            mainRibbon.repaint();
           
        
        }
         this.updateUI();
    }

    public boolean isMinimised(){
        return hidden;
    }
     public void actionPerformed(ActionEvent e){

         if(e.getActionCommand().equals("expand_panel")){

            
            
            this.remove(hiddenPanel);
            this.add(mapsPanel);

            Dimension d = new Dimension((int)mapsPanel.getPreferredSize().getWidth() +3,
                                        (int)mapsPanel.getPreferredSize().getHeight());

            this.setPreferredSize(d);
            this.setViewportView(mapsPanel);
            mapsPanel.repaint();

            mainRibbon.updatePreferredSizes();
             hidden=false;


         }else if(e.getActionCommand().equals("hide_panel")){
             
           
             this.remove(mapsPanel);
             this.add(hiddenPanel);
             this.setViewportView(hiddenPanel);

             

             this.setPreferredSize(hiddenPanel.getRecommendedContainerSize());
             //this.setSize(hiddenPanel.getPreferredSize());
             //this.setSize(hiddenPanel.getPreferredSize());

             mainRibbon.repaint();
             hiddenPanel.updateUI();
             hiddenPanel.repaint();
             
             
             this.updateUI();

             mainRibbon.updatePreferredSizes();
             hidden=true;
             
             

            

         }


     }
     public void valueChanged(ListSelectionEvent e){
         if (e.getValueIsAdjusting() == false) {
            //if nothing is selected then select the dafault map
            if (mapsPanel != null && mapsPanel.getList().getSelectedIndex() >= 0) {

                int shape = mainRibbon.getGameMaps().getCurrentMap().getEventCreatorShape();
                mainRibbon.getGameMaps().getCurrentMap().setIdle();
                mainRibbon.getGameMaps().setCurrentMap(mapsPanel.getList().getSelectedIndex());
                mainRibbon.repaint();
                mainRibbon.getGameMaps().getCurrentMap().setEventCreatorShape(shape);
                mainRibbon.deselectEventButtons();
            
            }
        }

     }

}
