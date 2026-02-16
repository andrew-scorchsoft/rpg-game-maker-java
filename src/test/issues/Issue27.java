package test.issues;

import java.awt.HeadlessException;

import javax.swing.*;

import org.jvnet.flamingo.common.JCommandButton;
import org.jvnet.flamingo.common.icon.EmptyResizableIcon;
import org.jvnet.flamingo.ribbon.*;

public class Issue27 {
	private static class RibbonTest extends JRibbonFrame {

		public RibbonTest(String title) throws HeadlessException {
			super(title);

			JRibbonBand band = new JRibbonBand("TestBand",
					new EmptyResizableIcon(16));
			band.addCommandButton(new JCommandButton("Test Command",
					new EmptyResizableIcon(16)), RibbonElementPriority.TOP);
			band.setTitle("Band Title");
			RibbonTask rt = new RibbonTask("TestTask", band);

			getRibbon().addTask(rt);
		}

	}

	public static void main(String[] args) {
		JFrame.setDefaultLookAndFeelDecorated(true);
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				JFrame frame = new RibbonTest("Test");
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.setSize(500, 500);
				frame.setLocation(0, 0);
				frame.setVisible(true);
			}
		});
	}
}
