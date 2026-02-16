package test.ribbon;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import org.jvnet.flamingo.ribbon.JRibbonFrame;

public class EmptyRibbon extends JRibbonFrame {
	public EmptyRibbon() {
		this.setSize(600, 300);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				new EmptyRibbon().setVisible(true);
			}
		});
	}
}
