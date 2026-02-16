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
 *  
 * Created on Nov 13, 2003
 */
package org.jvnet.flamingo.bcb.ui;

import java.awt.*;
import java.util.*;
import java.util.List;

import javax.swing.*;


/**
 * Scrollable popup. If there are more menus than the size specified, two
 * "scroller menus" will appear at the top and bottom. NOTE: to add menus, only
 * use the method <code>add(Action a)</code> like that:
 * 
 * <code>
 *	JMenuItem item = getPopup().add(action);
 *	if (item != null) {
 *		item.set...;
 *	}
 * </code>
 * Creation date: Nov 13, 2003
 */
public class ScrollablePopup extends JPopupMenu {

	public final static int ITEMS_MAX_NUMBER = 10;

	private int maxSize = -1;

	private List<Action> actions = null;

	private int maxWidth = 0;

	public static final String SELECTED_PROP = "ScrollablePopup.selected";

	/**
	 * The UI class ID string.
	 */
	public static final String uiClassID = "ScrollablePopupUI";

	public ScrollablePopup() {
		this(ITEMS_MAX_NUMBER);
	}

	public ScrollablePopup(int max) {
		super();
		maxSize = max;
		actions = new Vector<Action>();
	}

	public void removeAll() {
		super.removeAll();
		actions.clear();
		maxWidth = 0;
	}

	public JMenuItem add(Action a) {
		return add(a, getFontMetrics(getFont()).stringWidth(
				(String) a.getValue(Action.NAME)));
	}

	private JMenuItem add(Action a, int w) {
		if (getUI().getScrollUp() != null)
			getUI().getScrollUp().setEnabled(false);
		actions.add(a);
		// check max width
		maxWidth = Math.max(w + 20, maxWidth);
		try {
			((JMenuItem) getComponent(2)).setPreferredSize(new Dimension(
					maxWidth, (int) getComponent(2).getPreferredSize()
							.getHeight()));
		} catch (Exception e) {
		}
		if (actions.size() >= maxSize + 1) {
			getUI().addScrollers();
			return null;
		} else {
			JMenuItem item = super.add(a);
			return item;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.JPopupMenu#setVisible(boolean)
	 */
	public void setVisible(boolean b) {
		super.setVisible(b);
		if (!b) {
			getInvoker().repaint();
		} else {
			Point p = getInvoker().getLocationOnScreen();
			// Point me = getLocationOnScreen();
			if (getLocationOnScreen().getY() != p.getY()
					+ getInvoker().getPreferredSize().getHeight()) {
				p
						.translate((int) getInvoker().getPreferredSize()
								.getWidth(), 0);
				setLocation(p);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.JPopupMenu#setSelected(java.awt.Component)
	 */
	public void setSelected(Component sel) {
		if (sel instanceof JLabel) {
			JLabel label = (JLabel) sel;
			String actionName = label.getText();
			Action actionToSel = null;
			for (Iterator iter = actions.iterator(); iter.hasNext();) {
				Action action = (Action) iter.next();
				if (action.getValue(Action.NAME).toString().toLowerCase(
						Locale.ENGLISH).equals(
						actionName.toLowerCase(Locale.ENGLISH))) {
					actionToSel = action;
				}
				action.putValue(SELECTED_PROP, Boolean.FALSE);
			}
			if (actionToSel != null) {
				// Mark selected entry
				actionToSel.putValue(SELECTED_PROP, Boolean.TRUE);
				getUI().scrollTo(actions.indexOf(actionToSel), true);
			}
		} else {
			if (sel instanceof JMenuItem) {
				((JMenuItem) sel).setFont(new Font(sel.getFont().getName(),
						Font.BOLD, sel.getFont().getSize()));
			}
			super.setSelected(sel);
			sel.requestFocus();
		}
	}

	public List<Action> getActions() {
		return Collections.unmodifiableList(actions);
	}

	public int getMaxSize() {
		return maxSize;
	}

	/**
	 * Sets the new UI delegate.
	 * 
	 * @param ui
	 *            New UI delegate.
	 */
	public void setUI(BasicScrollablePopupUI ui) {
		super.setUI(ui);
	}

	/**
	 * Resets the UI property to a value from the current look and feel.
	 * 
	 * @see JComponent#updateUI
	 */
	public void updateUI() {
		if (UIManager.get(getUIClassID()) != null) {
			setUI((BasicScrollablePopupUI) UIManager.getUI(this));
		} else {
			setUI(BasicScrollablePopupUI.createUI(this));
		}
	}

	/**
	 * Returns the UI object which implements the L&F for this component.
	 * 
	 * @return a <code>BasicScrollablePopupUI</code> object
	 * @see #setUI
	 */
	public BasicScrollablePopupUI getUI() {
		return (BasicScrollablePopupUI) ui;
	}

	/**
	 * Returns the name of the UI class that implements the L&F for this
	 * component.
	 * 
	 * @return the string "BasicScrollablePopupUI"
	 * @see JComponent#getUIClassID
	 * @see UIDefaults#getUI
	 */
	public String getUIClassID() {
		return uiClassID;
	}

}
