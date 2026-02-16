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
package org.jvnet.flamingo.ribbon.ui;

import java.awt.*;
import java.awt.color.ColorSpace;
import java.awt.image.ColorConvertOp;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;

import org.jvnet.flamingo.common.HorizontalAlignment;
import org.jvnet.flamingo.common.icon.FilteredResizableIcon;
import org.jvnet.flamingo.common.icon.ResizableIcon;
import org.jvnet.flamingo.ribbon.JRibbonComponent;

public class BasicRibbonComponentUI extends RibbonComponentUI {
	/**
	 * The associated ribbon component.
	 */
	protected JRibbonComponent ribbonComponent;

	protected JLabel captionLabel;

	protected PropertyChangeListener propertyChangeListener;

	protected ResizableIcon disabledIcon;

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.plaf.ComponentUI#createUI(javax.swing.JComponent)
	 */
	public static ComponentUI createUI(JComponent c) {
		return new BasicRibbonComponentUI();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.plaf.ComponentUI#installUI(javax.swing.JComponent)
	 */
	@Override
	public void installUI(JComponent c) {
		this.ribbonComponent = (JRibbonComponent) c;
		installDefaults();
		installComponents();
		installListeners();
		c.setLayout(createLayoutManager());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.plaf.ComponentUI#uninstallUI(javax.swing.JComponent)
	 */
	@Override
	public void uninstallUI(JComponent c) {
		c.setLayout(null);
		uninstallListeners();
		uninstallDefaults();
		uninstallComponents();
	}

	/**
	 * Installs default parameters on the associated ribbon component.
	 */
	protected void installDefaults() {
		if (!this.ribbonComponent.isSimpleWrapper()) {
			ResizableIcon icon = this.ribbonComponent.getIcon();
			if (icon != null) {
				icon.setDimension(new Dimension(16, 16));
				this.disabledIcon = createDisabledIcon();
			}
		}

		this.ribbonComponent.getMainComponent().setOpaque(false);
		this.ribbonComponent.setOpaque(false);
	}

	/**
	 * Installs subcomponents on the associated ribbon component.
	 */
	protected void installComponents() {
		this.captionLabel = new JLabel(this.ribbonComponent.getCaption());
		this.captionLabel.setEnabled(this.ribbonComponent.isEnabled());
		this.ribbonComponent.add(this.captionLabel);

		JComponent mainComponent = this.ribbonComponent.getMainComponent();
		this.ribbonComponent.add(mainComponent);
	}

	/**
	 * Installs listeners on the associated ribbon component.
	 */
	protected void installListeners() {
		this.propertyChangeListener = new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				if ("enabled".equals(evt.getPropertyName())) {
					boolean isEnabled = (Boolean) evt.getNewValue();
					ribbonComponent.getMainComponent().setEnabled(isEnabled);
					if (!ribbonComponent.isSimpleWrapper()) {
						captionLabel.setEnabled(isEnabled);
					}
					ribbonComponent.repaint();
				}
			}
		};
		this.ribbonComponent
				.addPropertyChangeListener(this.propertyChangeListener);

	}

	/**
	 * Uninstalls default parameters from the associated ribbon component.
	 */
	protected void uninstallDefaults() {
	}

	/**
	 * Uninstalls components from the associated ribbon component.
	 */
	protected void uninstallComponents() {
		JComponent mainComponent = this.ribbonComponent.getMainComponent();
		this.ribbonComponent.remove(mainComponent);

		this.ribbonComponent.remove(this.captionLabel);
		this.captionLabel = null;
	}

	/**
	 * Uninstalls listeners from the associated ribbon component.
	 */
	protected void uninstallListeners() {
		this.ribbonComponent
				.removePropertyChangeListener(this.propertyChangeListener);
		this.propertyChangeListener = null;
	}

	@Override
	public Point getKeyTipAnchorCenterPoint() {
		if (this.ribbonComponent.isSimpleWrapper()) {
			return new Point(
					this.ribbonComponent.getMainComponent().getX() + 10,
					this.ribbonComponent.getHeight());
		} else {
			return new Point(this.captionLabel.getX(), this.ribbonComponent
					.getHeight());
		}
	}

	protected LayoutManager createLayoutManager() {
		return new ExtComponentLayout();
	}

	protected class ExtComponentLayout implements LayoutManager {
		@Override
		public void addLayoutComponent(String name, Component comp) {
		}

		@Override
		public void removeLayoutComponent(Component comp) {
		}

		@Override
		public Dimension minimumLayoutSize(Container parent) {
			Insets ins = ribbonComponent.getInsets();
			JComponent mainComponent = ribbonComponent.getMainComponent();
			Dimension minMain = mainComponent.getMinimumSize();
			if (ribbonComponent.isSimpleWrapper()) {
				return new Dimension(ins.left + ins.right + minMain.width,
						ins.top + ins.bottom + minMain.height);
			} else {
				Dimension minCaption = captionLabel.getMinimumSize();
				ResizableIcon icon = ribbonComponent.getIcon();
				return new Dimension(
						ins.left + ins.right
								+ ((icon == null) ? 0 : icon.getIconWidth())
								+ minCaption.width + minMain.width + 2
								* getLayoutGap(), ins.top
								+ ins.bottom
								+ Math.max(((icon == null) ? 0 : icon
										.getIconHeight()), Math.max(
										minCaption.height, minMain.height)));
			}
		}

		@Override
		public Dimension preferredLayoutSize(Container parent) {
			Insets ins = ribbonComponent.getInsets();
			JComponent mainComponent = ribbonComponent.getMainComponent();
			Dimension prefMain = mainComponent.getPreferredSize();
			if (ribbonComponent.isSimpleWrapper()) {
				return new Dimension(ins.left + ins.right + prefMain.width,
						ins.top + ins.bottom + prefMain.height);
			} else {
				Dimension prefCaption = captionLabel.getPreferredSize();
				ResizableIcon icon = ribbonComponent.getIcon();
				return new Dimension(ins.left + ins.right
						+ ((icon == null) ? 0 : icon.getIconWidth())
						+ prefCaption.width + prefMain.width + 2
						* getLayoutGap(), ins.top
						+ ins.bottom
						+ Math.max(((icon == null) ? 0 : icon.getIconHeight()),
								Math.max(prefCaption.height, prefMain.height)));
			}
		}

		@Override
		public void layoutContainer(Container parent) {
			JRibbonComponent ribbonComp = (JRibbonComponent) parent;
			Insets ins = ribbonComp.getInsets();
			int gap = getLayoutGap();
			int availableHeight = ribbonComp.getHeight() - ins.top - ins.bottom;
			int availableWidth = ribbonComp.getWidth() - ins.left - ins.right;

			HorizontalAlignment horizAlignment = ribbonComp
					.getHorizontalAlignment();
			JComponent mainComp = ribbonComp.getMainComponent();
			Dimension prefMainDim = mainComp.getPreferredSize();
			int prefMainWidth = prefMainDim.width;
			int finalHeight = Math.min(prefMainDim.height, availableHeight);

			if (ribbonComp.isSimpleWrapper()) {
				int finalMainWidth = Math.min(availableWidth, prefMainWidth);
				int offset = availableWidth - prefMainWidth;
				int topMain = ins.top + (availableHeight - finalHeight) / 2;
				switch (horizAlignment) {
				case LEADING:
					ribbonComp.getMainComponent().setBounds(ins.left, topMain,
							finalMainWidth, finalHeight);
					break;
				case TRAILING:
					ribbonComp.getMainComponent().setBounds(ins.left + offset,
							topMain, finalMainWidth, finalHeight);
					break;
				case CENTER:
					ribbonComp.getMainComponent().setBounds(
							ins.left + offset / 2, topMain, finalMainWidth,
							finalHeight);
					break;
				case FILL:
					ribbonComp.getMainComponent().setBounds(ins.left, topMain,
							availableWidth, finalHeight);
					break;
				}

			} else {
				int iconW = (ribbonComp.getIcon() != null) ? ribbonComp
						.getIcon().getIconWidth() : 0;
				// int iconH = extComp.getIcon().getIconHeight();

				Dimension prefCaptionDim = captionLabel.getPreferredSize();

				// int captionBaseline = captionLabel.getBaseline(
				// prefCaptionDim.width, prefCaptionDim.height);

				int x = ins.left + iconW + gap;
				captionLabel.setBounds(x, ins.top
						+ (availableHeight - prefCaptionDim.height) / 2,
						prefCaptionDim.width, prefCaptionDim.height);

				x += (prefCaptionDim.width + gap);

				int topMain = ins.top + (availableHeight - finalHeight) / 2;
				int finalMainWidth = Math.min(ribbonComp.getWidth() - ins.right
						- x, prefMainWidth);
				int offset = ribbonComp.getWidth() - ins.right - x
						- prefMainWidth;

				switch (horizAlignment) {
				case LEADING:
					ribbonComp.getMainComponent().setBounds(x, topMain,
							finalMainWidth, finalHeight);
					break;
				case TRAILING:
					ribbonComp.getMainComponent().setBounds(x + offset,
							topMain, finalMainWidth, finalHeight);
					break;
				case CENTER:
					ribbonComp.getMainComponent().setBounds(x + offset / 2,
							topMain, finalMainWidth, finalHeight);
					break;
				case FILL:
					ribbonComp.getMainComponent().setBounds(x, topMain,
							ribbonComp.getWidth() - ins.right - x, finalHeight);
					break;
				}

				// ribbonComp.getMainComponent().setBounds(x,
				// ins.top + (availableHeight - finalHeight) / 2,
				// prefMainWidth, finalHeight);

				// int mainBaseline = ribbonComp.getMainComponent().getBaseline(
				// prefMainDim.width, prefMainDim.height);
			}
			// System.out.println(captionBaseline + ":" + mainBaseline);
		}
	}

	@Override
	public void paint(Graphics g, JComponent c) {
		JRibbonComponent ribbonComp = (JRibbonComponent) c;
		if (!ribbonComp.isSimpleWrapper()) {
			Insets ins = ribbonComp.getInsets();
			ResizableIcon icon = ribbonComp.isEnabled() ? ribbonComp.getIcon()
					: this.disabledIcon;
			if (icon != null) {
				int availableHeight = ribbonComp.getHeight() - ins.top
						- ins.bottom;
				int iconY = Math.max(0, ins.top
						+ (availableHeight - icon.getIconHeight()) - 2);
				paintIcon(g, ribbonComp, icon, ins.left, iconY);
			}
		}
	}

	protected void paintIcon(Graphics g, JRibbonComponent ribbonComp,
			Icon icon, int x, int y) {
		icon.paintIcon(ribbonComp, g, x, y);
	}

	protected int getLayoutGap() {
		return 4;
	}

	protected ResizableIcon createDisabledIcon() {
		return new FilteredResizableIcon(this.ribbonComponent.getIcon(),
				new ColorConvertOp(ColorSpace.getInstance(ColorSpace.CS_GRAY),
						null));
	}
}
