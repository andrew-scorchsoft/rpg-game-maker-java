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
package org.jvnet.flamingo.slider;

import javax.swing.*;

import org.jvnet.flamingo.slider.FlexiRangeModel.Range;
import org.jvnet.flamingo.slider.ui.BasicFlexiSliderUI;
import org.jvnet.flamingo.slider.ui.FlexiSliderUI;

/**
 * 
 * @author Kirill Grouchnikov
 */
public class JFlexiSlider extends JComponent {
	/**
	 * The UI class ID string.
	 */
	public static final String uiClassID = "FlexiSliderUI";

	/**
	 * Sets the new UI delegate.
	 * 
	 * @param ui
	 *            New UI delegate.
	 */
	public void setUI(FlexiSliderUI ui) {
		super.setUI(ui);
	}

	/**
	 * Resets the UI property to a value from the current look and feel.
	 * 
	 * @see JComponent#updateUI
	 */
	public void updateUI() {
		if (UIManager.get(getUIClassID()) != null) {
			setUI((FlexiSliderUI) UIManager.getUI(this));
		} else {
			setUI(new BasicFlexiSliderUI());
		}
	}

	/**
	 * Returns the UI object which implements the L&F for this component.
	 * 
	 * @return UI object which implements the L&F for this component.
	 * @see #setUI
	 */
	public FlexiSliderUI getUI() {
		return (FlexiSliderUI) ui;
	}

	/**
	 * Returns the name of the UI class that implements the L&F for this
	 * component.
	 * 
	 * @return The name of the UI class that implements the L&F for this
	 *         component.
	 * @see JComponent#getUIClassID
	 * @see UIDefaults#getUI
	 */
	public String getUIClassID() {
		return uiClassID;
	}

	protected FlexiRangeModel model;

	private Icon[] controlPointIcons;

	private String[] controlPointTexts;

	public JFlexiSlider(Range[] ranges, Icon[] controlPointIcons,
			String[] controlPointTexts) throws NullPointerException,
			IllegalArgumentException {
		if ((ranges == null) || (controlPointIcons == null)
				|| (controlPointTexts == null))
			throw new NullPointerException("Parameters should be non-null");

		// check parameter sizes
		int rangeCount = ranges.length;
		if ((rangeCount != (controlPointIcons.length - 1))
				|| (rangeCount != (controlPointTexts.length - 1)))
			throw new IllegalArgumentException("Parameter sizes don't match");

		this.model = new DefaultFlexiRangeModel();
		this.model.setRanges(ranges);

		this.controlPointIcons = new Icon[controlPointIcons.length];
		// defensive array copy - so that changes in the application code
		// will not be reflected in the control
		for (int i = 0; i < controlPointIcons.length; i++) {
			this.controlPointIcons[i] = controlPointIcons[i];
		}

		this.controlPointTexts = new String[controlPointTexts.length];
		// defensive array copy - so that changes in the application code
		// will not be reflected in the control
		for (int i = 0; i < controlPointTexts.length; i++) {
			this.controlPointTexts[i] = controlPointTexts[i];
		}

		this.updateUI();
	}

	public int getControlPointCount() {
		return this.model.getRangeCount() + 1;
	}

	public Icon getControlPointIcon(int controlPointIndex) {
		return this.controlPointIcons[controlPointIndex];
	}

	public String getControlPointText(int controlPointIndex) {
		return this.controlPointTexts[controlPointIndex];
	}

	public FlexiRangeModel getModel() {
		return this.model;
	}

	public FlexiRangeModel.Value getValue() {
		return this.model.getValue();
	}

	public void setValue(FlexiRangeModel.Value value) {
		FlexiRangeModel m = getModel();
		FlexiRangeModel.Value oldValue = m.getValue();
		if (value.equals(oldValue)) {
			return;
		}
		m.setValue(value);
	}
}
