/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gamemaker;

import gamemaker.events.VariableOperation;
import gamemaker.events.VariableOperations;
import java.awt.BorderLayout;
import java.awt.Dialog.ModalityType;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Enumeration;
import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 *
 * @author Andy
 */
public class VariableChangeInput implements ActionListener {

    private JDialog theDialog;
    private GameMaps maps;
    private ArrayList<ButtonGroup> groups;
    private ArrayList<JTextField> textfields;
    private JTextArea messageBox;
    private JCheckBox useMessage;

    //these variables must correspond with the order
    //to which the radio buttons are added to the button group.
    private final int OPTION_NA = 0;
    private final int OPTION_ADD = 1;
    private final int OPTION_SUBTRACT = 2;
    private final int OPTION_SET = 3;


    //ArrayList<VariableOperation> varOperations;
    VariableOperations variableOperations;


    public VariableChangeInput(){

        //varOperations = new ArrayList(10);
        variableOperations = new VariableOperations();
     

        theDialog = new JDialog();
        theDialog.setTitle("Please specify how you want to edit the variables");
        theDialog.setDefaultCloseOperation(theDialog.DISPOSE_ON_CLOSE);
        theDialog.setModalityType(ModalityType.APPLICATION_MODAL);
        theDialog.setSize(500, 310);
        theDialog.setLocation(ScreenFunctions.getScreenCentre(theDialog));

         JPanel container = new JPanel();
         JPanel messageOptions = new JPanel();
         messageOptions.setLayout(new GridLayout(0,2));

         messageOptions.setSize(500, 200);
         container.setLayout(new BorderLayout());


         useMessage = new JCheckBox("Display Message When Triggered?");
         useMessage.setSelected(true);
         messageBox = new JTextArea("Variable Changed!");
         JScrollPane messageScroller = new JScrollPane(messageBox);
         messageScroller.setPreferredSize(new Dimension(300,50));
         messageBox.setLineWrap(true);
         messageBox.setSize(300,200);

         messageOptions.add(messageScroller);
         messageOptions.add(useMessage);

         

        JPanel boxes = new JPanel();
        boxes.setLayout(new GridLayout(0,6));

        groups = new ArrayList<ButtonGroup>();
        textfields = new ArrayList<JTextField>();
        //int numvars = maps.getCurrentMap().getSpritePanel().getSprite().getNumSpriteVars();
        int numvars = 10;
        for(int i = 0; i < numvars; i++){
            groups.add(new ButtonGroup());
            JRadioButton b0 = new JRadioButton("N/A");
            JRadioButton b1 = new JRadioButton("add");
            JRadioButton b2 = new JRadioButton("sub");
            JRadioButton b3 = new JRadioButton("set");
            groups.get(i).add(b0);
            groups.get(i).add(b1);
            groups.get(i).add(b2);
            groups.get(i).add(b3);

            groups.get(i).setSelected(b0.getModel(), true);
            textfields.add(new JTextField("0"));
            int num = i+1;
            boxes.add(new JLabel("Variable "+num+":"));
            boxes.add(textfields.get(i));
            boxes.add(b0);
            boxes.add(b1);
            boxes.add(b2);
            boxes.add(b3);
            
        }

        JButton okButton = new JButton("OK");
        okButton.addActionListener(this);
        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(this);

        boxes.add(new JLabel(""));
        boxes.add(new JLabel(""));
        boxes.add(new JLabel(""));
        boxes.add(new JLabel(""));
        boxes.add(okButton);
        boxes.add(cancelButton);


      
        container.add(messageOptions,BorderLayout.NORTH);
        container.add(boxes,BorderLayout.CENTER);
        theDialog.add(container);
        

    }

    /**
     * @return the array of variable operations
     * if the length of the array is 0 then cancel has been pressed
     * or there were no changes specified
     */
    public VariableOperations getInput(){

       variableOperations = new VariableOperations();
       theDialog.setVisible(true);
       theDialog.repaint();
       return this.variableOperations;


    }



    /**
     * Updates the array containing the variable operations in reference
     * to the radio buttons set and the values of the text areas.
     */
    private boolean updateVarOperations(){

        variableOperations = new VariableOperations();

        if(useMessage.isSelected() == true){
            variableOperations.setMessageEnabled(true);
            variableOperations.setMessage(this.messageBox.getText());
        }else{
            variableOperations.setMessageEnabled(false);
            variableOperations.setMessage("");
        }
        


        for(int i = 0; i < groups.size(); i++){

                Enumeration<AbstractButton> enumeration =groups.get(i).getElements();
                int count = 0;
                while(enumeration.hasMoreElements()){

                    //if the box has been selected then add
                    if(enumeration.nextElement().isSelected()){

                        int value = 0;

                        boolean passed = NumberFunctions.validateInteger(this.textfields.get(i).getText());
                        
                        if(passed){

                            value = Integer.parseInt(this.textfields.get(i).getText());

                        }else if((count == this.OPTION_ADD || count == this.OPTION_SUBTRACT || count == this.OPTION_SET)){
                            //only cancels out if an operation has been set in regards to the
                            //inproperly formatted jtextarea

                            variableOperations.setArrayList(new ArrayList());
                            return false;
                        }

                        //updates the variables in the sprite, only creates an object if an operation
                        //has been specified. Nothing happens for N/A
                        if(count == this.OPTION_ADD){
                               variableOperations.getArrayList().add(new VariableOperation(i,value,VariableOperation.ADD));

                        }else if(count == this.OPTION_SUBTRACT){
                                variableOperations.getArrayList().add(new VariableOperation(i,value,VariableOperation.SUBTRACT));

                        }else if(count == this.OPTION_SET){
                                variableOperations.getArrayList().add(new VariableOperation(i,value,VariableOperation.SET));

                        }

                    }
                    count ++;
                }
                count = 0;

                    //AbstractButton b1 = groups.get(i).getSelection().toString());
                    //System.out.println(b1.isSelected());


            }
        return true;
    }

    public void actionPerformed(ActionEvent e){

        if(e.getActionCommand().equals("OK")){
            //update the variable operations array
            boolean success = updateVarOperations();
            if(success == true){
                //close the dialogue box so that input is returned
                closeDialogue();
            }else{
                String error= "Values entered must be integers.";
                JOptionPane.showMessageDialog(this.theDialog,error);
            }
            

        }else if(e.getActionCommand().equals("Cancel")){
            //ensures that an empty array list is returned
            variableOperations.setArrayList(new ArrayList());
            //close the dialogue box so that input is returned
            closeDialogue();

        }else{

            //this should never be run
            System.out.println("invalid action command");
            
        }

    }
    public void closeDialogue(){
        theDialog.setVisible(false);
        theDialog.dispose();
    }

    public static void main(String[] args) {

        VariableChangeInput vcm = new VariableChangeInput();
        System.out.println(vcm.getInput());
    }

}
