package test.common;

import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.*;
import javax.swing.UIManager.LookAndFeelInfo;

public class ButtonOutsets extends JFrame {
	private class MeasurePanel extends JPanel {
		Insets outsets;

		public MeasurePanel() {
			this.setLayout(null);

			JButton testButton = new JButton("");
			testButton.setOpaque(false);
			testButton.putClientProperty("JButton.buttonStyle", "square");
			testButton.setFocusable(false);
			this.add(testButton);
			testButton.setBounds(100, 50, 100, 30);

			UIManager.addPropertyChangeListener(new PropertyChangeListener() {
				@Override
				public void propertyChange(PropertyChangeEvent evt) {
					if ("lookAndFeel".equals(evt.getPropertyName())) {
						syncOutsets();
					}
				}
			});
			syncOutsets();
		}

		private void syncOutsets() {
			JPanel panel = new JPanel(null);
			JButton button = new JButton("");
			button.setOpaque(false);
			button.putClientProperty("JButton.buttonStyle", "square");
			button.setFocusable(false);
			panel.add(button);
			button.setBounds(0, 0, 100, 50);

			GraphicsEnvironment e = GraphicsEnvironment
					.getLocalGraphicsEnvironment();
			GraphicsDevice d = e.getDefaultScreenDevice();
			GraphicsConfiguration c = d.getDefaultConfiguration();
			BufferedImage compatibleImage = c.createCompatibleImage(100, 50,
					Transparency.TRANSLUCENT);
			button.paint(compatibleImage.getGraphics());

			// analyze top
			int top = -1;
			for (int i = 0; i < 25; i++) {
				int rgba = compatibleImage.getRGB(50, i);
				int alpha = (rgba >>> 24) & 0xFF;
				if (alpha == 255) {
					top = i;
					break;
				}
			}
			// analyze bottom
			int bottom = -1;
			for (int i = 49; i > 25; i--) {
				int rgba = compatibleImage.getRGB(50, i);
				int alpha = (rgba >>> 24) & 0xFF;
				if (alpha == 255) {
					bottom = 49 - i;
					break;
				}
			}
			// analyze left
			int left = -1;
			for (int i = 0; i < 50; i++) {
				int rgba = compatibleImage.getRGB(i, 25);
				int alpha = (rgba >>> 24) & 0xFF;
				if (alpha == 255) {
					left = i;
					break;
				}
			}
			// analyze right
			int right = -1;
			for (int i = 99; i > 50; i--) {
				int rgba = compatibleImage.getRGB(i, 25);
				int alpha = (rgba >>> 24) & 0xFF;
				if (alpha == 255) {
					right = 99 - i;
					break;
				}
			}

			this.outsets = new Insets(top, left, bottom, right);
		}

		@Override
		protected void paintComponent(Graphics g) {
			g.setColor(new Color(255, 240, 190));
			g.fillRect(0, 0, getWidth(), getHeight());

			g.setColor(Color.gray);
			for (int i = 95; i <= 205; i += 5) {
				g.drawLine(i, 40, i, 90);
			}
			for (int i = 45; i <= 85; i += 5) {
				g.drawLine(90, i, 210, i);
			}

			g.setColor(Color.blue);
			// top
			g.drawLine(90, 50, 210, 50);
			// bottom
			g.drawLine(90, 80, 210, 80);
			// left
			g.drawLine(100, 40, 100, 90);
			// right
			g.drawLine(200, 40, 200, 90);

			g.setColor(Color.black);
			JButton c = (JButton) getComponent(0);
			Insets ins = c.getBorder().getBorderInsets(c);
			g.drawString("Content insets : " + ins.toString(), 50, 120);
			g.drawString("Outsets : " + this.outsets.toString(), 50, 150);
		}
	}

	public ButtonOutsets() {

		JPanel controlPanel = new JPanel(new FlowLayout());

		LookAndFeelInfo[] lafs = UIManager.getInstalledLookAndFeels();
		final JComboBox jcb = new JComboBox(lafs);
		for (LookAndFeelInfo lafi : lafs) {
			if (UIManager.getLookAndFeel().getName().equals(lafi.getName())) {
				jcb.setSelectedItem(lafi);
				break;
			}
		}
		jcb.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						JFrame frame = ButtonOutsets.this;
						try {
							LookAndFeelInfo selected = (LookAndFeelInfo) jcb
									.getSelectedItem();
							UIManager.setLookAndFeel(selected.getClassName());
							SwingUtilities.updateComponentTreeUI(frame);
						} catch (Exception exc) {
							exc.printStackTrace();
						}
					}
				});
			}
		});
		jcb.setRenderer(new DefaultListCellRenderer() {
			@Override
			public Component getListCellRendererComponent(JList list,
					Object value, int index, boolean isSelected,
					boolean cellHasFocus) {
				return super.getListCellRendererComponent(list,
						((LookAndFeelInfo) value).getName(), index, isSelected,
						cellHasFocus);
			}
		});

		controlPanel.add(jcb);

		this.add(controlPanel, BorderLayout.SOUTH);

		this.add(new MeasurePanel(), BorderLayout.CENTER);

		this.setSize(500, 300);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				new ButtonOutsets().setVisible(true);
			}
		});
	}

}
