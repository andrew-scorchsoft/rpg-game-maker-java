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
import javax.swing.event.MenuKeyEvent;
import javax.swing.event.MenuKeyListener;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicPopupMenuUI;

/**
 * Basic UI for scrollable popup ({@link ScrollablePopup}).
 * 
 * @author Topologi
 * @author Kirill Grouchnikov
 */
public class BasicScrollablePopupUI extends BasicPopupMenuUI {
	protected JMenuItem scrollDown;

	protected JMenuItem scrollUp;

	// protected boolean hasScrollers = false;

	protected boolean isScrolling = false;

	protected boolean isPressed = false;

	protected MenuKeyListener menuKeyListener;

	protected MouseWheelListener baseMouseWheelListener;

	protected static final BasicScrollablePopupUI INSTANCE = new BasicScrollablePopupUI();

	public BasicScrollablePopupUI() {
		super();
		// System.out.println("Installing components");
		this.scrollUp = this.createScroller(true);
		this.scrollDown = this.createScroller(false);
		// System.out.println("Up " + this.scrollUp.hashCode() + ", down "
		// + this.scrollDown.hashCode());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.plaf.ComponentUI#createUI(javax.swing.JComponent)
	 */
	public static ComponentUI createUI(JComponent c) {
		return INSTANCE;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.plaf.ComponentUI#installUI(javax.swing.JComponent)
	 */
	@Override
	public void installUI(JComponent c) {
		super.installUI(c);
		installComponents();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.plaf.ComponentUI#uninstallUI(javax.swing.JComponent)
	 */
	@Override
	public void uninstallUI(JComponent c) {
		uninstallComponents();
		super.uninstallUI(c);
	}

	protected void installComponents() {
		// this.hasScrollers = false;
	}

	@Override
	protected void installListeners() {
		super.installListeners();
		this.baseMouseWheelListener = new MouseWheelListener() {
			public void mouseWheelMoved(MouseWheelEvent e) {
				boolean hasScrollers = hasScrollers();
				// System.out.println("Scrollers : " + hasScrollers);
				if (!hasScrollers)
					return;
				boolean isScrollUp = (e.getWheelRotation() < 0);
				boolean canScroll = (isScrollUp && scrollUp.isEnabled())
						|| (!isScrollUp && scrollDown.isEnabled());
				if (canScroll)
					scroll(isScrollUp);
			}
		};
		this.popupMenu.addMouseWheelListener(this.baseMouseWheelListener);
	}

	@Override
	protected void uninstallDefaults() {
	}

	protected void uninstallComponents() {
		// if (this.scrollUp.getParent() != null)
		// this.scrollUp.getParent().remove(this.scrollUp);
		// this.scrollUp = null;
		// if (this.scrollDown.getParent() != null)
		// this.scrollDown.getParent().remove(this.scrollDown);
		// this.scrollDown = null;
	}

	@Override
	protected void uninstallListeners() {
		super.uninstallListeners();
		this.popupMenu.removeMouseWheelListener(this.baseMouseWheelListener);
		this.baseMouseWheelListener = null;
	}

	protected JMenuItem createScroller(final boolean up) {
		// System.out.println("Creating scroller");
		final JMenuItem scroller = new JMenuItem("") {
			@Override
			public void paint(Graphics g) {
				super.paint(g);
				paintArrow(g, getWidth(), getHeight(),
						isEnabled() ? getForeground() : Color.lightGray, up);
			}

			@Override
			public void menuSelectionChanged(boolean isIncluded) {
				// System.out.println("Menu selection on @" + this.hashCode()
				// + (up ? " up" : " down"));
				super.menuSelectionChanged(isIncluded);
				if (isIncluded) {
					scrollAction(this);
				} else {
					isPressed = false;
				}
			}
		};

		// scroller.setForeground(this.popupMenu.getForeground());
		scroller.setEnabled(false);
		scroller.addMenuKeyListener(getMenuListener());
		scroller.setOpaque(false);
		scroller.setBorder(BorderFactory.createEmptyBorder());
		scroller.setSize(new Dimension(0, 16));
		scroller.setPreferredSize(new Dimension(0, 16));
		return scroller;
	}

	private void paintArrow(Graphics g, int w, int h, Color color, boolean up) {
		g.setColor(color);
		int[] xs = new int[] { w / 2 - 4, w / 2 + 4, w / 2 };
		int[] ys = up ? new int[] { h / 2 + 2, h / 2 + 2, h / 2 - 2 }
				: new int[] { h / 2 - 2, h / 2 - 2, h / 2 + 2 };
		g.fillPolygon(xs, ys, 3);
	}

	protected void scrollAction(Object source) {
		if (!(source instanceof AbstractButton))
			return;
		AbstractButton b = (AbstractButton) source;
		// System.out.println("In scrollAction enabled is " + b.isEnabled());
		if (!b.isEnabled())
			return;
		final boolean up = b.equals(this.scrollUp);
		isPressed = true;
		Timer timer = new Timer(200, new ActionListener() {
			int i = 0;
			@Override
			public void actionPerformed(ActionEvent e) {
				Timer src = (Timer)e.getSource();
				if (!isPressed) {
					src.stop();
					isScrolling = false;
					return;
				}
				isPressed = scroll(up);
				if (i > 3)
					src.setDelay(Math.max(50, src.getDelay() - 30));
				i++;
			}
		});
		timer.setRepeats(true);
		if (isScrolling)
			return;
		isScrolling = true;
		timer.start();
//		
//		Thread scroller = new Thread() {
//			@Override
//			public void run() {
//				if (isScrolling)
//					return;
//				isScrolling = true;
//				long delay = 200;
//				int i = 0;
//				while (isPressed) {
//					isPressed = scroll(up);
//					try {
//						Thread.sleep(delay);
//					} catch (InterruptedException e1) {
//					}
//					if (i > 3)// && i % 2 != 0)
//						delay = Math.max(50, delay - 30);
//					i++;
//				}
//				isScrolling = false;
//			}
//		};
//		scroller.start();
	}

	// private void scrollAllTheWay(boolean up) {
	// ScrollablePopup scrollablePopup = (ScrollablePopup)this.popupMenu;
	// scrollTo(up ? 0 : scrollablePopup.getActions().size(), false);
	// }
	//
	public void scrollTo(int ind, boolean select) {
		ScrollablePopup scrollablePopup = (ScrollablePopup) this.popupMenu;
		if (!hasScrollers()) {
			scrollablePopup.setSelected(scrollablePopup.getComponent(ind));
			return;
		}
		int scrollTo = ind;
		if (ind + scrollablePopup.getMaxSize() > scrollablePopup.getActions()
				.size())
			ind = scrollablePopup.getActions().size()
					- scrollablePopup.getMaxSize();
		this.scrollUp.setEnabled(ind != 0);
		// System.out.println("Up is " + this.scrollUp.isEnabled());
		JMenuItem itemToSel = null;
		for (int i = 1; i < scrollablePopup.getMaxSize() + 1
				&& ind < scrollablePopup.getActions().size(); i++) {
			JMenuItem item = ((JMenuItem) scrollablePopup.getComponent(i));
			if (ind == scrollTo)
				itemToSel = item;
			Action action = scrollablePopup.getActions().get(ind++);
			item.setAction(action);
			// item.setName((String)action.getValue(Action.NAME));
			item.setFont(scrollablePopup.getFont());
		}
		this.scrollDown.setEnabled(ind < scrollablePopup.getActions().size());
		if (select && itemToSel != null)
			scrollablePopup.setSelected(itemToSel);
		Thread.yield();
		scrollablePopup.repaint();
	}

	private boolean scroll(boolean up) {
		ScrollablePopup scrollablePopup = (ScrollablePopup) this.popupMenu;
		// int selectedInd = scrollablePopup.getSelectionModel()
		// .getSelectedIndex();

		JMenuItem first = (JMenuItem) scrollablePopup.getComponent(1);
		int ind = scrollablePopup.getActions().indexOf(first.getAction());
		// scroll it
		int delta = (up ? -1 : 1);
		ind += delta;
		if (ind < 0) {
			this.scrollUp.setEnabled(false);
			return false;
		} else {
			this.scrollUp.setEnabled(true);
		}
		for (int i = 1; i < scrollablePopup.getMaxSize() + 1
				&& ind < scrollablePopup.getActions().size(); i++) {
			JMenuItem item = ((JMenuItem) scrollablePopup.getComponent(i));
			Action action = scrollablePopup.getActions().get(ind++);
			item.setAction(action);
			item.setFont(scrollablePopup.getFont());
			// maintain the selected entry
			if (Boolean.TRUE.equals(action
					.getValue(ScrollablePopup.SELECTED_PROP)))
				scrollablePopup.setSelected(item);
		}
		boolean stopNow = ind == scrollablePopup.getActions().size();
		this.scrollDown.setEnabled(!stopNow);
		return !stopNow;
	}

	protected MenuKeyListener getMenuListener() {
		if (menuKeyListener == null) {
			menuKeyListener = new MenuKeyListener() {
				/*
				 * (non-Javadoc)
				 * 
				 * @see
				 * javax.swing.event.MenuKeyListener#menuKeyPressed(javax.swing
				 * .event.MenuKeyEvent)
				 */
				public void menuKeyPressed(MenuKeyEvent e) {
					MenuSelectionManager msm = MenuSelectionManager
							.defaultManager();
					MenuElement path[] = msm.getSelectedPath();
					if (path[path.length - 1].equals(scrollDown))
						if (e.getKeyCode() == KeyEvent.VK_DOWN) {
							popupMenu.setSelected(scrollDown);
							scrollDown.requestFocus();
						}
				}

				/*
				 * (non-Javadoc)
				 * 
				 * @see
				 * javax.swing.event.MenuKeyListener#menuKeyReleased(javax.swing
				 * .event.MenuKeyEvent)
				 */
				public void menuKeyReleased(MenuKeyEvent e) {
				}

				/*
				 * (non-Javadoc)
				 * 
				 * @see
				 * javax.swing.event.MenuKeyListener#menuKeyTyped(javax.swing
				 * .event.MenuKeyEvent)
				 */
				public void menuKeyTyped(MenuKeyEvent e) {
				}
			};
		}
		return menuKeyListener;
	}

	public boolean hasScrollers() {
		// Component parent = this.scrollUp.getParent();

		// System.out.println(this.scrollUp.hashCode() + ":"
		// + ((parent == null) ? "null" : parent.hashCode()) + ":"
		// + this.popupMenu);
		return (this.scrollUp.getParent() == this.popupMenu);
	}

	public JMenuItem getScrollDown() {
		return scrollDown;
	}

	public JMenuItem getScrollUp() {
		return scrollUp;
	}

	public void addScrollers() {
		if (!this.hasScrollers()) {
			// System.out.println("Adding scroller");
			popupMenu.add(this.scrollDown);
			this.scrollDown.setEnabled(true);
			popupMenu.insert(this.scrollUp, 0);
			// this.hasScrollers = true;
			// System.out.println("Up " + this.scrollUp.hashCode() + ", down "
			// + this.scrollDown.hashCode() + ", parent" +
			// popupMenu.hashCode());
			// System.out.println("\tHas : " + this.hasScrollers());
		}
	}
}
