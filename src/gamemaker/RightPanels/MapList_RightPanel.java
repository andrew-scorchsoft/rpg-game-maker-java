/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gamemaker.RightPanels;

import gamemaker.GameMaps;
import gamemaker.ImageList;
import gamemaker.events.ListActionListener;
import java.awt.BorderLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author ug77alw
 */
public class MapList_RightPanel extends JPanel{
    private GameMaps maps;
    private ImageList theList;
    private ListActionListener actList;

    public  MapList_RightPanel(GameMaps maps, ListActionListener actList){

        this.maps = maps;
       
        this.actList = actList;
       
        setUpPanel();
        
    }

    public ImageList getList(){
        return theList;
    }
    public void setUpPanel(){
       generateList();
        this.setLayout(new BorderLayout());
        JButton hideButton = new JButton(">>");
        hideButton.setActionCommand("hide_panel");
        hideButton.addActionListener(actList);
        this.add(hideButton,BorderLayout.NORTH);

        
        this.add(theList.getListScrollPanel(),BorderLayout.CENTER);
         
        theList.addListSelectionListener(actList);
        //theList.repaint();
      
        theList.repaint();
        this.setVisible(true);
        this.repaint();

    
    }

    /**
     * generates the list to be displayed using the maps object
     */
    public void generateList(){
       
       this.removeAll();
  
       theList = new ImageList(maps);

        for(int i = 0; i <maps.size() ; i++){

           
            

            if(maps.getMap(i).getBackgroundImageThumb() != null){

                 JLabel imageLabel = new JLabel(new ImageIcon(maps.getMap(i).getBackgroundImageThumb()));
                 theList.addListItem(imageLabel);
                 theList.addComponentToLastListItem(new JLabel("Map ID: " + i));

            }else{
                theList.addListItem(new JLabel("Map ID: " + i));
            }
      
        }
       if(maps.getCurrentMapIndex() >=0){
            theList.setSelectedIndex(maps.getCurrentMapIndex());
          
       }

   

    }
}
