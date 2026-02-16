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
package org.jvnet.flamingo.common.ui;

import java.awt.*;

import javax.swing.JSeparator;

import org.jvnet.flamingo.common.AbstractCommandButton;
import org.jvnet.flamingo.utils.FlamingoUtilities;

public class CommandButtonLayoutManagerCustom extends
		CommandButtonLayoutManagerBig {

	public CommandButtonLayoutManagerCustom(AbstractCommandButton commandButton) {
		super(commandButton);
	}

	@Override
	public int getPreferredIconSize() {
		return -1;
	}

	@Override
	public Dimension getPreferredSize(AbstractCommandButton commandButton) {
		Insets borderInsets = commandButton.getInsets();
		int bx = borderInsets.left + borderInsets.right;
		int by = borderInsets.top + borderInsets.bottom;
		FontMetrics fm = commandButton.getFontMetrics(commandButton.getFont());
		JSeparator jsep = new JSeparator(JSeparator.HORIZONTAL);
		int layoutHGap = FlamingoUtilities.getHLayoutGap(commandButton);
		int layoutVGap = FlamingoUtilities.getVLayoutGap(commandButton);

		int title1Width = fm.stringWidth(this.titlePart1);
		int title2Width = fm.stringWidth(this.titlePart2);

		int width = Math.max(commandButton.getIcon().getIconWidth(), Math
				.max(title1Width,
						title2Width
								+ 4
								* layoutHGap
								+ jsep.getPreferredSize().width
								+ (FlamingoUtilities
										.hasPopupAction(commandButton) ? 1 + fm
										.getHeight() / 2 : 0)));

		int height = by + commandButton.getIcon().getIconHeight() + layoutVGap
				+ jsep.getPreferredSize().height;
		if ((commandButton.getText() != null)
				&& (commandButton.getText().length() > 0)) {
			height += (2 * fm.getAscent() + fm.getDescent());
		}
		return new Dimension(bx + width, height);
	}
}
