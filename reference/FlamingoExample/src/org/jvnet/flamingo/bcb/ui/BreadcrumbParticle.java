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
 * Created on Nov 10, 2003
 */
package org.jvnet.flamingo.bcb.ui;

import javax.swing.*;
import javax.swing.plaf.LabelUI;

import org.jvnet.flamingo.bcb.*;
import org.jvnet.flamingo.bcb.JBreadcrumbBar.BreadcrumbBarElement;

/**
 * Breadcrumb particle.
 * 
 * @author Kirill Grouchnikov
 */
public class BreadcrumbParticle<T> extends JLabel implements
		BreadcrumbBarElement {
	/**
	 * Breadcrumb item.
	 */
	private BreadcrumbItem<T> item = null;

	/**
	 * Model.
	 */
	private ButtonModel model;

	/**
	 * The associated breadcrumb bar.
	 */
	protected JBreadcrumbBar<T> bar;

	/**
	 * The UI class ID string.
	 */
	public static final String uiClassID = "BreadcrumbParticleUI";

	/**
	 * Creates a new breadcrumb particle.
	 * 
	 * @param bar
	 *            Breadcrumb bar.
	 * @param bi
	 *            Breadcrumb item.
	 * @param width
	 *            Width.
	 */
	public BreadcrumbParticle(JBreadcrumbBar<T> bar, BreadcrumbItem<T> bi,
			int width) {
		super();

		this.bar = bar;
		setText(bi.getKey());
		setIcon(bi.getIcon());
		setFocusable(true);
		item = bi;

		setHorizontalAlignment(SwingConstants.LEADING);
		setOpaque(false);
		setBorder(BorderFactory.createEmptyBorder());

		this.model = new DefaultButtonModel();

		this.updateUI();
	}

	public int getIndex() {
		return item.getIndex();
	}

	/**
	 * Validates the path.
	 */
	public void onSelection() {
		BreadcrumbBarModel<T> barModel = this.bar.getModel();
		int itemIndex = barModel.indexOf(item);
		int toLeave = (itemIndex < 0) ? 0 : itemIndex + 1;
		barModel.setCumulative(true);
		while (barModel.getItemCount() > toLeave) {
			barModel.removeLast();
		}
		barModel.setCumulative(false);
		//this.bar.getModel().replaceAllAfter(item);
	}

	/**
	 * Sets the new UI delegate.
	 * 
	 * @param ui
	 *            New UI delegate.
	 */
	@Override
	public void setUI(LabelUI ui) {
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
			setUI((LabelUI) UIManager.getUI(this));
		} else {
			setUI(new BasicBreadcrumbParticleUI());
		}
	}

	/**
	 * Returns the UI object which implements the L&F for this component.
	 * 
	 * @return a <code>BreadcrumbBarUI</code> object
	 * @see #setUI
	 */
	@Override
	public LabelUI getUI() {
		return (LabelUI) ui;
	}

	/**
	 * Returns the name of the UI class that implements the L&F for this
	 * component.
	 * 
	 * @return the string "BreadcrumbParticleUI"
	 * @see JComponent#getUIClassID
	 * @see UIDefaults#getUI
	 */
	@Override
	public String getUIClassID() {
		return uiClassID;
	}

	/**
	 * Returns the breadcrumb bar.
	 * 
	 * @return Breadcrumb bar.
	 */
	public JBreadcrumbBar getBar() {
		return bar;
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
