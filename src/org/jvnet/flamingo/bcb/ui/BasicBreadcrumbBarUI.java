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

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.UIResource;

import org.jvnet.flamingo.bcb.*;
import org.jvnet.flamingo.common.JCommandButton;
import org.jvnet.flamingo.common.StringValuePair;
import org.jvnet.flamingo.common.ui.BasicCommandButtonUI;
import org.jvnet.flamingo.common.ui.ScrollablePanel;
import org.jvnet.flamingo.common.ui.ScrollablePanel.ScrollTo;
import org.jvnet.flamingo.common.ui.ScrollablePanel.ScrollableSupport;
import org.jvnet.flamingo.utils.DoubleArrowResizableIcon;
import org.jvnet.flamingo.utils.FlamingoUtilities;

/**
 * Basic UI for breadcrumb bar ({@link JBreadcrumbBar}).
 * 
 * @author Topologi
 * @author Kirill Grouchnikov
 * @author Pawel Hajda
 */
public class BasicBreadcrumbBarUI extends BreadcrumbBarUI implements
		ScrollableSupport {
	/**
	 * The associated breadcrumb bar.
	 */
	protected JBreadcrumbBar breadcrumbBar;

	protected JPanel mainPanel;

	protected ScrollablePanel<JPanel> scrollerPanel;

	protected ComponentListener componentListener;

	/**
	 * Contains the item path (particles and selectors).
	 */
	protected Stack stack;

	/**
	 * The index of the popup initiator.
	 */
	protected int popupInitiatorIndex;

	/**
	 * Popup menu.
	 */
	private JPopupMenu popup = null;

	private Map<Object, Component> comps = new HashMap<Object, Component>();

	protected BreadcrumbPathListener pathListener;

	private AtomicInteger atomicCounter;

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.plaf.ComponentUI#createUI(javax.swing.JComponent)
	 */
	public static ComponentUI createUI(JComponent c) {
		return new BasicBreadcrumbBarUI();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.plaf.ComponentUI#installUI(javax.swing.JComponent)
	 */
	@Override
	public void installUI(JComponent c) {
		this.breadcrumbBar = (JBreadcrumbBar) c;

		this.stack = new Stack();

		installDefaults(this.breadcrumbBar);
		installComponents(this.breadcrumbBar);
		installListeners(this.breadcrumbBar);

		c.setLayout(createLayoutManager());
		this.popupInitiatorIndex = -1;

		if (this.breadcrumbBar.getCallback() != null) {
			SwingWorker<List<StringValuePair>, Void> worker = new SwingWorker<List<StringValuePair>, Void>() {
				@Override
				protected List<StringValuePair> doInBackground()
						throws Exception {
					return breadcrumbBar.getCallback().getPathChoices(null);
				}

				@Override
				protected void done() {
					try {
						pushChoices(new BreadcrumbItemChoices(null, get()));
					} catch (Exception exc) {
					}
				}
			};
			worker.execute();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.plaf.ComponentUI#uninstallUI(javax.swing.JComponent)
	 */
	@Override
	public void uninstallUI(JComponent c) {
		c.setLayout(null);

		uninstallListeners((JBreadcrumbBar) c);
		uninstallComponents((JBreadcrumbBar) c);
		uninstallDefaults((JBreadcrumbBar) c);
		this.breadcrumbBar = null;
	}

	protected void installDefaults(JBreadcrumbBar bar) {
		Font currFont = bar.getFont();
		if ((currFont == null) || (currFont instanceof UIResource)) {
			Font font = FlamingoUtilities.getFont(null, "BreadcrumbBar.font",
					"Button.font", "Panel.font");
			bar.setFont(font);
		}
	}

	protected void installComponents(JBreadcrumbBar bar) {
		this.mainPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
		this.mainPanel.setBorder(new EmptyBorder(2, 0, 2, 0));
		this.mainPanel.setOpaque(false);
		this.scrollerPanel = new ScrollablePanel<JPanel>(this.mainPanel, this);

		bar.add(this.scrollerPanel, BorderLayout.CENTER);
	}

	protected void installListeners(final JBreadcrumbBar bar) {
		this.atomicCounter = new AtomicInteger(0);

		this.componentListener = new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				updateComponents();
			}
		};
		bar.addComponentListener(this.componentListener);

		this.pathListener = new BreadcrumbPathListener() {
			private SwingWorker<Void, Object> pathChangeWorker;

			@Override
			public void breadcrumbPathEvent(BreadcrumbPathEvent event) {
				final int indexOfFirstChange = event.getIndexOfFirstChange();

				if ((this.pathChangeWorker != null)
						&& !this.pathChangeWorker.isDone()) {
					this.pathChangeWorker.cancel(true);
				}
				this.pathChangeWorker = new SwingWorker<Void, Object>() {
					@Override
					protected Void doInBackground() throws Exception {
						atomicCounter.incrementAndGet();

						synchronized (stack) {
							// remove stack elements after the first change
							if (indexOfFirstChange == 0) {
								stack.clear();
							} else {
								int toLeave = indexOfFirstChange * 2 + 1;
								while (stack.size() > toLeave)
									stack.pop();
							}
						}

						SwingUtilities.invokeLater(new Runnable() {
							@Override
							public void run() {
								updateComponents();
							}
						});

						if (indexOfFirstChange == 0) {
							List<StringValuePair> rootChoices = breadcrumbBar
									.getCallback().getPathChoices(null);
							BreadcrumbItemChoices bic = new BreadcrumbItemChoices(
									null, rootChoices);
							if (!this.isCancelled()) {
								publish(bic);
							}
						}

						List<BreadcrumbItem> items = breadcrumbBar.getModel()
								.getItems();
						if (items != null) {
							for (int itemIndex = indexOfFirstChange; itemIndex < items
									.size(); itemIndex++) {
								if (this.isCancelled())
									break;

								BreadcrumbItem item = items.get(itemIndex);
								publish(item);

								// now check if it has any children
								List<BreadcrumbItem> subPath = new ArrayList<BreadcrumbItem>();
								for (int j = 0; j <= itemIndex; j++) {
									subPath.add(items.get(j));
								}
								BreadcrumbItemChoices bic = new BreadcrumbItemChoices(
										item, breadcrumbBar.getCallback()
												.getPathChoices(subPath));
								if ((bic.getChoices() != null)
										&& (bic.getChoices().length > 0)) {
									// add the selector - the current item has
									// children
									publish(bic);
								}
							}
						}
						return null;
					}

					@Override
					protected void process(List<Object> chunks) {
						if (chunks != null) {
							for (Object chunk : chunks) {
								if (this.isCancelled()
										|| atomicCounter.get() > 1)
									break;

								if (chunk instanceof BreadcrumbItemChoices) {
									pushChoices((BreadcrumbItemChoices) chunk,
											false);
								}
								if (chunk instanceof BreadcrumbItem) {
									pushChoice((BreadcrumbItem) chunk, false);
								}
							}
						}
						updateComponents();
					}

					@Override
					protected void done() {
						atomicCounter.decrementAndGet();
					}
				};
				pathChangeWorker.execute();
			}
		};
		this.breadcrumbBar.getModel().addPathListener(this.pathListener);
	}

	protected void uninstallDefaults(JBreadcrumbBar bar) {
	}

	protected void uninstallComponents(JBreadcrumbBar bar) {
		this.mainPanel.removeAll();
		this.comps.clear();

		bar.remove(this.scrollerPanel);
	}

	protected void uninstallListeners(JBreadcrumbBar bar) {
		bar.removeComponentListener(this.componentListener);
		this.componentListener = null;

		this.breadcrumbBar.getModel().removePathListener(this.pathListener);
		this.pathListener = null;
	}

	/**
	 * Invoked by <code>installUI</code> to create a layout manager object to
	 * manage the {@link JBreadcrumbBar}.
	 * 
	 * @return a layout manager object
	 * 
	 * @see BreadcrumbBarLayout
	 */
	protected LayoutManager createLayoutManager() {
		return new BreadcrumbBarLayout();
	}

	/**
	 * Returns the popup menu of <code>this</code> breadcrumb bar.
	 * 
	 * @return The popup menu of <code>this</code> breadcrumb bar.
	 */
	@Override
	public JPopupMenu getPopup() {
		if (popup == null) {
			popup = new ScrollablePopup(15);
			popup.setFont(breadcrumbBar.getFont());
			popup.setBorder(BorderFactory.createLineBorder(Color.black));
			popup.addPopupMenuListener(new PopupMenuListener() {
				public void popupMenuCanceled(PopupMenuEvent e) {
					popupInitiatorIndex = -1;
				}

				public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
					popupInitiatorIndex = -1;
				};

				public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
				}
			});
		}
		return popup;
	}

	/**
	 * Hides the popup menu.
	 */
	@Override
	public void hidePopup() {
		getPopup().setVisible(false);
	}

	/**
	 * Layout for the breadcrumb bar.
	 * 
	 * @author Kirill Grouchnikov
	 * @author Topologi
	 */
	protected class BreadcrumbBarLayout implements LayoutManager {
		/**
		 * Creates new layout manager.
		 */
		public BreadcrumbBarLayout() {
		}

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
			return new Dimension(50, 21);
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
			int width = c.getWidth();
			int height = c.getHeight();
			scrollerPanel.setBounds(0, 0, width, height);
		}
	}

	// /**
	// * ScrollablePanel allows to have scrolling buttons on each side.
	// *
	// * @author jb Creation date: Nov 13, 2003
	// */
	// public class ScrollablePanel extends JPanel {
	// private JCommandButton leftScroller;
	//
	// private JCommandButton rightScroller;
	//
	// private JScrollPane scrollPane;
	//
	// private JComponent view = null;
	//
	// private MouseListener scrollMouseListener = null;
	//
	// private int widthToScrollTo = 0;
	//
	// public ScrollablePanel(JComponent c) {
	// super();
	// view = c;
	// this.scrollPane = new JScrollPane();
	// this.scrollPane.setBorder(BorderFactory.createEmptyBorder(2, 0, 2,
	// 0));
	// this.scrollPane.setOpaque(false);
	// this.scrollPane
	// .setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
	// this.scrollPane
	// .setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
	// this.scrollPane.setAutoscrolls(false);
	// this.scrollPane.getViewport().setOpaque(false);
	// this.scrollPane.setViewportView(c);
	// setOpaque(false);
	// add(this.scrollPane);
	//
	// this.leftScroller = createLeftScroller();
	// this.configureLeftScrollerButtonAction();
	//
	// this.rightScroller = createRightScroller();
	// this.configureRightScrollerButtonAction();
	//
	// this.setLayout(new ScrollablePanelLayout());
	// }
	//
	// public void increaseWidthBy(int increaseBy) {
	// validateScrolling(widthToScrollTo + increaseBy);
	// }
	//
	// public void validateScrolling(int width) {
	// widthToScrollTo = width;
	// int visibleWidth = view.getVisibleRect().width - 4;
	// if (visibleWidth > 0 && visibleWidth < widthToScrollTo) {
	// int x;
	// if (getComponentCount() <= 1) {
	// addScrollers();
	// x = widthToScrollTo + this.leftScroller.getWidth()
	// + this.rightScroller.getWidth() - 4;
	// } else {
	// x = widthToScrollTo - 4;
	// }
	// // scroll to last item
	// view.scrollRectToVisible(new Rectangle(x, 0, 4, 4));
	// } else {
	// removeScrollers();
	// }
	// }
	//
	// public void removeScrollers() {
	// view.scrollRectToVisible(new Rectangle(0, 0, 2, 2));
	// remove(this.leftScroller);
	// remove(this.rightScroller);
	// revalidate();
	// repaint();
	// }
	//
	// private void addScrollers() {
	// add(this.leftScroller, BorderLayout.WEST);
	// add(this.rightScroller, BorderLayout.EAST);
	// revalidate();
	// repaint();
	// }
	//
	// protected void configureLeftScrollerButtonAction() {
	// this.leftScroller.setAutoRepeatAction(true);
	// this.leftScroller.setAutoRepeatActionIntervals(200, 50);
	// this.leftScroller.setFireActionOnRollover(true);
	// this.leftScroller.addActionListener(new ActionListener() {
	// @Override
	// public void actionPerformed(ActionEvent e) {
	// int scrollBy = 12;
	// double x = view.getVisibleRect().getX() - scrollBy;
	// if (x < -scrollBy)
	// return;
	// Rectangle rect = new Rectangle((int) x, 0, scrollBy, 8);
	// // scroll the view to see the rectangle
	// view.scrollRectToVisible(rect);
	// }
	// });
	// }
	//
	// protected void configureRightScrollerButtonAction() {
	// this.rightScroller.setAutoRepeatAction(true);
	// this.rightScroller.setAutoRepeatActionIntervals(200, 50);
	// this.rightScroller.setFireActionOnRollover(true);
	// this.rightScroller.addActionListener(new ActionListener() {
	// @Override
	// public void actionPerformed(ActionEvent e) {
	// int scrollBy = 12;
	// double x = view.getVisibleRect().getX()
	// + view.getVisibleRect().getWidth();
	// if (x > widthToScrollTo)
	// return;
	// Rectangle rect = new Rectangle((int) x, 0, scrollBy, 8);
	// // scroll the view to see the rectangle
	// view.scrollRectToVisible(rect);
	// }
	// });
	// }
	// }
	//
	// /**
	// * Layout for the scrollable panel.
	// *
	// * @author Kirill Grouchnikov
	// * @author Topologi
	// */
	// protected class ScrollablePanelLayout implements LayoutManager {
	// /**
	// * Creates new layout manager.
	// */
	// public ScrollablePanelLayout() {
	// }
	//
	// /*
	// * (non-Javadoc)
	// *
	// * @see java.awt.LayoutManager#addLayoutComponent(java.lang.String,
	// * java.awt.Component)
	// */
	// public void addLayoutComponent(String name, Component c) {
	// }
	//
	// /*
	// * (non-Javadoc)
	// *
	// * @see java.awt.LayoutManager#removeLayoutComponent(java.awt.Component)
	// */
	// public void removeLayoutComponent(Component c) {
	// }
	//
	// /*
	// * (non-Javadoc)
	// *
	// * @see java.awt.LayoutManager#preferredLayoutSize(java.awt.Container)
	// */
	// public Dimension preferredLayoutSize(Container c) {
	// return new Dimension(c.getWidth(), 21);
	// }
	//
	// /*
	// * (non-Javadoc)
	// *
	// * @see java.awt.LayoutManager#minimumLayoutSize(java.awt.Container)
	// */
	// public Dimension minimumLayoutSize(Container c) {
	// return this.preferredLayoutSize(c);
	// }
	//
	// /*
	// * (non-Javadoc)
	// *
	// * @see java.awt.LayoutManager#layoutContainer(java.awt.Container)
	// */
	// public void layoutContainer(Container c) {
	// int width = c.getWidth();
	// int height = c.getHeight();
	//
	// ScrollablePanel sPanel = scrollerPanel;
	// boolean isScrollerButtonsShowing = (sPanel.leftScroller.getParent() ==
	// scrollerPanel);
	// int scrollPanelWidth = isScrollerButtonsShowing ? width
	// - sPanel.leftScroller.getPreferredSize().width
	// - sPanel.rightScroller.getPreferredSize().width - 4 : width;
	// int x = 0;
	// if (isScrollerButtonsShowing) {
	// int spw = sPanel.leftScroller.getPreferredSize().width;
	// int sph = sPanel.leftScroller.getPreferredSize().height;
	// sPanel.leftScroller.setBounds(0, (height - sph) / 2, spw, sph);
	// x += spw + 2;
	// }
	// sPanel.scrollPane.setBounds(x, 0, scrollPanelWidth, height);
	// x += scrollPanelWidth + 2;
	// if (isScrollerButtonsShowing) {
	// int spw = sPanel.rightScroller.getPreferredSize().width;
	// int sph = sPanel.rightScroller.getPreferredSize().height;
	// sPanel.rightScroller.setBounds(x, (height - sph) / 2, spw, sph);
	// }
	// }
	// }

	protected synchronized void updateComponents() {
		if (!this.breadcrumbBar.isVisible())
			return;

		this.mainPanel.removeAll();
		comps.clear();
		this.scrollerPanel.removeScrollers();
		// update the ui
		int width = 0;
		JComponent c = null;
		for (Iterator iter = stack.iterator(); iter.hasNext();) {
			Object element = iter.next();
			if (element instanceof BreadcrumbItemChoices) {
				BreadcrumbItemChoices bic = (BreadcrumbItemChoices) element;
				c = new ChoicesSelector(this.breadcrumbBar, bic);
				comps.put(bic, c);
			} else if (element instanceof BreadcrumbItem) {
				BreadcrumbItem bi = (BreadcrumbItem) element;
				c = new BreadcrumbParticle(this.breadcrumbBar, bi,
						this.breadcrumbBar.getFontMetrics(
								this.breadcrumbBar.getFont()).stringWidth(
								bi.getKey()));
				comps.put(bi, c);
			}
			width += c.getPreferredSize().getWidth();
			this.mainPanel.add(c);
		}
		this.scrollerPanel.validateScrolling(width, ScrollTo.LAST);
		this.mainPanel.validate();
		this.mainPanel.repaint();
		if (c != null && stack.lastElement() instanceof BreadcrumbItem)
			c.requestFocus();
	}

	@Override
	public boolean popup(int componentIndex) {
		// try to show popup
		if (componentIndex < 0
				|| componentIndex >= this.mainPanel.getComponentCount())
			return false;
		Component c = mainPanel.getComponent(componentIndex);
		if (c != null && c instanceof ChoicesSelector) {
			ChoicesSelector selector = (ChoicesSelector) c;
			return this.showPopup(selector.getIndex(), selector
					.getBreadcrumbChoices());
		}
		return false;
	}

	/**
	 * Shows the popup menu based on the specified parameters.
	 * 
	 * @param selectorIndex
	 *            The index of the originating selector.
	 * @param bic
	 *            The choice element.
	 * @return If <code>true</code>, the popup was shown (the choice element was
	 *         not empty). If <code>false</code>, the popup was not shown.
	 */
	public boolean showPopup(int selectorIndex, BreadcrumbItemChoices bic) {
		JPopupMenu popup = this.getPopup();
		this.popupInitiatorIndex = selectorIndex;
		popup.removeAll();
		Component toSelect = null;
		for (int i = 0; i < bic.getChoices().length; i++) {
			BreadcrumbItem bi = bic.getChoices()[i];
			Action action = new PopupAction(bic.getAncestor(), bi);
			JMenuItem item = popup.add(action);
			if (item != null) {
				// not hidden by scrollable popup
				item.setFont(breadcrumbBar.getFont());
				popup.setBackground(item.getBackground());
				// item.setForeground(getForeground());
				item.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
				// if (i == 0)
				// toSelect = item;
			}
		}
		// check for next component
		if (stack.size() > bic.getIndex() + 1) {
			BreadcrumbItem c = getItem(bic.getIndex() + 1);
			if (c != null) {
				// BreadcrumbItem bi = (BreadcrumbItem)
				// getValue(BreadCrumbItem_Key);
				toSelect = new JLabel(c.getKey());
			}
		}
		if (bic.getChoices().length > 0) {
			// System.out.println("Popup has " + popup.getComponentCount()
			// + " elements");
			// popup.revalidate();
			popup.show(comps.get(bic), 0, comps.get(bic).getHeight());
			if (toSelect != null)
				popup.setSelected(toSelect);
			return true;
		}
		this.popupInitiatorIndex = -1;
		return false;
	}

	@Override
	public BreadcrumbParticle getParticle(int selectorIndex) {
		if (selectorIndex == 0
				|| selectorIndex >= this.mainPanel.getComponentCount())
			return null;
		Component c = mainPanel.getComponent(selectorIndex);
		if (c != null && c instanceof ChoicesSelector) {
			return (BreadcrumbParticle) mainPanel
					.getComponent(selectorIndex - 1);
		}
		return null;
	}

	@Override
	public ChoicesSelector getSelector(int selectorIndex) {
		if (selectorIndex == 0
				|| selectorIndex >= this.mainPanel.getComponentCount())
			return null;
		Component c = mainPanel.getComponent(selectorIndex);
		if (c != null && c instanceof ChoicesSelector) {
			return (ChoicesSelector) c;
		}
		return null;
	}

	@Override
	public JCommandButton createLeftScroller() {
		JCommandButton b = new JCommandButton(null,
				new DoubleArrowResizableIcon(new Dimension(9, 9),
						SwingConstants.WEST));
		b.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
		b.setFocusable(false);
		b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		b.putClientProperty(BasicCommandButtonUI.EMULATE_SQUARE_BUTTON,
				Boolean.TRUE);
		return b;
	}

	@Override
	public JCommandButton createRightScroller() {
		JCommandButton b = new JCommandButton(null,
				new DoubleArrowResizableIcon(new Dimension(9, 9),
						SwingConstants.EAST));
		b.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
		b.setFocusable(false);
		b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		b.putClientProperty(BasicCommandButtonUI.EMULATE_SQUARE_BUTTON,
				Boolean.TRUE);
		return b;
	}

	/**
	 * Action for the popup menu.
	 */
	private class PopupAction<T> extends AbstractAction {
		BreadcrumbItem<T> ancestorItem;
		BreadcrumbItem<T> itemToSelect;

		public PopupAction(BreadcrumbItem<T> ancestorItem,
				BreadcrumbItem<T> itemToSelect) {
			super(itemToSelect.getKey(), itemToSelect.getIcon());
			this.ancestorItem = ancestorItem;
			this.itemToSelect = itemToSelect;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent
		 * )
		 */
		public void actionPerformed(ActionEvent e) {
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					if (itemToSelect == null)
						return;

					BreadcrumbBarModel barModel = breadcrumbBar.getModel();
					barModel.setCumulative(true);
					int itemIndex = barModel.indexOf(ancestorItem);
					int toLeave = ((ancestorItem == null) || (itemIndex < 0)) ? 0
							: itemIndex + 1;
					while (barModel.getItemCount() > toLeave) {
						barModel.removeLast();
					}
					barModel.addLast(itemToSelect);
					barModel.setCumulative(false);
					//
					// breadcrumbBar.getModel().replaceAllAfter(ancestorItem,
					// itemToSelect);
				}
			});
		}
	}

	/**
	 * Returns the index of the popup initiator.
	 * 
	 * @return The index of the popup initiator.
	 */
	@Override
	public int getPopupInitiatorIndex() {
		return this.popupInitiatorIndex;
	}

	/**
	 * Pushes a choice to the top position of the stack. If the current top is
	 * already a {@link BreadcrumbItemChoices}, replace it.
	 * 
	 * @param bic
	 *            The choice item to push.
	 * @return The item that has been pushed.
	 */
	protected Object pushChoices(BreadcrumbItemChoices bic) {
		return pushChoices(bic, true);
	}

	/**
	 * Pushes a choice to the top position of the stack. If the current top is
	 * already a {@link BreadcrumbItemChoices}, replace it.
	 * 
	 * @param bic
	 *            The choice item to push.
	 * @param toUpdateUI
	 *            Indication whether the bar should be repainted.
	 * @return The item that has been pushed.
	 */
	protected synchronized Object pushChoices(BreadcrumbItemChoices bic,
			boolean toUpdateUI) {
		if (bic == null)
			return null;
		Object result;
		synchronized (stack) {
			if (stack.size() % 2 == 1) {
				stack.pop();
			}
			bic.setIndex(stack.size());
			result = stack.push(bic);
		}
		if (toUpdateUI) {
			updateComponents();
		}
		return result;
	}

	/**
	 * Pushes an item to the top position of the stack. If the current top is
	 * already a {@link BreadcrumbItem}, replace it.
	 * 
	 * @param bi
	 *            The item to push.
	 * @return The item that has been pushed.
	 */
	protected Object pushChoice(BreadcrumbItem bi) {
		return pushChoice(bi, true);
	}

	/**
	 * Pushes an item to the top position of the stack. If the current top is
	 * already a {@link BreadcrumbItemChoices}, replace it.
	 * 
	 * @param bi
	 *            The item to push.
	 * @param toUpdateUI
	 *            Indication whether the bar should be repainted.
	 * @return The item that has been pushed.
	 */
	protected Object pushChoice(BreadcrumbItem bi, boolean toUpdateUI) {
		assert (bi != null);
		Object result;
		synchronized (stack) {
			if (!stack.isEmpty() && stack.size() % 2 == 0) {
				stack.pop();
			}
			bi.setIndex(stack.size());
			result = stack.push(bi);
		}
		return result;
	}

	protected BreadcrumbItem getItem(int index) {
		if (stack.isEmpty())
			return null;

		if ((index < 0) || (index >= stack.size()))
			return null;

		Object last = stack.get(index);
		if (last instanceof BreadcrumbItem)
			return (BreadcrumbItem) last;
		return null;

	}

}