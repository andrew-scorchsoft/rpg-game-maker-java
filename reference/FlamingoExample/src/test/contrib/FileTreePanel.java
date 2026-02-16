package test.contrib;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.*;

import javax.swing.*;
import org.jvnet.flamingo.bcb.BreadcrumbItem;
import org.jvnet.flamingo.bcb.BreadcrumbPathEvent;
import org.jvnet.flamingo.bcb.BreadcrumbPathListener;
import org.jvnet.flamingo.bcb.core.BreadcrumbFileSelector;
import org.jvnet.flamingo.common.StringValuePair;

/**
 * @author Kirill Grouchnikov
 */
public class FileTreePanel extends JPanel {
	final JButton back_btn = new JButton("Back");
	Stack back = new Stack();

	private BreadcrumbFileSelector bar;

	public FileTreePanel() {
		this.bar = new BreadcrumbFileSelector();
		back.add("C:\\jprojects\\trident\\src\\test");
		back.add("C:\\Users\\Owner");
		back.add("C:\\Users\\Owner\\Desktop\\liat");
		back.add("C:\\Users\\Owner\\Desktop");
		back.add("C:\\Tools");
		back.add("C:\\Users\\Owner\\Desktop\\liat");
		back.add("C:\\jprojects\\trident\\src\\test");
		back.add("C:\\Users\\Owner");
		back.add("C:\\Users\\Owner");
		back.add("C:\\Users\\Owner");

		back_btn.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				Object backpop = null;

				if (back.empty() == false) {
					backpop = back.pop();

					if (back.empty() == true) {

						back_btn.setEnabled(false);
					}
					bar.setPath(new File(backpop.toString()));
				} else {
					back_btn.setEnabled(false);
				}
			}
		});

		JPanel navigation = new JPanel(new BorderLayout());

		navigation.add(back_btn, BorderLayout.WEST);

		//this.bar.setPreferredSize(new Dimension(500, 25));
		navigation.add(bar, BorderLayout.CENTER);
		this.setLayout(new BorderLayout());
		this.add(navigation);
		this.bar.setPath(new File(System.getProperty("user.home")));
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				JFrame frame = new JFrame("File tree");
				frame.setSize(800, 80);
				frame.setLocationRelativeTo(null);

				frame.add(new FileTreePanel());
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.setVisible(true);
			}
		});
	}
}