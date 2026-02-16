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
import java.awt.image.BufferedImage;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.plaf.*;
import javax.swing.plaf.basic.BasicGraphicsUtils;
import javax.swing.plaf.basic.BasicToggleButtonUI;

import org.jvnet.flamingo.ribbon.JRibbon;
import org.jvnet.flamingo.ribbon.RibbonContextualTaskGroup;
import org.jvnet.flamingo.utils.*;

/**
 * Basic UI for toggle button of ribbon tasks {@link JRibbonTaskToggleButton}.
 * 
 * @author Kirill Grouchnikov
 */
public class BasicRibbonTaskToggleButtonUI extends BasicToggleButtonUI {
	/**
	 * Used to provide a LAF-consistent appearance under core LAFs.
	 */
	protected CellRendererPane buttonRendererPane;

	/**
	 * Used to provide a LAF-consistent appearance under core LAFs.
	 */
	protected JToggleButton rendererButton;

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.plaf.ComponentUI#createUI(javax.swing.JComponent)
	 */
	public static ComponentUI createUI(JComponent c) {
		return new BasicRibbonTaskToggleButtonUI();
	}

	/**
	 * The associated toggle tab button.
	 */
	protected JRibbonTaskToggleButton toggleTabButton;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.swing.plaf.basic.BasicButtonUI#installUI(javax.swing.JComponent)
	 */
	@Override
	public void installUI(JComponent c) {
		this.toggleTabButton = (JRibbonTaskToggleButton) c;
		super.installUI(c);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seejavax.swing.plaf.basic.BasicButtonUI#installDefaults(javax.swing.
	 * AbstractButton)
	 */
	@Override
	protected void installDefaults(AbstractButton b) {
		super.installDefaults(b);
		Font f = b.getFont();
		if (f == null || f instanceof UIResource) {
			b.setFont(FlamingoUtilities.getFont(null, "Ribbon.font",
					"Button.font", "Panel.font"));
		}

		Border border = b.getBorder();
		if (border == null || border instanceof UIResource) {
			Border toInstall = UIManager
					.getBorder("RibbonTaskToggleButton.border");
			if (toInstall == null)
				toInstall = new BorderUIResource.EmptyBorderUIResource(1, 12,
						1, 12);
			b.setBorder(toInstall);
		}

		this.buttonRendererPane = new CellRendererPane();
		b.add(buttonRendererPane);
		this.rendererButton = new JToggleButton("");

		b.setRolloverEnabled(true);
		b.setOpaque(false);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seejavax.swing.plaf.basic.BasicButtonUI#uninstallDefaults(javax.swing.
	 * AbstractButton)
	 */
	@Override
	protected void uninstallDefaults(AbstractButton b) {
		b.remove(this.buttonRendererPane);
		this.buttonRendererPane = null;

		this.rendererButton = null;

		super.uninstallDefaults(b);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.swing.plaf.basic.BasicButtonUI#paintButtonPressed(java.awt.Graphics
	 * , javax.swing.AbstractButton)
	 */
	@Override
	protected void paintButtonPressed(Graphics g, AbstractButton b) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.plaf.ComponentUI#update(java.awt.Graphics,
	 * javax.swing.JComponent)
	 */
	@Override
	public void update(Graphics g, JComponent c) {
		Graphics2D g2d = (Graphics2D) g.create();
		RenderingUtils.installDesktopHints(g2d);
		this.paintButtonBackground(g2d, new Rectangle(0, 0, c.getWidth(), c
				.getHeight() + 10));
		this.paintText(g2d);
		g2d.dispose();
	}

	protected void paintText(Graphics g) {
		FontMetrics fm = g.getFontMetrics();
		String toPaint = this.toggleTabButton.getText();

		// compute the insets
		int fullInsets = this.toggleTabButton.getInsets().left;
		int pw = this.getPreferredSize(this.toggleTabButton).width;
		int mw = this.getMinimumSize(this.toggleTabButton).width;
		int w = this.toggleTabButton.getWidth();
		int h = this.toggleTabButton.getHeight();
		int insets = fullInsets - (pw - w) * (fullInsets - 2) / (pw - mw);

		// and the text rectangle
		Rectangle textRect = new Rectangle(insets,
				1 + (h - fm.getHeight()) / 2, w - 2 * insets, fm.getHeight());

		// show the first characters that fit into the available text rectangle
		while (true) {
			if (toPaint.length() == 0)
				break;
			int strWidth = fm.stringWidth(toPaint);
			if (strWidth <= textRect.width)
				break;
			toPaint = toPaint.substring(0, toPaint.length() - 1);
		}
		BasicGraphicsUtils.drawString(g, toPaint, -1, textRect.x, textRect.y
				+ fm.getAscent());
	}

	/**
	 * Paints the button background.
	 * 
	 * @param graphics
	 *            Graphics context.
	 * @param toFill
	 *            Rectangle to fill.
	 * @param button
	 *            The button itself.
	 */
	protected void paintButtonBackground(Graphics graphics, Rectangle toFill) {
		JRibbon ribbon = (JRibbon) SwingUtilities.getAncestorOfClass(
				JRibbon.class, this.toggleTabButton);

		this.buttonRendererPane.setBounds(toFill.x, toFill.y, toFill.width,
				toFill.height);
		ButtonModel model = this.rendererButton.getModel();
		model.setEnabled(this.toggleTabButton.isEnabled());
		model.setSelected(false);
		// System.out.println(toggleTabButton.getText() + ":"
		// + toggleTabButton.isSelected());

		// selected task toggle button should not have any background if
		// the ribbon is minimized and it is not shown in a popup
		boolean displayAsSelected = this.toggleTabButton.isSelected()
				&& (!ribbon.isMinimized() || FlamingoUtilities
						.isShowingMinimizedRibbonInPopup(ribbon));
		model.setRollover(displayAsSelected
				|| this.toggleTabButton.getModel().isRollover());
		model.setPressed(false);
		if (model.isRollover()) {
			Graphics2D g2d = (Graphics2D) graphics.create();
			// partial translucency if it is not selected
			if (!this.toggleTabButton.isSelected()) {
				g2d.setComposite(AlphaComposite.SrcOver.derive(0.4f));
			}
			g2d.translate(toFill.x, toFill.y);

			boolean isContextualTask = (this.toggleTabButton
					.getContextualGroupHueColor() != null);
			if (!isContextualTask) {
				Shape clip = g2d.getClip();
				g2d.clip(FlamingoUtilities.getRibbonTaskToggleButtonOutline(
						toFill.width, toFill.height, 2));
				this.buttonRendererPane.paintComponent(g2d,
						this.rendererButton, this.toggleTabButton, toFill.x
								- toFill.width / 2, toFill.y - toFill.height
								/ 2, 2 * toFill.width, 2 * toFill.height, true);
				g2d.setColor(FlamingoUtilities.getBorderColor().darker());
				g2d.setClip(clip);
				g2d.draw(FlamingoUtilities.getRibbonTaskToggleButtonOutline(
						toFill.width, toFill.height + 1, 2));
			} else {
				// draw to an offscreen image, colorize and draw the colorized
				// image
				BufferedImage offscreen = FlamingoUtilities.getBlankImage(
						toFill.width, toFill.height);
				Graphics2D offscreenGraphics = offscreen.createGraphics();
				Shape clip = g2d.getClip();
				offscreenGraphics.clip(FlamingoUtilities
						.getRibbonTaskToggleButtonOutline(toFill.width,
								toFill.height, 2));
				this.buttonRendererPane.paintComponent(offscreenGraphics,
						this.rendererButton, this.toggleTabButton, toFill.x
								- toFill.width / 2, toFill.y - toFill.height
								/ 2, 2 * toFill.width, 2 * toFill.height, true);
				offscreenGraphics.setColor(FlamingoUtilities.getBorderColor()
						.darker());
				offscreenGraphics.setClip(clip);
				offscreenGraphics.draw(FlamingoUtilities
						.getRibbonTaskToggleButtonOutline(toFill.width,
								toFill.height + 1, 2));
				offscreenGraphics.dispose();

				ColorShiftFilter filter = new ColorShiftFilter(
						this.toggleTabButton.getContextualGroupHueColor(),
						RibbonContextualTaskGroup.HUE_ALPHA);
				BufferedImage colorized = filter.filter(offscreen, null);
				g2d.drawImage(colorized, 0, 0, null);
			}
			g2d.dispose();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.swing.plaf.basic.BasicButtonUI#getPreferredSize(javax.swing.JComponent
	 * )
	 */
	@Override
	public Dimension getPreferredSize(JComponent c) {
		AbstractButton b = (AbstractButton) c;

		Icon icon = b.getIcon();
		String text = b.getText();

		Font font = b.getFont();
		FontMetrics fm = b.getFontMetrics(font);

		Rectangle iconR = new Rectangle();
		Rectangle textR = new Rectangle();
		Rectangle viewR = new Rectangle(Short.MAX_VALUE, Short.MAX_VALUE);

		SwingUtilities.layoutCompoundLabel(b, fm, text, icon, b
				.getVerticalAlignment(), b.getHorizontalAlignment(), b
				.getVerticalTextPosition(), b.getHorizontalTextPosition(),
				viewR, iconR, textR, (text == null ? 0 : b.getIconTextGap()));

		Rectangle r = iconR.union(textR);

		Insets insets = b.getInsets();
		r.width += insets.left + insets.right;
		r.height += insets.top + insets.bottom;

		return r.getSize();
	}

	@Override
	public Dimension getMinimumSize(JComponent c) {
		AbstractButton b = (AbstractButton) c;

		Icon icon = b.getIcon();
		String text = "Www";

		Font font = b.getFont();
		FontMetrics fm = b.getFontMetrics(font);

		Rectangle iconR = new Rectangle();
		Rectangle textR = new Rectangle();
		Rectangle viewR = new Rectangle(Short.MAX_VALUE, Short.MAX_VALUE);

		SwingUtilities.layoutCompoundLabel(b, fm, text, icon, b
				.getVerticalAlignment(), b.getHorizontalAlignment(), b
				.getVerticalTextPosition(), b.getHorizontalTextPosition(),
				viewR, iconR, textR, (text == null ? 0 : b.getIconTextGap()));

		Rectangle r = iconR.union(textR);

		Insets insets = b.getInsets();
		r.width += 4;
		r.height += insets.top + insets.bottom;

		return r.getSize();
	}
}
