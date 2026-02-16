/**
 * 
 */
package test.common;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import org.jvnet.flamingo.common.JCommandButtonPanel;
import org.jvnet.flamingo.common.JCommandToggleButton;
import org.jvnet.flamingo.common.icon.DecoratedResizableIcon;
import org.jvnet.flamingo.common.icon.ResizableIcon;

import test.svg.transcoded.font_x_generic;

public class QuickStylesPanel extends JCommandButtonPanel {
	public QuickStylesPanel() {
		super(32);

		for (int groupIndex = 0; groupIndex < 4; groupIndex++) {
			String iconGroupName = "Styles " + groupIndex;
			this.addButtonGroup(iconGroupName, groupIndex);
			for (int i = 0; i < 15; i++) {
				final int index = i;
				ResizableIcon fontIcon = new font_x_generic();
				ResizableIcon finalIcon = new DecoratedResizableIcon(fontIcon,
						new DecoratedResizableIcon.IconDecorator() {
							@Override
							public void paintIconDecoration(Component c,
									Graphics g, int x, int y, int width,
									int height) {
								Graphics2D g2d = (Graphics2D) g.create();
								g2d.setColor(Color.black);
								g2d.drawString("" + index, x + 2, y + height
										- 2);
								g2d.dispose();
							}
						});
				JCommandToggleButton jrb = new JCommandToggleButton(null,
						finalIcon);
				jrb.setName("Group " + groupIndex + ", index " + i);
				jrb.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						System.out.println("Invoked action on " + index);
					}
				});
				this.addButtonToLastGroup(jrb);
			}
		}
		this.setSingleSelectionMode(true);
		this.setToShowGroupLabels(false);
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				JFrame frame = new JFrame("Command button panel");

				final QuickStylesPanel buttonPanel = new QuickStylesPanel();
				frame.add(new JScrollPane(buttonPanel), BorderLayout.CENTER);
				JPanel controlPanel = new JPanel(new FlowLayout(
						FlowLayout.RIGHT));

				final JCheckBox toShowGroupLabels = new JCheckBox(
						"show group labels");
				toShowGroupLabels
						.setSelected(buttonPanel.isToShowGroupLabels());
				toShowGroupLabels.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						buttonPanel.setToShowGroupLabels(toShowGroupLabels
								.isSelected());
						buttonPanel.revalidate();
					}
				});
				controlPanel.add(toShowGroupLabels);

				final JCheckBox isRowFillLayout = new JCheckBox(
						"use row fill layout");
				isRowFillLayout
						.setSelected(buttonPanel.getLayoutKind() == LayoutKind.ROW_FILL);
				isRowFillLayout.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						buttonPanel
								.setLayoutKind(isRowFillLayout.isSelected() ? LayoutKind.ROW_FILL
										: LayoutKind.COLUMN_FILL);
						// buttonPanel.revalidate();
					}
				});
				controlPanel.add(isRowFillLayout);

				frame.add(controlPanel, BorderLayout.SOUTH);
				frame.setSize(500, 300);
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.setLocationRelativeTo(null);
				frame.setVisible(true);
			}
		});
	}
}