package test.common;

import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.*;
import javax.swing.plaf.metal.MetalLookAndFeel;

import org.jvnet.flamingo.common.*;
import org.jvnet.flamingo.common.JCommandButton.CommandButtonKind;
import org.jvnet.flamingo.common.icon.EmptyResizableIcon;
import org.jvnet.flamingo.common.popup.*;

import com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel;

public class TestWebStartLAF extends JFrame {
	public TestWebStartLAF() {
		super("Click the button");

		JPanel panel = new JPanel(new FlowLayout()) {
			@Override
			protected void paintComponent(Graphics g) {
				if (!(UIManager.getLookAndFeel() instanceof MetalLookAndFeel)) {
					System.out.println("In panel, look and feel is "
							+ UIManager.getLookAndFeel().getName());
				}
				super.paintComponent(g);
			}
		};

		final JCommandButton button = new JCommandButton("Click me!",
				new EmptyResizableIcon(16)) {
			@Override
			protected void paintComponent(Graphics g) {
				if (!(UIManager.getLookAndFeel() instanceof MetalLookAndFeel)) {
					System.out.println("In button, look and feel is "
							+ UIManager.getLookAndFeel().getName());
				}
				super.paintComponent(g);
			}
		};
		button.setDisplayState(CommandButtonDisplayState.MEDIUM);
		button.setCommandButtonKind(CommandButtonKind.POPUP_ONLY);
		button.setPopupCallback(new PopupPanelCallback() {
			@Override
			public JPopupPanel getPopupPanel(JCommandButton commandButton) {
				JCommandPopupMenu menu = new JCommandPopupMenu();
				menu.addMenuButton(new JCommandMenuButton("Test menu item 1",
						new EmptyResizableIcon(16)));
				menu.addMenuButton(new JCommandMenuButton("Test menu item 2",
						new EmptyResizableIcon(16)));
				menu.addMenuButton(new JCommandMenuButton("Test menu item 3",
						new EmptyResizableIcon(16)));
				return menu;
			}
		});
		button.setFlat(false);

		panel.add(button);
		this.add(panel, BorderLayout.CENTER);

		this.setSize(400, 300);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	}

	public static void main(String[] args) throws Exception {
		UIManager.addPropertyChangeListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				if ("lookAndFeel".equals(evt.getPropertyName())) {
					LookAndFeel oldLaf = (LookAndFeel) evt.getOldValue();
					LookAndFeel newLaf = (LookAndFeel) evt.getNewValue();
					System.out.println("Look-and-feel change from "
							+ ((oldLaf == null) ? "null" : oldLaf.getName())
							+ " to "
							+ ((newLaf == null) ? "null" : newLaf.getName()));
				}
			}
		});
		UIManager.setLookAndFeel(new MetalLookAndFeel());
		ToolTipManager.sharedInstance().setLightWeightPopupEnabled(false);
		JPopupMenu.setDefaultLightWeightPopupEnabled(false);
		JFrame.setDefaultLookAndFeelDecorated(true);
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				new TestWebStartLAF().setVisible(true);
			}
		});
	}

}
