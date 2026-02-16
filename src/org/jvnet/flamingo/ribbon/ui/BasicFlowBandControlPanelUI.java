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
import java.util.List;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;

import org.jvnet.flamingo.common.AbstractCommandButton;
import org.jvnet.flamingo.common.CommandButtonDisplayState;
import org.jvnet.flamingo.ribbon.AbstractRibbonBand;
import org.jvnet.flamingo.ribbon.resize.IconRibbonBandResizePolicy;
import org.jvnet.flamingo.ribbon.resize.RibbonBandResizePolicy;
import org.jvnet.flamingo.ribbon.ui.BasicRibbonBandUI.CollapsedButtonPopupPanel;

/**
 * Basic UI for control panel of ribbon band {@link JBandControlPanel}.
 * 
 * @author Kirill Grouchnikov
 */
public class BasicFlowBandControlPanelUI extends AbstractBandControlPanelUI {
	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.plaf.ComponentUI#createUI(javax.swing.JComponent)
	 */
	public static ComponentUI createUI(JComponent c) {
		return new BasicFlowBandControlPanelUI();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jvnet.flamingo.ribbon.ui.AbstractBandControlPanelUI#createLayoutManager
	 * ()
	 */
	protected LayoutManager createLayoutManager() {
		return new FlowControlPanelLayout();
	}

	/**
	 * Layout for the control panel of flow ribbon band.
	 * 
	 * @author Kirill Grouchnikov
	 */
	private class FlowControlPanelLayout implements LayoutManager {

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
			// The height of ribbon band control panel is
			// computed based on the preferred height of a command
			// button in BIG state.
			int buttonHeight = dummy.getPreferredSize().height;

			Insets ins = c.getInsets();
			return new Dimension(c.getWidth(), buttonHeight + ins.top
					+ ins.bottom);
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
			JFlowBandControlPanel flowBandControlPanel = (JFlowBandControlPanel) c;
			AbstractRibbonBand ribbonBand = flowBandControlPanel
					.getRibbonBand();
			RibbonBandResizePolicy currentResizePolicy = ribbonBand
					.getCurrentResizePolicy();
			if (currentResizePolicy == null)
				return;

			// need place for border
			Insets ins = c.getInsets();
			int x = ins.left;
			int gap = getLayoutGap();
			int availableHeight = c.getHeight() - ins.top - ins.bottom;

			if (SwingUtilities.getAncestorOfClass(
					CollapsedButtonPopupPanel.class, c) != null) {
				List<RibbonBandResizePolicy> resizePolicies = ribbonBand
						.getResizePolicies();
				// install the most permissive resize policy on the popup
				// panel of a collapsed ribbon band
				resizePolicies.get(0).install(availableHeight, gap);
			} else {
				if (currentResizePolicy instanceof IconRibbonBandResizePolicy) {
					return;
				}

				// Installs the resize policy
				currentResizePolicy.install(availableHeight, gap);
			}

			// compute the max preferred height of the components and the
			// number of rows
			int maxHeight = 0;
			int rowCount = 1;
			for (JComponent flowComponent : flowBandControlPanel
					.getFlowComponents()) {
				Dimension prefSize = flowComponent.getPreferredSize();
				if ((x + prefSize.width) > (c.getWidth() - ins.right)) {
					x = ins.left;
					rowCount++;
				}
				x += prefSize.width + gap;
				maxHeight = Math.max(maxHeight, prefSize.height);
			}
			// rowCount++;

			int vGap = (availableHeight - rowCount * maxHeight) / rowCount;
			if (vGap < 0) {
				vGap = 2;
				maxHeight = (availableHeight - vGap * (rowCount - 1))
						/ rowCount;
			}
			int y = ins.top + vGap / 2;
			x = ins.left;
			int rowIndex = 0;
			for (JComponent flowComponent : flowBandControlPanel
					.getFlowComponents()) {
				Dimension prefSize = flowComponent.getPreferredSize();
				if ((x + prefSize.width) > (c.getWidth() - ins.right)) {
					x = ins.left;
					y += (maxHeight + vGap);
					rowIndex++;
				}
				int height = Math.min(maxHeight, prefSize.height);
				flowComponent.setBounds(x, y + (maxHeight - height) / 2,
						prefSize.width, height);
				flowComponent.putClientProperty(
						AbstractBandControlPanelUI.TOP_ROW, Boolean
								.valueOf(rowIndex == 0));
				flowComponent.putClientProperty(
						AbstractBandControlPanelUI.MID_ROW, Boolean
								.valueOf((rowIndex > 0)
										&& (rowIndex < (rowCount - 1))));
				flowComponent.putClientProperty(
						AbstractBandControlPanelUI.BOTTOM_ROW, Boolean
								.valueOf(rowIndex == (rowCount - 1)));
				x += prefSize.width + gap;
			}

		}
	}
}
