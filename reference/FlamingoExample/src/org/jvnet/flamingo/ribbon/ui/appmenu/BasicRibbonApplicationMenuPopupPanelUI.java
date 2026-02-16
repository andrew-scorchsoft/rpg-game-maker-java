/*
 * Copyright (c) 2005-2009 Flamingo Kirill Grouchnikov. All Rights Reserved.
 *
 * Redistribution and use in source and binary forms, with or without 
 * modification, are permitted provided that the following conditions are met:
 * 
 *  o Redistributions of source code must retain the above copyright notice, 
 *    this list of conditions and the following disclaimer. 
 *     
 *  o Redistributions in binary form must reproduce the above copyright notice, 
 *    this list of conditions and the following disclaimer in the documentation 
 *    and/or other materials provided with the distribution. 
 *     
 *  o Neither the name of Flamingo Kirill Grouchnikov nor the names of 
 *    its contributors may be used to endorse or promote products derived 
 *    from this software without specific prior written permission. 
 *     
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" 
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, 
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR 
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR 
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, 
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, 
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; 
 * OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, 
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE 
 * OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, 
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE. 
 */
package org.jvnet.flamingo.ribbon.ui.appmenu;

import java.awt.*;
import java.awt.color.ColorSpace;
import java.awt.event.ActionEvent;
import java.awt.image.ColorConvertOp;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.plaf.ComponentUI;

import org.jvnet.flamingo.common.*;
import org.jvnet.flamingo.common.JCommandButton.CommandButtonKind;
import org.jvnet.flamingo.common.JCommandButton.CommandButtonPopupOrientationKind;
import org.jvnet.flamingo.common.icon.FilteredResizableIcon;
import org.jvnet.flamingo.common.popup.JPopupPanel;
import org.jvnet.flamingo.common.ui.BasicPopupPanelUI;
import org.jvnet.flamingo.ribbon.*;
import org.jvnet.flamingo.ribbon.RibbonApplicationMenuEntryPrimary.PrimaryRolloverCallback;
import org.jvnet.flamingo.utils.FlamingoUtilities;

/**
 * Basic UI for ribbon application menu button
 * {@link JRibbonApplicationMenuButton}.
 * 
 * @author Kirill Grouchnikov
 */
public class BasicRibbonApplicationMenuPopupPanelUI extends BasicPopupPanelUI {
	protected JCommandButtonPanel panelLevel1;

	protected JPanel panelLevel2;

	protected JPanel footerPanel;

	protected static final CommandButtonDisplayState MENU_TILE_LEVEL_1 = new CommandButtonDisplayState(
			"Ribbon application menu tile level 1", 32) {
		@Override
		public CommandButtonLayoutManager createLayoutManager(
				AbstractCommandButton commandButton) {
			return new CommandButtonLayoutManagerMenuTileLevel1();
		}

		@Override
		public CommandButtonSeparatorOrientation getSeparatorOrientation() {
			return CommandButtonSeparatorOrientation.VERTICAL;
		}
	};

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.plaf.ComponentUI#createUI(javax.swing.JComponent)
	 */
	public static ComponentUI createUI(JComponent c) {
		return new BasicRibbonApplicationMenuPopupPanelUI();
	}

	/**
	 * The associated application menu button.
	 */
	protected JRibbonApplicationMenuPopupPanel applicationMenuPopupPanel;

	protected JPanel mainPanel;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.swing.plaf.basic.BasicButtonUI#installUI(javax.swing.JComponent)
	 */
	@Override
	public void installUI(JComponent c) {
		this.applicationMenuPopupPanel = (JRibbonApplicationMenuPopupPanel) c;
		this.popupPanel = (JPopupPanel) c;

		this.applicationMenuPopupPanel.setLayout(new BorderLayout());
		
		installDefaults();
		installComponents();
		installListeners();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.plaf.ComponentUI#uninstallUI(javax.swing.JComponent)
	 */
	public void uninstallUI(JComponent c) {
		uninstallListeners();
		uninstallComponents();
		uninstallDefaults();

		this.applicationMenuPopupPanel = null;
	}

	protected void installDefaults() {
		super.installDefaults();
	}

	protected void installComponents() {
		super.installComponents();

		this.mainPanel = createMainPanel();

		this.panelLevel1 = new JCommandButtonPanel(MENU_TILE_LEVEL_1);
		this.panelLevel1.setMaxButtonColumns(1);

		this.panelLevel1.addButtonGroup("main");
		this.panelLevel1.setToShowGroupLabels(false);

		RibbonApplicationMenu ribbonAppMenu = this.applicationMenuPopupPanel
				.getRibbonAppMenu();

		if (ribbonAppMenu != null) {
			for (final RibbonApplicationMenuEntryPrimary menuEntry : ribbonAppMenu
					.getPrimaryEntries()) {
				final JCommandMenuButton commandButton = new JCommandMenuButton(
						menuEntry.getText(), menuEntry.getIcon());
				commandButton.setCommandButtonKind(menuEntry.getEntryKind());
				commandButton.addActionListener(menuEntry
						.getMainActionListener());
				commandButton.setActionKeyTip(menuEntry.getActionKeyTip());
				commandButton.setPopupKeyTip(menuEntry.getPopupKeyTip());
				if (menuEntry.getSecondaryGroupCount() == 0) {
					// if there are no secondary menu items, register the
					// application rollover callback to populate the
					// second level panel
					commandButton
							.addRolloverActionListener(new RolloverActionListener() {
								@Override
								public void actionPerformed(ActionEvent e) {
									//System.out.println("Rollover action");
									PrimaryRolloverCallback callback = menuEntry
											.getRolloverCallback();
									if (callback != null) {
										callback
												.menuEntryActivated(panelLevel2);
									}
									else {
										//System.out.println("Removing all");
										panelLevel2.removeAll();
										panelLevel2.revalidate();
										panelLevel2.repaint();
									}
								}
							});
				} else {
					// register a core callback to populate the second level
					// panel with secondary menu items
					final PrimaryRolloverCallback coreCallback = new PrimaryRolloverCallback() {
						@Override
						public void menuEntryActivated(JPanel targetPanel) {
							targetPanel.removeAll();
							targetPanel.setLayout(new BorderLayout());
							JRibbonApplicationMenuPopupPanelSecondary secondary = new JRibbonApplicationMenuPopupPanelSecondary(
									menuEntry) {
								@Override
								public void removeNotify() {
									super.removeNotify();
									commandButton.getPopupModel()
											.setPopupShowing(false);
								}
							};
							targetPanel.add(secondary, BorderLayout.CENTER);
						}
					};
					commandButton
							.addRolloverActionListener(new RolloverActionListener() {
								@Override
								public void actionPerformed(ActionEvent e) {
									coreCallback
											.menuEntryActivated(panelLevel2);
									// emulate showing the popup so the button
									// remains "selected"
									commandButton.getPopupModel()
											.setPopupShowing(true);
								}
							});
				}
				commandButton.setDisplayState(MENU_TILE_LEVEL_1);
				commandButton.setHorizontalAlignment(SwingUtilities.LEADING);
				commandButton
						.setPopupOrientationKind(CommandButtonPopupOrientationKind.SIDEWARD);
				commandButton.setEnabled(menuEntry.isEnabled());
				this.panelLevel1.addButtonToLastGroup(commandButton);
			}
		}

		mainPanel.add(this.panelLevel1, BorderLayout.WEST);

		this.panelLevel2 = new JPanel();
		this.panelLevel2.setBorder(new Border() {
			@Override
			public Insets getBorderInsets(Component c) {
				return new Insets(0, 1, 0, 0);
			}

			@Override
			public boolean isBorderOpaque() {
				return true;
			}

			@Override
			public void paintBorder(Component c, Graphics g, int x, int y,
					int width, int height) {
				g.setColor(FlamingoUtilities.getColor(Color.gray,
						"Label.disabledForeground"));
				g.drawLine(x, y, x, y + height);
			}
		});
		this.panelLevel2.setPreferredSize(new Dimension(30 * FlamingoUtilities
				.getFont(this.panelLevel1, "Ribbon.font", "Button.font",
						"Panel.font").getSize() - 30, 10));

		mainPanel.add(this.panelLevel2, BorderLayout.CENTER);

		this.applicationMenuPopupPanel.add(mainPanel, BorderLayout.CENTER);

		this.footerPanel = new JPanel(new FlowLayout(FlowLayout.TRAILING)) {
			@Override
			protected void paintComponent(Graphics g) {
				FlamingoUtilities.renderSurface(g, footerPanel, new Rectangle(
						0, 0, footerPanel.getWidth(), footerPanel.getHeight()),
						false, false, false);
			}
		};
		if (ribbonAppMenu != null) {
			for (RibbonApplicationMenuEntryFooter footerEntry : ribbonAppMenu
					.getFooterEntries()) {
				JCommandButton commandFooterButton = new JCommandButton(
						footerEntry.getText(), footerEntry.getIcon());
				commandFooterButton.setDisabledIcon(new FilteredResizableIcon(
						footerEntry.getIcon(), new ColorConvertOp(ColorSpace
								.getInstance(ColorSpace.CS_GRAY), null)));
				commandFooterButton
						.setCommandButtonKind(CommandButtonKind.ACTION_ONLY);
				commandFooterButton.addActionListener(footerEntry
						.getMainActionListener());
				commandFooterButton
						.setDisplayState(CommandButtonDisplayState.MEDIUM);
				commandFooterButton.setFlat(false);
				commandFooterButton.setEnabled(footerEntry.isEnabled());
				this.footerPanel.add(commandFooterButton);
			}
		}

		this.applicationMenuPopupPanel
				.add(this.footerPanel, BorderLayout.SOUTH);

		this.applicationMenuPopupPanel.setBorder(new Border() {
			@Override
			public Insets getBorderInsets(Component c) {
				return new Insets(20, 2, 2, 2);
			}

			@Override
			public boolean isBorderOpaque() {
				return true;
			}

			@Override
			public void paintBorder(Component c, Graphics g, int x, int y,
					int width, int height) {
				g.setColor(FlamingoUtilities.getColor(Color.gray,
						"Label.disabledForeground"));
				g.drawRect(x, y, width - 1, height - 1);
				g.setColor(FlamingoUtilities.getColor(Color.gray,
						"Label.disabledForeground").brighter().brighter());
				g.drawRect(x + 1, y + 1, width - 3, height - 3);
				FlamingoUtilities.renderSurface(g, applicationMenuPopupPanel,
						new Rectangle(x + 2, y + 2, width - 4, 24), false,
						false, false);

				// draw the application menu button
				JRibbonApplicationMenuButton button = applicationMenuPopupPanel
						.getAppMenuButton();
				JRibbonApplicationMenuButton rendererButton = new JRibbonApplicationMenuButton();
				rendererButton.setPopupKeyTip(button.getPopupKeyTip());
				rendererButton.setIcon(button.getIcon());
				rendererButton.getPopupModel().setRollover(false);
				rendererButton.getPopupModel().setPressed(true);
				rendererButton.getPopupModel().setArmed(true);
				rendererButton.getPopupModel().setPopupShowing(true);

				CellRendererPane buttonRendererPane = new CellRendererPane();
				Point buttonLoc = button.getLocationOnScreen();
				Point panelLoc = c.getLocationOnScreen();

				buttonRendererPane.setBounds(panelLoc.x - buttonLoc.x,
						panelLoc.y - buttonLoc.y, button.getWidth(), button
								.getHeight());
				buttonRendererPane.paintComponent(g, rendererButton,
						(Container) c, -panelLoc.x + buttonLoc.x, -panelLoc.y
								+ buttonLoc.y, button.getWidth(), button
								.getHeight(), true);
			}
		});
	}

	protected JPanel createMainPanel() {
		JPanel result = new JPanel(new BorderLayout());
		result.setBorder(new Border() {
			@Override
			public Insets getBorderInsets(Component c) {
				return new Insets(2, 2, 2, 2);
			}

			@Override
			public boolean isBorderOpaque() {
				return true;
			}

			@Override
			public void paintBorder(Component c, Graphics g, int x, int y,
					int width, int height) {
				g.setColor(FlamingoUtilities.getColor(Color.gray,
						"Label.disabledForeground").brighter().brighter());
				g.drawRect(x, y, width - 1, height - 1);
				g.setColor(FlamingoUtilities.getColor(Color.gray,
						"Label.disabledForeground"));
				g.drawRect(x + 1, y + 1, width - 3, height - 3);
			}
		});
		return result;
	}

	protected void installListeners() {
		super.installListeners();
	}

	protected void uninstallDefaults() {
		super.uninstallDefaults();
	}

	protected void uninstallComponents() {
		super.uninstallComponents();
	}

	protected void uninstallListeners() {
		super.uninstallListeners();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jvnet.flamingo.common.ui.BasicCommandButtonUI#paint(java.awt.Graphics
	 * , javax.swing.JComponent)
	 */
	@Override
	public void paint(Graphics g, JComponent c) {
		Graphics2D g2d = (Graphics2D) g.create();

		g2d.dispose();
	}

	public JCommandButtonPanel getPanelLevel1() {
		return panelLevel1;
	}

	public JPanel getPanelLevel2() {
		return panelLevel2;
	}
}
