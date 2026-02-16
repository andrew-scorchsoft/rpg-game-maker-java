/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gamemaker;

import gamemaker.events.EventCondition;
import gamemaker.Exceptions.IllegalReferenceValueException;
import java.awt.BorderLayout;
import java.awt.Dialog.ModalityType;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Enumeration;
import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

/**
 *
 * @author Andy
 */
public class VariableConditionInput implements ActionListener{

    private JDialog theDialog;
    private GameMaps maps;
    private JComboBox var1;
    private JComboBox var2;
    private ButtonGroup conditionGroup;
    private ButtonGroup relianceGroup;
    private JTextField valueField;
    private EventCondition eventCondition;

    public VariableConditionInput(){

        theDialog = new JDialog();
        theDialog.setTitle("Condition");
        theDialog.setDefaultCloseOperation(theDialog.DISPOSE_ON_CLOSE);
        theDialog.setModalityType(ModalityType.APPLICATION_MODAL);

        int x = 220;
        int y = 200;
        theDialog.setSize(x, y);
        theDialog.setMinimumSize(new Dimension(x, y));
        theDialog.setMaximumSize(new Dimension(x, y));
        

        theDialog.setLocation(ScreenFunctions.getScreenCentre(theDialog));


        JPanel topPanel = new JPanel();
        JPanel middlePanel = new JPanel();
        JPanel signPanel = new JPanel();
        JPanel reliancePanel = new JPanel();
        JPanel bottomPanel = new JPanel();

        BorderLayout borderLayout = new BorderLayout();
        FlowLayout flowCenter = new FlowLayout();
        FlowLayout flowRight= new FlowLayout();
        GridLayout gridLayout = new GridLayout(0,3);
        flowCenter.setAlignment(flowCenter.CENTER);
        flowRight.setAlignment(flowCenter.RIGHT);

        theDialog.setLayout(borderLayout);
        
        
      
        topPanel.setLayout(flowCenter);
        reliancePanel.setLayout(flowCenter);

        var1 = new JComboBox();
        var2 = new JComboBox();
        
        for(int i = 1; i <= 10; i++){
            var1.addItem("Variable " + i);
            var2.addItem("Variable " + i);
        }
        var1.setSelectedIndex(0);
        var2.setSelectedIndex(0);

        topPanel.add(var1);
        topPanel.add(new JLabel(" Must Be:"));

        signPanel.setLayout(gridLayout);

        conditionGroup = new ButtonGroup();

        JRadioButton b1 = new JRadioButton(">     ");
        JRadioButton b2 = new JRadioButton("<     ");
        JRadioButton b3 = new JRadioButton("==     ");
        JRadioButton b4 = new JRadioButton(">=     ");
        JRadioButton b5 = new JRadioButton("<=     ");
        JRadioButton b6 = new JRadioButton("!=     ");
        b3.setSelected(true);

        conditionGroup.add(b1);
        conditionGroup.add(b2);
        conditionGroup.add(b3);
        conditionGroup.add(b4);
        conditionGroup.add(b5);
        conditionGroup.add(b6);

        signPanel.setAlignmentX(middlePanel.CENTER_ALIGNMENT);
        signPanel.add(b1);
        signPanel.add(b2);
        signPanel.add(b3);
        signPanel.add(b4);
        signPanel.add(b5);
        signPanel.add(b6);

        relianceGroup = new ButtonGroup();
        JRadioButton r1 = new JRadioButton("");
        JRadioButton r2 = new JRadioButton("");
        r2.setSelected(true);

        relianceGroup.add(r1);
        relianceGroup.add(r2);

        reliancePanel.add(r1);
        reliancePanel.add(var2);
        reliancePanel.add(r2);
        valueField = new JTextField("Value");
        valueField.setColumns(5);
        reliancePanel.add(valueField);

        JButton okButton = new JButton("OK");
        JButton cancelButton = new JButton("Cancel");
        okButton.addActionListener(this);
        cancelButton.addActionListener(this);
        bottomPanel.add(okButton);
        bottomPanel.add(cancelButton);


       

        middlePanel.add(signPanel, borderLayout.CENTER);
        middlePanel.add(reliancePanel, borderLayout.SOUTH);

        theDialog.add(topPanel, borderLayout.NORTH);
        theDialog.add(middlePanel, borderLayout.CENTER);
        theDialog.add(bottomPanel, borderLayout.SOUTH);

    }




    public void closeDialogue(){
        theDialog.setVisible(false);
        theDialog.dispose();
    }

    public boolean validateInputs(){
        //checks if val or var is selected to be compared
        int selectedIndex =getSelectedButtonIndex(relianceGroup);
        if(selectedIndex == 1){ //if value is selected

            return NumberFunctions.validateInteger(this.valueField.getText());
        
        }

        return true;
    }

     public void actionPerformed(ActionEvent e){

        if(e.getActionCommand().equals("OK")){

            if(validateInputs() == true){
                //close the dialogue box so that input is returned
                try{
                    this.eventCondition = new EventCondition(getVar1Selection(),this.getVar2orVal(),getSelectedOperationCode(),getVar2orValBoolean());
                }catch(IllegalReferenceValueException irve){
                    JOptionPane.showMessageDialog(this.theDialog,"Error: Incorrect variable reference");
                    irve.printStackTrace();
                    this.eventCondition = new EventCondition();
                }
                closeDialogue();
            }else{

                String error= "Please enter a value or select a variable.\n" +
                        "The value must be an integer.";
                JOptionPane.showMessageDialog(this.theDialog,error);


            }


            

        }else if(e.getActionCommand().equals("Cancel")){

            this.eventCondition = new EventCondition();
            //close the dialogue box so that input is returned
            closeDialogue();

        }else{

            //this should never be run
            System.out.println("invalid action command");

        }

    }

     private int getVar1Selection(){
         return var1.getSelectedIndex();
     }

     /**
      *
      * @return true if variable, false if value
      */
     private boolean getVar2orValBoolean(){
         int selectedIndex =getSelectedButtonIndex(relianceGroup);
         if(selectedIndex == 0){
             //variable is selected
             return EventCondition.USE_VARIABLE;
         }else
         if(selectedIndex == 1){
             //value is selected
             return EventCondition.USE_VALUE;
         }else{

             return false;
         }
     }

     private int getVar2orVal(){

        boolean varOrVal = getVar2orValBoolean();

         if(varOrVal == EventCondition.USE_VARIABLE){
             //variable is selected

             return var2.getSelectedIndex();
      
         }else
         if(varOrVal == EventCondition.USE_VALUE){
             //value is selected
          
             try{
                return Integer.parseInt(this.valueField.getText());
             }catch(NumberFormatException nfe){
                 return 0;
             }
         }else{
             System.err.println("Variable Condition Error");
             return 0;
         }
     }

     private int getSelectedButtonIndex(ButtonGroup group){
         Enumeration<AbstractButton> enumeration = group.getElements();

         int index = 0;
       
         //loops through all ratio buttons in the group to find the one that is
         //selected. It then returns its position in the group
         while(enumeration.hasMoreElements()){
             if(enumeration.nextElement().isSelected()){
                 return index;
             }
             index++;
         }

         return -1;
     }

     public int getSelectedOperationCode(){

         int selected = getSelectedButtonIndex(this.conditionGroup);
  
         if(selected == 0){
            return EventCondition.GREATER_THAN;
         }else if(selected == 1){
            return EventCondition.LESS_THAN;
         }else if(selected == 2){
            return EventCondition.EQUAL_TO;
         }else if(selected == 3){
            return EventCondition.GREATER_THAN_OR_EQUAL_TO;
         }else if(selected == 4){
            return EventCondition.LESS_THAN_OR_EQUAL_TO;
         }else if(selected == 5){
            return EventCondition.NOT_EQUAL_TO;
         }else{
   
            return EventCondition.EQUAL_TO;
         }

     }

     /**
      *
      * @return
      * @throws IllegalReferenceValueException
      */
     public EventCondition getInput() throws IllegalReferenceValueException{


       theDialog.setVisible(true);
       theDialog.repaint();


       return eventCondition;
  

    }

    public static void main(String[] args) {

        VariableConditionInput vcm = new VariableConditionInput();
        try{
            vcm.getInput();
        }catch(IllegalReferenceValueException irve){
            System.err.println(irve);
        }
    }

}
