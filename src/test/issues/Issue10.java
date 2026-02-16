package test.issues;

import java.awt.*;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import org.jvnet.flamingo.common.icon.EmptyResizableIcon;
import org.jvnet.flamingo.ribbon.*;

public class Issue10 extends JRibbonFrame {
	public Issue10() {
		super("Ribbon test");
		this.setIconImage(new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB));
		this.getRibbon().addTask(
				new RibbonTask("Empty", new JRibbonBand("Empty",
						new EmptyResizableIcon(32))));
	}

	public static void main(String[] args) {
		JFrame.setDefaultLookAndFeelDecorated(true);
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				Issue10 c = new Issue10();
				Rectangle r = GraphicsEnvironment.getLocalGraphicsEnvironment()
						.getMaximumWindowBounds();
				c.setPreferredSize(new Dimension(r.width, r.height / 2));
				c.pack();
				c.setLocation(r.x, r.y);
				c.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				c.setVisible(true);
			}
		});
	}
}
