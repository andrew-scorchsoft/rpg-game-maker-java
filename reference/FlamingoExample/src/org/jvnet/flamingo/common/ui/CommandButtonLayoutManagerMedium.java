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
import java.beans.PropertyChangeEvent;
import java.util.ArrayList;

import javax.swing.JSeparator;
import javax.swing.SwingConstants;

import org.jvnet.flamingo.common.*;
import org.jvnet.flamingo.common.icon.ResizableIcon;
import org.jvnet.flamingo.utils.FlamingoUtilities;

public class CommandButtonLayoutManagerMedium implements
		CommandButtonLayoutManager {

	@Override
	public int getPreferredIconSize() {
		return 16;
	}

	@Override
	public Dimension getPreferredSize(AbstractCommandButton commandButton) {
		Insets borderInsets = commandButton.getInsets();
		int bx = borderInsets.left + borderInsets.right;
		int by = borderInsets.top + borderInsets.bottom;
		FontMetrics fm = commandButton.getFontMetrics(commandButton.getFont());
		JSeparator jsep = new JSeparator(JSeparator.VERTICAL);

		int titleWidth = fm.stringWidth(commandButton.getText());
		int layoutHGap = FlamingoUtilities.getHLayoutGap(commandButton);

		int width = bx;
		width += this.getPreferredIconSize();
		width += layoutHGap;
		width += jsep.getPreferredSize().width;
		width += layoutHGap;
		width += titleWidth;
		if (FlamingoUtilities.hasPopupAction(commandButton)) {
			width += 2 * layoutHGap;
			width += 1 + fm.getHeight() / 2;
			width += 2 * layoutHGap;
		}

		return new Dimension(width, by
				+ Math.max(this.getPreferredIconSize(), fm.getAscent()
						+ fm.getDescent()));
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
	}

	@Override
	public Point getKeyTipAnchorCenterPoint(AbstractCommandButton commandButton) {
		Insets ins = commandButton.getInsets();
		int height = commandButton.getHeight();
		ResizableIcon buttonIcon = commandButton.getIcon();
		// bottom-right corner of the icon area
		return new Point(ins.left + buttonIcon.getIconWidth(),
				(height + buttonIcon.getIconHeight()) / 2);
	}

	@Override
	public CommandButtonLayoutInfo getLayoutInfo(
			AbstractCommandButton commandButton, Graphics g) {
		CommandButtonLayoutInfo result = new CommandButtonLayoutInfo();

		result.actionClickArea = new Rectangle(0, 0, 0, 0);
		result.popupClickArea = new Rectangle(0, 0, 0, 0);

		Insets ins = commandButton.getInsets();

		result.iconRect = new Rectangle();
		result.popupActionRect = new Rectangle();

		int width = commandButton.getWidth();
		int height = commandButton.getHeight();

		int x = ins.left;
		// int y = ins.top;

		FontMetrics fm = g.getFontMetrics();
		int labelHeight = fm.getAscent() + fm.getDescent();

		JCommandButton.CommandButtonKind buttonKind = (commandButton instanceof JCommandButton) ? ((JCommandButton) commandButton)
				.getCommandButtonKind()
				: JCommandButton.CommandButtonKind.ACTION_ONLY;

		if (buttonKind == JCommandButton.CommandButtonKind.ACTION_ONLY) {
			result.actionClickArea.x = 0;
			result.actionClickArea.y = 0;
			result.actionClickArea.width = width;
			result.actionClickArea.height = height;
			result.isTextInActionArea = true;
		}
		if (buttonKind == JCommandButton.CommandButtonKind.POPUP_ONLY) {
			result.popupClickArea.x = 0;
			result.popupClickArea.y = 0;
			result.popupClickArea.width = width;
			result.popupClickArea.height = height;
			result.isTextInActionArea = false;
		}

		JSeparator jsep = new JSeparator(JSeparator.VERTICAL);
		int layoutHGap = FlamingoUtilities.getHLayoutGap(commandButton);

		ResizableIcon buttonIcon = commandButton.getIcon();

		result.iconRect.x = x;
		result.iconRect.y = (height - buttonIcon.getIconHeight()) / 2;
		result.iconRect.width = buttonIcon.getIconWidth();
		result.iconRect.height = buttonIcon.getIconHeight();

		x += commandButton.getIcon().getIconWidth();
		if (buttonKind == JCommandButton.CommandButtonKind.ACTION_AND_POPUP_MAIN_POPUP) {
			result.actionClickArea.x = 0;
			result.actionClickArea.y = 0;
			result.actionClickArea.width = x + layoutHGap;
			result.actionClickArea.height = height;

			result.popupClickArea.x = x + layoutHGap;
			result.popupClickArea.y = 0;
			result.popupClickArea.width = width - x - layoutHGap;
			result.popupClickArea.height = height;

			result.isTextInActionArea = false;
		}
		x += 2 * layoutHGap + jsep.getPreferredSize().width;

		TextLayoutInfo lineLayoutInfo = new TextLayoutInfo();
		lineLayoutInfo.text = commandButton.getText();
		lineLayoutInfo.textRect = new Rectangle();

		lineLayoutInfo.textRect.x = x;
		lineLayoutInfo.textRect.y = (height - labelHeight) / 2;
		lineLayoutInfo.textRect.width = fm.stringWidth(commandButton.getText());
		lineLayoutInfo.textRect.height = labelHeight;

		result.textLayoutInfoList = new ArrayList<TextLayoutInfo>();
		result.textLayoutInfoList.add(lineLayoutInfo);

		x += (int) fm.getStringBounds(commandButton.getText(), g).getWidth();
		if (buttonKind == JCommandButton.CommandButtonKind.ACTION_AND_POPUP_MAIN_ACTION) {
			result.actionClickArea.x = 0;
			result.actionClickArea.y = 0;
			result.actionClickArea.width = x + layoutHGap;
			result.actionClickArea.height = height;

			result.popupClickArea.x = x + layoutHGap;
			result.popupClickArea.y = 0;
			result.popupClickArea.width = width - x - layoutHGap;
			result.popupClickArea.height = height;

			result.isTextInActionArea = true;
		}
		x += 2 * layoutHGap + jsep.getPreferredSize().width;

		if (FlamingoUtilities.hasPopupAction(commandButton)) {
			result.popupActionRect.x = x;
			result.popupActionRect.y = (height - labelHeight) / 2 - 1;
			result.popupActionRect.width = 1 + labelHeight / 2;
			result.popupActionRect.height = labelHeight + 2;
			x += result.popupActionRect.width;
		}

		x += ins.right;
		if (commandButton.getHorizontalAlignment() == SwingConstants.CENTER) {
			// at this point, we may have extra space left due to more
			// width than was required. Need to move the popup action icon
			// to the right and center the other areas
			int extraMediumSpace = width - x;
			if (extraMediumSpace > 0) {
				if (FlamingoUtilities.hasPopupAction(commandButton)) {
					result.popupActionRect.x += extraMediumSpace;
				}
				if (buttonKind == JCommandButton.CommandButtonKind.ACTION_AND_POPUP_MAIN_ACTION) {
					result.popupClickArea.x += extraMediumSpace;
					result.actionClickArea.width += extraMediumSpace;
				}
				if (buttonKind == JCommandButton.CommandButtonKind.ACTION_AND_POPUP_MAIN_POPUP) {
					result.popupClickArea.x += extraMediumSpace / 2;
					result.actionClickArea.width += extraMediumSpace / 2;
				}
				result.iconRect.x += extraMediumSpace / 2;
				lineLayoutInfo.textRect.x += extraMediumSpace / 2;
			}
		}

		return result;
	}

}
