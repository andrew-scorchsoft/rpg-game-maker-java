/*
 * Copyright (c) 2003-2009 Flamingo Kirill Grouchnikov
 * and <a href="http://www.topologi.com">Topologi</a>. 
 * Contributed by <b>Rick Jelliffe</b> of <b>Topologi</b> 
 * in January 2006. in All Rights Reserved.
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
 *  o Neither the name of Flamingo Kirill Grouchnikov Topologi nor the names of 
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
package org.jvnet.flamingo.bcb.ui;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.UIResource;
import javax.swing.plaf.basic.BasicLabelUI;

import org.jvnet.flamingo.bcb.JBreadcrumbBar;
import org.jvnet.flamingo.utils.FlamingoUtilities;

/**
 * Basic UI for breadcrumb particle ({@link BreadcrumbParticle}).
 * 
 * @author Topologi
 * @author Kirill Grouchnikov
 */
public class BasicBreadcrumbParticleUI extends BasicLabelUI {
	/**
	 * The associated breadcrumb particle.
	 */
	protected BreadcrumbParticle particle;

	protected FocusListener baseFocusListener;

	protected MouseListener baseMouseListener;

	protected KeyListener baseKeyListener;

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.plaf.ComponentUI#createUI(javax.swing.JComponent)
	 */
	public static ComponentUI createUI(JComponent c) {
		return new BasicBreadcrumbParticleUI();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.plaf.ComponentUI#installUI(javax.swing.JComponent)
	 */
	@Override
	public void installUI(JComponent c) {
		this.particle = (BreadcrumbParticle) c;
		installDefaults(this.particle);
		installComponents(this.particle);
		installListeners(this.particle);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.plaf.ComponentUI#uninstallUI(javax.swing.JComponent)
	 */
	@Override
	public void uninstallUI(JComponent c) {
		c.setLayout(null);

		uninstallListeners((BreadcrumbParticle) c);
		uninstallComponents((BreadcrumbParticle) c);
		uninstallDefaults((BreadcrumbParticle) c);
		this.particle = null;
	}

	protected void installDefaults(BreadcrumbParticle particle) {
		Font currFont = particle.getFont();
		if ((currFont == null) || (currFont instanceof UIResource)) {
			Font font = FlamingoUtilities.getFont(null, "BreadcrumbBar.font",
					"Button.font", "Panel.font");
			particle.setFont(font);
		}
		particle.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		particle.setBorder(new EmptyBorder(0, 2, 0, 2));
	}

	protected void installComponents(BreadcrumbParticle particle) {
	}

	protected void installListeners(final BreadcrumbParticle particle) {

		this.baseMouseListener = new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				particle.getModel().setRollover(true);
				particle.getBar().repaint();
			}

			@Override
			public void mouseExited(MouseEvent e) {
				particle.getModel().setRollover(false);
				particle.getBar().repaint();
			}

			@Override
			public void mouseClicked(MouseEvent me) {
				if (me.getModifiers() == MouseEvent.BUTTON1_MASK) {
					particle.onSelection();
				}
			}
		};
		particle.addMouseListener(this.baseMouseListener);

		this.baseKeyListener = new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_DOWN) {
					// try to show popup
					particle.getBar().getUI().popup(particle.getIndex());
				}
			}

			@Override
			public void keyTyped(KeyEvent e) {
				if (!(e.getSource() instanceof JLabel))
					return;
				if (e.getKeyChar() == KeyEvent.VK_ESCAPE) {
					return;
				} else if (e.getKeyChar() == KeyEvent.VK_ENTER) {
					particle.onSelection();
					return;
				}
			}
		};
		particle.addKeyListener(this.baseKeyListener);
	}

	protected void uninstallDefaults(BreadcrumbParticle particle) {
	}

	protected void uninstallComponents(BreadcrumbParticle particle) {
	}

	protected void uninstallListeners(BreadcrumbParticle particle) {
		particle.removeKeyListener(this.baseKeyListener);
		this.baseKeyListener = null;
		particle.removeMouseListener(this.baseMouseListener);
		this.baseMouseListener = null;
	}

	@Override
	public void paint(Graphics g, JComponent c) {
		Graphics2D graphics = (Graphics2D) g.create();
		boolean toMark = particle.getModel().isSelected()
				|| particle.getModel().isArmed()
				|| particle.getModel().isRollover()
				|| particle.getModel().isPressed();
		if (toMark) {
			JBreadcrumbBar bar = this.particle.getBar();
			BreadcrumbBarUI barUI = bar.getUI();
			ChoicesSelector selector = barUI.getSelector(this.particle
					.getIndex() + 1);
			this.paintSelectedBackground(graphics, c, selector);
		}

		super.paint(g, c);

		if (toMark) {
			this.paintSelectedForeground(graphics, c);
		}

		graphics.dispose();
	}

	protected void paintSelectedBackground(Graphics2D graphics, JComponent c,
			ChoicesSelector selector) {
	}

	protected void paintSelectedForeground(Graphics2D graphics, JComponent c) {
		graphics.setColor(particle.getForeground());

		Rectangle viewR = new Rectangle();
		Rectangle iconR = new Rectangle();
		Rectangle textR = new Rectangle();

		Insets paintViewInsets = new Insets(0, 0, 0, 0);
		Insets insets = c.getInsets(paintViewInsets);

		viewR.x = insets.left;
		viewR.y = insets.top;
		viewR.width = c.getWidth() - (insets.left + insets.right);
		viewR.height = c.getHeight() - (insets.top + insets.bottom);

		SwingUtilities.layoutCompoundLabel(particle, particle
				.getFontMetrics(particle.getFont()), particle.getText(),
				particle.getIcon(), particle.getVerticalAlignment(), particle
						.getHorizontalAlignment(), particle
						.getVerticalTextPosition(), particle
						.getHorizontalTextPosition(), viewR, iconR, textR,
				particle.getIconTextGap());

		graphics.drawLine(textR.x, textR.y + textR.height, textR.x
				+ textR.width, textR.y + textR.height);
	}
}
