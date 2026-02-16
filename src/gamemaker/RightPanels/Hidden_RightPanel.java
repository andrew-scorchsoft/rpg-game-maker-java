/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gamemaker.RightPanels;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JPanel;

/**
 *
 * @author ug77alw
 */
public class Hidden_RightPanel extends JPanel{

    public Hidden_RightPanel(ActionListener l){
        super();
        this.setPreferredSize(new Dimension(25,25));

        JButton expandButton = new JButton("<");
         expandButton.setSize(15, 15);
         //expandButton.setFont(new Font("Serif", Font.PLAIN, 12));
         expandButton.setMargin(new Insets(0,0,0,0));
        expandButton.setActionCommand("expand_panel");
        this.add(expandButton);
        expandButton.addActionListener(l);
    }

    public Dimension getRecommendedContainerSize(){
        return new Dimension(  (int)this.getPreferredSize().getWidth()+3,
                                (int)this.getPreferredSize().getHeight());
    }


   


}
