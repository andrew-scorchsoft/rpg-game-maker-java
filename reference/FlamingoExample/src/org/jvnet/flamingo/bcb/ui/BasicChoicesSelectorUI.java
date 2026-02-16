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

import javax.swing.JComponent;
import javax.swing.SwingUtilities;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.plaf.ComponentUI;

import org.jvnet.flamingo.bcb.JBreadcrumbBar;

/**
 * Basic UI for choices selector ({@link ChoicesSelector}).
 * 
 * @author Topologi
 * @author Kirill Grouchnikov
 */
public class BasicChoicesSelectorUI extends ChoicesSelectorUI {
	/**
	 * The associated choices selector.
	 */
	protected ChoicesSelector choicesSelector;

	protected FocusListener baseFocusListener;

	protected MouseListener baseMouseListener;

	protected KeyListener baseKeyListener;

	protected PopupMenuListener basePopupMenuListener;

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.plaf.ComponentUI#createUI(javax.swing.JComponent)
	 */
	public static ComponentUI createUI(JComponent c) {
		return new BasicChoicesSelectorUI();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.plaf.ComponentUI#installUI(javax.swing.JComponent)
	 */
	@Override
	public void installUI(JComponent c) {
		this.choicesSelector = (ChoicesSelector) c;
		installDefaults(this.choicesSelector);
		installComponents(this.choicesSelector);
		installListeners(this.choicesSelector);
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

		uninstallListeners((ChoicesSelector) c);
		uninstallComponents((ChoicesSelector) c);
		uninstallDefaults((ChoicesSelector) c);
		this.choicesSelector = null;
	}

	protected void installDefaults(ChoicesSelector selector) {
		selector.setFont(selector.getOwnerBar().getFont());
		selector.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		selector.setFocusable(true);
		selector.setOpaque(false);
		selector.setForeground(selector.getOwnerBar().getForeground());
	}

	protected void installComponents(ChoicesSelector selector) {
	}

	protected void installListeners(final ChoicesSelector selector) {

		this.baseMouseListener = new MouseAdapter() {
			private boolean showPopup(final ChoicesSelector sel) {
				// // get path before this element
				// int index = sel.getBreadcrumbChoices().getIndex();
				// // reload it
				// BreadcrumbItemChoices abic = sel.getOwnerBar().getCallback()
				// .getChoices(selector.getOwnerBar().getPath(index));
				// if (abic != null) {
				// abic.setIndex(index);
				// sel.setBreadcrumbChoices(abic);
				// }
				return sel.getOwnerBar().getUI().popup(selector.getIndex());
				// return sel.getOwnerBar().showPopup(selector.getIndex(),
				// selector.getBreadcrumbChoices());
			}

			@Override
			public void mousePressed(MouseEvent e) {
				selector.getModel().setPressed(true);
				selector.getModel().setSelected(true);
				synchronizeWithParticle();
				selector.repaint();
				JBreadcrumbBar bar = selector.getOwnerBar();
				int index = bar.getUI().getPopupInitiatorIndex();
				if (index < 0) {
					boolean hasPopup = this.showPopup(selector);
					if (!hasPopup) {
						selector.getModel().setSelected(false);
						selector.repaint();
					}
				}
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				selector.getModel().setRollover(true);
				synchronizeWithParticle();
				selector.getOwnerBar().repaint();
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						JBreadcrumbBar bar = selector.getOwnerBar();
						int index = bar.getUI().getPopupInitiatorIndex();
						if (index >= 0) {
							if (selector.getIndex() != index) {
								// hide old popup and simulate mouse click
								bar.getUI().hidePopup();
								selector.getModel().setSelected(true);
								synchronizeWithParticle();
								if (!showPopup(selector)) {
									selector.getModel().setSelected(false);
									synchronizeWithParticle();
								}
							}
						}
					}
				});
			}

			@Override
			public void mouseExited(MouseEvent e) {
				selector.getModel().setRollover(false);
				synchronizeWithParticle();
				selector.getOwnerBar().repaint();
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				selector.getModel().setPressed(false);
				synchronizeWithParticle();
				selector.getOwnerBar().repaint();
			}
		};
		selector.addMouseListener(this.baseMouseListener);

		this.baseKeyListener = new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (selector.getModel().isArmed()
						&& e.getKeyCode() == KeyEvent.VK_DOWN
						|| e.getKeyCode() == KeyEvent.VK_SPACE
						|| e.getKeyCode() == KeyEvent.VK_ENTER) {
					selector.getModel().setSelected(true);
					selector.repaint();
					if (!selector.getOwnerBar().getUI().popup(
							selector.getIndex())) {
						// if
						// (!selector.getOwnerBar().getUI().showPopup(selector.
						// getIndex(),
						// selector.getBreadcrumbChoices())) {
						selector.getModel().setSelected(false);
						selector.repaint();
					}
				}
			}
		};
		selector.addKeyListener(this.baseKeyListener);

		this.basePopupMenuListener = new PopupMenuListener() {
			public void popupMenuCanceled(PopupMenuEvent e) {
				selector.getModel().setSelected(false);
				selector.getModel().setRollover(false);
				synchronizeWithParticle();
			}

			public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
				selector.getModel().setSelected(false);
				selector.getModel().setRollover(false);
				synchronizeWithParticle();
			}

			public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
			}
		};
		selector.getOwnerBar().getUI().getPopup().addPopupMenuListener(
				this.basePopupMenuListener);

	}

	protected void uninstallDefaults(ChoicesSelector selector) {
	}

	protected void uninstallComponents(ChoicesSelector selector) {
	}

	protected void uninstallListeners(ChoicesSelector selector) {
		selector.removeKeyListener(this.baseKeyListener);
		this.baseKeyListener = null;
		selector.removeMouseListener(this.baseMouseListener);
		this.baseMouseListener = null;
		selector.getOwnerBar().getUI().getPopup().removePopupMenuListener(
				this.basePopupMenuListener);
		this.basePopupMenuListener = null;
	}

	protected BreadcrumbParticle synchronizeWithParticle() {
		JBreadcrumbBar bar = this.choicesSelector.getOwnerBar();
		BreadcrumbBarUI barUI = bar.getUI();
		BreadcrumbParticle particle = barUI.getParticle(this.choicesSelector
				.getIndex());
		if (particle != null) {
			particle.getModel().setArmed(choicesSelector.getModel().isArmed());
			particle.getModel().setSelected(
					choicesSelector.getModel().isSelected());
			particle.getModel().setRollover(
					choicesSelector.getModel().isRollover());
			particle.getModel().setPressed(
					choicesSelector.getModel().isPressed());
			choicesSelector.getOwnerBar().repaint();
		}
		return particle;
	}

	/**
	 * Invoked by <code>installUI</code> to create a layout manager object to
	 * manage the {@link ChoicesSelector}.
	 * 
	 * @return a layout manager object
	 * 
	 * @see ChoicesSelectorLayout
	 */
	protected LayoutManager createLayoutManager() {
		return new ChoicesSelectorLayout();
	}

	/**
	 * Layout for the choices selector.
	 * 
	 * @author Kirill Grouchnikov
	 */
	protected static class ChoicesSelectorLayout implements LayoutManager {
		/**
		 * Creates new layout manager.
		 */
		public ChoicesSelectorLayout() {
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.awt.LayoutManager#addLayoutComponent(java.lang.String,
		 * java.awt.Component)
		 */
		public void addLayoutComponent(String name, Component c) {
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.awt.LayoutManager#removeLayoutComponent(java.awt.Component)
		 */
		public void removeLayoutComponent(Component c) {
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.awt.LayoutManager#preferredLayoutSize(java.awt.Container)
		 */
		public Dimension preferredLayoutSize(Container c) {
			return new Dimension(15, 16);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.awt.LayoutManager#minimumLayoutSize(java.awt.Container)
		 */
		public Dimension minimumLayoutSize(Container c) {
			return this.preferredLayoutSize(c);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.awt.LayoutManager#layoutContainer(java.awt.Container)
		 */
		public void layoutContainer(Container c) {
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.plaf.ComponentUI#paint(java.awt.Graphics,
	 * javax.swing.JComponent)
	 */
	@Override
	public void paint(Graphics g, JComponent c) {
		Graphics2D graphics = (Graphics2D) g.create();
		graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_OFF);

		JBreadcrumbBar bar = this.choicesSelector.getOwnerBar();
		BreadcrumbBarUI barUI = bar.getUI();
		BreadcrumbParticle particle = barUI.getParticle(this.choicesSelector
				.getIndex());

		this.paintBackground(graphics, c, particle);
		this.paintForeground(graphics, c);

		graphics.dispose();
	}

	protected void paintBackground(Graphics2D graphics, JComponent c,
			BreadcrumbParticle particle) {
		ChoicesSelector selector = (ChoicesSelector) c;
		boolean toMark = selector.getModel().isArmed()
				|| selector.getModel().isRollover()
				|| selector.getModel().isSelected()
				|| selector.getModel().isPressed();
		if (!toMark)
			return;
		int width = c.getWidth();
		Color boxColor = this.choicesSelector.getForeground();
		graphics.setColor(boxColor);
		graphics.drawRect(0, 0, width - 2, width - 1);
	}

	protected void paintForeground(Graphics2D graphics, JComponent c) {
		ChoicesSelector selector = (ChoicesSelector) c;
		if (selector.getModel().isSelected() || selector.getModel().isPressed()) {
			this.paintPressedState(graphics, c);
		} else {
			this.paintRegularState(graphics, c);
		}
	}

	protected void paintRegularState(Graphics2D graphics, JComponent c) {
		int width = c.getWidth();
		graphics.setColor(this.choicesSelector.getForeground());
		graphics.fillPolygon(new int[] { 3 * width / 8, 5 * width / 8,
				3 * width / 8 }, new int[] { width / 4, width / 2,
				3 * width / 4 }, 3);
	}

	protected void paintPressedState(Graphics2D graphics, JComponent c) {
		int width = c.getWidth();
		graphics.rotate(Math.PI / 2.0, width / 2.0, width / 2.0);
		graphics.setColor(this.choicesSelector.getForeground());
		graphics.fillPolygon(new int[] { 3 * width / 8, 5 * width / 8,
				3 * width / 8 }, new int[] { width / 4, width / 2,
				3 * width / 4 }, 3);
	}
}