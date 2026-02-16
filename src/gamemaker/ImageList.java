/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gamemaker;


import java.awt.Color;
import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.Vector;
import javax.swing.BoxLayout;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;

/**
 *
 * @author ug77alw
 */
public class ImageList extends JList {

     private JScrollPane listScrollPane;
     private JPanel listPane;
     private Vector vector;
     private ArrayList<JPanel> listItems;
     private static final int LISTWIDTH = 197;
     private static final int LISTHEIGHT = 70;
     private GameMaps maps;


    public ImageList(GameMaps maps){
        this.maps = maps;
        setCellRenderer(new ImageCellRenderer());
         listPane = new JPanel();
         
         listItems = new ArrayList<JPanel>();



           
    }

    public void repaint(){
        if(maps != null && maps.getCurrentMapIndex() >=0 ){
            this.setSelectedIndex(maps.getCurrentMapIndex());
        }
        if(listItems != null){
             for(int i = 0; i < listItems.size(); i++){
               listItems.get(i).repaint();
            }
        }
        if(listPane != null){
            listPane.repaint();
        }
        super.repaint();
    }

    public JScrollPane getListScrollPanel(){

        refreshVector();
        this.setListData(vector);
        listPane = new JPanel();
        listPane.setLayout(new BoxLayout(listPane, BoxLayout.Y_AXIS));
        listPane.add(this);
        listPane.setForeground(Color.black);
        listPane.setBackground(Color.WHITE);

        
        //scroll pane so that the list can scroll
        listScrollPane = new JScrollPane();

        //this list will contain panels that act as the list items
        listScrollPane.add(listPane);
        listScrollPane.setViewportView(listPane);

        listScrollPane.setVisible(true);
        listPane.setVisible(true);

        return listScrollPane;
    }



    /**
     * refreshes the vector that contains all of
     * the list items
     */
    public void refreshVector(){
        vector = new Vector();
        for(int i = 0; i < listItems.size(); i++){
            vector.addElement(listItems.get(i));
        
        }
     
    }
    /**
     * Adds a new item to the list
     */
    public void increaseListCount(){
        JPanel p = new JPanel(); //new items are panels
        listItems.add(p);
    }

    /**
     * this adds a panel to the list as if it were a list item...
     * which it will be
     * @param p the panel to add to the list
     */
    public void addListItem(JPanel p){
        FlowLayout flowLayout = new FlowLayout();
        flowLayout.setAlignment(java.awt.FlowLayout.LEFT);

        p.setLayout(flowLayout); //so it aligns on a horizontal line
        p.setPreferredSize(new Dimension(LISTWIDTH, LISTHEIGHT));
        p.setSize(new Dimension(LISTWIDTH, LISTHEIGHT));
        p.setBackground(Color.red);
        p.setVisible(true);
        listItems.add(p);
      
    }
    /**
     * Adds a component to the list. This method creates a panel and then
     * adds the component into it.
     * @param comp
     */
    public void addListItem(Component comp){
        JPanel p = new JPanel();

        FlowLayout flowLayout = new FlowLayout();
        flowLayout.setAlignment(java.awt.FlowLayout.LEFT);

        p.setLayout(flowLayout);

        p.setPreferredSize(new Dimension(LISTWIDTH, LISTHEIGHT));
        p.setSize(new Dimension(LISTWIDTH, LISTHEIGHT));
        p.add(comp);
        p.setAlignmentX(p.LEFT_ALIGNMENT);
        listItems.add(p);
         p.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
        
        
    }
    /**
     * adds a component to a panel in the list
     * @param index the panel to add the component to
     * @param comp the component to add
     */
    public void addComponentToListIndex(int index, Component comp){
         listItems.get(index).add(comp);
    }
    /**
     * adds a component to the last panel in the list
     * @param comp the component to add
     */
    public void addComponentToLastListItem(Component comp){
        listItems.get(listItems.size()-1).add(comp);
    }


    /**
     * This class is a renderer that is used by the list in order to know
     * how to deal with highlighted panels.
     */
    class ImageCellRenderer implements ListCellRenderer {

    public Component getListCellRendererComponent(JList list, Object value, int index,boolean isSelected,boolean cellHasFocus) {
      Component component = (Component)value;

      //sets the background colours when selected
      Color background;
      if(isSelected == true){
          //background = Color.GRAY;
          //a nice light blue
          background = new Color(145,179,242);
      }else{
          background = Color.white;
      }

      //set the foreground items when selected
      Color foreground;
      if(isSelected == true){
          foreground = Color.white;
      }else{
          foreground = new Color(145,179,242);
      }

      component.setBackground(background);
      
      component.setForeground(foreground);
      return component;
      }
    }


}
