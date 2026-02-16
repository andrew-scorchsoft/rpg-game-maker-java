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
package org.jvnet.flamingo.common;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;
import java.util.*;

import javax.swing.ButtonGroup;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * Group of command toggle buttons. Unlike the {@link ButtonGroup}, this class
 * operates on buttons and not on button models.
 * 
 * @author Kirill Grouchnikov
 */
public class CommandToggleButtonGroup implements Serializable {
	/**
	 * Contains all group buttons.
	 */
	protected Vector<JCommandToggleButton> buttons;

	/**
	 * Map of registered model change listeners.
	 */
	protected Map<JCommandToggleButton, ChangeListener> modelChangeListeners;

	/**
	 * Property change support to track the registered property change
	 * listeners.
	 */
	private PropertyChangeSupport changeSupport;

	/**
	 * Name of the property change event fired when the group selection is
	 * changed.
	 */
	public static final String SELECTED_PROPERTY = "selected";

	/**
	 * The currently selected button. Can be <code>null</code>.
	 */
	protected JCommandToggleButton selection;

	/**
	 * Creates a new button group.
	 */
	public CommandToggleButtonGroup() {
		this.buttons = new Vector<JCommandToggleButton>();
		this.modelChangeListeners = new HashMap<JCommandToggleButton, ChangeListener>();
	}

	/**
	 * Adds the specified button to the group.
	 * 
	 * @param b
	 *            The button to be added.
	 */
	public void add(final JCommandToggleButton b) {
		if (b == null) {
			return;
		}
		buttons.addElement(b);

		boolean wasSelectionNull = (this.selection == null);
		if (b.getActionModel().isSelected()) {
			if (wasSelectionNull) {
				selection = b;
			} else {
				b.getActionModel().setSelected(false);
			}
		}
		ChangeListener cl = new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				boolean isSelected = b.getActionModel().isSelected();
				setSelected(b, isSelected);
			}
		};

		b.getActionModel().addChangeListener(cl);
		this.modelChangeListeners.put(b, cl);

		if (wasSelectionNull) {
			this.firePropertyChange(SELECTED_PROPERTY, null, b);
		}
	}

	/**
	 * Removes the specified button from the group.
	 * 
	 * @param b
	 *            The button to be removed
	 */
	public void remove(JCommandToggleButton b) {
		if (b == null) {
			return;
		}
		buttons.removeElement(b);
		boolean wasSelected = (b == selection);
		if (wasSelected) {
			selection = null;
		}
		b.getActionModel().removeChangeListener(
				this.modelChangeListeners.get(b));
		this.modelChangeListeners.remove(b);

		if (wasSelected) {
			this.firePropertyChange(SELECTED_PROPERTY, b, null);
		}
	}

	/**
	 * Changes the selected status of the specified button.
	 * 
	 * @param button
	 *            Button.
	 * @param isSelected
	 *            Selection indication.
	 */
	public void setSelected(JCommandToggleButton button, boolean isSelected) {
		if (isSelected && button != null && button != selection) {
			JCommandToggleButton oldSelection = selection;
			selection = button;
			if (oldSelection != null) {
				oldSelection.getActionModel().setSelected(false);
			}
			button.getActionModel().setSelected(true);

			this.firePropertyChange(SELECTED_PROPERTY, oldSelection, button);
		}
	}

	/**
	 * Returns the selected button of this group.
	 * 
	 * @return The selected button of this group. The result can be
	 *         <code>null</code>.
	 */
	public JCommandToggleButton getSelected() {
		return this.selection;
	}

	/**
	 * Clears the selection of this button group.
	 */
	public void clearSelection() {
		JCommandToggleButton oldSelection = selection;

		if (this.selection != null)
			this.selection.getActionModel().setSelected(false);
		this.selection = null;

		this.firePropertyChange(SELECTED_PROPERTY, oldSelection, null);
	}

	/**
	 * Adds the specified property change listener on this button group.
	 * 
	 * @param listener
	 *            Listener to add.
	 */
	public synchronized void addPropertyChangeListener(
			PropertyChangeListener listener) {
		if (listener == null) {
			return;
		}
		if (changeSupport == null) {
			changeSupport = new PropertyChangeSupport(this);
		}
		changeSupport.addPropertyChangeListener(listener);
	}

	/**
	 * Removes the specified property change listener from this button group.
	 * 
	 * @param listener
	 *            Listener to remove.
	 */
	public synchronized void removePropertyChangeListener(
			PropertyChangeListener listener) {
		if (listener == null || changeSupport == null) {
			return;
		}
		changeSupport.removePropertyChangeListener(listener);
	}

	/**
	 * Fires a property change event on all registered listeners.
	 * 
	 * @param propertyName
	 *            Name of the changed property.
	 * @param oldValue
	 *            Old property value.
	 * @param newValue
	 *            New property value.
	 */
	protected void firePropertyChange(String propertyName, Object oldValue,
			Object newValue) {
		PropertyChangeSupport changeSupport = this.changeSupport;
		if (changeSupport == null || oldValue == newValue) {
			return;
		}
		changeSupport.firePropertyChange(propertyName, oldValue, newValue);
	}

}
