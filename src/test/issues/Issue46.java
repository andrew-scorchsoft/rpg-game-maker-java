package test.issues;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import org.jvnet.flamingo.common.JCommandButton;
import org.jvnet.flamingo.common.icon.EmptyResizableIcon;
import org.jvnet.flamingo.ribbon.*;

public class Issue46 extends JRibbonFrame {

	RibbonTask task1;

	RibbonTask task2;

	public void configureRibbon() {

		JRibbonBand band11 = new JRibbonBand("Band 1", new EmptyResizableIcon(
				32));
		JRibbonBand band12 = new JRibbonBand("Band 2", new EmptyResizableIcon(
				32));
		band11.addCommandButton(new JCommandButton("Test 11",
				new EmptyResizableIcon(32)), RibbonElementPriority.TOP);
		band11.addCommandButton(new JCommandButton("Test 12",
				new EmptyResizableIcon(32)), RibbonElementPriority.TOP);

		JRibbonBand band21 = new JRibbonBand("Band 3", new EmptyResizableIcon(
				32));
		band21.addCommandButton(new JCommandButton("Test 21",
				new EmptyResizableIcon(32)), RibbonElementPriority.TOP);
		band21.addCommandButton(new JCommandButton("Test 22",
				new EmptyResizableIcon(32)), RibbonElementPriority.TOP);

		JCommandButton cmdTest = new JCommandButton("Click me!",
				new EmptyResizableIcon(32));
		cmdTest.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run() {
						System.out.println("afasfd");
						task1.setTitle("t1 changed");
						task2.setTitle("t2 changed");
					}
				});
			}
		});
		band12.addCommandButton(cmdTest, RibbonElementPriority.TOP);

		task1 = new RibbonTask("Task1", band11, band12);
		task2 = new RibbonTask("Task2", band21);

		getRibbon().addTask(task1);
		getRibbon().addTask(task2);

		getRibbon().setSelectedTask(task1);

	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				Issue46 c = new Issue46();

				c.configureRibbon();

				Rectangle r = GraphicsEnvironment.getLocalGraphicsEnvironment()
						.getMaximumWindowBounds();
				c.setPreferredSize(new Dimension(r.width, r.height / 1));
				c.pack();
				c.setLocation(r.x, r.y);
				c.setVisible(true);
				c.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			}
		});
	}
}
