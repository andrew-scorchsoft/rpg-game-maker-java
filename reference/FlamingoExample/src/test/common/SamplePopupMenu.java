/**
 * 
 */
package test.common;

import org.jvnet.flamingo.common.JCommandMenuButton;
import org.jvnet.flamingo.common.icon.EmptyResizableIcon;
import org.jvnet.flamingo.common.popup.JCommandPopupMenu;

public class SamplePopupMenu extends JCommandPopupMenu {
	public SamplePopupMenu() {
		this.addMenuButton(new JCommandMenuButton("Test menu item 1",
				new EmptyResizableIcon(16)));
		this.addMenuButton(new JCommandMenuButton("Test menu item 2",
				new EmptyResizableIcon(16)));
		this.addMenuButton(new JCommandMenuButton("Test menu item 3",
				new EmptyResizableIcon(16)));
		this.addMenuSeparator();
		this.addMenuButton(new JCommandMenuButton("Test menu item 4",
				new EmptyResizableIcon(16)));
		this.addMenuButton(new JCommandMenuButton("Test menu item 5",
				new EmptyResizableIcon(16)));
	}
}