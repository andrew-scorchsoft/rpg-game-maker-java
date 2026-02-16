/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package flamingoexample;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import org.jvnet.flamingo.common.JCommandButton;
import org.jvnet.flamingo.common.JCommandButton.CommandButtonKind;
import org.jvnet.flamingo.ribbon.JRibbonBand;
import org.jvnet.flamingo.ribbon.JRibbonFrame;
import org.jvnet.flamingo.ribbon.RibbonApplicationMenu;
import org.jvnet.flamingo.ribbon.RibbonApplicationMenuEntryPrimary;
import org.jvnet.flamingo.ribbon.RibbonElementPriority;
import org.jvnet.flamingo.ribbon.RibbonTask;
import org.jvnet.flamingo.ribbon.resize.CoreRibbonResizePolicies;
import test.svg.transcoded.document_new;
import test.svg.transcoded.edit_clear;
import test.svg.transcoded.edit_cut;
import test.svg.transcoded.edit_paste;
import test.svg.transcoded.edit_select_all;
/**
 *
 * @author Andy
 */
public class Main extends JRibbonFrame {

    /**
     * @param args the command line arguments
     */
     public Main() {

                
                toolTipBand();
                theBand();
                appMenu();

                this.setSize(600, 300);
                this.setLocationRelativeTo(null);
                this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

                
	}

        private void appMenu(){
            RibbonApplicationMenuEntryPrimary amEntryNew = new RibbonApplicationMenuEntryPrimary(
				new document_new(), "New", new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						System.out.println("Invoked creating new document");
					}
				}, CommandButtonKind.ACTION_ONLY);

           RibbonApplicationMenu applicationMenu = new RibbonApplicationMenu();
           applicationMenu.addMenuEntry(amEntryNew);
           this.getRibbon().setApplicationMenu(applicationMenu);
        }
        private void toolTipBand(){
            
                JCommandButton taskbarButtonPaste = new JCommandButton("",new edit_paste());
                JCommandButton taskbarButtonClear = new JCommandButton("",new edit_clear());
                this.getRibbon().addTaskbarComponent(taskbarButtonPaste);
                this.getRibbon().addTaskbarComponent(taskbarButtonClear);
            
        }
        private void theBand(){
        
                JRibbonBand homeBand = new JRibbonBand("Home", new edit_select_all());
                JRibbonBand viewBand = new JRibbonBand("View", new edit_select_all());

                homeBand.addCommandButton(new JCommandButton("clear", new edit_clear()),RibbonElementPriority.MEDIUM);
                homeBand.addCommandButton(new JCommandButton("cut", new edit_cut()),RibbonElementPriority.MEDIUM);

                viewBand.addCommandButton(new JCommandButton("clear", new edit_clear()),RibbonElementPriority.MEDIUM);
                viewBand.addCommandButton(new JCommandButton("cut", new edit_cut()),RibbonElementPriority.MEDIUM);

                //sets how icons are resized to fit within the band
                viewBand.setResizePolicies(CoreRibbonResizePolicies.getCorePoliciesRestrictive(viewBand));

                RibbonTask homeTask = new RibbonTask("Home", homeBand);
                RibbonTask viewTask = new RibbonTask("View", viewBand);

                this.getRibbon().addTask(homeTask);
                this.getRibbon().addTask(viewTask);
     
        }

	public static void main(String[] args) {

                //runs a new ribbon. Does so as a thread
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				new Main().setVisible(true);
			}
		});
	}

}
