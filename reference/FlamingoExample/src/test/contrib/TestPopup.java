package test.contrib;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.jvnet.flamingo.common.CommandButtonDisplayState;
import org.jvnet.flamingo.common.JCommandButtonPanel;

public class TestPopup extends JFrame {

	private static final long serialVersionUID = 0;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				TestPopup test = new TestPopup();
				test.setVisible(true);
			}
		});
	}

	public TestPopup() {
		super("Test Popup");

		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		JPanel test_panel = new JPanel(new BorderLayout());
		setContentPane(test_panel);

		JCommandButtonPanel panel = new JCommandButtonPanel(
				CommandButtonDisplayState.MEDIUM);
		test_panel.add(panel, BorderLayout.CENTER);
		panel.addButtonGroup("Blah");
		panel.setToShowGroupLabels(false);
		pack();
	}
}
