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
package org.jvnet.flamingo.slider.ui;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.ComponentUI;

import org.jvnet.flamingo.slider.FlexiRangeModel;
import org.jvnet.flamingo.slider.JFlexiSlider;

/**
 * Basic UI for flexi slider {@link JFlexiSlider}.
 * 
 * @author Kirill Grouchnikov
 */
public class BasicFlexiSliderUI extends FlexiSliderUI {
	/**
	 * The associated flexi slider.
	 */
	protected JFlexiSlider flexiSlider;

	protected JLabel[] controlPointLabels;

	protected JSlider slider;

	protected CellRendererPane sliderRendererPane;

	protected MouseListener mouseListener;

	protected MouseMotionListener mouseMotionListener;

	protected ChangeListener flexiSliderChangeListener;

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.plaf.ComponentUI#createUI(javax.swing.JComponent)
	 */
	public static ComponentUI createUI(JComponent c) {
		return new BasicFlexiSliderUI();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.plaf.ComponentUI#installUI(javax.swing.JComponent)
	 */
	public void installUI(JComponent c) {
		this.flexiSlider = (JFlexiSlider) c;
		installDefaults();
		installComponents();
		installListeners();

		c.setLayout(createLayoutManager());
		c.setBorder(new EmptyBorder(1, 1, 1, 1));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.plaf.ComponentUI#uninstallUI(javax.swing.JComponent)
	 */
	public void uninstallUI(JComponent c) {
		c.setLayout(null);
		uninstallListeners();
		uninstallComponents();
		uninstallDefaults();

		this.flexiSlider = null;
	}

	public void installDefaults() {

	}

	public void installComponents() {
		int controlPointCount = this.flexiSlider.getControlPointCount();
		this.controlPointLabels = new JLabel[controlPointCount];
		for (int i = 0; i < controlPointCount; i++) {
			this.controlPointLabels[i] = new JLabel(this.flexiSlider
					.getControlPointText(i));
			this.controlPointLabels[i].setIcon(this.flexiSlider
					.getControlPointIcon(i));
			// this.controlPointLabels[i].setBorder(new LineBorder(Color.blue));
			this.flexiSlider.add(this.controlPointLabels[i]);
		}
		this.slider = new JSlider(JSlider.VERTICAL);
		this.slider.setFocusable(false);
		// this.flexiSlider.add(this.slider);

		this.sliderRendererPane = new CellRendererPane();
		this.flexiSlider.add(sliderRendererPane);

	}

	public void installListeners() {
		this.mouseListener = new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				flexiSlider.getModel().setValueIsAdjusting(false);
			}

			@Override
			public void mousePressed(MouseEvent e) {
				flexiSlider.getModel().setValueIsAdjusting(true);
				int y = e.getY();
				FlexiRangeModel.Value modelValue = sliderValueToModelValue(y);
				flexiSlider.setValue(modelValue);
				// the following lines does the "magic" of snapping
				// the slider thumb to control points of discrete ranges.
				slider.setValue(modelValueToSliderValue(modelValue));
			}
		};
		this.flexiSlider.addMouseListener(this.mouseListener);

		this.mouseMotionListener = new MouseMotionAdapter() {
			@Override
			public void mouseDragged(MouseEvent e) {
				flexiSlider.getModel().setValueIsAdjusting(true);
				int y = e.getY();
				FlexiRangeModel.Value modelValue = sliderValueToModelValue(y);
				if (modelValue == null)
					return;
				flexiSlider.setValue(modelValue);
				// the following lines does the "magic" of snapping
				// the slider thumb to control points of discrete ranges.
				slider.setValue(modelValueToSliderValue(modelValue));
			}
		};
		this.flexiSlider.addMouseMotionListener(this.mouseMotionListener);

		this.flexiSliderChangeListener = new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				flexiSlider.repaint();
			}
		};
		this.flexiSlider.getModel().addChangeListener(
				this.flexiSliderChangeListener);
	}

	public void uninstallDefaults() {
		for (JLabel label : this.controlPointLabels)
			this.flexiSlider.remove(label);
		this.controlPointLabels = null;
		this.flexiSlider.remove(this.sliderRendererPane);
		this.sliderRendererPane = null;
	}

	public void uninstallComponents() {

	}

	public void uninstallListeners() {
		this.flexiSlider.removeMouseListener(this.mouseListener);
		this.mouseListener = null;

		this.flexiSlider.removeMouseMotionListener(this.mouseMotionListener);
		this.mouseMotionListener = null;

		this.flexiSlider.getModel().removeChangeListener(
				this.flexiSliderChangeListener);
		this.flexiSliderChangeListener = null;
	}

	@Override
	public void paint(Graphics g, JComponent c) {
		super.paint(g, c);
		this.paintSlider(g);
	}

	protected void paintSlider(Graphics g) {
		Rectangle sliderBounds = sliderRendererPane.getBounds();
		this.sliderRendererPane.paintComponent(g, this.slider,
				this.flexiSlider, sliderBounds.x, sliderBounds.y,
				sliderBounds.width, sliderBounds.height, true);
	}

	protected int modelValueToSliderValue(FlexiRangeModel.Value modelValue) {
		if (modelValue == null)
			return 0;

		// get the range and the model
		FlexiRangeModel.Range range = modelValue.range;
		FlexiRangeModel model = this.flexiSlider.getModel();
		// find the range in the model
		for (int i = 0; i < model.getRangeCount(); i++) {
			if (range.equals(model.getRange(i))) {
				// get locations of control point labels
				int rangeStartLoc = controlPointLabels[i].getY();
				if (i != 0) {
					rangeStartLoc += controlPointLabels[i].getHeight() / 2;
				} else {
					rangeStartLoc += controlPointLabels[i].getHeight();
				}
				int rangeEndLoc = controlPointLabels[i + 1].getY();
				if (i != (model.getRangeCount() - 1)) {
					rangeEndLoc += controlPointLabels[i + 1].getHeight() / 2;
				}
				Insets ins = flexiSlider.getInsets();
				rangeStartLoc -= ins.top;
				rangeEndLoc -= ins.top;

				// apply the range fraction
				int result = rangeStartLoc
						+ (int) (modelValue.rangeFraction * (rangeEndLoc - rangeStartLoc));
				return result;
			}
		}
		return 0;
	}

	protected FlexiRangeModel.Value sliderValueToModelValue(int sliderValue) {
		// get the model
		FlexiRangeModel model = this.flexiSlider.getModel();
		// iterate over the ranges and try to find range that contains the
		// specified
		// value
		for (int i = 0; i < model.getRangeCount(); i++) {
			FlexiRangeModel.Range currRange = model.getRange(i);
			// get slider values that correspond to the control points
			// of this range
			int startSliderValue = this
					.modelValueToSliderValue(new FlexiRangeModel.Value(
							currRange, 0.0));
			int endSliderValue = this
					.modelValueToSliderValue(new FlexiRangeModel.Value(
							currRange, 1.0));
			if ((sliderValue >= endSliderValue)
					&& (sliderValue <= startSliderValue)) {
				// we have a match. Now check whether the range is discrete
				if (currRange.isDiscrete()) {
					// find the closest control point
					int deltaStart = startSliderValue - sliderValue;
					int deltaEnd = sliderValue - endSliderValue;
					if (deltaStart < deltaEnd) {
						// closer to start
						return new FlexiRangeModel.Value(currRange, 0.0);
					} else {
						// closer to end
						return new FlexiRangeModel.Value(currRange, 1.0);
					}
				} else {
					// compute the range fraction
					return new FlexiRangeModel.Value(
							currRange,
							(double) (startSliderValue - sliderValue)
									/ (double) (startSliderValue - endSliderValue));
				}
			}
		}
		return null;
	}

	/**
	 * Invoked by <code>installUI</code> to create a layout manager object to
	 * manage the {@link JFlexiSlider}.
	 * 
	 * @return a layout manager object
	 */
	protected LayoutManager createLayoutManager() {
		return new FlexiSliderLayout();
	}

	/**
	 * Layout for the flexi slider.
	 * 
	 * @author Kirill Grouchnikov
	 */
	protected class FlexiSliderLayout implements LayoutManager {
		/*
		 * (non-Javadoc)
		 * 
		 * @see java.awt.LayoutManager#addLayoutComponent(java.lang.String,
		 *      java.awt.Component)
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
			int width = 0;
			int height = 0;
			Insets ins = c.getInsets();

			JFlexiSlider flexiSlider = (JFlexiSlider) c;
			// get the control point count
			int controlPointCount = flexiSlider.getControlPointCount();
			// the preferred height is the combined height of all labels +
			// vertical gaps in between
			for (int i = 0; i < controlPointCount; i++) {
				height += controlPointLabels[i].getPreferredSize().height;
			}
			height += 4 * (controlPointCount - 1);

			// the preferred width is the width of the slider + the width
			// of the widest label
			int maxLabelWidth = 0;
			for (int i = 0; i < controlPointCount; i++) {
				maxLabelWidth = Math.max(maxLabelWidth, controlPointLabels[i]
						.getPreferredSize().width);
			}

			width = slider.getPreferredSize().width + 4 + maxLabelWidth;

			return new Dimension(width + ins.left + ins.right, height + ins.top
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
			Insets ins = c.getInsets();
			int width = c.getWidth();
			int height = c.getHeight();

			JFlexiSlider flexiSlider = (JFlexiSlider) c;
			// get the model
			FlexiRangeModel model = flexiSlider.getModel();
			// get the number of ranges
			int rangeCount = model.getRangeCount();

			// get the preferred height
			int prefHeight = this.preferredLayoutSize(c).height;

			// compute the extra height for distribution among the
			// contiguous ranges
			int extraHeight = height /*- ins.top - ins.bottom */- prefHeight;

			// start from the first range (bottom to top). If extra height
			// is negative, all ranges get smaller heights (even if it means
			// overlapping icons). If extra height is positive and there are no
			// contiguous ranges, all ranges get bigger heights. If extra
			// height is positive and there is at least one contiguous range,
			// the extra height is distributed among these ranges according to
			// their weights
			double totalContiguousWeight = 0.0;
			for (int i = 0; i < rangeCount; i++) {
				FlexiRangeModel.Range range = model.getRange(i);
				if (!range.isDiscrete()) {
					totalContiguousWeight += range.getWeight();
				}
			}

			int bumpY = ((totalContiguousWeight == 0.0) || (extraHeight < 0.0)) ? (int) (extraHeight / rangeCount)
					: 0;
			// the first control point is at the bottom
			int labelX = slider.getPreferredSize().width + ins.left + 4;
			int currentY = height - ins.bottom
					- controlPointLabels[0].getPreferredSize().height;
			controlPointLabels[0].setBounds(labelX, currentY, width - labelX
					- ins.left - ins.right, controlPointLabels[0]
					.getPreferredSize().height);
			for (int i = 0; i < rangeCount; i++) {
				FlexiRangeModel.Range range = model.getRange(i);
				JLabel endLabel = controlPointLabels[i + 1];
				if (range.isDiscrete()) {
					int deltaY = endLabel.getPreferredSize().height + 4 + bumpY;
					currentY -= deltaY;
					controlPointLabels[i + 1].setBounds(labelX, currentY, width
							- labelX - ins.left - ins.right, endLabel
							.getPreferredSize().height);
				} else {
					double rangeWeight = range.getWeight();
					int currBump = 0;

					if (bumpY < 0.0) {
						currBump = bumpY;
					} else {
						// a little bit of bookkeeping here. If we just use
						// the proportionate fractions, the rounding errors
						// (rounding a double to int always goes down) will
						// result in extra pixels above the last control point
						// label, which will not be properly aligned with the
						// top bound of the slider.
						currBump = (int) (extraHeight * rangeWeight / totalContiguousWeight);
						// reduce the total remaining weight and total remaining
						// extra height. At the last contiguous range the entire
						// remaining height will go to it.
						totalContiguousWeight -= rangeWeight;
						extraHeight -= currBump;
					}
					int deltaY = endLabel.getPreferredSize().height + 4
							+ currBump;
					currentY -= deltaY;
					controlPointLabels[i + 1].setBounds(labelX, currentY, width
							- labelX - ins.left - ins.right, endLabel
							.getPreferredSize().height);
				}
			}

			int firstLabelCenter = controlPointLabels[0].getY()
					+ controlPointLabels[0].getHeight();// / 2;
			int lastLabelCenter = controlPointLabels[rangeCount].getY();
			// + controlPointLabels[rangeCount].getHeight() / 2;
			int sliderHeight = firstLabelCenter - lastLabelCenter;

			// int
			// lastLabel
			// sliderHeight = height - ins.top - ins.bottom;

			sliderRendererPane.setBounds(ins.left, lastLabelCenter, slider
					.getPreferredSize().width, sliderHeight);
			slider.setMinimum(0);
			slider.setMaximum(sliderHeight - 1);
			slider.setInverted(true);
			slider.setValue(modelValueToSliderValue(flexiSlider.getValue()));
		}
	}
}
