package test.ribbon;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import org.jvnet.flamingo.common.JCommandButton;
import org.jvnet.flamingo.ribbon.*;
import org.jvnet.flamingo.ribbon.resize.CoreRibbonResizePolicies;

import test.svg.transcoded.*;

public class RibbonCutoff extends JRibbonFrame {
	public RibbonCutoff() {
		JRibbonBand band = new JRibbonBand("Test", new edit_select_all());
		band.addCommandButton(new JCommandButton("copy", new edit_copy()),
				RibbonElementPriority.MEDIUM);
		band.addCommandButton(new JCommandButton("cut", new edit_cut()),
				RibbonElementPriority.MEDIUM);
		band.addCommandButton(new JCommandButton("paste", new edit_paste()),
				RibbonElementPriority.MEDIUM);
		band.addCommandButton(new JCommandButton("clear", new edit_clear()),
				RibbonElementPriority.MEDIUM);
		band.setResizePolicies(CoreRibbonResizePolicies.getCorePoliciesRestrictive(band));
		
		RibbonTask task = new RibbonTask("test", band);
		this.getRibbon().addTask(task);
		
		this.setSize(600, 300);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				new RibbonCutoff().setVisible(true);
			}
		});
	}

}
