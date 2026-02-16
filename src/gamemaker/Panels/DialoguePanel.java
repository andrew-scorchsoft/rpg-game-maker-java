/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gamemaker.Panels;

import gamemaker.GameMap;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.font.TextLayout;

import java.awt.geom.Rectangle2D;
import javax.swing.JPanel;

/**
 *
 * @author ug77alw
 */
public class DialoguePanel extends JPanel {

    
    public final int TYPE_MESSAGE = 1;

    private boolean displayDialogue;
    //private int dialogueType;
    private String message;
    private Font boxFont = new Font("Arial", Font.PLAIN, 24);
    private GameMap gameMap;
    private final int boxmargin = 10;

    public DialoguePanel(GameMap gameMap){
        this.setOpaque(false);
        this.setLayout(null);
        displayDialogue= false;
        this.gameMap = gameMap;
        //dialogueType = TYPE_MESSAGE; //default dialogue type is a message
        this.setMessage("No Dialogue Set liubl bhl hbl jbhl  \n testing this thing \n one \n two \n three four five six seven eight nine ten \n 11 \n 12");
    }

    public void setDialogueOn(boolean isOn){
        displayDialogue = isOn;
        this.repaint();
       
    }

    public void action(){
        displayDialogue = false;
        this.repaint();
    }

    public boolean isDialogueDisplayed(){
       return displayDialogue;
    }
    public void setMessage(String message){
        message = message.replace("\n ", System.getProperty("line.separator"));
        message = message.replace(" \n", System.getProperty("line.separator"));
        message = message.replace(" \n ", System.getProperty("line.separator"));
        message = message.replace("\n", System.getProperty("line.separator"));
        this.message = message;
    }

    /**
     * calculates how many characters wide and how many lines heigh a string is.
     * @param s the string to analyse
     * @return int[0] in the width in characters, int[1] is the height in lines
     */
    private int[] getStringCharWidth(String s){
        String[] strings = s.split(System.getProperty("line.separator"));
        int[] size = new int[2];
        size[0] = 0; //length in characters
        size[1] = 0; //height in lines
        int i = 0;
        while(i < strings.length){
            int len = strings[i].length();
            if(len > size[0]){ size[0] = len;}
            i++;
        }
        size[1] = i;

        return size;

    }

    public void paintComponent(Graphics g){
        Graphics2D g2 = (Graphics2D) g;

        
        if(displayDialogue == true){

            /**
             * sets the width and location in reference to the scroll pane, NOT the JPanel.
             * This is important so that the player can see the message in the middle of their
             * playable screen.
             */
            //int viewWidth = (int)Math.round(gameMap.getGameMaps().getMapsContainer().getJScrollPane().getViewportBorderBounds().getWidth());
            //int viewHeight = (int)Math.round(gameMap.getGameMaps().getMapsContainer().getJScrollPane().getViewportBorderBounds().getHeight());

            int viewWidth = (int)Math.ceil(gameMap.getGameMaps().getPaintPanel().getVisibleRect().getWidth());
            int viewHeight = (int)Math.ceil(gameMap.getGameMaps().getPaintPanel().getVisibleRect().getHeight());
            //int viewHeight = gameMap.getGameMaps().getPaintPanel().getPaintHeight();
            /**
             * If the scrollpane has zoomed out to show past the area of the map
             * then the box will be drawn in reference to the JPanel rather than
             * the jscroll panel.
             */
            /**
            if(viewHeight > this.getHeight()){
                viewHeight = this.getHeight();
            }
            if(viewWidth > this.getWidth()){
                viewWidth = this.getWidth();
            }**/

            //int viewx= (int)Math.round(gameMap.getGameMaps().getMapsContainer().getJScrollPane().getHorizontalScrollBar().getValue());
            //int viewy= (int)Math.round(gameMap.getGameMaps().getMapsContainer().getJScrollPane().getVerticalScrollBar().getValue());

            int viewx = gameMap.getGameMaps().getPaintPanel().getTopX();
            int viewy = gameMap.getGameMaps().getPaintPanel().getTopY();

          
            //double charWidth = Math.round(boxFont.getSize()*0.48);
            int[] stringSize = getStringCharWidth(message);
            int numlines = message.split(System.getProperty("line.separator")).length;
            message.replace("\n",System.getProperty("line.separator"));

            String temp = message.replace(" ", "");
            int width = 10;
            //prevents erors with textlayout and 0 length strings
            if(temp.length() != 0){
                TextLayout textLayout = new TextLayout(getLongestLine(message),boxFont,g2.getFontRenderContext());
                width = (int)textLayout.getBounds().getWidth()+30;
            }

            int height = (int)Math.ceil(boxFont.getStringBounds(message, g2.getFontRenderContext()).getHeight())*numlines+20;

            
            int x = viewx + (viewWidth/2) - width/2;
            int y =  viewy + (viewHeight/2) - height/2;


            //fills the panel with a transparent rectangle
                //Rectangle2D area = new Rectangle2D.Double(viewx,viewy,this.getWidth(),viewHeight);
                Rectangle2D area = new Rectangle2D.Double(viewx,viewy,viewWidth,viewHeight);
                //Rectangle2D area = new Rectangle2D.Double(0,0,this.getWidth(),this.getHeight());
                g2.setColor(new Color(255,255,255,200));
                g2.fill(area);
            ///////////////////////////////////

            //shadow effect
                g2.setColor(Color.DARK_GRAY);
                g2.fill(new Rectangle2D.Double(x+5,y+5,width,height));
            ///////////////////////////
            g2.setColor(new Color(230,230,230));
            Rectangle2D rect = new Rectangle2D.Double(x,y,width,height);

            g2.fill(rect);
            g2.setColor(Color.black);
            g2.draw(rect);
            drawText(message,g2,x,y);
        
            g2.setColor(Color.black);
            g2.fill(new Rectangle2D.Double(x,y-boxFont.getSize()-boxmargin,130,34));
            g2.setColor(Color.white);
            drawText("Message:",g2,x,y-boxFont.getSize()-boxmargin-10);
        }
        
    }

    private String getLongestLine(String message){
        message.replace("\n", System.getProperty("line.separator"));

        String[] lines = message.split(System.getProperty("line.separator"));
        int longestIndex = -1;
        int largestCount = 0;
        for(int i = 0; i < lines.length; i++ ){
            if(lines[i].length() > largestCount){
                longestIndex = i;
                largestCount = lines[i].length();
            }
        }

        if(longestIndex != -1){
            return lines[longestIndex];
        }else{
            return "";
        }
        
    }

    private void drawText(String text, Graphics2D g2, int x, int y){

        int margin = 3;
        //sets line height based on the font size
        //int lineheight = (int)Math.round(boxFont.getSize() * 1.2 + 1);
        int lineheight = (int)Math.ceil(boxFont.getStringBounds(message, g2.getFontRenderContext()).getHeight());
 
        g2.setFont(boxFont);

        String[] strings = text.split(System.getProperty("line.separator"));
        int i = 0;
        while(i < strings.length){
            g2.drawString(strings[i], boxmargin+x, boxmargin + y + boxFont.getSize() + lineheight*i);
            i++;
        }
        
    }


}
