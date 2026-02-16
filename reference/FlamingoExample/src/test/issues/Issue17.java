package test.issues;

import java.awt.FlowLayout;
import java.awt.KeyboardFocusManager;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import org.jvnet.flamingo.common.CommandToggleButtonGroup;
import org.jvnet.flamingo.common.JCommandToggleButton;
import org.jvnet.flamingo.common.icon.EmptyResizableIcon;

public class Issue17 extends JFrame {
	public Issue17() {
		super("Test command button focus");

		this.setLayout(new FlowLayout());

		JCommandToggleButton button1 = new JCommandToggleButton("text",
				new EmptyResizableIcon(16));
		button1.setName("Button 1");
		JCommandToggleButton button2 = new JCommandToggleButton("text",
				new EmptyResizableIcon(16));
		button2.setName("Button 2");

		this.add(button1);
		this.add(button2);

		CommandToggleButtonGroup group = new CommandToggleButtonGroup();
		group.add(button1);
		group.add(button2);

		this.setSize(400, 200);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public static void main(String[] args) {
		KeyboardFocusManager.getCurrentKeyboardFocusManager()
				.addPropertyChangeListener(new PropertyChangeListener() {
					@Override
					public void propertyChange(PropertyChangeEvent evt) {
						if ("focusOwner".equals(evt.getPropertyName())) {
							// Object to = evt.getNewValue();
							// if (to instanceof JCommandToggleButton) {
							// JCommandToggleButton bto = (JCommandToggleButton)
							// to;
							// if (bto.getName().compareTo("Index 1") == 0) {
							// int i = 20;
							// i++;
							// }
							// }
							System.out.println("From " + evt.getOldValue()
									+ "\nTo   " + evt.getNewValue() + "\n");
						}
					}
				});

		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				new Issue17().setVisible(true);
			}
		});
	}

}
