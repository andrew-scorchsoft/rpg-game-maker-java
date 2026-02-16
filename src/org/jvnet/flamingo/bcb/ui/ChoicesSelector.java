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

import javax.swing.*;

import org.jvnet.flamingo.bcb.JBreadcrumbBar;
import org.jvnet.flamingo.bcb.JBreadcrumbBar.BreadcrumbBarElement;

/**
 * Choices selector for the breadcrumb bar.
 * 
 * @author Kirill Grouchnikov
 */
public class ChoicesSelector extends JComponent implements BreadcrumbBarElement {
	/**
	 * The owner breadcrumb bar.
	 */
	protected JBreadcrumbBar ownerBar;

	/**
	 * The associated choices.
	 */
	protected BreadcrumbItemChoices breadcrumbChoices = null;

	/**
	 * Model.
	 */
	protected ButtonModel model;

	/**
	 * The UI class ID string.
	 */
	public static final String uiClassID = "ChoicesSelectorUI";

	/**
	 * Sets the new UI delegate.
	 * 
	 * @param ui
	 *            New UI delegate.
	 */
	public void setUI(ChoicesSelectorUI ui) {
		super.setUI(ui);
	}

	/**
	 * Resets the UI property to a value from the current look and feel.
	 * 
	 * @see JComponent#updateUI
	 */
	@Override
	public void updateUI() {
		if (UIManager.get(getUIClassID()) != null) {
			setUI((ChoicesSelectorUI) UIManager.getUI(this));
		} else {
			setUI(new BasicChoicesSelectorUI());
		}
	}

	/**
	 * Returns the UI object which implements the L&F for this component.
	 * 
	 * @return a <code>ChoicesSelectorUI</code> object
	 * @see #setUI
	 */
	public ChoicesSelectorUI getUI() {
		return (ChoicesSelectorUI) ui;
	}

	/**
	 * Returns the name of the UI class that implements the L&F for this
	 * component.
	 * 
	 * @return the string "ChoicesSelectorUI"
	 * @see JComponent#getUIClassID
	 * @see UIDefaults#getUI
	 */
	@Override
	public String getUIClassID() {
		return uiClassID;
	}

	/**
	 * Creates a new selector.
	 * 
	 * @param ownerBar
	 *            Owner breadcrumb bar.
	 * @param choices
	 *            Choices.
	 */
	public ChoicesSelector(JBreadcrumbBar ownerBar, BreadcrumbItemChoices choices) {
		super();
		breadcrumbChoices = choices;
		this.ownerBar = ownerBar;

		this.model = new DefaultButtonModel();

		this.updateUI();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jvnet.flamingo.bcb.BreadcrumbBar.BreadcrumbBarElement#getText()
	 */
	public String getText() {
		return "";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jvnet.flamingo.bcb.BreadcrumbBar.BreadcrumbBarElement#getIndex()
	 */
	public int getIndex() {
		return breadcrumbChoices.getIndex();
	}

	/**
	 * Returns the owner breadcrumb bar.
	 * 
	 * @return Owner breadcrumb bar.
	 */
	public JBreadcrumbBar getOwnerBar() {
		return ownerBar;
	}

	/**
	 * Returns the choices.
	 * 
	 * @return Choices.
	 */
	public BreadcrumbItemChoices getBreadcrumbChoices() {
		return breadcrumbChoices;
	}

	/**
	 * Sets the choices.
	 * 
	 * @param bic
	 *            Choices.
	 */
	public void setBreadcrumbChoices(BreadcrumbItemChoices bic) {
		this.breadcrumbChoices = bic;
		// this.breadcrumbChoices.setComponent(this);
	}

	/**
	 * Returns the model.
	 * 
	 * @return Model.
	 */
	public ButtonModel getModel() {
		return model;
	}

}
