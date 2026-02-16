package test.common;

import java.awt.BorderLayout;

import javax.swing.*;

import org.jvnet.flamingo.common.JCommandButton;
import org.jvnet.flamingo.common.JCommandButtonStrip;
import org.jvnet.flamingo.common.JCommandButtonStrip.StripOrientation;

import test.svg.transcoded.*;

import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.FormLayout;

public class TestButtonStripVertical extends JFrame {
	public TestButtonStripVertical() {
		super("Tests for vertical buttons strips");

		FormLayout lm = new FormLayout(
				"center:pref, 4dlu, center:pref, 4dlu, center:pref, 4dlu, center:pref, 4dlu, center:pref, 4dlu, center:pref",
				"");
		DefaultFormBuilder builder = new DefaultFormBuilder(lm);
		builder.setDefaultDialogBorder();

		builder.append("h1.0-v0.5");
		builder.append("h1.0-v0.75");
		builder.append("h1.0-v1.0");
		builder.append("h0.5-v0.75");
		builder.append("h0.75-v0.75");
		builder.append("h1.0-v0.75");

		builder.append(getStrip1(1.0, 0.5), getStrip1(1.0, 0.75), getStrip1(
				1.0, 1.0));
		builder.append(getStrip1(0.5, 0.75), getStrip1(0.75, 0.75), getStrip1(
				1.0, 0.75));

		builder.append(getStrip2(1.0, 0.5), getStrip2(1.0, 0.75), getStrip2(
				1.0, 1.0));
		builder.append(getStrip2(0.5, 0.75), getStrip2(0.75, 0.75), getStrip2(
				1.0, 0.75));

		this.add(builder.getPanel(), BorderLayout.CENTER);
		this.pack();
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}

	private JCommandButtonStrip getStrip1(double hgapScaleFactor,
			double vgapScaleFactor) {
		JCommandButtonStrip buttonStrip = new JCommandButtonStrip(
				StripOrientation.VERTICAL);
		buttonStrip.setHGapScaleFactor(hgapScaleFactor);
		buttonStrip.setVGapScaleFactor(vgapScaleFactor);
		buttonStrip.add(new JCommandButton("", new format_justify_left()));
		buttonStrip.add(new JCommandButton("", new format_justify_center()));
		buttonStrip.add(new JCommandButton("", new format_justify_right()));
		return buttonStrip;
	}

	private JCommandButtonStrip getStrip2(double hgapScaleFactor,
			double vgapScaleFactor) {
		JCommandButtonStrip buttonStrip2 = new JCommandButtonStrip(
				StripOrientation.VERTICAL);
		buttonStrip2.setHGapScaleFactor(hgapScaleFactor);
		buttonStrip2.setVGapScaleFactor(vgapScaleFactor);
		buttonStrip2.add(new JCommandButton("", new format_text_bold()));
		buttonStrip2.add(new JCommandButton("", new format_text_italic()));
		buttonStrip2.add(new JCommandButton("", new format_text_underline()));
		buttonStrip2
				.add(new JCommandButton("", new format_text_strikethrough()));
		return buttonStrip2;
	}

	public static void main(String[] args) throws Exception {
		UIManager
				.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				new TestButtonStripVertical().setVisible(true);
			}
		});
	}
}
