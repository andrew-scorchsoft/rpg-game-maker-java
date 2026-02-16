package test.slider;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.jvnet.flamingo.slider.FlexiRangeModel;
import org.jvnet.flamingo.slider.JFlexiSlider;

public class FlexiSliderFrame extends JFrame {
	public FlexiSliderFrame() {
		this.setLayout(new GridLayout(1, 3));
		JPanel panel1 = new JPanel(new BorderLayout());

		FlexiRangeModel.Range rangeTilesDetails = new FlexiRangeModel.Range(
				"Tiles -> Details", true, 0.0);
		FlexiRangeModel.Range rangeDetailsSmall = new FlexiRangeModel.Range(
				"Details -> Small Icons", true, 0.0);
		FlexiRangeModel.Range rangeSmallMedium = new FlexiRangeModel.Range(
				"Small Icons -> Medium Icons", false, 2.0);
		FlexiRangeModel.Range rangeMediumLarge = new FlexiRangeModel.Range(
				"Medium Icons -> Large Icons", false, 1.0);
		FlexiRangeModel.Range rangeLargeVeryLarge = new FlexiRangeModel.Range(
				"Large Icons -> Very Large Icons", false, 2.0);

		Icon iconTiles = new ImageIcon(FlexiSliderFrame.class.getClassLoader()
				.getResource("test/resource/tiles.png"));
		Icon iconDetails = new ImageIcon(FlexiSliderFrame.class
				.getClassLoader().getResource("test/resource/details.png"));
		Icon iconSmallIcons = new ImageIcon(FlexiSliderFrame.class
				.getClassLoader().getResource("test/resource/small-icons.png"));
		Icon iconMediumIcons = new ImageIcon(FlexiSliderFrame.class
				.getClassLoader().getResource("test/resource/medium-icons.png"));
		Icon iconLargeIcons = new ImageIcon(FlexiSliderFrame.class
				.getClassLoader().getResource("test/resource/large-icons.png"));
		Icon iconVeryLargeIcons = new ImageIcon(FlexiSliderFrame.class
				.getClassLoader().getResource(
						"test/resource/very-large-icons.png"));

		FlexiRangeModel.Range[] ranges = new FlexiRangeModel.Range[] {
				rangeTilesDetails, rangeDetailsSmall, rangeSmallMedium,
				rangeMediumLarge, rangeLargeVeryLarge };
		Icon[] icons = new Icon[] { iconTiles, iconDetails, iconSmallIcons,
				iconMediumIcons, iconLargeIcons, iconVeryLargeIcons };
		String[] texts = new String[] { "Tiles", "Details", "Small Icons",
				"Medium Icons", "Large Icons", "Very Large Icons" };
		final JFlexiSlider slider0 = new JFlexiSlider(ranges, icons, texts);
		// slider0.setBorder(LineBorder.createBlackLineBorder());
		panel1.add(slider0, BorderLayout.CENTER);

		slider0.setValue(new FlexiRangeModel.Value(rangeLargeVeryLarge, 1.0));

		slider0.getModel().addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				System.out.print(slider0.getModel().getValueIsAdjusting());
				FlexiRangeModel.Value value = slider0.getValue();
				System.out.println("\t"
						+ ((value != null) ? value.toString() : ""));
			}
		});

		this.add(panel1);

		this.setSize(400, 300);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// LAF changing
		JMenu lafMenu = new JMenu("Look & feel");
		JMenu substanceMenus = new JMenu("Substance family");
		substanceMenus.add(LafChanger.getMenuItem(this, "Substance",
				"org.jvnet.substance.SubstanceLookAndFeel"));
		substanceMenus.add(LafChanger.getMenuItem(this, "Substance Default",
				"org.jvnet.substance.SubstanceDefaultLookAndFeel"));
		substanceMenus.addSeparator();
		substanceMenus.add(LafChanger.getMenuItem(this, "Substance Business",
				"org.jvnet.substance.skin.SubstanceBusinessLookAndFeel"));
		substanceMenus.add(LafChanger.getMenuItem(this, "Substance Creme",
				"org.jvnet.substance.skin.SubstanceCremeLookAndFeel"));
		substanceMenus.add(LafChanger.getMenuItem(this, "Substance Moderate",
				"org.jvnet.substance.skin.SubstanceModerateLookAndFeel"));
		substanceMenus
				.add(LafChanger
						.getMenuItem(this, "Substance Office Silver 2007",
								"org.jvnet.substance.skin.SubstanceOfficeSilver2007LookAndFeel"));
		substanceMenus.add(LafChanger.getMenuItem(this, "Substance Sahara",
				"org.jvnet.substance.skin.SubstanceSaharaLookAndFeel"));
		substanceMenus.addSeparator();
		substanceMenus.add(LafChanger.getMenuItem(this,
				"Substance Field of Wheat",
				"org.jvnet.substance.skin.SubstanceFieldOfWheatLookAndFeel"));
		substanceMenus.add(LafChanger.getMenuItem(this,
				"Substance Green Magic",
				"org.jvnet.substance.skin.SubstanceGreenMagicLookAndFeel"));
		substanceMenus.add(LafChanger.getMenuItem(this, "Substance Mango",
				"org.jvnet.substance.skin.SubstanceMangoLookAndFeel"));
		substanceMenus.add(LafChanger.getMenuItem(this,
				"Substance Office Blue 2007",
				"org.jvnet.substance.skin.SubstanceOfficeBlue2007LookAndFeel"));
		substanceMenus.addSeparator();
		substanceMenus.add(LafChanger.getMenuItem(this,
				"Substance Challenger Deep",
				"org.jvnet.substance.skin.SubstanceChallengerDeepLookAndFeel"));
		substanceMenus.add(LafChanger.getMenuItem(this,
				"Substance Emerald Dusk",
				"org.jvnet.substance.skin.SubstanceEmeraldDuskLookAndFeel"));
		substanceMenus.add(LafChanger.getMenuItem(this, "Substance Magma",
				"org.jvnet.substance.skin.SubstanceMagmaLookAndFeel"));
		substanceMenus.add(LafChanger.getMenuItem(this, "Substance Raven",
				"org.jvnet.substance.skin.SubstanceRavenLookAndFeel"));
		lafMenu.add(substanceMenus);
		lafMenu.addSeparator();
		JMenu coreLafMenus = new JMenu("Core LAFs");
		lafMenu.add(coreLafMenus);
		coreLafMenus.add(LafChanger.getMenuItem(this, "Metal",
				"javax.swing.plaf.metal.MetalLookAndFeel"));
		coreLafMenus.add(LafChanger.getMenuItem(this, "Windows",
				"com.sun.java.swing.plaf.windows.WindowsLookAndFeel"));
		coreLafMenus.add(LafChanger.getMenuItem(this, "Windows Classic",
				"com.sun.java.swing.plaf.windows.WindowsClassicLookAndFeel"));
		coreLafMenus.add(LafChanger.getMenuItem(this, "Motif",
				"com.sun.java.swing.plaf.motif.MotifLookAndFeel"));

		JMenu customLafMenus = new JMenu("Custom LAFs");
		lafMenu.add(customLafMenus);
		JMenu jgoodiesMenu = new JMenu("JGoodies family");
		customLafMenus.add(jgoodiesMenu);
		jgoodiesMenu.add(LafChanger.getMenuItem(this, "JGoodies Plastic",
				"com.jgoodies.looks.plastic.PlasticLookAndFeel"));
		jgoodiesMenu.add(LafChanger.getMenuItem(this, "JGoodies PlasticXP",
				"com.jgoodies.looks.plastic.PlasticXPLookAndFeel"));
		jgoodiesMenu.add(LafChanger.getMenuItem(this, "JGoodies Plastic3D",
				"com.jgoodies.looks.plastic.Plastic3DLookAndFeel"));

		JMenu jtattooMenu = new JMenu("JTattoo family");
		customLafMenus.add(jtattooMenu);
		jtattooMenu.add(LafChanger.getMenuItem(this, "JTattoo Acryl",
				"com.jtattoo.plaf.acryl.AcrylLookAndFeel"));
		jtattooMenu.add(LafChanger.getMenuItem(this, "JTattoo Aero",
				"com.jtattoo.plaf.aero.AeroLookAndFeel"));
		jtattooMenu.add(LafChanger.getMenuItem(this, "JTattoo Aluminium",
				"com.jtattoo.plaf.aluminium.AluminiumLookAndFeel"));
		jtattooMenu.add(LafChanger.getMenuItem(this, "JTattoo Bernstein",
				"com.jtattoo.plaf.bernstein.BernsteinLookAndFeel"));
		jtattooMenu.add(LafChanger.getMenuItem(this, "JTattoo Fast",
				"com.jtattoo.plaf.fast.FastLookAndFeel"));
		jtattooMenu.add(LafChanger.getMenuItem(this, "JTattoo HiFi",
				"com.jtattoo.plaf.hifi.HiFiLookAndFeel"));
		jtattooMenu.add(LafChanger.getMenuItem(this, "JTattoo Luna",
				"com.jtattoo.plaf.luna.LunaLookAndFeel"));
		jtattooMenu.add(LafChanger.getMenuItem(this, "JTattoo McWin",
				"com.jtattoo.plaf.mcwin.McWinLookAndFeel"));
		jtattooMenu.add(LafChanger.getMenuItem(this, "JTattoo Mint",
				"com.jtattoo.plaf.mint.MintLookAndFeel"));
		jtattooMenu.add(LafChanger.getMenuItem(this, "JTattoo Smart",
				"com.jtattoo.plaf.smart.SmartLookAndFeel"));

		JMenu syntheticaMenu = new JMenu("Synthetica family");
		customLafMenus.add(syntheticaMenu);
		syntheticaMenu.add(LafChanger.getMenuItem(this, "Synthetica base",
				"de.javasoft.plaf.synthetica.SyntheticaStandardLookAndFeel"));
		syntheticaMenu.add(LafChanger.getMenuItem(this, "Synthetica BlackMoon",
				"de.javasoft.plaf.synthetica.SyntheticaBlackMoonLookAndFeel"));
		syntheticaMenu.add(LafChanger.getMenuItem(this, "Synthetica BlackStar",
				"de.javasoft.plaf.synthetica.SyntheticaBlackStarLookAndFeel"));
		syntheticaMenu.add(LafChanger.getMenuItem(this, "Synthetica BlueIce",
				"de.javasoft.plaf.synthetica.SyntheticaBlueIceLookAndFeel"));
		syntheticaMenu.add(LafChanger.getMenuItem(this, "Synthetica BlueMoon",
				"de.javasoft.plaf.synthetica.SyntheticaBlueMoonLookAndFeel"));
		syntheticaMenu.add(LafChanger.getMenuItem(this, "Synthetica BlueSteel",
				"de.javasoft.plaf.synthetica.SyntheticaBlueSteelLookAndFeel"));
		syntheticaMenu.add(LafChanger.getMenuItem(this,
				"Synthetica GreenDream",
				"de.javasoft.plaf.synthetica.SyntheticaGreenDreamLookAndFeel"));
		syntheticaMenu
				.add(LafChanger
						.getMenuItem(this, "Synthetica OrangeMetallic",
								"de.javasoft.plaf.synthetica.SyntheticaOrangeMetallicLookAndFeel"));
		syntheticaMenu.add(LafChanger.getMenuItem(this,
				"Synthetica SilverMoon",
				"de.javasoft.plaf.synthetica.SyntheticaSilverMoonLookAndFeel"));

		JMenu officeMenu = new JMenu("Office family");
		customLafMenus.add(officeMenu);
		officeMenu.add(LafChanger.getMenuItem(this, "Office 2003",
				"org.fife.plaf.Office2003.Office2003LookAndFeel"));
		officeMenu.add(LafChanger.getMenuItem(this, "Office XP",
				"org.fife.plaf.OfficeXP.OfficeXPLookAndFeel"));
		officeMenu.add(LafChanger.getMenuItem(this, "Visual Studio 2005",
				"org.fife.plaf.VisualStudio2005.VisualStudio2005LookAndFeel"));

		customLafMenus.add(LafChanger.getMenuItem(this, "A03",
				"apprising.api.swing.plaf.a03.A03LookAndFeel"));
		customLafMenus.add(LafChanger.getMenuItem(this, "Alloy",
				"com.incors.plaf.alloy.AlloyLookAndFeel"));
		customLafMenus.add(LafChanger.getMenuItem(this, "FH",
				"com.shfarr.ui.plaf.fh.FhLookAndFeel"));
		customLafMenus.add(LafChanger.getMenuItem(this, "Hippo",
				"se.diod.hippo.plaf.HippoLookAndFeel"));
		customLafMenus.add(LafChanger.getMenuItem(this, "Kuntstoff",
				"com.incors.plaf.kunststoff.KunststoffLookAndFeel"));
		customLafMenus.add(LafChanger.getMenuItem(this, "Liquid",
				"com.birosoft.liquid.LiquidLookAndFeel"));
		customLafMenus.add(LafChanger.getMenuItem(this, "Lipstik",
				"com.lipstikLF.LipstikLookAndFeel"));
		customLafMenus.add(LafChanger.getMenuItem(this, "Metouia",
				"net.sourceforge.mlf.metouia.MetouiaLookAndFeel"));
		customLafMenus.add(LafChanger.getMenuItem(this, "Napkin",
				"net.sourceforge.napkinlaf.NapkinLookAndFeel"));
		customLafMenus.add(LafChanger.getMenuItem(this, "NimROD",
				"com.nilo.plaf.nimrod.NimRODLookAndFeel"));
		customLafMenus.add(LafChanger.getMenuItem(this, "Oyoaha",
				"com.oyoaha.swing.plaf.oyoaha.OyoahaLookAndFeel"));
		customLafMenus.add(LafChanger.getMenuItem(this, "Pagosoft",
				"com.pagosoft.plaf.PgsLookAndFeel"));
		customLafMenus.add(LafChanger.getMenuItem(this, "Simple",
				"com.memoire.slaf.SlafLookAndFeel"));
		customLafMenus.add(LafChanger.getMenuItem(this, "Skin",
				"com.l2fprod.gui.plaf.skin.SkinLookAndFeel"));
		customLafMenus.add(LafChanger.getMenuItem(this, "Smooth Metal",
				"smooth.metal.SmoothLookAndFeel"));
		customLafMenus.add(LafChanger.getMenuItem(this, "Squareness",
				"net.beeger.squareness.SquarenessLookAndFeel"));
		customLafMenus.add(LafChanger.getMenuItem(this, "Tiny",
				"de.muntjak.tinylookandfeel.TinyLookAndFeel"));
		customLafMenus.add(LafChanger.getMenuItem(this, "Tonic",
				"com.digitprop.tonic.TonicLookAndFeel"));
		customLafMenus.add(LafChanger.getMenuItem(this, "Trendy",
				"com.Trendy.swing.plaf.TrendyLookAndFeel"));

		JMenuBar jmb = new JMenuBar();
		jmb.add(lafMenu);
		this.setJMenuBar(jmb);

	}

	/**
	 * Main method for testing.
	 * 
	 * @param args
	 *            Ignored.
	 */
	public static void main(String[] args) throws Exception {
		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				// JFrame.setDefaultLookAndFeelDecorated(true);
				new FlexiSliderFrame().setVisible(true);
			}
		});
	}

}
