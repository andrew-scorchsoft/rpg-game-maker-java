/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gamemaker;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dialog.ModalityType;
import java.awt.event.ActionListener;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 *
 * @author ug77alw
 */
public class MessageInput{


    private JDialog theDialog;

    private JTextArea theText;
    private ActionListener actionListener;

    public MessageInput(ActionListener actionListener){
        this.actionListener = actionListener;
        theText = new JTextArea();
        theText.setLineWrap(true);
        theText.setSize(300,200);
       
        
    }

    public void closeDialogue(){
        theDialog.setVisible(false);
        theDialog.dispose();
    }

    private void constructFrame(){

         theDialog = new JDialog();
         theDialog.setModalityType(ModalityType.APPLICATION_MODAL);
         theDialog.setSize(300, 150);
         theDialog.setTitle("Please Enter A Message");
         //theFrame.setVisible(false);
         
         theDialog.setDefaultCloseOperation(theDialog.DISPOSE_ON_CLOSE);
         theDialog.setLocationRelativeTo(null);
         //theFrame.setLayout(new GridLayout(1,2));


         Container contentPane = theDialog.getContentPane();
         BoxLayout boxLayout = new BoxLayout(contentPane, BoxLayout.Y_AXIS);
         contentPane.setLayout(boxLayout);
         


         //JPanel top = new JPanel();
         //top.setLayout(new BoxLayout(top, BoxLayout.Y_AXIS));

         JPanel bottom = new JPanel();
         bottom.setLayout(new BoxLayout(bottom, BoxLayout.LINE_AXIS));

         JScrollPane scroller = new JScrollPane(theText);
        

         contentPane.add(scroller,  BorderLayout.CENTER);
       
         contentPane.add(bottom, BorderLayout.PAGE_END);
         
         //contentPane.add(b);
         
         //scroller.setSize(300, 300);
        // scroller.add();
         //top.setLayout(new BoxLayout(bottom, BoxLayout.Y_AXIS));

         //contentPane.add(top);
         //


         JButton ok = new JButton("OK");
         ok.setActionCommand("setmessage_ok");

         JButton cancel = new JButton("Cancel");
         cancel.setActionCommand("setmessage_cancel");

         ok.setAlignmentX(Component.CENTER_ALIGNMENT);
         ok.addActionListener(actionListener);

         cancel.setAlignmentX(Component.CENTER_ALIGNMENT);
         cancel.addActionListener(actionListener);


         bottom.add(ok);
         bottom.add(cancel);
         
         

         
         
         
         
    
         theDialog.setVisible(true);
         scroller.setVisible(true);
         scroller.repaint();
         theDialog.repaint();
         

    }

    public String getInput(){

        theText.setText("");
        constructFrame();
        String text = theText.getText()+" ";
        text = text.replace("<", "");
        text = text.replace(">", "");
        return text;
        

    }
 


    public static void main(String[] args) {
      JFrame f = new JFrame();
      f.setSize(200,200);
      f.setVisible(true);
      //MessageInput mess = new MessageInput();

      //mess.getInput();
       
    }
}
