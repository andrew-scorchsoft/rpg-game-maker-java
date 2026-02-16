package test.common;

import java.awt.BorderLayout;

import javax.swing.*;

import org.jvnet.flamingo.common.JCommandButton;
import org.jvnet.flamingo.common.JCommandButtonStrip;
import org.jvnet.flamingo.common.JCommandButton.CommandButtonKind;
import org.jvnet.flamingo.common.popup.JPopupPanel;
import org.jvnet.flamingo.common.popup.PopupPanelCallback;

import test.svg.transcoded.*;

import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.FormLayout;

public class TestButtonStripHorizontal extends JFrame {
	public TestButtonStripHorizontal() {
		super("Tests for horizontal buttons strips");

		FormLayout lm = new FormLayout(
				"right:pref, 4dlu, left:pref, 4dlu, left:pref", "");
		DefaultFormBuilder builder = new DefaultFormBuilder(lm);
		builder.setDefaultDialogBorder();

		builder.append("hgap 0.5,  vgap 1.0", getStrip1(0.5, 1.0), getStrip2(
				0.5, 1.0));
		builder.append("hgap 0.75, vgap 1.0", getStrip1(0.75, 1.0), getStrip2(
				0.75, 1.0));
		builder.append("hgap 1.0,  vgap 1.0", getStrip1(1.0, 1.0), getStrip2(
				1.0, 1.0));

		builder.append("hgap 0.75, vgap 0.5", getStrip1(0.75, 0.5), getStrip2(
				0.75, 0.5));
		builder.append("hgap 0.75, vgap 0.75", getStrip1(0.75, 0.75),
				getStrip2(0.75, 0.75));
		builder.append("hgap 0.75, vgap 1.0", getStrip1(0.75, 1.0), getStrip2(
				0.75, 1.0));

		this.add(builder.getPanel(), BorderLayout.CENTER);
		this.pack();
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}

	private JCommandButtonStrip getStrip1(double hgapScaleFactor,
			double vgapScaleFactor) {
		JCommandButtonStrip buttonStrip = new JCommandButtonStrip();
		buttonStrip.setHGapScaleFactor(hgapScaleFactor);
		buttonStrip.setVGapScaleFactor(vgapScaleFactor);
		buttonStrip.add(new JCommandButton("", new format_justify_left()));
		buttonStrip.add(new JCommandButton("", new format_justify_center()));
		buttonStrip.add(new JCommandButton("", new format_justify_right()));
		return buttonStrip;
	}

	private JCommandButtonStrip getStrip2(double hgapScaleFactor,
			double vgapScaleFactor) {
		JCommandButtonStrip buttonStrip2 = new JCommandButtonStrip();
		buttonStrip2.setHGapScaleFactor(hgapScaleFactor);
		buttonStrip2.setVGapScaleFactor(vgapScaleFactor);
		buttonStrip2.add(new JCommandButton("", new format_text_bold()));
		buttonStrip2.add(new JCommandButton("", new format_text_italic()));
		buttonStrip2.add(new JCommandButton("", new format_text_underline()));
		JCommandButton strike = new JCommandButton("",
				new format_text_strikethrough());
		strike
				.setCommandButtonKind(CommandButtonKind.ACTION_AND_POPUP_MAIN_ACTION);
		strike.setPopupCallback(new PopupPanelCallback() {
			@Override
			public JPopupPanel getPopupPanel(JCommandButton commandButton) {
				return new SamplePopupMenu();
			}
		});
		buttonStrip2.add(strike);
		return buttonStrip2;
	}

	public static void main(String[] args) throws Exception {
		UIManager
				.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				new TestButtonStripHorizontal().setVisible(true);
			}
		});
	}
}
