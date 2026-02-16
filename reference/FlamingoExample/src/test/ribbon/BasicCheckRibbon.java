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
package test.ribbon;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.*;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.border.EmptyBorder;

import org.jvnet.flamingo.common.*;
import org.jvnet.flamingo.common.JCommandButton.CommandButtonKind;
import org.jvnet.flamingo.common.icon.*;
import org.jvnet.flamingo.common.popup.*;
import org.jvnet.flamingo.ribbon.*;
import org.jvnet.flamingo.ribbon.resize.CoreRibbonResizePolicies;
import org.jvnet.flamingo.utils.RenderingUtils;

import test.svg.transcoded.*;

public class BasicCheckRibbon extends JRibbonFrame {
	protected static class QuickStylesPanel extends JCommandButtonPanel {
		public QuickStylesPanel() {
			super(32);

			for (int groupIndex = 0; groupIndex < 4; groupIndex++) {
				String iconGroupName = "Styles " + groupIndex;
				this.addButtonGroup(iconGroupName, groupIndex);
				for (int i = 0; i < 15; i++) {
					final int index = i;
					ResizableIcon fontIcon = new font_x_generic();
					ResizableIcon finalIcon = new DecoratedResizableIcon(
							fontIcon,
							new DecoratedResizableIcon.IconDecorator() {
								@Override
								public void paintIconDecoration(Component c,
										Graphics g, int x, int y, int width,
										int height) {
									Graphics2D g2d = (Graphics2D) g.create();
									g2d.setColor(Color.black);
									g2d
											.setFont(UIManager
													.getFont("Label.font"));
									RenderingUtils.installDesktopHints(g2d);
									g2d.drawString("" + index, x + 2, y
											+ height - 2);
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
					jrb.setActionRichTooltip(new RichTooltip("Index " + i,
							"Sample tooltip for " + i));
					this.addButtonToLastGroup(jrb);
				}
			}
			this.setSingleSelectionMode(true);
		}
	}

	private class ExpandActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			JOptionPane.showMessageDialog(BasicCheckRibbon.this,
					"Expand button clicked");
		}
	}

	public static class SamplePopupMenu extends JCommandPopupMenu {
		public SamplePopupMenu() {
			JCommandMenuButton menuButton1 = new JCommandMenuButton(
					"Test menu item 1", new EmptyResizableIcon(16));
			menuButton1.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					System.out.println("Test menu item 1 activated");
				}
			});
			menuButton1.setActionKeyTip("1");
			this.addMenuButton(menuButton1);

			JCommandMenuButton menuButton2 = new JCommandMenuButton(
					"Test menu item 2", new EmptyResizableIcon(16));
			menuButton2.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					System.out.println("Test menu item 2 activated");
				}
			});
			menuButton2.setActionKeyTip("2");
			this.addMenuButton(menuButton2);

			JCommandMenuButton menuButton3 = new JCommandMenuButton(
					"Test menu item 3", new EmptyResizableIcon(16));
			menuButton3.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					System.out.println("Test menu item 3 activated");
				}
			});
			menuButton3.setActionKeyTip("3");
			this.addMenuButton(menuButton3);

			this.addMenuSeparator();

			JCommandMenuButton menuButton4 = new JCommandMenuButton(
					"Test menu item 4", new EmptyResizableIcon(16));
			menuButton4.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					System.out.println("Test menu item 4 activated");
				}
			});
			menuButton4.setActionKeyTip("4");
			this.addMenuButton(menuButton4);

			JCommandMenuButton menuButton5 = new JCommandMenuButton(
					"Test menu item 5", new EmptyResizableIcon(16));
			menuButton5.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					System.out.println("Test menu item 5 activated");
				}
			});
			menuButton5.setActionKeyTip("5");
			this.addMenuButton(menuButton5);
		}
	}

	private AbstractCommandButton getIconButton(final Icon icon,
			boolean isToggle, boolean isSelected, boolean hasPopup) {
		ResizableIcon resizableIcon = new ResizableIcon() {
			int width = icon.getIconWidth();
			int height = icon.getIconHeight();

			@Override
			public int getIconHeight() {
				return this.height;
			}

			@Override
			public int getIconWidth() {
				return this.width;
			}

			@Override
			public void paintIcon(Component c, Graphics g, int x, int y) {
				icon.paintIcon(c, g, x, y);
			}

			@Override
			public void setDimension(Dimension newDimension) {
				this.width = newDimension.width;
				this.height = newDimension.height;
			}
		};
		AbstractCommandButton button = isToggle ? new JCommandToggleButton("",
				resizableIcon) : new JCommandButton("", resizableIcon);
		button.setDisplayState(CommandButtonDisplayState.SMALL);
		button.setGapScaleFactor(0.5);
		if (isSelected)
			button.getActionModel().setSelected(true);

		// make the button narrower by stripping away some of the right-left
		// insets
		Insets currInsets = button.getInsets();
		button.setBorder(new EmptyBorder(currInsets.top, currInsets.top / 2,
				currInsets.bottom, currInsets.bottom / 2));

		if (hasPopup) {
			((JCommandButton) button)
					.setPopupCallback(new PopupPanelCallback() {
						@Override
						public JPopupPanel getPopupPanel(
								JCommandButton commandButton) {
							return new SamplePopupMenu();
						}
					});
		}
		return button;
	}

	protected JRibbonBand getActionBand() {
		JRibbonBand actionBand = new JRibbonBand("Action", new document_new(),
				new ExpandActionListener());
		actionBand.setResizePolicies(CoreRibbonResizePolicies
				.getCorePoliciesRestrictive(actionBand));

		actionBand.startGroup();
		JCommandButton addressBookButton = new JCommandButton("Address book",
				new address_book_new());
		addressBookButton.setActionKeyTip("NA");
		actionBand.addCommandButton(addressBookButton,
				RibbonElementPriority.TOP);

		actionBand.startGroup();
		JCommandButton documentButton = new JCommandButton("Document",
				new document_new());
		documentButton.setActionKeyTip("ND");
		actionBand.addCommandButton(documentButton, RibbonElementPriority.TOP);

		JCommandButton appointmentButton = new JCommandButton("Appointment",
				new appointment_new());
		appointmentButton.setActionKeyTip("NP");
		actionBand.addCommandButton(appointmentButton,
				RibbonElementPriority.MEDIUM);

		JCommandButton bookmarkButton = new JCommandButton("Bookmark",
				new bookmark_new());
		bookmarkButton.setActionKeyTip("NB");
		actionBand.addCommandButton(bookmarkButton,
				RibbonElementPriority.MEDIUM);

		JCommandButton contactButton = new JCommandButton("Contact",
				new contact_new());
		contactButton.setActionKeyTip("NC");
		actionBand
				.addCommandButton(contactButton, RibbonElementPriority.MEDIUM);

		return actionBand;
	}

	protected JRibbonBand getPreferencesBand() {
		JRibbonBand preferencesBand = new JRibbonBand("Preferences",
				new preferences_desktop_font(), new ExpandActionListener());
		preferencesBand.setResizePolicies(CoreRibbonResizePolicies
				.getCorePoliciesRestrictive(preferencesBand));

		preferencesBand.startGroup();
		JCommandButton accessibility = new JCommandButton("Accessibility",
				new preferences_desktop_accessibility());
		accessibility.setActionKeyTip("Y");
		preferencesBand.addCommandButton(accessibility,
				RibbonElementPriority.MEDIUM);

		JCommandButton assistiveTech = new JCommandButton(
				"Assistive technologies",
				new preferences_desktop_assistive_technology());
		assistiveTech.setActionKeyTip("E");
		preferencesBand.addCommandButton(assistiveTech,
				RibbonElementPriority.MEDIUM);

		JCommandButton keyboardShortcuts = new JCommandButton(
				"Keyboard shortcuts",
				new preferences_desktop_keyboard_shortcuts());
		keyboardShortcuts.setPopupKeyTip("H");
		keyboardShortcuts
				.setCommandButtonKind(JCommandButton.CommandButtonKind.POPUP_ONLY);
		keyboardShortcuts.setPopupCallback(new PopupPanelCallback() {
			@Override
			public JPopupPanel getPopupPanel(JCommandButton commandButton) {
				return new SamplePopupMenu();
			}
		});
		preferencesBand.addCommandButton(keyboardShortcuts,
				RibbonElementPriority.MEDIUM);

		preferencesBand.startGroup();

		JCommandButton size = new JCommandButton("Font",
				new preferences_desktop_font());
		size.setActionKeyTip("Z");
		preferencesBand.addCommandButton(size, RibbonElementPriority.TOP);

		JCommandButton locale = new JCommandButton("Locale",
				new preferences_desktop_locale());
		locale.setActionKeyTip("L");
		preferencesBand.addCommandButton(locale, RibbonElementPriority.TOP);

		preferencesBand.startGroup();
		JCommandButton screensaver = new JCommandButton("Screensaver",
				new preferences_desktop_screensaver());
		screensaver.setActionKeyTip("V");
		preferencesBand.addCommandButton(screensaver,
				RibbonElementPriority.MEDIUM);

		JCommandButton themes = new JCommandButton("Themes",
				new preferences_desktop_theme());
		themes.setActionKeyTip("T");
		preferencesBand.addCommandButton(themes, RibbonElementPriority.MEDIUM);

		return preferencesBand;
	}

	protected JRibbonBand getParagraphBand() {
		JRibbonBand paragraphBand = new JRibbonBand("Paragraph",
				new format_justify_left(), null);

		paragraphBand.startGroup("Indent");
		JRibbonComponent justifyLeftWrapper = new JRibbonComponent(
				new format_justify_left(), "Left:", new JSpinner(
						new SpinnerNumberModel(0, 0, 100, 5)));
		justifyLeftWrapper.setKeyTip("PL");

		RichTooltip justifyLeftTooltip = new RichTooltip();
		justifyLeftTooltip.setTitle("Indent Left");
		justifyLeftTooltip
				.addDescriptionSection("Move in the left side of the paragraph by a certain amount");
		justifyLeftTooltip
				.addDescriptionSection("To change the margins for the whole document, click the Margins button");
		justifyLeftWrapper.setRichTooltip(justifyLeftTooltip);

		paragraphBand.addRibbonComponent(justifyLeftWrapper);

		JRibbonComponent justifyRightWrapper = new JRibbonComponent(
				new format_justify_right(), "Right:", new JSpinner(
						new SpinnerNumberModel(0, 0, 100, 5)));
		justifyRightWrapper.setKeyTip("PR");

		RichTooltip justifyRightTooltip = new RichTooltip();
		justifyRightTooltip.setTitle("Indent Right");
		justifyRightTooltip
				.addDescriptionSection("Move in the right side of the paragraph by a certain amount");
		justifyRightTooltip
				.addDescriptionSection("To change the margins for the whole document, click the Margins button");
		justifyRightWrapper.setRichTooltip(justifyRightTooltip);

		paragraphBand.addRibbonComponent(justifyRightWrapper);

		paragraphBand.startGroup("Spacing");
		JRibbonComponent beforeWrapper = new JRibbonComponent(new JSpinner(
				new SpinnerNumberModel(0, 0, 100, 5)));
		beforeWrapper.setKeyTip("PB");
		paragraphBand.addRibbonComponent(beforeWrapper);

		JRibbonComponent afterWrapper = new JRibbonComponent(new JSpinner(
				new SpinnerNumberModel(10, 0, 100, 5)));
		afterWrapper.setKeyTip("PA");
		paragraphBand.addRibbonComponent(afterWrapper);

		return paragraphBand;
	}

	protected JRibbonBand getShowHideBand() {
		JRibbonBand showHideBand = new JRibbonBand("Show/Hide",
				new format_justify_left(), null);

		JCheckBox ruler = new JCheckBox("Ruler");
		ruler.setSelected(true);
		JRibbonComponent rulerWrapper = new JRibbonComponent(ruler);
		rulerWrapper.setKeyTip("SR");
		showHideBand.addRibbonComponent(rulerWrapper);

		JCheckBox gridlines = new JCheckBox("Gridlines");
		JRibbonComponent gridlinesWrapper = new JRibbonComponent(gridlines);
		gridlinesWrapper.setKeyTip("SG");
		showHideBand.addRibbonComponent(gridlinesWrapper);

		JCheckBox messageBar = new JCheckBox("Message Bar");
		messageBar.setEnabled(false);
		JRibbonComponent messageBarWrapper = new JRibbonComponent(messageBar);
		messageBarWrapper.setKeyTip("SM");
		showHideBand.addRibbonComponent(messageBarWrapper);

		JCheckBox documentMap = new JCheckBox("Document Map");
		JRibbonComponent documentMapWrapper = new JRibbonComponent(documentMap);
		documentMapWrapper.setKeyTip("SD");
		showHideBand.addRibbonComponent(documentMapWrapper);

		JCheckBox thumbnails = new JCheckBox("Thumbnails");
		JRibbonComponent thumbnailsWrapper = new JRibbonComponent(thumbnails);
		thumbnailsWrapper.setKeyTip("ST");
		showHideBand.addRibbonComponent(thumbnailsWrapper);

		return showHideBand;
	}

	protected JRibbonBand getApplicationsBand() {
		JRibbonBand applicationsBand = new JRibbonBand("Applications",
				new applications_other(), new ExpandActionListener());

		JRibbonComponent games = new JRibbonComponent(new applications_games(),
				"Games", new JComboBox(new Object[] { "Tetris", "Minesweeper",
						"Doom" }));
		games.setKeyTip("AG");
		applicationsBand.addRibbonComponent(games);

		JRibbonComponent internet = new JRibbonComponent(
				new applications_internet(), "Internet", new JComboBox(
						new Object[] { "Firefox", "Opera", "Konqueror" }));
		internet.setKeyTip("AI");
		internet.setEnabled(false);
		applicationsBand.addRibbonComponent(internet);

		JRibbonComponent multimedia = new JRibbonComponent(
				new applications_multimedia(), "Multimedia", new JComboBox(
						new Object[] { "Pictures", "Video", "Audio" }));
		multimedia.setKeyTip("AM");
		applicationsBand.addRibbonComponent(multimedia);

		return applicationsBand;
	}

	protected JRibbonBand getDocumentBand() {
		JRibbonBand result = new JRibbonBand("Documents",
				new applications_office(), new ExpandActionListener());
		result.setExpandButtonKeyTip("FY");
		result.setCollapsedStateKeyTip("ZD");

		result.startGroup();
		JCommandButton localFolderButton = new JCommandButton("Local",
				new folder());
		result
				.addCommandButton(localFolderButton,
						RibbonElementPriority.MEDIUM);

		JCommandButton remoteFolderButton = new JCommandButton("Remote",
				new folder_remote());
		result.addCommandButton(remoteFolderButton,
				RibbonElementPriority.MEDIUM);

		JCommandButton savedFolderButton = new JCommandButton("Saved",
				new folder_saved_search());
		result
				.addCommandButton(savedFolderButton,
						RibbonElementPriority.MEDIUM);

		result.startGroup();

		JCommandButton docNewButton = new JCommandButton("New document",
				new document_new());
		result.addCommandButton(docNewButton, RibbonElementPriority.LOW);

		JCommandButton docOpenButton = new JCommandButton("Open document",
				new document_open());
		result.addCommandButton(docOpenButton, RibbonElementPriority.LOW);

		JCommandButton docSaveButton = new JCommandButton("Save document",
				new document_save());
		result.addCommandButton(docSaveButton, RibbonElementPriority.LOW);

		JCommandButton docPrintButton = new JCommandButton("Print",
				new document_print());
		result.addCommandButton(docPrintButton, RibbonElementPriority.LOW);

		JCommandButton docPrintPreviewButton = new JCommandButton(
				"Print preview", new document_print_preview());
		result.addCommandButton(docPrintPreviewButton,
				RibbonElementPriority.LOW);

		JCommandButton docPropertiesButton = new JCommandButton("Properties",
				new document_properties());
		result.addCommandButton(docPropertiesButton, RibbonElementPriority.LOW);

		return result;
	}

	protected JRibbonBand getClipboardBand() {
		JRibbonBand clipboardBand = new JRibbonBand("Clipboard",
				new edit_paste(), new ExpandActionListener());
		clipboardBand.setExpandButtonKeyTip("FO");
		RichTooltip expandRichTooltip = new RichTooltip();
		expandRichTooltip.setTitle("Clipboard");
		expandRichTooltip
				.addDescriptionSection("Show the Clipboard Task Pane.");
		clipboardBand.setExpandButtonRichTooltip(expandRichTooltip);
		clipboardBand.setCollapsedStateKeyTip("ZC");

		JCommandButton mainButton = new JCommandButton("Paste",
				new edit_paste());
		mainButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("Pasted!");
			}
		});
		mainButton.setPopupCallback(new PopupPanelCallback() {
			@Override
			public JPopupPanel getPopupPanel(JCommandButton commandButton) {
				return new SamplePopupMenu();
			}
		});
		mainButton
				.setCommandButtonKind(JCommandButton.CommandButtonKind.ACTION_AND_POPUP_MAIN_ACTION);
		RichTooltip mainRichTooltip = new RichTooltip();
		mainRichTooltip.setTitle("Paste");
		mainRichTooltip
				.addDescriptionSection("Paste the contents of the Clipboard");
		mainButton.setActionRichTooltip(mainRichTooltip);
		mainButton.setPopupKeyTip("V");

		RichTooltip mainPopupRichTooltip = new RichTooltip();
		mainPopupRichTooltip.setTitle("Paste");
		mainPopupRichTooltip
				.addDescriptionSection("Click here for more options such as pasting only the values or formatting");
		mainButton.setPopupRichTooltip(mainPopupRichTooltip);

		clipboardBand.addCommandButton(mainButton, RibbonElementPriority.TOP);

		JCommandButton cutButton = new JCommandButton("Cut", new edit_cut());
		cutButton.setPopupCallback(new PopupPanelCallback() {
			@Override
			public JPopupPanel getPopupPanel(JCommandButton commandButton) {
				return new SamplePopupMenu();
			}
		});
		cutButton
				.setCommandButtonKind(JCommandButton.CommandButtonKind.ACTION_AND_POPUP_MAIN_ACTION);
		RichTooltip cutRichTooltip = new RichTooltip();
		cutRichTooltip.setTitle("Cut");
		cutRichTooltip
				.addDescriptionSection("Cut the selection from the document and put it on the Clipboard");
		cutButton.setActionRichTooltip(cutRichTooltip);
		cutButton.setPopupKeyTip("X");

		clipboardBand.addCommandButton(cutButton, RibbonElementPriority.MEDIUM);

		JCommandButton copyButton = new JCommandButton("Copy", new edit_copy());
		copyButton.setPopupCallback(new PopupPanelCallback() {
			@Override
			public JPopupPanel getPopupPanel(JCommandButton commandButton) {
				return new SamplePopupMenu();
			}
		});
		copyButton
				.setCommandButtonKind(JCommandButton.CommandButtonKind.ACTION_AND_POPUP_MAIN_POPUP);
		copyButton.setPopupKeyTip("C");

		clipboardBand
				.addCommandButton(copyButton, RibbonElementPriority.MEDIUM);

		JCommandButton formatButton = new JCommandButton("Format",
				new edit_paste());
		formatButton.setPopupCallback(new PopupPanelCallback() {
			@Override
			public JPopupPanel getPopupPanel(JCommandButton commandButton) {
				JCommandPopupMenu popupMenu = new JCommandPopupMenu(
						new QuickStylesPanel(), 5, 3);
				JCommandMenuButton saveSelectionButton = new JCommandMenuButton(
						"Save Selection", new EmptyResizableIcon(16));
				saveSelectionButton.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						System.out.println("Save Selection activated");
					}
				});
				saveSelectionButton.setActionKeyTip("SS");
				popupMenu.addMenuButton(saveSelectionButton);

				JCommandMenuButton clearSelectionButton = new JCommandMenuButton(
						"Clear Selection", new EmptyResizableIcon(16));
				clearSelectionButton.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						System.out.println("Clear Selection activated");
					}
				});
				clearSelectionButton.setActionKeyTip("SC");
				popupMenu.addMenuButton(clearSelectionButton);

				popupMenu.addMenuSeparator();
				JCommandMenuButton applyStylesButton = new JCommandMenuButton(
						"Apply Styles", new EmptyResizableIcon(16));
				applyStylesButton.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						System.out.println("Apply Styles activated");
					}
				});
				applyStylesButton.setActionKeyTip("SA");
				popupMenu.addMenuButton(applyStylesButton);
				return popupMenu;
			}
		});

		formatButton
				.setCommandButtonKind(JCommandButton.CommandButtonKind.POPUP_ONLY);
		// pasteButton.addPopupActionListener(new SamplePopupActionListener());
		formatButton
				.setCommandButtonKind(JCommandButton.CommandButtonKind.POPUP_ONLY);
		formatButton.setPopupKeyTip("FP");

		clipboardBand.addCommandButton(formatButton,
				RibbonElementPriority.MEDIUM);

		clipboardBand.setResizePolicies(CoreRibbonResizePolicies
				.getCorePoliciesRestrictive(clipboardBand));

		return clipboardBand;
	}

	protected JRibbonBand getFindBand() {
		JRibbonBand findBand = new JRibbonBand("Find (toggle)", new edit_find());
		findBand.setCollapsedStateKeyTip("ZY");

		JCommandToggleButton findButton = new JCommandToggleButton("Find",
				new system_search());
		findButton.setActionKeyTip("FD");
		findBand.addCommandButton(findButton, RibbonElementPriority.TOP);

		JCommandToggleButton replaceButton = new JCommandToggleButton("Find",
				new edit_find());
		findBand.addCommandButton(replaceButton, RibbonElementPriority.MEDIUM);

		JCommandToggleButton findReplaceButton = new JCommandToggleButton(
				"Find replace", new edit_find_replace());
		findReplaceButton.setEnabled(false);
		findBand.addCommandButton(findReplaceButton,
				RibbonElementPriority.MEDIUM);

		JCommandToggleButton selectAllButton = new JCommandToggleButton(
				"Select all", new edit_select_all());
		findBand
				.addCommandButton(selectAllButton, RibbonElementPriority.MEDIUM);

		return findBand;
	}

	protected JRibbonBand getQuickStylesBand() {
		JRibbonBand quickStylesBand = new JRibbonBand("Quick Styles",
				new preferences_desktop_theme());
		quickStylesBand.setCollapsedStateKeyTip("ZS");

		quickStylesBand.setResizePolicies(CoreRibbonResizePolicies
				.getCorePoliciesRestrictive(quickStylesBand));

		Map<RibbonElementPriority, Integer> stylesGalleryVisibleButtonCounts = new HashMap<RibbonElementPriority, Integer>();
		stylesGalleryVisibleButtonCounts.put(RibbonElementPriority.LOW, 1);
		stylesGalleryVisibleButtonCounts.put(RibbonElementPriority.MEDIUM, 2);
		stylesGalleryVisibleButtonCounts.put(RibbonElementPriority.TOP, 2);

		List<StringValuePair<List<JCommandToggleButton>>> stylesGalleryButtons = new ArrayList<StringValuePair<List<JCommandToggleButton>>>();
		List<JCommandToggleButton> stylesGalleryButtonsList = new ArrayList<JCommandToggleButton>();
		List<JCommandToggleButton> stylesGalleryButtonsList2 = new ArrayList<JCommandToggleButton>();
		for (int i = 0; i < 30; i++) {
			final int index = i;
			ResizableIcon fontIcon = new font_x_generic();
			ResizableIcon finalIcon = new DecoratedResizableIcon(fontIcon,
					new DecoratedResizableIcon.IconDecorator() {
						@Override
						public void paintIconDecoration(Component c,
								Graphics g, int x, int y, int width, int height) {
							Graphics2D g2d = (Graphics2D) g.create();
							g2d.setColor(Color.black);
							RenderingUtils.installDesktopHints(g2d);
							g2d.setFont(UIManager.getFont("Label.font"));
							g2d.drawString("" + index, x + 2, y + height - 2);
							g2d.dispose();
						}
					});
			JCommandToggleButton jrb = new JCommandToggleButton("Style " + i,
					finalIcon);
			if (i == 1)
				jrb.getActionModel().setSelected(true);
			jrb.setName("Index " + i);
			jrb.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					System.out.println("Invoked action on " + index);
				}
			});
			if (i < 10)
				stylesGalleryButtonsList.add(jrb);
			else
				stylesGalleryButtonsList2.add(jrb);
		}

		stylesGalleryButtons
				.add(new StringValuePair<List<JCommandToggleButton>>("Styles",
						stylesGalleryButtonsList));
		stylesGalleryButtons
				.add(new StringValuePair<List<JCommandToggleButton>>(
						"Extended Styles", stylesGalleryButtonsList2));

		quickStylesBand.addRibbonGallery("Styles", stylesGalleryButtons,
				stylesGalleryVisibleButtonCounts, 3, 3,
				RibbonElementPriority.TOP);
		quickStylesBand.setRibbonGalleryPopupCallback("Styles",
				new JRibbonBand.RibbonGalleryPopupCallback() {
					public void popupToBeShown(JCommandPopupMenu menu) {
						JCommandMenuButton saveSelectionButton = new JCommandMenuButton(
								"Save Selection", new EmptyResizableIcon(16));
						saveSelectionButton
								.addActionListener(new ActionListener() {
									@Override
									public void actionPerformed(ActionEvent e) {
										System.out
												.println("Save Selection activated");
									}
								});
						saveSelectionButton.setActionKeyTip("SS");
						menu.addMenuButton(saveSelectionButton);

						JCommandMenuButton clearSelectionButton = new JCommandMenuButton(
								"Clear Selection", new EmptyResizableIcon(16));
						clearSelectionButton
								.addActionListener(new ActionListener() {
									@Override
									public void actionPerformed(ActionEvent e) {
										System.out
												.println("Clear Selection activated");
									}
								});
						clearSelectionButton.setActionKeyTip("SC");
						menu.addMenuButton(clearSelectionButton);

						menu.addMenuSeparator();
						JCommandMenuButton applyStylesButton = new JCommandMenuButton(
								"Apply Styles", new font_x_generic());
						applyStylesButton
								.addActionListener(new ActionListener() {
									@Override
									public void actionPerformed(ActionEvent e) {
										System.out
												.println("Apply Styles activated");
									}
								});
						applyStylesButton.setActionKeyTip("SA");
						menu.addMenuButton(applyStylesButton);
					}
				});
		quickStylesBand.setRibbonGalleryExpandKeyTip("Styles", "L");

		JCommandButton stylesButton1 = new JCommandButton("Styles1",
				new font_x_generic());
		stylesButton1.setActionKeyTip("SA");
		quickStylesBand.addCommandButton(stylesButton1,
				RibbonElementPriority.MEDIUM);

		JCommandButton styles2Button = new JCommandButton("Styles2",
				new image_x_generic());
		styles2Button.setActionKeyTip("SB");
		quickStylesBand.addCommandButton(styles2Button,
				RibbonElementPriority.MEDIUM);
		styles2Button.setEnabled(false);

		JCommandButton styles3Button = new JCommandButton("Styles3",
				new text_html());
		styles3Button.setActionKeyTip("SC");
		quickStylesBand.addCommandButton(styles3Button,
				RibbonElementPriority.MEDIUM);

		return quickStylesBand;
	}

	protected JRibbonBand getPreviewBand() {
		JRibbonBand previewBand = new JRibbonBand("Preview",
				new SimpleResizableIcon(RibbonElementPriority.TOP, 32, 32));

		previewBand.setResizePolicies(CoreRibbonResizePolicies
				.getCorePoliciesNone(previewBand));

		JCommandButton findButton = new JCommandButton("Preview",
				new SimpleResizableIcon(RibbonElementPriority.TOP, 32, 32));
		previewBand.addCommandButton(findButton, RibbonElementPriority.TOP);

		JCommandButton slideShowButton = new JCommandButton("Slide Show",
				new SimpleResizableIcon(RibbonElementPriority.TOP, 32, 32));
		previewBand
				.addCommandButton(slideShowButton, RibbonElementPriority.TOP);

		return previewBand;
	}

	protected JRibbonBand getRowSpanBand() {
		JRibbonBand rowSpanBand = new JRibbonBand("Row spans",
				new SimpleResizableIcon(RibbonElementPriority.TOP, 32, 32));

		JTextPane textPane1 = new JTextPane();
		textPane1.setText("this control should be shown on three rows");
		JScrollPane scrollPane1 = new JScrollPane(textPane1);
		scrollPane1.setPreferredSize(new Dimension(80, 100));
		JRibbonComponent text1 = new JRibbonComponent(scrollPane1);
		rowSpanBand.addRibbonComponent(text1, 3);

		JRibbonComponent text2 = new JRibbonComponent(new JTextField("one row",
				8));
		rowSpanBand.addRibbonComponent(text2, 1);

		JTextPane textPane3 = new JTextPane();
		textPane3.setText("this control should be shown on two rows");
		JScrollPane scrollPane3 = new JScrollPane(textPane3);
		scrollPane3.setPreferredSize(new Dimension(80, 100));
		JRibbonComponent text3 = new JRibbonComponent(scrollPane3);
		rowSpanBand.addRibbonComponent(text3, 2);

		JTextPane textPane4 = new JTextPane();
		textPane4.setText("this control should be shown on two rows");
		JScrollPane scrollPane4 = new JScrollPane(textPane4);
		scrollPane4.setPreferredSize(new Dimension(80, 100));
		JRibbonComponent text4 = new JRibbonComponent(scrollPane4);
		rowSpanBand.addRibbonComponent(text4, 2);

		JRibbonComponent text5 = new JRibbonComponent(new JTextField("one row",
				8));
		rowSpanBand.addRibbonComponent(text5, 1);

		JTextPane textPane6 = new JTextPane();
		textPane6.setText("this control should be shown on two rows");
		JScrollPane scrollPane6 = new JScrollPane(textPane6);
		scrollPane6.setPreferredSize(new Dimension(80, 100));
		JRibbonComponent text6 = new JRibbonComponent(scrollPane6);
		rowSpanBand.addRibbonComponent(text6, 2);

		JTextPane textPane7 = new JTextPane();
		textPane7.setText("this control should be shown on two rows");
		JScrollPane scrollPane7 = new JScrollPane(textPane7);
		scrollPane7.setPreferredSize(new Dimension(80, 100));
		JRibbonComponent text7 = new JRibbonComponent(scrollPane7);
		rowSpanBand.addRibbonComponent(text7, 2);

		rowSpanBand.setResizePolicies(CoreRibbonResizePolicies
				.getCorePoliciesNone(rowSpanBand));

		return rowSpanBand;
	}

	protected JRibbonBand getAlignmentBand() {
		JRibbonBand alignmentBand = new JRibbonBand("Alignment",
				new format_justify_left(), null);

		alignmentBand.startGroup();

		JRibbonComponent wideWrapper1 = new JRibbonComponent(new JLabel(
				"Some long long long text"));
		alignmentBand.addRibbonComponent(wideWrapper1);

		JRibbonComponent alignLeadingWrapper1 = new JRibbonComponent(null,
				"leading", new JSpinner(new SpinnerNumberModel(0, 0, 100, 5)));
		alignLeadingWrapper1
				.setHorizontalAlignment(HorizontalAlignment.LEADING);
		alignmentBand.addRibbonComponent(alignLeadingWrapper1);

		JRibbonComponent alignTrailingWrapper1 = new JRibbonComponent(null,
				"trailing", new JSpinner(new SpinnerNumberModel(0, 0, 100, 5)));
		alignTrailingWrapper1
				.setHorizontalAlignment(HorizontalAlignment.TRAILING);
		alignmentBand.addRibbonComponent(alignTrailingWrapper1);

		JRibbonComponent wideWrapper2 = new JRibbonComponent(new JLabel(
				"Some long long long text"));
		alignmentBand.addRibbonComponent(wideWrapper2);

		JRibbonComponent alignCenterWrapper1 = new JRibbonComponent(null,
				"center", new JSpinner(new SpinnerNumberModel(0, 0, 100, 5)));
		alignCenterWrapper1.setHorizontalAlignment(HorizontalAlignment.CENTER);
		alignmentBand.addRibbonComponent(alignCenterWrapper1);

		JRibbonComponent alignFillWrapper1 = new JRibbonComponent(null, "fill",
				new JSpinner(new SpinnerNumberModel(0, 0, 100, 5)));
		alignFillWrapper1.setHorizontalAlignment(HorizontalAlignment.FILL);
		alignmentBand.addRibbonComponent(alignFillWrapper1);

		alignmentBand.startGroup();

		JRibbonComponent wideWrapper3 = new JRibbonComponent(new JLabel(
				"Some long text"));
		alignmentBand.addRibbonComponent(wideWrapper3);

		JRibbonComponent alignLeadingWrapper2 = new JRibbonComponent(
				new JSpinner(new SpinnerNumberModel(0, 0, 100, 5)));
		alignLeadingWrapper2
				.setHorizontalAlignment(HorizontalAlignment.LEADING);
		alignmentBand.addRibbonComponent(alignLeadingWrapper2);

		JRibbonComponent alignTrailingWrapper2 = new JRibbonComponent(
				new JSpinner(new SpinnerNumberModel(0, 0, 100, 5)));
		alignTrailingWrapper2
				.setHorizontalAlignment(HorizontalAlignment.TRAILING);
		alignmentBand.addRibbonComponent(alignTrailingWrapper2);

		JRibbonComponent wideWrapper4 = new JRibbonComponent(new JLabel(
				"Some long text"));
		alignmentBand.addRibbonComponent(wideWrapper4);

		JRibbonComponent alignCenterWrapper2 = new JRibbonComponent(
				new JSpinner(new SpinnerNumberModel(0, 0, 100, 5)));
		alignCenterWrapper2.setHorizontalAlignment(HorizontalAlignment.CENTER);
		alignmentBand.addRibbonComponent(alignCenterWrapper2);

		JRibbonComponent alignFillWrapper2 = new JRibbonComponent(new JSpinner(
				new SpinnerNumberModel(0, 0, 100, 5)));
		alignFillWrapper2.setHorizontalAlignment(HorizontalAlignment.FILL);
		alignmentBand.addRibbonComponent(alignFillWrapper2);

		return alignmentBand;
	}

	protected JRibbonBand getAnimationBand() {
		JRibbonBand animationBand = new JRibbonBand("Animation...",
				new SimpleResizableIcon(RibbonElementPriority.TOP, 32, 32));

		animationBand.setResizePolicies(CoreRibbonResizePolicies
				.getCorePoliciesNone(animationBand));

		JCommandButton findButton = new JCommandButton("Custom Animation",
				new SimpleResizableIcon(RibbonElementPriority.TOP, 32, 32));
		animationBand.addCommandButton(findButton, RibbonElementPriority.TOP);

		return animationBand;
	}

	protected JRibbonBand getTransitionBand() {
		JRibbonBand transitionBand = new JRibbonBand(
				"Transition To This Slide", new SimpleResizableIcon(
						RibbonElementPriority.TOP, 32, 32));

		Map<RibbonElementPriority, Integer> transitionGalleryVisibleButtonCounts = new HashMap<RibbonElementPriority, Integer>();
		transitionGalleryVisibleButtonCounts.put(RibbonElementPriority.LOW, 2);
		transitionGalleryVisibleButtonCounts.put(RibbonElementPriority.MEDIUM,
				4);
		transitionGalleryVisibleButtonCounts.put(RibbonElementPriority.TOP, 6);

		List<StringValuePair<List<JCommandToggleButton>>> transitionGalleryButtons = new ArrayList<StringValuePair<List<JCommandToggleButton>>>();
		List<JCommandToggleButton> transitionGalleryButtonsList = new ArrayList<JCommandToggleButton>();

		for (int i = 1; i <= 20; i++) {
			JCommandToggleButton additionalButton = new JCommandToggleButton(
					"Trans no." + i, new NumberedResizableIcon(i, 100, 80));
			transitionGalleryButtonsList.add(additionalButton);
		}

		transitionGalleryButtons
				.add(new StringValuePair<List<JCommandToggleButton>>(null,
						transitionGalleryButtonsList));
		transitionBand.addRibbonGallery("Transitions",
				transitionGalleryButtons, transitionGalleryVisibleButtonCounts,
				6, 3, RibbonElementPriority.TOP);

		transitionBand.startGroup();
		transitionBand.addRibbonComponent(new JRibbonComponent(
				new SimpleResizableIcon(RibbonElementPriority.TOP, 16, 16),
				"Sound", new JComboBox(new Object[] { "[No Sound]     " })));
		transitionBand.addRibbonComponent(new JRibbonComponent(null, "Speed",
				new JComboBox(new Object[] { "Medium           " })));
		JCommandButton applyToAll = new JCommandButton("Apply To All",
				new SimpleResizableIcon(RibbonElementPriority.TOP, 16, 16));
		applyToAll.setDisplayState(CommandButtonDisplayState.MEDIUM);
		applyToAll.setVGapScaleFactor(0.5);
		transitionBand.addRibbonComponent(new JRibbonComponent(applyToAll));

		return transitionBand;
	}

	protected JRibbonBand getTransitionNextBand() {
		JRibbonBand transitionBand = new JRibbonBand(
				"Transition To Next Slide", new SimpleResizableIcon(
						RibbonElementPriority.TOP, 32, 32));

		JCheckBox mouseClick = new JCheckBox("On Mouse Click");
		mouseClick.setSelected(true);
		JRibbonComponent mouseClickWrapper = new JRibbonComponent(mouseClick);
		transitionBand.addRibbonComponent(mouseClickWrapper);

		JCheckBox autoAfter = new JCheckBox("Automatically After");
		JRibbonComponent autoAfterWrapper = new JRibbonComponent(autoAfter);
		transitionBand.addRibbonComponent(autoAfterWrapper);

		transitionBand.addRibbonComponent(new JRibbonComponent(
				new SimpleResizableIcon(RibbonElementPriority.TOP, 16, 16), "",
				new JSpinner(new SpinnerDateModel())));

		return transitionBand;
	}

	protected RibbonContextualTaskGroup group1;
	protected RibbonContextualTaskGroup group2;

	public BasicCheckRibbon() {
		super("Ribbon test");
		try {
			this.setIconImages(Arrays.asList(ImageIO
					.read(BasicCheckRibbon.class
							.getResource("ribbon-main-icon-16.png")), ImageIO
					.read(BasicCheckRibbon.class
							.getResource("ribbon-main-icon.png"))));
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}

	public void configureRibbon() {
		JRibbonBand clipboardBand = this.getClipboardBand();
		JRibbonBand quickStylesBand = this.getQuickStylesBand();
		JFlowRibbonBand fontBand = this.getFontBand();
		JRibbonBand documentBand = this.getDocumentBand();
		JRibbonBand findBand = this.getFindBand();
		RibbonTask pageLayoutTask = new RibbonTask("Page Layout",
				clipboardBand, quickStylesBand, fontBand, documentBand,
				findBand);
		pageLayoutTask.setKeyTip("P");

		JRibbonBand themeBand = this.getActionBand();
		JRibbonBand preferencesBand = this.getPreferencesBand();
		JRibbonBand arrangeBand = this.getApplicationsBand();
		JRibbonBand paragraphBand = this.getParagraphBand();
		JRibbonBand showHideBand = this.getShowHideBand();
		RibbonTask writeTask = new RibbonTask("Write", themeBand,
				preferencesBand, arrangeBand, paragraphBand, showHideBand);
		writeTask.setKeyTip("W");

		JRibbonBand previewBand = this.getPreviewBand();
		JRibbonBand animationBand = this.getAnimationBand();
		JRibbonBand transitionBand = this.getTransitionBand();
		JRibbonBand transitionNextBand = this.getTransitionNextBand();
		RibbonTask animationsTask = new RibbonTask("Animations", previewBand,
				animationBand, transitionBand, transitionNextBand);
		animationsTask.setKeyTip("A");

		JRibbonBand rowSpanBand = this.getRowSpanBand();
		JRibbonBand alignmentBand = this.getAlignmentBand();
		RibbonTask wrappedTask = new RibbonTask("Wrapped", rowSpanBand,
				alignmentBand);
		wrappedTask.setKeyTip("R");

		this.getRibbon().addTask(pageLayoutTask);
		this.getRibbon().addTask(writeTask);
		this.getRibbon().addTask(animationsTask);
		this.getRibbon().addTask(wrappedTask);

		this.getRibbon().configureHelp(new help_browser(),
				new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						JOptionPane.showMessageDialog(BasicCheckRibbon.this,
								"Help button clicked");
					}
				});

		group1 = new RibbonContextualTaskGroup("Group 1", Color.red,
				getContextualRibbonTask("Group 1-1", "XA"),
				getContextualRibbonTask("Group 1-2", "XB"));
		group2 = new RibbonContextualTaskGroup("Group 2", Color.green,
				getContextualRibbonTask("Group 2-1", "YA"));
		this.getRibbon().addContextualTaskGroup(group1);
		this.getRibbon().addContextualTaskGroup(group2);

		configureTaskBar();

		// application menu
		configureApplicationMenu();

		JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		this.configureControlPanel(controlPanel);

		this.add(controlPanel, BorderLayout.SOUTH);
		this.add(new RulerPanel(), BorderLayout.CENTER);
	}

	protected void configureTaskBar() {
		// taskbar components
		JCommandButton taskbarButtonPaste = new JCommandButton("",
				new edit_paste());
		taskbarButtonPaste
				.setCommandButtonKind(CommandButtonKind.ACTION_AND_POPUP_MAIN_ACTION);
		taskbarButtonPaste.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("Taskbar Paste activated");
			}
		});
		taskbarButtonPaste.setPopupCallback(new PopupPanelCallback() {
			@Override
			public JPopupPanel getPopupPanel(JCommandButton commandButton) {
				return new SamplePopupMenu();
			}
		});
		taskbarButtonPaste.setActionRichTooltip(new RichTooltip("Paste",
				"Paste the contents of the Clipboard"));
		taskbarButtonPaste
				.setPopupRichTooltip(new RichTooltip("Paste",
						"Click here for more options such as pasting only the values or formatting"));
		taskbarButtonPaste.setActionKeyTip("1");
		this.getRibbon().addTaskbarComponent(taskbarButtonPaste);

		JCommandButton taskbarButtonClear = new JCommandButton("",
				new edit_clear());
		taskbarButtonClear.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("Taskbar Clear activated");
			}
		});
		taskbarButtonClear.setEnabled(false);
		taskbarButtonClear.setActionKeyTip("2");
		this.getRibbon().addTaskbarComponent(taskbarButtonClear);

		JCommandButton taskbarButtonCopy = new JCommandButton("",
				new edit_copy());
		taskbarButtonCopy.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("Taskbar Copy activated");
			}
		});
		taskbarButtonCopy.setActionKeyTip("3");
		this.getRibbon().addTaskbarComponent(taskbarButtonCopy);

		this.getRibbon().addTaskbarComponent(
				new JSeparator(JSeparator.VERTICAL));

		JCommandButton taskbarButtonFind = new JCommandButton("",
				new edit_find());
		taskbarButtonFind.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("Taskbar Find activated");
			}
		});
		taskbarButtonFind.setActionKeyTip("4");
		this.getRibbon().addTaskbarComponent(taskbarButtonFind);
	}

	protected void configureApplicationMenu() {
		RibbonApplicationMenuEntryPrimary amEntryNew = new RibbonApplicationMenuEntryPrimary(
				new document_new(), "New", new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						System.out.println("Invoked creating new document");
					}
				}, CommandButtonKind.ACTION_ONLY);
		// amEntryNew
		// .setRolloverCallback(new
		// RibbonApplicationMenuEntryPrimary.PrimaryRolloverCallback() {
		// @Override
		// public void menuEntryActivated(JPanel targetPanel) {
		// targetPanel.removeAll();
		// }
		// });
		amEntryNew.setActionKeyTip("N");

		RibbonApplicationMenuEntryPrimary amEntryOpen = new RibbonApplicationMenuEntryPrimary(
				new document_open(), "Open", new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						System.out.println("Invoked opening document");
					}
				}, CommandButtonKind.ACTION_ONLY);
		amEntryOpen
				.setRolloverCallback(new RibbonApplicationMenuEntryPrimary.PrimaryRolloverCallback() {
					@Override
					public void menuEntryActivated(JPanel targetPanel) {
						targetPanel.removeAll();
						JCommandButtonPanel openHistoryPanel = new JCommandButtonPanel(
								CommandButtonDisplayState.MEDIUM);
						String groupName = "Recent Documents";
						openHistoryPanel.addButtonGroup(groupName);
						for (int i = 0; i < 5; i++) {
							JCommandButton historyButton = new JCommandButton(i
									+ "    " + "document" + i + ".html",
									new text_html());
							historyButton
									.setHorizontalAlignment(SwingUtilities.LEFT);
							openHistoryPanel
									.addButtonToLastGroup(historyButton);
						}
						openHistoryPanel.setMaxButtonColumns(1);
						targetPanel.setLayout(new BorderLayout());
						targetPanel.add(openHistoryPanel, BorderLayout.CENTER);
					}
				});
		amEntryOpen.setActionKeyTip("O");

		RibbonApplicationMenuEntryPrimary amEntrySave = new RibbonApplicationMenuEntryPrimary(
				new document_save(), "Save", new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						System.out.println("Invoked saving document");
					}
				}, CommandButtonKind.ACTION_ONLY);
		// amEntrySave
		// .setRolloverCallback(new
		// RibbonApplicationMenuEntryPrimary.PrimaryRolloverCallback() {
		// @Override
		// public void menuEntryActivated(JPanel targetPanel) {
		// targetPanel.removeAll();
		// }
		// });
		amEntrySave.setEnabled(false);
		amEntrySave.setActionKeyTip("S");

		RibbonApplicationMenuEntryPrimary amEntrySaveAs = new RibbonApplicationMenuEntryPrimary(
				new document_save_as(), "Save As", new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						System.out.println("Invoked saving document as");
					}
				}, CommandButtonKind.ACTION_AND_POPUP_MAIN_ACTION);
		amEntrySaveAs.setActionKeyTip("A");
		amEntrySaveAs.setPopupKeyTip("F");

		RibbonApplicationMenuEntrySecondary amEntrySaveAsWord = new RibbonApplicationMenuEntrySecondary(
				new x_office_document(), "Word Document", null,
				CommandButtonKind.ACTION_ONLY);
		amEntrySaveAsWord
				.setDescriptionText("Save the document in the default file format");
		amEntrySaveAsWord.setActionKeyTip("W");
		RibbonApplicationMenuEntrySecondary amEntrySaveAsHtml = new RibbonApplicationMenuEntrySecondary(
				new text_html(), "HTML Document", null,
				CommandButtonKind.ACTION_ONLY);
		amEntrySaveAsHtml
				.setDescriptionText("Publish a copy of the document as an HTML file");
		amEntrySaveAsHtml.setEnabled(false);
		amEntrySaveAsHtml.setActionKeyTip("H");
		RibbonApplicationMenuEntrySecondary amEntrySaveAsOtherFormats = new RibbonApplicationMenuEntrySecondary(
				new document_save_as(), "Other Formats", null,
				CommandButtonKind.ACTION_ONLY);
		amEntrySaveAsOtherFormats
				.setDescriptionText("Open the Save As dialog box to select from all possible file types");
		amEntrySaveAsOtherFormats.setActionKeyTip("O");

		amEntrySaveAs
				.addSecondaryMenuGroup("Save a copy of the document",
						amEntrySaveAsWord, amEntrySaveAsHtml,
						amEntrySaveAsOtherFormats);

		RibbonApplicationMenuEntryPrimary amEntryPrint = new RibbonApplicationMenuEntryPrimary(
				new document_print(), "Print", new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						System.out.println("Invoked printing document");
					}
				}, CommandButtonKind.ACTION_AND_POPUP_MAIN_ACTION);
		amEntryPrint.setActionKeyTip("P");
		amEntryPrint.setPopupKeyTip("W");

		RibbonApplicationMenuEntrySecondary amEntryPrintSelect = new RibbonApplicationMenuEntrySecondary(
				new printer(), "Print", null, CommandButtonKind.ACTION_ONLY);
		amEntryPrintSelect
				.setDescriptionText("Select a printer, number of copies and other printing options before printing");
		amEntryPrintSelect.setActionKeyTip("P");
		RibbonApplicationMenuEntrySecondary amEntryPrintDefault = new RibbonApplicationMenuEntrySecondary(
				new document_print(), "Quick Print", null,
				CommandButtonKind.ACTION_ONLY);
		amEntryPrintDefault
				.setDescriptionText("Send the document directly to the default printer without making changes");
		amEntryPrintDefault.setActionKeyTip("Q");
		RibbonApplicationMenuEntrySecondary amEntryPrintPreview = new RibbonApplicationMenuEntrySecondary(
				new document_print_preview(), "Print Preview", null,
				CommandButtonKind.ACTION_ONLY);
		amEntryPrintPreview
				.setDescriptionText("Preview and make changes to the pages before printing");
		amEntryPrintPreview.setActionKeyTip("V");

		amEntryPrint.addSecondaryMenuGroup("Preview and print the document",
				amEntryPrintSelect, amEntryPrintDefault, amEntryPrintPreview);

		RibbonApplicationMenuEntrySecondary amEntryPrintMemo = new RibbonApplicationMenuEntrySecondary(
				new text_x_generic(), "Memo Style", null,
				CommandButtonKind.ACTION_ONLY);
		amEntryPrintMemo.setActionKeyTip("M");

		amEntryPrint.addSecondaryMenuGroup("Page Setup", amEntryPrintMemo);

		RibbonApplicationMenuEntryPrimary amEntrySend = new RibbonApplicationMenuEntryPrimary(
				new mail_forward(), "Send", new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						System.out.println("Invoked sending document");
					}
				}, CommandButtonKind.POPUP_ONLY);
		amEntrySend.setPopupKeyTip("D");

		RibbonApplicationMenuEntrySecondary amEntrySendMail = new RibbonApplicationMenuEntrySecondary(
				new mail_message_new(), "E-mail", null,
				CommandButtonKind.ACTION_ONLY);
		amEntrySendMail
				.setDescriptionText("Send a copy of the document in an e-mail message as an attachment");
		amEntrySendMail.setActionKeyTip("E");
		RibbonApplicationMenuEntrySecondary amEntrySendHtml = new RibbonApplicationMenuEntrySecondary(
				new text_html(), "E-mail as HTML Attachment", null,
				CommandButtonKind.ACTION_ONLY);
		amEntrySendHtml
				.setDescriptionText("Send a copy of the document in a message as an HTML attachment");
		amEntrySendHtml.setActionKeyTip("H");
		RibbonApplicationMenuEntrySecondary amEntrySendDoc = new RibbonApplicationMenuEntrySecondary(
				new x_office_document(), "E-mail as Word Attachment", null,
				CommandButtonKind.ACTION_ONLY);
		amEntrySendDoc
				.setDescriptionText("Send a copy of the document in a message as a Word attachment");
		amEntrySendDoc.setActionKeyTip("W");
		RibbonApplicationMenuEntrySecondary amEntrySendWireless = new RibbonApplicationMenuEntrySecondary(
				new network_wireless(), "Wireless", null,
				CommandButtonKind.POPUP_ONLY);
		amEntrySendWireless.setPopupKeyTip("X");

		amEntrySendWireless.setPopupCallback(new PopupPanelCallback() {
			@Override
			public JPopupPanel getPopupPanel(JCommandButton commandButton) {
				JCommandPopupMenu wirelessChoices = new JCommandPopupMenu();

				JCommandMenuButton wiFiMenuButton = new JCommandMenuButton(
						"Via WiFi", new EmptyResizableIcon(16));
				wiFiMenuButton.setActionKeyTip("W");
				wiFiMenuButton.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						System.out.println("WiFi activated");
					}
				});
				wirelessChoices.addMenuButton(wiFiMenuButton);

				JCommandMenuButton blueToothMenuButton = new JCommandMenuButton(
						"Via BlueTooth", new EmptyResizableIcon(16));
				blueToothMenuButton.setActionKeyTip("B");
				blueToothMenuButton.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						System.out.println("BlueTooth activated");
					}
				});
				wirelessChoices.addMenuButton(blueToothMenuButton);
				return wirelessChoices;
			}
		});

		amEntrySendWireless
				.setDescriptionText("Locate a wireless device and send a copy of the document to it");

		amEntrySend.addSecondaryMenuGroup(
				"Send a copy of the document to other people", amEntrySendMail,
				amEntrySendHtml, amEntrySendDoc, amEntrySendWireless);

		RibbonApplicationMenuEntryPrimary amEntryExit = new RibbonApplicationMenuEntryPrimary(
				new system_log_out(), "Exit", new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						System.exit(0);
					}
				}, CommandButtonKind.ACTION_ONLY);
		// amEntryExit
		// .setRolloverCallback(new
		// RibbonApplicationMenuEntryPrimary.PrimaryRolloverCallback() {
		// @Override
		// public void menuEntryActivated(JPanel targetPanel) {
		// targetPanel.removeAll();
		// }
		// });
		amEntryExit.setActionKeyTip("X");

		RibbonApplicationMenu applicationMenu = new RibbonApplicationMenu();
		applicationMenu.addMenuEntry(amEntryNew);
		applicationMenu.addMenuEntry(amEntryOpen);
		applicationMenu.addMenuEntry(amEntrySave);
		applicationMenu.addMenuEntry(amEntrySaveAs);
		applicationMenu.addMenuEntry(amEntryPrint);
		applicationMenu.addMenuEntry(amEntrySend);
		applicationMenu.addMenuEntry(amEntryExit);

		RibbonApplicationMenuEntryFooter amFooterProps = new RibbonApplicationMenuEntryFooter(
				new document_properties(), "Options", new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						System.out.println("Invoked Options");
					}
				});
		RibbonApplicationMenuEntryFooter amFooterExit = new RibbonApplicationMenuEntryFooter(
				new system_log_out(), "Exit", new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						System.exit(0);
					}
				});
		amFooterExit.setEnabled(false);
		applicationMenu.addFooterEntry(amFooterProps);
		applicationMenu.addFooterEntry(amFooterExit);

		this.getRibbon().setApplicationMenu(applicationMenu);

		RichTooltip appMenuRichTooltip = new RichTooltip();
		appMenuRichTooltip.setTitle("Test App Button");
		appMenuRichTooltip
				.addDescriptionSection("Click here to open, save, or print, and to see everything else you can do with your document");
		try {
			appMenuRichTooltip
					.setMainImage(ImageIO
							.read(BasicCheckRibbon.class
									.getResource("/test/ribbon/appmenubutton-tooltip-main.png")));
			appMenuRichTooltip.setFooterImage(ImageIO
					.read(BasicCheckRibbon.class
							.getResource("/test/ribbon/help-browser.png")));
		} catch (IOException ioe) {
		}
		appMenuRichTooltip.addFooterSection("Press F1 for more help");
		this.getRibbon().setApplicationMenuRichTooltip(appMenuRichTooltip);
		this.getRibbon().setApplicationMenuKeyTip("F");
	}

	protected RibbonTask getContextualRibbonTask(String title, String keyTip) {
		JRibbonBand actionBand = this.getActionBand();
		JRibbonBand arrangeBand = this.getApplicationsBand();
		JRibbonBand previewBand = this.getPreviewBand();
		JRibbonBand transitionBand = this.getTransitionBand();
		RibbonTask task = new RibbonTask(title, actionBand, arrangeBand,
				previewBand, transitionBand);
		task.setKeyTip(keyTip);
		return task;
	}

	protected void configureControlPanel(JPanel controlPanel) {
		final JCheckBox group1Visible = new JCheckBox("Group 1");
		final JCheckBox group2Visible = new JCheckBox("Group 2");
		group1Visible.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run() {
						getRibbon().setVisible(group1,
								group1Visible.isSelected());
					}
				});
			}
		});
		group2Visible.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run() {
						getRibbon().setVisible(group2,
								group2Visible.isSelected());
					}
				});
			}
		});
		controlPanel.add(group1Visible);
		controlPanel.add(group2Visible);

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
						JFrame frame = BasicCheckRibbon.this;
						boolean wasDecoratedByOS = !frame.isUndecorated();
						try {
							LookAndFeelInfo selected = (LookAndFeelInfo) jcb
									.getSelectedItem();
							UIManager.setLookAndFeel(selected.getClassName());
							SwingUtilities.updateComponentTreeUI(frame);
						} catch (Exception exc) {
							exc.printStackTrace();
						}
						boolean canBeDecoratedByLAF = UIManager
								.getLookAndFeel()
								.getSupportsWindowDecorations();
						if (canBeDecoratedByLAF == wasDecoratedByOS) {
							boolean wasVisible = frame.isVisible();

							frame.setVisible(false);
							frame.dispose();
							if (!canBeDecoratedByLAF) {
								// see the java docs under the method
								// JFrame.setDefaultLookAndFeelDecorated(boolean
								// value) for description of these 2 lines:
								frame.setUndecorated(false);
								frame.getRootPane().setWindowDecorationStyle(
										JRootPane.NONE);

							} else {
								frame.setUndecorated(true);
								frame.getRootPane().setWindowDecorationStyle(
										JRootPane.FRAME);
							}
							frame.setVisible(wasVisible);
							wasDecoratedByOS = !frame.isUndecorated();
						}

						// for (Object key : UIManager.getDefaults().keySet()) {
						// Object val = UIManager.getDefaults().get(key);
						// if (val instanceof Color) {
						// String skey = (String) key;
						// if (skey.toLowerCase().indexOf("menu") >= 0)
						// System.out.println(key + ":" + val);
						// }
						// }
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

		final JCheckBox appMenuVisible = new JCheckBox("has app menu");
		appMenuVisible.setSelected(true);
		appMenuVisible.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run() {
						if (!appMenuVisible.isSelected())
							getRibbon().setApplicationMenu(null);
						else
							configureApplicationMenu();
					}
				});
			}
		});
		controlPanel.add(appMenuVisible);
	}

	protected JFlowRibbonBand getFontBand() {
		JFlowRibbonBand fontBand = new JFlowRibbonBand("Font",
				new preferences_desktop_font(), new ExpandActionListener());
		fontBand.setExpandButtonKeyTip("FN");
		fontBand.setCollapsedStateKeyTip("ZF");

		JComboBox fontCombo = new JComboBox(new Object[] {
				"+ Minor (Calibri)   ", "+ Minor (Columbus)   ",
				"+ Minor (Consolas)   ", "+ Minor (Cornelius)   ",
				"+ Minor (Cleopatra)   ", "+ Minor (Cornucopia)   ",
				"+ Minor (Candella)   ", "+ Minor (Cambria)   " });
		JRibbonComponent fontComboWrapper = new JRibbonComponent(fontCombo);
		fontComboWrapper.setKeyTip("SF");
		fontBand.addFlowComponent(fontComboWrapper);

		JComboBox sizeCombo = new JComboBox(new Object[] { "11  " });
		JRibbonComponent sizeComboWrapper = new JRibbonComponent(sizeCombo);
		sizeComboWrapper.setKeyTip("SS");
		fontBand.addFlowComponent(sizeComboWrapper);

		JCommandButtonStrip indentStrip = new JCommandButtonStrip();

		JCommandButton indentLeftButton = new JCommandButton("",
				new format_indent_less());
		indentLeftButton.setActionKeyTip("AO");
		indentStrip.add(indentLeftButton);

		JCommandButton indentRightButton = new JCommandButton("",
				new format_indent_more());
		indentRightButton.setActionKeyTip("AI");
		indentStrip.add(indentRightButton);

		fontBand.addFlowComponent(indentStrip);

		JCommandButtonStrip styleStrip = new JCommandButtonStrip();

		JCommandToggleButton styleBoldButton = new JCommandToggleButton("",
				new format_text_bold());
		styleBoldButton.getActionModel().setSelected(true);
		styleBoldButton.setActionRichTooltip(new RichTooltip("Bold",
				"Make the selected text bold"));
		styleBoldButton.setActionKeyTip("1");
		styleStrip.add(styleBoldButton);

		JCommandToggleButton styleItalicButton = new JCommandToggleButton("",
				new format_text_italic());
		styleItalicButton.setActionRichTooltip(new RichTooltip("Italic",
				"Italicize the selected text"));
		styleItalicButton.setActionKeyTip("2");
		styleStrip.add(styleItalicButton);

		JCommandToggleButton styleUnderlineButton = new JCommandToggleButton(
				"", new format_text_underline());
		styleUnderlineButton.setActionRichTooltip(new RichTooltip("Underline",
				"Underline the selected text"));
		styleUnderlineButton.setActionKeyTip("3");
		styleStrip.add(styleUnderlineButton);

		JCommandToggleButton styleStrikeThroughButton = new JCommandToggleButton(
				"", new format_text_strikethrough());
		styleStrikeThroughButton.setActionRichTooltip(new RichTooltip(
				"Strikethrough",
				"Draw a line through the middle of the selected text"));
		styleStrikeThroughButton.setActionKeyTip("4");
		styleStrip.add(styleStrikeThroughButton);

		fontBand.addFlowComponent(styleStrip);

		JCommandButtonStrip alignStrip = new JCommandButtonStrip();
		CommandToggleButtonGroup alignGroup = new CommandToggleButtonGroup();

		JCommandToggleButton alignLeftButton = new JCommandToggleButton("",
				new format_justify_left());
		alignLeftButton.setActionKeyTip("AL");
		alignLeftButton.getActionModel().setSelected(true);
		alignGroup.add(alignLeftButton);
		alignStrip.add(alignLeftButton);

		JCommandToggleButton alignCenterButton = new JCommandToggleButton("",
				new format_justify_center());
		alignCenterButton.setActionKeyTip("AC");
		alignGroup.add(alignCenterButton);
		alignStrip.add(alignCenterButton);

		JCommandToggleButton alignRightButton = new JCommandToggleButton("",
				new format_justify_right());
		alignRightButton.setActionKeyTip("AR");
		alignGroup.add(alignRightButton);
		alignStrip.add(alignRightButton);

		JCommandToggleButton alignFillButton = new JCommandToggleButton("",
				new format_justify_fill());
		alignFillButton.setActionKeyTip("AF");
		alignGroup.add(alignFillButton);
		alignStrip.add(alignFillButton);

		fontBand.addFlowComponent(alignStrip);

		return fontBand;
	}

	/**
	 * Main method for testing.
	 * 
	 * @param args
	 *            Ignored.
	 */
	public static void main(String[] args) {
		UIManager.installLookAndFeel("JGoodies Plastic",
				"com.jgoodies.looks.plastic.PlasticLookAndFeel");
		UIManager.installLookAndFeel("JGoodies PlasticXP",
				"com.jgoodies.looks.plastic.PlasticXPLookAndFeel");
		UIManager.installLookAndFeel("JGoodies Plastic3D",
				"com.jgoodies.looks.plastic.Plastic3DLookAndFeel");
		UIManager.installLookAndFeel("JGoodies Windows",
				"com.jgoodies.looks.windows.WindowsLookAndFeel");

		UIManager.installLookAndFeel("Synthetica base",
				"de.javasoft.plaf.synthetica.SyntheticaStandardLookAndFeel");
		UIManager.installLookAndFeel("Synthetica BlackMoon",
				"de.javasoft.plaf.synthetica.SyntheticaBlackMoonLookAndFeel");
		UIManager.installLookAndFeel("Synthetica BlackStar",
				"de.javasoft.plaf.synthetica.SyntheticaBlackStarLookAndFeel");
		UIManager.installLookAndFeel("Synthetica BlueIce",
				"de.javasoft.plaf.synthetica.SyntheticaBlueIceLookAndFeel");
		UIManager.installLookAndFeel("Synthetica BlueMoon",
				"de.javasoft.plaf.synthetica.SyntheticaBlueMoonLookAndFeel");
		UIManager.installLookAndFeel("Synthetica BlueSteel",
				"de.javasoft.plaf.synthetica.SyntheticaBlueSteelLookAndFeel");
		UIManager.installLookAndFeel("Synthetica GreenDream",
				"de.javasoft.plaf.synthetica.SyntheticaGreenDreamLookAndFeel");
		UIManager
				.installLookAndFeel("Synthetica MauveMetallic",
						"de.javasoft.plaf.synthetica.SyntheticaMauveMetallicLookAndFeel");
		UIManager
				.installLookAndFeel("Synthetica OrangeMetallic",
						"de.javasoft.plaf.synthetica.SyntheticaOrangeMetallicLookAndFeel");
		UIManager.installLookAndFeel("Synthetica SkyMetallic",
				"de.javasoft.plaf.synthetica.SyntheticaSkyMetallicLookAndFeel");
		UIManager.installLookAndFeel("Synthetica SilverMoon",
				"de.javasoft.plaf.synthetica.SyntheticaSilverMoonLookAndFeel");
		UIManager.installLookAndFeel("Synthetica WhiteVision",
				"de.javasoft.plaf.synthetica.SyntheticaWhiteVisionLookAndFeel");
		UIManager.installLookAndFeel("Synthetica Simple2D",
				"de.javasoft.plaf.synthetica.SyntheticaSimple2DLookAndFeel");

		UIManager.installLookAndFeel("A03", "a03.swing.plaf.A03LookAndFeel");
		UIManager.installLookAndFeel("Liquid",
				"com.birosoft.liquid.LiquidLookAndFeel");
		UIManager.installLookAndFeel("Napkin",
				"net.sourceforge.napkinlaf.NapkinLookAndFeel");
		UIManager.installLookAndFeel("Pagosoft",
				"com.pagosoft.plaf.PgsLookAndFeel");
		UIManager.installLookAndFeel("Squareness",
				"net.beeger.squareness.SquarenessLookAndFeel");

		JFrame.setDefaultLookAndFeelDecorated(true);
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception exc) {
		}
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				final BasicCheckRibbon c = new BasicCheckRibbon();
				c.configureRibbon();
				Rectangle r = GraphicsEnvironment.getLocalGraphicsEnvironment()
						.getMaximumWindowBounds();
				c.setPreferredSize(new Dimension(r.width, r.height / 2));
				c.pack();
				c.setLocation(r.x, r.y);
				c.setVisible(true);
				c.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

				// c.addComponentListener(new ComponentAdapter() {
				// @Override
				// public void componentResized(ComponentEvent e) {
				// System.out.println("Size " + c.getSize());
				// }
				// });

				c.getRootPane().getInputMap(
						JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(
						KeyStroke.getKeyStroke("alt shift E"),
						"installTracingRepaintManager");
				c.getRootPane().getActionMap().put(
						"installTracingRepaintManager", new AbstractAction() {
							@Override
							public void actionPerformed(ActionEvent e) {
								RepaintManager
										.setCurrentManager(new TracingRepaintManager());
							}
						});
			}
		});
	}

	public static class RulerPanel extends JPanel {
		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);

			Graphics2D g2d = (Graphics2D) g.create();
			RenderingUtils.installDesktopHints(g2d);
			g2d.setColor(Color.gray);

			// horizontal ruler on top
			int offset = 20;
			for (int i = offset; i < this.getWidth(); i += 10) {
				if ((i - offset) % 100 == 0)
					continue;
				g2d.drawLine(i, 9, i, 11);
			}
			for (int i = offset + 50; i < this.getWidth(); i += 100) {
				g2d.drawLine(i, 7, i, 13);
			}
			for (int i = offset; i < this.getWidth(); i += 100) {
				int c = ((i - offset) / 100) % 10;
				g2d.drawString("" + c, i - 2, 15);
			}

			// vertical ruler on left
			for (int i = offset; i < this.getHeight(); i += 10) {
				if ((i - offset) % 100 == 0)
					continue;
				g2d.drawLine(9, i, 11, i);
			}
			for (int i = offset + 50; i < this.getHeight(); i += 100) {
				g2d.drawLine(7, i, 13, i);
			}
			for (int i = offset; i < this.getHeight(); i += 100) {
				int c = ((i - offset) / 100) % 10;
				g2d.drawString("" + c, 8, i + 4);
			}

			g2d.dispose();
		}
	}
}
