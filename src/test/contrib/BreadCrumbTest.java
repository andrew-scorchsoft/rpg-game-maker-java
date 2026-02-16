package test.contrib;

import java.awt.BorderLayout;
import java.io.File;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import org.jvnet.flamingo.bcb.core.BreadcrumbFileSelector;

public class BreadCrumbTest extends JFrame {
	protected BreadcrumbFileSelector bar;

	public BreadCrumbTest() {
		super("BreadCrumb test");

		this.bar = new BreadcrumbFileSelector();

		bar.setPath(new File("c:/temp"));

		bar.setPath(new File("c:/temp"));
		this.add(bar, BorderLayout.NORTH);

	}

	/**
	 * Main method for testing.
	 * 
	 * @param args
	 *            Ignored.
	 */
	public static void main(String... args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				BreadCrumbTest test = new BreadCrumbTest();

				test.setSize(550, 200);
				test.setLocationRelativeTo(null);
				test.setVisible(true);
				test.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			}
		});
	}
}