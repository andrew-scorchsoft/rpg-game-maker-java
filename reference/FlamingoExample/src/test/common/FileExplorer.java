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
package test.common;

import java.awt.BorderLayout;
import java.io.File;
import java.util.List;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.jvnet.flamingo.bcb.*;
import org.jvnet.flamingo.bcb.core.BreadcrumbFileSelector;
import org.jvnet.flamingo.common.StringValuePair;
import org.jvnet.flamingo.slider.FlexiRangeModel;
import org.jvnet.flamingo.slider.JFlexiSlider;

public class FileExplorer extends JFrame {
	private ExplorerFileViewPanel<File> filePanel;

	private BreadcrumbFileSelector bar;

	public FileExplorer() {
		super("File explorer");

		this.bar = new BreadcrumbFileSelector();

		this.setLayout(new BorderLayout());
		this.add(bar, BorderLayout.NORTH);

		this.filePanel = new ExplorerFileViewPanel<File>(bar, 32, null);
		JScrollPane fileListScrollPane = new JScrollPane(this.filePanel);

		this.bar.getModel().addPathListener(new BreadcrumbPathListener() {
			@Override
			public void breadcrumbPathEvent(BreadcrumbPathEvent event) {
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						final List<BreadcrumbItem<File>> newPath = bar
								.getModel().getItems();
						if (newPath.size() > 0) {
							SwingWorker<List<StringValuePair<File>>, Void> worker = new SwingWorker<List<StringValuePair<File>>, Void>() {
								@Override
								protected List<StringValuePair<File>> doInBackground()
										throws Exception {
									return bar.getCallback().getLeafs(newPath);
								}

								@Override
								protected void done() {
									try {
										filePanel.setFolder(get());
									} catch (Exception exc) {
									}
								}
							};
							worker.execute();
						}
						return;
					}
				});
			}
		});

		final FlexiRangeModel.Range rangeSmallMedium = new FlexiRangeModel.Range(
				"Small Icons -> Medium Icons", true, 0.0);
		final FlexiRangeModel.Range rangeMediumLarge = new FlexiRangeModel.Range(
				"Medium Icons -> Large Icons", false, 1.0);
		final FlexiRangeModel.Range rangeLargeVeryLarge = new FlexiRangeModel.Range(
				"Large Icons -> Giant Icons", false, 2.0);

		Icon iconSmallIcons = new ImageIcon(FileExplorer.class.getClassLoader()
				.getResource("test/resource/small-icons.png"));
		Icon iconMediumIcons = new ImageIcon(FileExplorer.class
				.getClassLoader().getResource("test/resource/medium-icons.png"));
		Icon iconLargeIcons = new ImageIcon(FileExplorer.class.getClassLoader()
				.getResource("test/resource/large-icons.png"));
		Icon iconVeryLargeIcons = new ImageIcon(FileExplorer.class
				.getClassLoader().getResource(
						"test/resource/very-large-icons.png"));

		FlexiRangeModel.Range[] ranges = new FlexiRangeModel.Range[] {
				rangeSmallMedium, rangeMediumLarge, rangeLargeVeryLarge };
		Icon[] icons = new Icon[] { iconSmallIcons, iconMediumIcons,
				iconLargeIcons, iconVeryLargeIcons };
		String[] texts = new String[] { "Small Icons", "Medium Icons",
				"Large Icons", "Very Large Icons" };
		final JFlexiSlider sizeSlider = new JFlexiSlider(ranges, icons, texts);

		sizeSlider.setValue(new FlexiRangeModel.Value(rangeSmallMedium, 0.0));

		sizeSlider.getModel().addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				if (sizeSlider.getModel().getValueIsAdjusting())
					return;

				FlexiRangeModel.Value value = sizeSlider.getValue();
				FlexiRangeModel.Range range = value.range;
				double fraction = value.rangeFraction;

				int iconSize = 0;
				if (range == rangeSmallMedium) {
					if (fraction == 0.0)
						iconSize = 32;
					else
						iconSize = 64;
				}
				if (range == rangeMediumLarge) {
					iconSize = (int) (64 + 64 * fraction);
				}
				if (range == rangeLargeVeryLarge) {
					iconSize = (int) (128 + 128 * fraction);
				}

				filePanel.cancelMainWorker();
				filePanel.setIconDimension(iconSize);
			}
		});

		this.add(sizeSlider, BorderLayout.WEST);
		this.add(fileListScrollPane, BorderLayout.CENTER);
	}

	/**
	 * Main method for testing.
	 * 
	 * @param args
	 *            Ignored.
	 */
	public static void main(String... args) throws Exception {
		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				FileExplorer test = new FileExplorer();
				test.setSize(500, 400);
				test.setLocationRelativeTo(null);
				test.setVisible(true);
				test.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			}
		});
	}
}
